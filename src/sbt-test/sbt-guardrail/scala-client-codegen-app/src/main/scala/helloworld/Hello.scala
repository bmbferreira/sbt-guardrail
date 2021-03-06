package helloworld

import scala.concurrent.Future
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.implicits._
import com.example.clients.petstore.user.UserClient

object Hello {

  def buildUserClient(): UserClient = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val server = buildServer()

    implicit val actorSys = ActorSystem()
    implicit val materializer = ActorMaterializer()

    UserClient.httpClient(server)
  }

  private def buildServer(): HttpRequest => Future[HttpResponse] = {
    import com.example.servers.petstore.user._
    import akka.http.scaladsl.server.Route
    import akka.http.scaladsl.settings.RoutingSettings

    implicit val actorSys = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val routingSettings = RoutingSettings(actorSys)

    Route.asyncHandler(
      UserResource.routes(new DummyUserHandler())
    )
  }
}

class DummyUserHandler
  extends com.example.servers.petstore.user.UserHandler {

  import com.example.servers.petstore.user._
  import com.example.servers.petstore.definitions._
  import scala.collection._

  def createUser(respond: UserResource.createUserResponse.type)(body: User): scala.concurrent.Future[UserResource.createUserResponse] = ???
  def createUsersWithArrayInput(respond: UserResource.createUsersWithArrayInputResponse.type)(body: Vector[User]): scala.concurrent.Future[UserResource.createUsersWithArrayInputResponse] = ???
  def createUsersWithListInput(respond: UserResource.createUsersWithListInputResponse.type)(body: Vector[User]): scala.concurrent.Future[UserResource.createUsersWithListInputResponse] = ???
  def loginUser(respond: UserResource.loginUserResponse.type)(username: String, password: String): scala.concurrent.Future[UserResource.loginUserResponse] = ???
  def logoutUser(respond: UserResource.logoutUserResponse.type)(): scala.concurrent.Future[UserResource.logoutUserResponse] = ???
  def getUserByName(respond: UserResource.getUserByNameResponse.type)(username: String): scala.concurrent.Future[UserResource.getUserByNameResponse] = {
    val user = new User(
      id = Some(1234),
      username = Some(username),
      firstName = Some("First"),
      lastName = Some("Last"),
      email = Some(username + "@example.com"))
    Future.successful(UserResource.getUserByNameResponseOK(user))
  }
  def updateUser(respond: UserResource.updateUserResponse.type)(username: String, body: User): scala.concurrent.Future[UserResource.updateUserResponse] = ???
  def deleteUser(respond: UserResource.deleteUserResponse.type)(username: String): scala.concurrent.Future[UserResource.deleteUserResponse] = ???
}
