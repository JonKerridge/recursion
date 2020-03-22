package seqMS
import java.util.List;

import groovy.transform.CompileStatic

@CompileStatic
public class msResult extends GPP_Library.DataClass {

  boolean overall = true
  static String init = "initClass"
  static String collector = "collector"
  static String finalise = "finalise"

  int initClass ( List d){
    return completedOK
  }
  int previous = -1 // less than minimum key
  boolean stillTesting = true
  static int count = 0

  int collector (List<msData> list) {
	  count++
	  for (int i = 1; i < list.size(); i++) {
		  count++
		  if (list[i].index < list[i - 1].index) {
			  overall = false
			  break
		  }
	  }
    return completedOK
  }

  int finalise (List d){
    print " $overall, $count, "
    return completedOK
  }

}