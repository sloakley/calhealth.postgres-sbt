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

  def getFilesPaths(path:String): Array[java.io.File] = new File(path).listFiles()

  def newEntry(newDate:String, newFood:Double, newExercise:Boolean, newBooze: Boolean, newLast_update:String ) =
    <entry date={newDate}>
      <food>{newFood}</food>
      <exercise>{newExercise}</exercise>
      <booze>{newBooze}</booze>
      <last_update>{newLast_update}</last_update>
    </entry>

  def x() {
    var fl = getFilesPaths("/Users/lucas/Projects/scala1/Users/")
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
        println(newXML)
        val mergedXML = newXML.copy(child = newXML.child ++ newNode)
        println("merged:")
        println(mergedXML)
        scala.xml.XML.save("/Users/lucas/Projects/scala1/Users/Janet11.xml", mergedXML)

    }
  }

  def main(args: Array[String]) {
    val a = new XMLUpdateHolder("1/1/2014", 2.0, false, false, "1_Jan_2014")
    println("bbb)")
    println(a.date)
    a.prin()
  }
}