package future.talk

import spray.routing.HttpService
import scala.concurrent.{Promise, ExecutionContext, Future}
import scala.async.Async._
import ExecutionContext.Implicits.global

trait HealthCheckResource extends HttpService {

  val healthCheckRoute = {
    get {
      pathPrefix("health") {
        val p = Promise[String]()

        complete {
          async {
            println("Do something.")
            p.success("success")
          }

          p.future
        }
      }
    }
  }
}