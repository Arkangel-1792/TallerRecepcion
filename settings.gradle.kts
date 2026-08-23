pluginManagement {
    repositories {
        val localProxy = System.getenv("LOCAL_MAVEN_PROXY")
        if (localProxy != null) {
            maven(url = "$localProxy/google") {
                content {
                    includeGroupByRegex("androidx(\\..*)?")
                    includeGroupByRegex("com\\.android(\\..*)?")
                    includeGroupByRegex("com\\.google\\.android(\\..*)?")
                    includeGroupByRegex("com\\.google\\.firebase(\\..*)?")
                    includeGroupByRegex("com\\.google\\.testing(\\..*)?")
                }
            }
            maven(url = "$localProxy/plugins") {
                content {
                    includeGroupByRegex("org\\.jetbrains\\.kotlin(\\..*)?")
                }
            }
            maven(url = "$localProxy/maven")
        } else {
            google()
            mavenCentral()
            gradlePluginPortal()
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        val localProxy = System.getenv("LOCAL_MAVEN_PROXY")
        if (localProxy != null) {
            maven(url = "$localProxy/google") {
                content {
                    includeGroupByRegex("androidx(\\..*)?")
                    includeGroupByRegex("com\\.android(\\..*)?")
                    includeGroupByRegex("com\\.google\\.android(\\..*)?")
                    includeGroupByRegex("com\\.google\\.firebase(\\..*)?")
                    includeGroupByRegex("com\\.google\\.testing(\\..*)?")
                }
            }
            maven(url = "$localProxy/maven")
        } else {
            google()
            mavenCentral()
        }
    }
}

rootProject.name = "TallerRecepcion"
include(":app")
