package prototypeQS5C
import java.util.List;
import groovy.transform.CompileStatic

@CompileStatic
class qsData extends GPP_Library.DataClass {

  int index
  String dataValue
  static int elements = 1024
  static String init = "initClass"
  static String create = "createInstance"
  static int [] array

  int initClass ( List d){
    elements = d[0]
    Random r = new Random()
    array = r.ints(elements, 0, 2048).toArray();
//    println"array: $array"
    return completedOK
  }

  //int myInstance
  int createInstance (List d){
      return normalTermination
  }

//  String toString(){
//    String s = "QSData: $index, $dataValue"
//    return s
//  }

}