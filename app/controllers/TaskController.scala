package controllers

import models.Task

import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api.Html
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class TaskController @Inject()(val cc: MessagesControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {


  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
    def tasks = Action { implicit request: Request[AnyContent] => {
        val allTasks = Task.all()
        render {
          case Accepts.Json() => Ok(Json.toJson(allTasks))
          case Accepts.Html() => Ok(views.html.tasks(allTasks, TaskForm.form))
        }
      }
    }

    def newTask = Action { implicit request =>
      taskForm.bindFromRequest.fold(
        errors => BadRequest(views.html.tasks(Task.all(), TaskForm.form)),
        x=>x match { case(label,who,body) => {
          val today = Calendar.getInstance().getTime()
          val timeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
          val time = timeFormat.format(today)
          
          Task.create(label, who, time, body)
          Redirect(routes.TaskController.tasks)
          }
        }
      )
    }

    def editTask(id: Long) = Action { implicit request =>
      taskForm.bindFromRequest.fold(
        errors => BadRequest(views.html.tasks(Task.all(), TaskForm.form)),
        x=>x match { case(label,who,body) => {
          val today = Calendar.getInstance().getTime()
          val timeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
          val time = timeFormat.format(today)
          
          Task.update(id, label, who, time, body)
          Redirect(routes.TaskController.tasks)
          }
        }
      )
    }

    def deleteTask(id: Long) = Action {
      Task.delete(id)
      Redirect(routes.TaskController.tasks)
    }

    def completeTask(id: Long) = Action {
      Task.complete(id)
      Redirect(routes.TaskController.tasks)
    }

    val taskForm = Form(
      tuple (
        "label" -> nonEmptyText,
        "who" -> nonEmptyText,
        "body" -> nonEmptyText
      )
    )

}
