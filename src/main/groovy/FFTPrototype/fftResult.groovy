package FFTPrototype
import java.util.List;

import groovy.transform.CompileStatic

@CompileStatic
class fftResult extends GPP_Library.DataClass {

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

  int collector (fftData data) {
	  show(data, "y = fft(x)")
    return completedOK
  }

  int finalise (List d){
    //print " $overall, $count "
    return completedOK
  }
  
  // display an array of Complex numbers to standard output
  public static void show(fftData data, String title) {
	  System.out.println(title);
	  System.out.println("-------------------");
	  for (int i = 0; i < data.x.length; i++) {
		  System.out.println(data.x[i]);
	  }
	  System.out.println();
  }
}