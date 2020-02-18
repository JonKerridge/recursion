package recursion

import GPP_Library.DataClass
import GPP_Library.LocalDetails
import GPP_Library.UniversalTerminator
import jcsp.lang.*

class RecursionInit1 extends DataClass implements CSProcess {
 
    ChannelInput input
    ChannelOutput output
    One2OneChannel toManager = Channel.one2one()
     
    LocalDetails lDetails
    String workMethod1
    String workMethod2
    int splitCount
         
    Parallel par
    Object dataset = [] 
    int maxDepth
    int nTotalProcesses
    CSProcess[] processes
         
    public void run() {
        //read in dataSet from Emit
        def o = input.read()
        while ( ! (o instanceof UniversalTerminator) ) {
            dataset << o
            o = input.read()
        }
        //set up network
        nTotalProcesses = Math.pow(splitCount, maxDepth)
        List<Any2AnyChannel> Parent2ChildList = []
        List<Any2OneChannel> Child2ParentList = []
         
        processes = new CSProcess[nTotalProcesses]

        int index1, index3 , index2
        index2 = 1
        index3 = 0
         
        --maxDepth //to fit 0-based iteration
        0.upto(maxDepth) { depth ->
            //number of new channels and processes
            int nProcesses = Math.pow(splitCount, depth)
             
            for (int i = 0; i < nProcesses; ++i) {               
                Parent2ChildList << Channel.any2any()
                Child2ParentList << Channel.any2one()
            }
             
            for (int i = 0; i < nProcesses; i = i + splitCount) {
                if (depth == 0) { //initial RP                  
                    def rp = new RecursiveProcess1(lDetails: lDetails,
                                                   workMethod1: workMethod1,
                                                   workMethod2: workMethod2 == "" ? "" : workMethod2,
                                                   dataset: dataset,
                                                   depth: depth,
                                                   splitCount: splitCount,
                                                   maxDepthReached: depth == maxDepth ? true : false,
                                                   parameters: lDetails.lInitData,
                                                   //channels
                                                   toParent: toManager.out(),
                                                   toChildren: Parent2ChildList[0].out(),
                                                   fromChildren: Child2ParentList[0].in())
                    processes[index3++] = rp
                    continue
                } else {
                    for (int j = 0; j < splitCount; ++j) {                       
                        def rp = new RecursiveProcess1(lDetails: lDetails,
                                                       workMethod1: workMethod1,
                                                       workMethod2: workMethod2 == "" ? "" : workMethod2,
                                                       dataset: dataset,
                                                       depth: depth,
                                                       splitCount: splitCount,
                                                       maxDepthReached: depth == maxDepth ? true : false,
                                                       parameters: lDetails.lInitData,
                                                       //channels
                                                       fromParent: Parent2ChildList[index1].in(),
                                                       toParent: Child2ParentList[index1].out(),
                                                       toChildren: Parent2ChildList[index2].out(),
                                                       fromChildren: Child2ParentList[index2].in(),
//                                                       p2c: Parent2ChildList[index1]
                        )
                        processes[index3++] = rp
                        index2++
                    }
                }
                index1++
            }
        }
        def rm = new RecursionManager(fromRP: toManager.in(),
                                      output: output,
                                      processes: processes)
        processes[index3] = rm
        par = new Parallel(processes)
        par.run()
    }
}