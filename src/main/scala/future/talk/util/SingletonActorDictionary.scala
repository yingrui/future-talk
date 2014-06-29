package future.talk.util

import scala.collection.mutable

object SingletonActorDictionary {

  val dictionary = mutable.Map[String, String]()

  def apply[T <: Any](classOf: Class[T]): String = dictionary(classOf.toString)
  def update[T <: Any](classOf: Class[T], path: String): Unit = dictionary += (classOf.toString -> path)

}
