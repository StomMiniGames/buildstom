plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "wtf.nucker"
version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("dev.hollowcube:minestom-ce:54e839e58a")
    implementation("dev.hollowcube:polar:1.3.1")
    implementation("com.github.emortalmc:TNT:1.19.4_2")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")
}

application {
    mainClass.set("wtf.nucker.buildstom.MainKt")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    jar {
        archiveFileName.set("server.jar")
        manifest {
            attributes["Main-Class"] = application.mainClass
        }
    }

    distTar {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }

    distZip {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }

    named<JavaExec>("run") {
        systemProperty("user.dir", "$projectDir/run")
    }
}