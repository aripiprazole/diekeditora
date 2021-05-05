rootProject.name = "backend"

include("web")
include("domain")
include("app")
include("infra")

includeBuild("composite-build")
