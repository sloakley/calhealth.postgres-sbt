/*
  21 March 2014
  Simply holds update info
   Used in the XMLHandler.scala


*/
package XMLUpdateHolder


class XMLUpdateHolder(newDate:String, newFood:Double, newExercise:Boolean,
                      newBooze: Boolean, newLast_update:String) {

  var (date:String, food:Double, exercise:Boolean, booze:Boolean, last_update:String) =
    (newDate,newFood, newExercise, newBooze, newLast_update)

}