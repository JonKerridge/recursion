package mains

import seqMS.msData
import seqMS.msResult
import seqMS.msWorker

class SeqMSMain {
  public static void main(String[] args) {
    int instances

    if (args.size() == 0) {
      instances = 25000
    } else {
      instances = Integer.parseInt(args[0])
    }

    print "SeqMS, $instances, "

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
    println "${((endtime - startime)/1000)}"

  }
}
