package com.printscript

import org.gradle.api.Plugin
import org.gradle.api.Project

class KtlintConventionsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // Aplica el plugin de ktlint (la versión la resuelve pluginManagement)
        project.pluginManager.apply("org.jlleitschuh.gradle.ktlint")

        // Configura la extensión por nombre (sin depender de tipos en compile time)
        def ext = project.extensions.findByName("ktlint")
        if (ext != null) {
            ext.ignoreFailures = false
            ext.verbose = true
            ext.android = false
            // ext.baseline = project.file("${project.projectDir}/ktlint-baseline.xml") // opcional
        }

        // Repos por si hace falta
        project.repositories {
            mavenCentral()
            gradlePluginPortal()
        }
    }
}
