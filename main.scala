/*  postgresCalHealth
    Last update 13 March 2013
5 March 2014 -
  First IntelliJ scala project -
    Oughta deal with servers, using sbt, slick, and postgres
    1. Got the postgres setup, pita.
    2. Gotta learn sbt. But slick should work automajically.
    3. slick is the overlay for sql access/handling

*/

/*

Notes on the postgres serv.
	Filling the server: initdb -D /Users/lucas/Projects/scala1/server
	Starting the server: pg_ctl -D /Users/lucas/Projects/scala1/server -l logfile start
	Stopping the server: pg_ctl -D /Users/lucas/Projects/scala1/server stop

  Using postgres and Slick

*/

/* Architecture -
  Each individual has a different file and associated ID number that should
    allow quick searching through the DB when the info needs to be pulled.
  The ID number will be associated to a username/email in a list or map =>
    IDs['hannah@gmail.com'] yields 1234;
    infoblock = DB query 1234
    //infoblock will contain all the user's data, that will then be sent
    // via packaged JSON to the client?
    // The package will either be info from a certain day, or a block of data
    // that the client will parse to be displayed.
  The info will be cleared from the scala portion of the code.

*/

///Users/lucas/Projects/scala1/src/main.scala

//import scala.slick.driver.PostgresDriver._
//import scala.slick.session.Database
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
//import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import Database.dynamicSession


object main {
//                                    Date,         food ratio, exercise, booze, last update
//                                    "DD/MM/YYYY", 1.0,        true,     true,  java_format_datetime
  class DayInfo(tag: Tag) extends Table[(String, Double, Boolean, Boolean, String)](tag, "DAY") {
    def date = column[String]("DATE")//, O.PrimaryColumnKey)
    def food = column[Double]("FOOD")
    def exercise = column[Boolean]("EXERCISE")
    def booze = column[Boolean]("BOOZE")
    def last_update = column[String]("LAST_UPDATE")
    def * = (date, food, exercise, booze, last_update)
  }

  //def createDay

  def main(args: Array[String]) {
    println("22222")
    Class.forName("org.postgresql.Driver")
    val cxn = Database.forURL("jdbc:postgresql://localhost:3333/template1", driver = "org.postgresql.Driver")
    println(cxn)
    val DaysTable = TableQuery[DayInfo]

    println("tryna enter db")
    // put info in
    cxn withSession {
      implicit session: Session =>
      println("entered db")
      println("creating table")
      //DaysTable.ddl.create

      println("created table")
      //DaysTable ++= Seq(("1/1/2014", 1.0, true, true, "1_Jan_2014"))
      //DaysTable += ("2/2/2014", 2.0, false, false, "2_Feb_2014")
      println("info should be in?")
      DaysTable foreach { case (date, food, exercise, booze, last_update) =>
        println("date " + date, "food " + food)
      }
      println("done printing info")
    // take info out
    }

    println("trying external query method")
    val query = for (c <- DaysTable) yield c.date
    val result = cxn.withDynSession { implicit session: Session =>
      query.list
      println(query.list)
    }

    println("DONE END")
  }
}