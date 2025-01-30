enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google{
            content{
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google{
            content{
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "NewsSearchApp"
include(":app")
include(":news-api")
include(":database")
include(":features:news-main:domain")
include(":features:news-main:ui")
include(":news-data")
include(":news-common")
include(":news-uikit")
include(":baselineprofile")
