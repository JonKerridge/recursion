package mergeSort


import groovy.transform.CompileStatic

@CompileStatic
class msResult extends GPP_Library.DataClass {

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

  int collector (msData obj) {
	  count++
	  for (int i = 1; i < obj.array.size(); i++) {
		  count++
		  if (obj.array[i] < obj.array[i - 1]) {
			  overall = false
			  break
		  }
	  }
    return completedOK
  }

  int finalise (List d){
    print "$overall, $count,"
    return completedOK
  }

}