package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import models.Task

object Application extends Controller {
  
  val taskForm = Form(
	"url" -> nonEmptyText
	)
  
  def index = Action {
    Redirect(routes.Application.tasks)
  }
  
  def tasks = Action {
	Ok(views.html.index(Task.all(), taskForm))
  }
  
  def newTask = Action { implicit request =>
 
	taskForm.bindFromRequest.fold(
		errors => BadRequest(views.html.index(Task.all(), errors)),
		url  => {
			Task.create(url.hashCode(), url)
			Redirect(routes.Application.tasks)
			}
		)
	}


  val RetrieveForm = Form(
    "hash" -> nonEmptyText
  )

	def getTask = Action {  implicit request =>

    RetrieveForm.bindFromRequest.fold(
      errors => BadRequest("getTask: hash value is required but not submitted"),
      hash => {
        val url: Option[String] = Task.getTask(hash)
        println("url = " + url)

        url match {
          case Some(url) => Redirect(url)
          case None => NotFound("This URL leads nowhere. Nice try. :(")
        }
      }
    )
	}
	
  def deleteTask(hash: Int) = Action {
	Task.delete(hash)
	Redirect(routes.Application.tasks)
  }
}