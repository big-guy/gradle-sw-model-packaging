package com.greenebeans.packaging.rules;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.nativeplatform.plugins.NativeComponentModelPlugin;

public class PackagingPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(NativeComponentModelPlugin.class);
        project.getPluginManager().apply(PackagingRules.class);
    }
}
