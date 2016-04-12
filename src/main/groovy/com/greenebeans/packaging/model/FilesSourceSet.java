package com.greenebeans.packaging.model;

import org.gradle.language.base.DependentSourceSet;
import org.gradle.model.Managed;

@Managed
public interface FilesSourceSet extends DependentSourceSet {
    void setDest(String dest);
    String getDest();
}
