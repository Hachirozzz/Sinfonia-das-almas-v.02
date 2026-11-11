// Núcleo de RP: plugin próprio do servidor.
// Versões fixadas de propósito; conferidas em 11/nov/2026:
//  - paper-api: https://repo.papermc.io/repository/maven-public/io/papermc/paper/paper-api/
//    (mesmo build do servidor, ver ../versoes.env)
//  - PlaceholderAPI 2.12.3: https://modrinth.com/plugin/placeholderapi/version/pIvQcXW8
//    (o repositório Maven oficial dele não é alcançável daqui; o Maven do Modrinth entrega o mesmo jar)
plugins {
    java
}

group = "nucleorp"
version = "0.1.0"

val paperApi = "26.2.build.135-stable"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") }
        filter { includeGroup("maven.modrinth") }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperApi")
    compileOnly("maven.modrinth:placeholderapi:2.12.3")

    // O driver SQLite vem dentro do Paper; nos testes ele precisa ser trazido à parte
    // (mesma versão que o Paper 26.2 carrega em libraries/).
    testImplementation("org.xerial:sqlite-jdbc:3.49.1.0")
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain { languageVersion = JavaLanguageVersion.of(25) }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = 25
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.processResources {
    filteringCharset = "UTF-8"
    val versao = project.version.toString()
    inputs.property("versao", versao)
    filesMatching("plugin.yml") { expand("versao" to versao) }
}

tasks.test {
    useJUnitPlatform()
    testLogging { events("failed", "skipped") }
}

tasks.jar {
    archiveBaseName = "NucleoRP"
}
