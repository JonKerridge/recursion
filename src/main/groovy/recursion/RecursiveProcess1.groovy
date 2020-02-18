package recursion

import GPP_Library.DataClass
import GPP_Library.LocalDetails
import GPP_Library.UniversalTerminator
import jcsp.lang.Any2AnyChannel
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class RecursiveProcess1 extends DataClass implements CSProcess {

    ChannelOutput toParent
    ChannelInput fromParent
    ChannelOutput toChildren
    ChannelInput fromChildren
//    List<Any2AnyChannel> p2c
     
    String workMethod1
    String workMethod2
    def parameters = null
//	  def buffer = []
    Integer splitCount
         
    LocalDetails lDetails = null
     
    Object dataset
     
    int depth
     
    boolean maxDepthReached = false 
    boolean used = false
         
    void run() {        
         
        if (depth > 0)
            parameters = fromParent.read()
             
        used = true
         
        if ( ! ( parameters[0] instanceof UniversalTerminator) ) {
			
            def wc = null
            Class workerClass = Class.forName(lDetails.lName)
            wc = workerClass.newInstance()
             
            List newParameters = wc.&"$workMethod1"(parameters)
             
            //If new parameters[0] is true - no base case reached
            if (newParameters[0]) {
                if (!maxDepthReached) { //pass to next depth
                    // Send new parameters to child RPs
                    1.upto(splitCount) { i ->
                        toChildren.write(newParameters[i])
                    }
                    // Wait for finish message from child RPs
                    int readCount = 0
                    while (readCount < splitCount) {
                        fromChildren.read()
                        ++readCount
                    }
                     
                    //call second work method (merge in MS)
                    wc.&"$workMethod2"(dataset, newParameters[splitCount + 1])
                     
                } else { //cycle in this process
                     
                    def params1 = [newParameters[1], newParameters[2]]
                    def params2 = [newParameters[splitCount + 1]]
                    //while there are newParams to be processed
                    while (params1.size() > 0) {
                        def newParams = wc.&"$workMethod1"(params1[0])
                        if (newParams[0]) {
                            1.upto(splitCount) { i ->
                                params1 << newParams[i]
                            }
                            params2 << newParams[splitCount + 1]
                        }
                        params1.remove(params1[0])
                    }
                    while (params2.size() > 0) {
                        wc.&"$workMethod2"(dataset, params2.last())
                        params2.removeLast()
                    }
                }
            }
            if (depth != 0)
                toParent.write()
            else
                toParent.write(dataset)
        }
    }
}