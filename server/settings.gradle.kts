rootProject.name = "server"

include("shared")
include("domain")
include("infra")
include("app")

includeBuild("composite-build")
