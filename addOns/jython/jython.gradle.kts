import org.zaproxy.gradle.addon.AddOnStatus

version = "11"
description = "Allows Python to be used for ZAP scripting - templates included"

zapAddOn {
    addOnName.set("Python Scripting")
    addOnStatus.set(AddOnStatus.BETA)
    zapVersion.set("2.7.0")

    manifest {
        author.set("ZAP Dev Team")
    }
}

dependencies {
    implementation("org.python:jython-standalone:2.7.1")

    testImplementation(project(":testutils"))
}
