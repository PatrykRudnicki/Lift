package code.snippet

import net.liftweb._
import net.liftweb.common.Full
import net.liftweb.http._
import net.liftweb.util.Helpers._

/**
  * Created by patry on 20.11.2016.
  */
class Form {
  var firstName = ""
  var lastName = ""
  var age = ""
  var sex = ""
  private val formRenderer = SHtml.idMemoize { renderer =>
    ".firstName" #> SHtml.text(firstName, firstName = _) &
    ".lastName" #> SHtml.text(lastName, lastName = _) &
    ".age" #> SHtml.text(age, age = _) &
    ".sex" #> SHtml.select(Seq(("M", "Male"), ("F", "Female")), Full("Male"), sex = _) &
    ".save" #> SHtml.ajaxOnSubmit(() => {
      val user = User(firstName, lastName, age, sex)
      Users.users += user
      S.notice(Users.users(0).firstName + " " + Users.users(0).lastName + " " + Users.users(0).age)
      renderer.setHtml() & usersRenderer.setHtml()
    })
  }

  private val usersRenderer = SHtml.idMemoize { renderer =>
    ".users" #> Users.users.map{ user =>
      ".firstName *" #> user.firstName &
      ".lastName *" #> user.lastName &
      ".age *" #> user.age &
      ".sex *" #> user.sex
    }
  }


  def render = {
    SHtml.makeFormsAjax andThen
    "#container" #> formRenderer

  }

  def renderUsers = {
    "#users" #> usersRenderer
  }
}