package mergeSort

import GPP_Library.DataDetails
import GPP_Library.LocalDetails
import GPP_Library.ResultDetails
import GPP_Library.terminals.Collect
import GPP_Library.terminals.EmitSingle
import jcsp.lang.CSProcess
import jcsp.lang.Channel
import jcsp.lang.Parallel
import mergeSort.MSRecursiveWorker as msrw
import mergeSort.msData as msd
import mergeSort.msResult as msr
import recursion.RecursionEngine

int instances, splitCount, maxDepth

if (args.size() == 0) {
  instances = 1048576
  splitCount = 2
  maxDepth = 5
} else {
  instances = Integer.parseInt(args[0])
  splitCount = Integer.parseInt(args[1])
  maxDepth = Integer.parseInt(args[2])
}

int totalProcesses = Math.pow(splitCount, maxDepth) - 1
int leafProcesses = Math.pow(splitCount, maxDepth - 1)

print "RecursiveMS, $instances, "

def startime = System.currentTimeMillis()

def emitterDetails = new DataDetails(dName: msd.getName(),
    dInitMethod: msd.init,
    dInitData: [instances],
    dCreateMethod: msd.create)

def resultDetails = new ResultDetails(rName: msr.getName(),
    rInitMethod: msr.init,
    rCollectMethod: msr.collector,
    rFinaliseMethod: msr.finalise)

def workerDetails = new LocalDetails(lName: msrw.getName(),
    lInitData: [0, instances - 1])

def chan1 = Channel.one2one()
def chan2 = Channel.one2one()

def emitter = new EmitSingle(output: chan1.out(),
    eDetails: emitterDetails)

def recursionNetwork = new RecursionEngine(type: 1,
    input: chan1.in(),
    output: chan2.out(),
    lDetails: workerDetails,
    splitCount: splitCount,
    maxDepth: maxDepth,
    workMethod1: msrw.workMethod1,
    workMethod2: msrw.workMethod2)

def collector = new Collect(input: chan2.in(),
    rDetails: resultDetails)

new Parallel([emitter, recursionNetwork, collector] as CSProcess[]).run()
def endtime = System.currentTimeMillis()
print " ${((endtime - startime)/1000)}\n "
