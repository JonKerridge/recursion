package mains

import seqQS.QSData
import seqQS.QSResult
import seqQS.QSWorker

class SeqQSMain {
  public static void main(String[] args) {
    int instances

    if (args.size() == 0) {
      instances = 1048576
    } else {
      instances = Integer.parseInt(args[0])
    }

    int workers = 1
      print "SeqSort, $instances, "

      System.gc()
      def startime = System.currentTimeMillis()

      QSData qsd = new QSData()
      qsd.initClass([instances, workers])
      def qsw = new QSWorker()
      def qsr = new QSResult()
      for (i in 0..<instances) {
        qsd = new QSData()
        qsd.createInstance(null)
        qsw.iFunc(null, qsd)
      }
      qsw.wFunc()
      def sortedBuffer = qsw.qsBuffer
      for (i in 0..<instances) qsr.collector(sortedBuffer[i])
      qsr.finalise(null)

      def endtime = System.currentTimeMillis()
      println " ${((endtime - startime)/1000)}"


    }
}
