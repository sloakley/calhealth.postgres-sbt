/*
  Last update: 21 March 2014

This is the xml handler that'll read and write information
  written to the DB.




*/


import scala.xml._
import java.net._
import scala.io.Source
import java.io.File
import java.io.FileReader
import XMLUpdateHolder.XMLUpdateHolder

import scala.xml.transform._

//import XMLUpdateHolder._


object main {

  val usersLoc:String = "/Users/lucas/Projects/scala1/Users/"

  def getFilesPaths(path:String): Array[java.io.File] = new File(path).listFiles()

  def newEntry(newDate:String, newFood:Double, newExercise:Boolean, newBooze: Boolean, newLast_update:String ) =
    <entry date={newDate}>
      <food>{newFood}</food>
      <exercise>{newExercise}</exercise>
      <booze>{newBooze}</booze>
      <last_update>{newLast_update}</last_update>
    </entry>


  // email, password, name
  def createNewUser(name:String, email:String, password:String) {
    // Here to the next marker is to update the Users.xml file
    val usersFileLoc = new File(usersLoc, "Users.xml").toString // store this for readablity's sake
    val usersFileXML = scala.xml.XML.loadFile(usersFileLoc)
    //check to see if the email already exists in the file
    usersFileXML match {
      case <Users>{nodes @ _*}</Users> =>
        if ((nodes.map(node => (node \ "email").text == email)).contains(true)) {
          println("contains")
          return
        }
    }
    val numberOfUsers = (usersFileXML \\ "@num").toString().toInt // get it from <Users num="5">
    val refinedChild = usersFileXML.child.filter(n => "'"+n.attributes!=""+"'")
    val refinedChildString =
      "<Users num="+'"'+(numberOfUsers+1).toString+'"'+">"+refinedChild.map(n => n.toString()).mkString+"</Users>"
    val newRefined = scala.xml.XML.loadString(refinedChildString)
    val newXMLID = <user id={numberOfUsers.toString}>
        <name>{name}</name>
        <email>{email}</email>
        <file>{numberOfUsers.toString+".xml"}</file>
      </user>
    val newXMLToWrite = newRefined.copy(child = newRefined.child ++ newXMLID)
    scala.xml.XML.save(usersFileLoc, newXMLToWrite) // SOLELY FOR THE Users.xml FILE!!!
    scala.xml.XML.save(new File(usersLoc, numberOfUsers.toString+".xml").toString,
      <entries></entries>) // write a new xml file for the userID.xml
  }



  def x() {
    var fl = getFilesPaths(usersLoc)
    var filteredFiles = fl.filter (_.toString.endsWith(".xml"))

    println(filteredFiles(1).toString())

    val comparisondate = "7/2/2014"

    val (newDate, newFood, newExercise, newBooze, newLast_update) = ("7/2/2014", 7.0, false, true, "6_June_2014")

    val filedXML = scala.xml.XML.loadFile(filteredFiles(1).toString())

    filedXML match {
      case <entries>{nodes @ _*}</entries> =>
        println("checking to delete duplicate date record")
        //filteredXML contains the nodes that dont match the date
        val filteredXML = nodes.filter(node => ((node \ "@date").text != comparisondate))
        //create the new entry to merge with the new node list
        val newNode = newEntry(newDate, newFood, newExercise, newBooze, newLast_update)
        //refine the filteredXML list to remove empty entries
        val refinedXML = filteredXML.filter(n => "'"+n.attributes!=""+"'")
        //create the new xml to write
        val newXMLString = "<entries>"+refinedXML.map(n => n.toString()).mkString+"\n"+"</entries>"
        val newXML = scala.xml.XML.loadString(newXMLString)
        val mergedXML = newXML.copy(child = newXML.child ++ newNode)
        scala.xml.XML.save("/Users/lucas/Projects/scala1/Users/Janet11.xml", mergedXML)

    }
  }

  def main(args: Array[String]) {
    val a = new XMLUpdateHolder("1/1/2014", 2.0, false, false, "1_Jan_2014")
    a.prin()
    println(new File(usersLoc, "Users.xml").toString)
    createNewUser("Lucas Oakley", "smiley.puma@gmail.com", "password")
  }
}