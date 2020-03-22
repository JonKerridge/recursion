package mergeSort


import groovy.transform.CompileStatic

@CompileStatic
class msData extends GPP_Library.DataClass {

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
    return completedOK
  }

  int createInstance (List d){
      return normalTermination
  }

//  String toString(){
//    String s = "MSData: $index, $dataValue"
//    return s
//  }

}