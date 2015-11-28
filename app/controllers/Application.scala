package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.data.Form
import play.api.data.Forms._
import models.{Person,DB}

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("La nueva aplicacion esta lista."))
  }

  val formPerson: Form[Person] = Form{
      mapping(
          "name" -> text,
          "age" -> number
          )(Person.apply)(Person.unapply)
  }


  def addPerson = Action{ implicit request =>
    val persons = formPerson.bindFromRequest.get
    DB.save(persons)
    Redirect(routes.Application.index)    
  }
  
  


def getPersons = Action {
      val persons = DB.query[Person].fetch
      Ok(Json.toJson(persons))
}

def getByAge(a:Int) = Action {
    val persons = DB.query[Person].fetch
    val (minors, adults) =  persons partition (_.age <18)
    if (a==1) Ok(Json.toJson(adults)) else Ok(Json.toJson(minors))
  }

}
