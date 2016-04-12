package com.greenebeans.packaging

import com.greenebeans.packaging.model.InstallationSpec

class PackagingPluginIntegrationSpec extends AbstractIntegrationSpec {
    def setup() {
        buildFile << """
plugins {
    id "com.greenebeans.packaging"
}
"""
    }

    def "does not explode"() {
        expect:
        build("help")
    }

    def "can define custom package"() {
        given:

        file("src/foo").mkdirs()
        file("src/foo/file1.txt").text = "content"
        file("src/main/cpp").mkdirs()
        file("src/main/cpp/main.cpp").text = "int main() { return 0; }"

        buildFile << """
import ${InstallationSpec.getPackage().getName()}.*

plugins {
    id "org.gradle.cpp"
}

model {
    platforms {
        x86
        x86_64
    }
    buildTypes {
        debug
        release
    }
    components {
        main(NativeExecutableSpec)

        localInstall(LocalInstallationSpec) {
            sources {
                bin(FilesSourceSet) {
                    dest = "bin"
                    source {
                        srcDir "src/foo"
                    }
//                    dependencies {
//                        project('foo').library('blah')
//                    }
                }
            }
        }
    }
}
"""
        when:
        build("components")
        then:
        def output = result.output
        output.contains("InstallationBinarySpec 'localInstall:debugx86_64pkg'")
        output.contains("InstallationBinarySpec 'localInstall:debugx86pkg'")
        output.contains("InstallationBinarySpec 'localInstall:releasex86_64pkg'")
        output.contains("InstallationBinarySpec 'localInstall:releasex86pkg'")
    }
}
