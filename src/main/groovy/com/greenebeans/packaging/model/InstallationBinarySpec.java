package com.greenebeans.packaging.model;

import org.gradle.model.Managed;
import org.gradle.platform.base.BinarySpec;

import java.io.File;

@Managed
public interface InstallationBinarySpec extends BinarySpec {
    File getOutputDir();
    void setOutputDir(File outputDir);
}