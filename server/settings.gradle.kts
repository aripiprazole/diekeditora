rootProject.name = "server"

include("web")
include("domain")
include("app")
include("infra")
include("shared")

includeBuild("composite-build")
