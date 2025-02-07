import java.net.URI

plugins {
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("java-library")
    id("net.neoforged.gradle.userdev") version "7.0.124"
}

val mod_version: String by properties
val mod_group_id: String by properties
val mod_id: String by properties
val neo_version: String by properties
val mod_name: String by properties

version = mod_version
group = mod_group_id

base {
    archivesName = mod_id
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

runs {
    configureEach {
        systemProperty("forge.logging.markers", "REGISTRIES")
        // Recommended logging level for the console
        // You can set various levels here.
        // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
        systemProperty("forge.logging.console.level", "debug")
        //modSource(project.sourceSets.main)
    }
}

repositories {
    maven {
        url = URI("https://www.cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
}

dependencies {
    implementation("net.neoforged:neoforge:${neo_version}")


    //implementation(fg.deobf("curse.maven:jade-324717:4973483"))
    //implementation(fg.deobf("curse.maven:terrafirmacraft-302973:4976574"))
//    runtimeOnly(fg.deobf("curse.maven:patchouli-306770:4966125"))
}

tasks.named<ProcessResources>("processResources").configure {
    inputs.properties(
        mapOf(
            "neo_version" to neo_version,
            "mod_id" to mod_id,
            "mod_version" to mod_version
        )
    )

    filesMatching(listOf("META-INF/neoforge.mods.toml", "pack.mcmeta")) {
        expand(inputs.properties)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8" // Use the UTF-8 charset for Java compilation
}
