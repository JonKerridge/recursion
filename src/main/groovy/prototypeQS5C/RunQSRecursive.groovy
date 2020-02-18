package prototypeQS5C

import groovyJCSP.*
import jcsp.lang.*

import GPP_Library.DataDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitSingle
import recursion.RecursionEngine
import prototypeQS5C.qsData as qsd
import prototypeQS5C.qsResult as qsr

def elements, splitCount, maxDepth

if (args.size() == 0){
	elements = 1000000
	splitCount = 2
	maxDepth = 5
}
else {
	elements = Integer.parseInt(args[0])
	splitCount = Integer.parseInt(args[1])
	maxDepth = Integer.parseInt(args[2])
}

int totalProcesses = Math.pow(splitCount, maxDepth) - 1
int leafProcesses = Math.pow(splitCount, maxDepth - 1)

print "QuickSort recursive - elements, $elements,"
print "split, $splitCount, depth, $maxDepth, totalPs, $totalProcesses, leafPs, $leafProcesses, "

def startime = System.currentTimeMillis()

def emitterDetails = new DataDetails(dName: qsd.getName(),
                                     dInitMethod: qsd.init,
                                     dInitData: [elements],
                                     dCreateMethod: qsd.create)

def resultDetails = new ResultDetails(rName: qsr.getName(),
                                      rInitMethod: qsr.init,
                                      rCollectMethod: qsr.collector,
                                      rFinaliseMethod: qsr.finalise)

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
println "${endtime - startime}"