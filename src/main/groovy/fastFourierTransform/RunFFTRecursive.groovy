package fastFourierTransform
import jcsp.lang.*
import GPP_Library.DataDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitSingle
import fastFourierTransform.fftData as fftd
import fastFourierTransform.fftResult as fftr
import recursion.RecursionEngine

int instances, n, splitCount, maxDepth
// 2^14    15       16       17       18        19        20        21        22        23
// 16384   32768    65536    131072   262144    524288    1048576   2097152   4194304   8388608
// 16k     32k      64k      128k     256k      512k      1m        2m        4m        8m

instances = 1
if (args.size() == 0){
	n = 8388608
	splitCount = 2
	maxDepth = 6
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

def emitterDetails = new DataDetails(dName: fftd.getName(),
                                     dInitMethod: fftd.init,
                                     dInitData: [instances, n],
                                     dCreateMethod: fftd.create)

def resultDetails = new ResultDetails(rName: fftr.getName(),
                                      rInitMethod: fftr.init,
                                      rCollectMethod: fftr.collector,
                                      rFinaliseMethod: fftr.finalise)

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
println "${((endtime - startime)/1000)} "