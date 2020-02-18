package FFTPrototype
import groovyJCSP.*
import jcsp.lang.*

import GPP_Library.DataDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitSingle
import FFTPrototype.fftData as fftd
import FFTPrototype.fftResult as fftr
import recursion.RecursionEngine

def instances, n, splitCount, maxDepth

if (args.size() == 0){
	instances = 1
	n = 8
	splitCount = 2
	maxDepth = 12
}
else {
	instances = Integer.parseInt(args[0])
	n = Integer.parseInt(args[1])
	splitCount = Integer.parseInt(args[2])
	maxDepth = Integer.parseInt(args[3])
}

int totalProcesses = Math.pow(splitCount, maxDepth) - 1
int leafProcesses = Math.pow(splitCount, maxDepth - 1)

print "FFT recursive - instances: $instances, n: $n maxDepth: $maxDepth \n"

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

//def recursionInit = new RecursionInit3(input: chan1.in(),
//						       		   output: chan2.out(),
//									   lDetails: workerDetails,
//									   splitCount: splitCount,
//									   maxDepth: maxDepth,
//									   workMethod1: FFTRecursiveWorker.workMethod1,
//									   workMethod2: FFTRecursiveWorker.workMethod2)

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
println "${endtime - startime}"