package models

import play.api.libs.json._

case class Task(id: Long, label: String, who: String, mytime: String, body: String, var ready: Short)

object Task {

  private var nextIndex = 3L

  private val todos = scala.collection.mutable.Map(
        1L -> Task(1, "Add more items", "YK", "1", "body1", 0),
        2L -> Task(2, "Add more items", "YK", "1231114", "body2", 0)
    )

  def all(): Iterable[Task] = todos.values

  def create(label: String, who: String, time: String, body: String) {
      todos += (nextIndex -> Task(nextIndex, label, who, time, body, 0))
      nextIndex+=1
  }

  def update(id: Long, label: String, who: String, time: String, body: String) {
      todos(id) = Task(id, label, who, time, body, 0)
  }

  def delete(id: Long) {
    todos -= id 
  }

  def complete(id: Long) {
    todos(id).ready = 1
  }

  
  implicit val taskWrites = new Writes[Task] {
    def writes(task: Task) = Json.obj(
      "id"  -> task.id,
      "label" -> task.label,
      "who" -> task.who,
      "mytime" -> task.mytime,
      "body" -> task.body,
      "ready" -> task.ready
    )
  }

}