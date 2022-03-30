rootProject.name = "server"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("modules:logging")
include("modules:lib")
include("modules:graphql")
include("modules:user")
include("modules:app")
