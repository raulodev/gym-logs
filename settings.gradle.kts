pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://nexus.xebialabs.com/nexus/content/repositories/public/")
        }
        mavenCentral()
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://nexus.xebialabs.com/nexus/content/repositories/public/")
        }
        mavenCentral()
        google()
    }
}

rootProject.name = "GymPago3"
include(":app")
