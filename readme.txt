Full setup procedure:

build.gradle...

1) add plugin: "id 'org.openjfx.javafxplugin' version '0.0.9'"
2) add module: 

javafx {
    version = "15.0.1"
    modules = [ 'javafx.controls', 'javafx.fxml' ]
}



Useful resources for later:

https://openjfx.io/openjfx-docs/#modular