import io.papermc.paperweight.util.constants.PAPERCLIP_CONFIG

plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("io.papermc.paperweight.patcher") version "1.5.8"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        content { onlyForConfigurations(PAPERCLIP_CONFIG) }
    }
}

dependencies {
    remapper("net.fabricmc:tiny-remapper:0.8.6:fat")
    decompiler("net.minecraftforge:forgeflower:2.0.627.2")
    paperclip("io.papermc:paperclip:3.0.3")
}

subprojects {
    apply(plugin = "java")
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
    }
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io")
    }
}

paperweight {
    serverProject.set(project(":yellowknife-server"))
    remapRepo.set("https://maven.fabricmc.net/")
    decompileRepo.set("https://files.minecraftforge.net/maven/")
    useStandardUpstream("purpur") {
        url.set(github("PurpurMC", "Purpur"))
        ref.set(providers.gradleProperty("purpur"))
        withStandardPatcher {
            baseName("Purpur")
            apiSourceDirPath.set("Purpur-API")
            serverSourceDirPath.set("Purpur-Server")
            apiPatchDir.set(layout.projectDirectory.dir("patches/api"))
            serverPatchDir.set(layout.projectDirectory.dir("patches/server"))
            apiOutputDir.set(layout.projectDirectory.dir("yellowknife-api"))
            serverOutputDir.set(layout.projectDirectory.dir("yellowknife-server"))
        }
    }
}