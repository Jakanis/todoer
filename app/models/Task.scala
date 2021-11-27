package models

import play.api.libs.json._

case class Task(id: Long, label: String, who: String, mytime: String, var ready: Short)

object Task {

  private var nextIndex = 3L

  private val todos = scala.collection.mutable.Map(
        1L -> Task(1, "Add more items", "YK", "1", 0),
        2L -> Task(2, "Add more items", "YK", "1231114", 0)
    )

  def all(): Iterable[Task] = todos.values

  def create(label: String, who: String, time: String) {
      todos += (nextIndex -> Task(nextIndex, label, who, time, 0))
      nextIndex+=1
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
      "ready" -> task.ready
    )
  }

}