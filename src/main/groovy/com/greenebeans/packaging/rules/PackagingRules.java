package com.greenebeans.packaging.rules;

import com.greenebeans.packaging.model.FilesSourceSet;
import com.greenebeans.packaging.model.InstallationBinarySpec;
import com.greenebeans.packaging.model.InstallationSpec;
import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.internal.resolve.ProjectModelResolver;
import org.gradle.api.tasks.Sync;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.ModelMap;
import org.gradle.model.Path;
import org.gradle.model.RuleSource;
import org.gradle.nativeplatform.BuildType;
import org.gradle.nativeplatform.BuildTypeContainer;
import org.gradle.platform.base.*;

import java.io.File;

public class PackagingRules extends RuleSource {
    @ComponentType
    void registerFilesSourceSet(TypeBuilder<FilesSourceSet> builder) {
    }

    @ComponentType
    void registerInstallationSpec(TypeBuilder<InstallationSpec> builder) {
    }

    @ComponentType
    void registerInstallationBinarySpec(TypeBuilder<InstallationBinarySpec> builder) {
    }

    @ComponentBinaries
    void generateBinariesForComponents(ModelMap<InstallationBinarySpec> binaries, InstallationSpec component,
                                       BuildTypeContainer buildTypes, PlatformContainer platforms,
                                       @Path("buildDir") File buildDir) {
        for (BuildType buildType : buildTypes) {
            for (Platform platform : platforms) {
                binaries.create(buildType.getName() + platform.getName() + "pkg", InstallationBinarySpec.class, new Action<InstallationBinarySpec>() {
                    public void execute(InstallationBinarySpec binary) {
                        binary.setOutputDir(new File(buildDir, binary.getName()));
                    }
                });
            }
        }
    }

    @BinaryTasks
    void generateTasksForBinaries(ModelMap<Task> tasks, ServiceRegistry serviceRegistry, InstallationBinarySpec binary) {
        String taskName = "stage" + binary.getBuildTask().getName();
        ProjectModelResolver modelResolver = serviceRegistry.get(ProjectModelResolver.class);
        tasks.create(taskName, Sync.class, new Action<Sync>() {
            public void execute(Sync sync) {
                binary.getInputs().withType(FilesSourceSet.class).all(new Action<FilesSourceSet>() {
                    @Override
                    public void execute(FilesSourceSet sourceSet) {
                        sync.into(binary.getOutputDir());
                        sync.into(sourceSet.getDest(), new Closure(this) {
                            @Override
                            public Object call(Object... args) {
                                sync.from(sourceSet.getSource());
                                return null;
                            }
                        });
                    }
                });
            }
        });
        binary.getBuildTask().dependsOn(taskName);
    }
}
