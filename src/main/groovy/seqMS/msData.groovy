package seqMS
import java.util.List;
import groovy.transform.CompileStatic

@CompileStatic
public class msData extends GPP_Library.DataClass {

  int index
  String dataValue
  static int instance = 1
  static int instances = 1024
  static String init = "initClass"
  static String create = "createInstance"
  static int [] randomNumbers

  int initClass ( List d){
    instances = d[0]
    Random r = new Random()
    randomNumbers = r.ints(instances + 1, 0, 2048).toArray();
    return completedOK
  }

  int myInstance
  int createInstance (List d){
    if ( instance > instances) return normalTermination
    else {
      index = randomNumbers[instance]
      myInstance = instance
      dataValue = "Instance $myInstance"
      instance = instance + 1
      return normalContinuation
    }
  }

//  String toString(){
//    String s = "MSData: $index, $dataValue"
//    return s
//  }

}