package mains

import GPP_Library.DataDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitSingle
import jcsp.lang.CSProcess
import jcsp.lang.Channel
import jcsp.lang.Parallel
import recursion.RecursionEngine
import fastFourierTransform.fftData
import fastFourierTransform.fftResult
import fastFourierTransform.FFTRecursiveWorker

class ParFFTMain {
  public static void main(String[] args) {
    int instances, n, splitCount, maxDepth
// 2^14    15       16       17       18        19        20        21        22        23
// 16384   32768    65536    131072   262144    524288    1048576   2097152   4194304   8388608
// 16k     32k      64k      128k     256k      512k      1m        2m        4m        8m

    instances = 1
    if (args.size() == 0){
      n = 1048576
      splitCount = 2
      maxDepth = 5
    }
    else {
      n = Integer.parseInt(args[0])
      splitCount = Integer.parseInt(args[1])
      maxDepth = Integer.parseInt(args[2])
    }

    int totalProcesses = Math.pow(splitCount, maxDepth) - 1
    int leafProcesses = Math.pow(splitCount, maxDepth - 1)

    print "recursiveFFT, $n, $maxDepth, "

    def startime = System.currentTimeMillis()

    def emitterDetails = new DataDetails(dName: fftData.getName(),
        dInitMethod: fftData.init,
        dInitData: [instances, n],
        dCreateMethod: fftData.create)

    def resultDetails = new ResultDetails(rName: fftResult.getName(),
        rInitMethod: fftResult.init,
        rCollectMethod: fftResult.collector,
        rFinaliseMethod: fftResult.finalise)

    def workerDetails = new LocalDetails( lName: FFTRecursiveWorker.getName(),
        lInitData: [0]) //initial parameters

    def chan1 = Channel.one2one()
    def chan2 = Channel.one2one()

    def emitter = new EmitSingle(output: chan1.out(),
        eDetails: emitterDetails)

    def recursionNetwork = new RecursionEngine(type: 2,
        input: chan1.in(),
        output: chan2.out(),
        lDetails: workerDetails,
        splitCount: splitCount,
        maxDepth: maxDepth,
        workMethod1: FFTRecursiveWorker.workMethod1,
        workMethod2: FFTRecursiveWorker.workMethod2)

    def collector = new Collect(input: chan2.in(),
        rDetails: resultDetails)

    new Parallel ([emitter, recursionNetwork, collector] as CSProcess[]).run()
    def endtime = System.currentTimeMillis()
    println "${((endtime - startime)/1000)} "  }
}
