package mains

import GPP_Library.DataDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitSingle
import jcsp.lang.CSProcess
import jcsp.lang.Channel
import jcsp.lang.Parallel
import quickSort.QSRecursiveWorker
import quickSort.qsResult
import quickSort.qsData
import recursion.RecursionEngine

class ParQSMain {
  public static void main(String[] args) {
    int elements, splitCount, maxDepth

    if (args.size() == 0){
      elements = 1048576
      splitCount = 2
      maxDepth = 6
    }
    else {
      elements = Integer.parseInt(args[0])
      splitCount = Integer.parseInt(args[1])
      maxDepth = Integer.parseInt(args[2])
    }

    int totalProcesses = Math.pow(splitCount, maxDepth) - 1
    int leafProcesses = Math.pow(splitCount, maxDepth - 1)
    System.gc()
    print "recursiveQS, $elements, "
    print "depth, $maxDepth, totalPs, $totalProcesses, leafPs, $leafProcesses, "
    def startime = System.currentTimeMillis()

    def emitterDetails = new DataDetails(dName: qsData.getName(),
        dInitMethod: qsData.init,
        dInitData: [elements],
        dCreateMethod: qsData.create)

    def resultDetails = new ResultDetails(rName: qsResult.getName(),
        rInitMethod: qsResult.init,
        rCollectMethod: qsResult.collector,
        rFinaliseMethod: qsResult.finalise)

    def workerDetails = new LocalDetails( lName: QSRecursiveWorker.getName(),
        lInitData: [0, elements - 1]) //initial parameters

    def chan1 = Channel.one2one()
    def chan2 = Channel.one2one()

    def emitter = new EmitSingle(output: chan1.out(),					// was EmitSingle
        eDetails: emitterDetails)

    def recursionNetwork = new RecursionEngine(
        type: 0,
        input: chan1.in(),
        output: chan2.out(),
        lDetails: workerDetails,
        splitCount: splitCount,
        maxDepth: maxDepth,
        workMethod1: QSRecursiveWorker.workMethod)

    def collector = new Collect(input: chan2.in(),
        rDetails: resultDetails)

    new Parallel ([emitter, recursionNetwork, collector] as CSProcess[]).run()
    def endtime = System.currentTimeMillis()
    println "${((endtime - startime)/1000)} "
  }
}
