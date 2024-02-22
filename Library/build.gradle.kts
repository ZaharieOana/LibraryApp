plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("mysql:mysql-connector-java:8.0.33")

}

javafx {
    version = "21.0.1"
    modules("javafx.controls", "javafx.fxml")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
application {
    mainClass.set("launcher.Launcher")
}