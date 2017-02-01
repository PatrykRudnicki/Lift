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

  def loadUsers: ArrayBuffer[User] = {
    if (searchField.isEmpty) {
      Users.users
    } else {
      choice match {
        case "firstName" => Users.users.filter(_.firstName == searchField)
        case "lastName" => Users.users.filter(_.lastName == searchField)
        case "age" => Users.users.filter(_.age == searchField)
        case "sex" => Users.users.filter(_.sex == searchField)
      }
    }
  }

  private val formRenderer = SHtml.idMemoize { renderer =>
    ".firstName" #> SHtml.text(firstName, firstName = _) &
    ".lastName" #> SHtml.text(lastName, lastName = _) &
    ".age" #> SHtml.text(age, age = _) &
    ".sex" #> SHtml.select(Seq(("M", "Male"), ("F", "Female")), Full("Male"), sex = _) &
    ".save" #> SHtml.ajaxOnSubmit(() => {
      if(!firstName.isEmpty && !lastName.isEmpty && !age.isEmpty){
        val user = User(firstName, lastName, age, sex)
        Users.users += user
      }
      renderer.setHtml() & usersRenderer.setHtml()
    })
  }

  private val usersRenderer = SHtml.idMemoize { renderer =>
    ".users" #> loadUsers.map { user =>
      ".firstName *" #> user.firstName &
      ".lastName *" #> user.lastName &
      ".age *" #> user.age &
      ".sex *" #> user.sex
    }
  }

  private val usersSearch = SHtml.idMemoize { renderer =>
    ".choice" #> SHtml.select(Seq(("firstName", "FirstName"), ("lastName", "LastName"), ("age", "Age"), ("sex", "Sex")), Full(choice), choice = _) &
    ".search" #> SHtml.text(searchField, searchField = _) &
    ".save" #> SHtml.ajaxOnSubmit(() => {
      renderer.setHtml() & usersRenderer.setHtml()
    })
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

}