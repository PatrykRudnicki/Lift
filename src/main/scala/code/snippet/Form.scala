package code.snippet

import net.liftweb._
import net.liftweb.common.Full
import net.liftweb.http._
import net.liftweb.util.Helpers._

import scala.collection.mutable.ArrayBuffer

/**
  * Created by patry on 20.11.2016.
  */
class Form {
  var firstName = ""
  var lastName = ""
  var age = ""
  var sex = ""
  var searchField = ""
  var choice = ""
  var searchedUsers: ArrayBuffer[User] = ArrayBuffer[User]()

  private val formRenderer = SHtml.idMemoize { renderer =>
    ".firstName" #> SHtml.text(firstName, firstName = _) &
    ".lastName" #> SHtml.text(lastName, lastName = _) &
    ".age" #> SHtml.text(age, age = _) &
    ".sex" #> SHtml.select(Seq(("M", "Male"), ("F", "Female")), Full("Male"), sex = _) &
    ".save" #> SHtml.ajaxOnSubmit(() => {
      val user = User(firstName, lastName, age, sex)
      Users.users += user
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

  private val usersSearch = SHtml.idMemoize { renderer =>
    ".search" #> SHtml.text(searchField, searchField = _) &
      ".save" #> SHtml.ajaxOnSubmit(() => {
        searchedUsers = Users.users.filter(_.lastName == searchField)
        renderer.setHtml() & searchRenderer.setHtml()
      })
  }

  private val usersSearch2 = SHtml.idMemoize { renderer =>
    ".choice" #> SHtml.select(Seq(("first", "firstName"), ("last", "lastName"), ("age", "age"), ("sex", "sex")), Full("firstName"), choice = _) &
    ".search" #> SHtml.text(searchField, searchField = _) &
    ".save" #> SHtml.ajaxOnSubmit(() => {
      choice match{
        case "firstName" => searchedUsers = Users.users.filter(_.firstName == searchField)
        case "lastName" => searchedUsers = Users.users.filter(_.lastName == searchField)
        case "age" => searchedUsers = Users.users.filter(_.age == searchField)
        case "sex" => searchedUsers = Users.users.filter(_.sex == searchField)
      }
      renderer.setHtml() & searchRenderer.setHtml()
    })
  }

  private val searchRenderer = SHtml.idMemoize { renderer =>
    ".users" #> searchedUsers.map{ user =>
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

  def search = {
    SHtml.makeFormsAjax andThen
    "#search" #> usersSearch
  }

  def search2 = {
    SHtml.makeFormsAjax andThen
    "#search2" #> usersSearch2
  }
}