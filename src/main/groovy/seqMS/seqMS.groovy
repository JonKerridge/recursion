package seqMS

int instances

if (args.size() == 0) {
	instances = 1048576
} else {
	instances = Integer.parseInt(args[0])
}

print "seqMSort, $instances, "

System.gc()
def startime = System.currentTimeMillis()

def msd = new msData()
msd.initClass([instances])
def msw = new msWorker()
def msr = new msResult()
for (i in 0..< instances) {
	msd = new msData()
	msd.createInstance(null)
	msw.iFunc(null, msd)
}
msw.wFunc()
def sortedBuffer = msw.msBuffer
msr.collector(sortedBuffer)
msr.finalise(null)

def endtime = System.currentTimeMillis()
println "${(int)((endtime - startime)/1000)}"
