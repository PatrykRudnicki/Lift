package code.snippet

import net.liftweb._
import net.liftweb.http._
import net.liftweb.util.Helpers._

/**
  * Created by patry on 20.11.2016.
  */
class Form {
  var name = ""

  val formRenderer = SHtml.idMemoize { renderer =>
    ".name" #> SHtml.text(name, name = _)

  }

  def render = {
    SHtml.makeFormsAjax andThen
      "form" #> formRenderer &
      "type=submit" #> SHtml.ajaxOnSubmit(() => {
        name = "hello " + name
        S.notice(name)
        formRenderer.setHtml()
        )
      }

  }