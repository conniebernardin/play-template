// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/connie.bernardin/PlayFramework_tasks/play-template/conf/routes
// @DATE:Thu May 26 11:20:50 BST 2022


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
