package code.snippet

import net.liftweb._
import net.liftweb.http._
import net.liftweb.util.Helpers._

/**
  * Created by patry on 20.11.2016.
  */
class Form {
  var name = ""

  private val formRenderer = SHtml.idMemoize { renderer =>
    ".name" #> SHtml.text(name, name = _) &
    ".save" #> SHtml.ajaxOnSubmit(() => {
      name = "hello " + name
      S.notice(name)
      renderer.setHtml()
    })
  }

  def render = {
    SHtml.makeFormsAjax andThen
    "#container" #> formRenderer
  }
}