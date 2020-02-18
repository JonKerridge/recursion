package recursion

import GPP_Library.LocalDetails
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class RecursionEngine implements CSProcess{

  ChannelInput input
  ChannelOutput output
  LocalDetails lDetails = null
  int splitCount = -1
  int maxDepth = -1
  String workMethod1 = ""
  String workMethod2 = ""
  int type = -1

  @Override
  void run() {
    def recursionInit
    assert ((type >= 0) && ( type <= 2)) : "Recursion type must be 0, 1 or 2"
    assert (workMethod1 != ""): "Recursion workMethod1 not specified"
    if ( type == 0 ) assert (workMethod2 == "") : "Recursion workMethod2 is $workMethod2 but should be null because type is 0"
    if ( type > 0 ) assert (workMethod2 != "") : "Recursion workMethod2 null but must be set because type is 1 or 2"
    assert (splitCount > 0): "Recursion splitCount must be specified"
    assert (maxDepth > 0): "Recursion maxDepth must be specified"
    assert (lDetails != null): "Recursion lDetails worker class must be specified"
    switch (type) {
      case 0: //in place
        recursionInit = new RecursionInit0(input: input,
            output: output,
            lDetails: lDetails,
            splitCount: splitCount,
            maxDepth: maxDepth,
            workMethod: workMethod1)
        break
      case 1: //not in place, no data returned up network
        recursionInit = new RecursionInit1(input: input,
            output: output,
            lDetails: lDetails,
            splitCount: splitCount,
            maxDepth: maxDepth,
            workMethod1: workMethod1,
            workMethod2: workMethod2)
        break
      case 2: //not in place, data returned up network
        recursionInit = new recursion.RecursionInit2(input: input,
            output: output,
            lDetails: lDetails,
            splitCount: splitCount,
            maxDepth: maxDepth,
            workMethod1: workMethod1,
            workMethod2: workMethod2)
        break
      default:
        break
    }
    recursionInit.run()
  }
}

