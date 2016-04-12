model {
    platforms {
        x86
        x86_64
    }
    buildTypes {
        debug
        release
    }
    distributions {
        local(LocalInstall) {
            copySpecs {
                bin {
                    dest = "bin"
                    projects(":a:b:c") {
                        components.withType(NativeComponentSpec) {
                            binaries.withType(NativeExecutableBinarySpec) {
                                srcs.add(executableFile)
                            }
                        }
                    }
                }
            }
        }
        prod(DebianPkg) {
            copySpecs {
                bin {
                    dest = "usr/local/bin"
                    projects(":a:b:c") {
                        components.withType(NativeComponentSpec) {
                            binaries.withType(NativeExecutableBinarySpec) {
                                srcs.add(executableFile)
                            }
                        }
                    }
                }
            }
        }
        devel(DebianPkg) {
            base $.distributions.prod
            copySpecs {
                headers {
                    dest = "usr/include"
                    projects(':a:b:c') {
                        components.withType(NativeComponentSpec) {
                            binaries.withType(NativeLibraryBinarySpec) {
                                sources.withType(HeaderExportingSourceSet) {
                                    srcs.add(exportedHeaders)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}