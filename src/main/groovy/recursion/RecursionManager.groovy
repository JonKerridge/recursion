package recursion

import GPP_Library.DataClass
import GPP_Library.UniversalTerminator
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class RecursionManager extends DataClass implements CSProcess {
	
	ChannelInput fromRP
	ChannelOutput output
	ChannelOutput toRP

	
	Object dataset = []

	CSProcess[] processes = []
	def unusedProcesses = []
	
//	int splitCount
		
	void run() {
		dataset = fromRP.read()              //wait for finished message
		output.write(dataset)
		output.write(new UniversalTerminator())

		//Terminate unused processes
		0.upto(processes.length - 2) { i ->
			if (!processes[i].used)
				unusedProcesses << processes[i]
		}
		
		if (unusedProcesses.size() > 0) {
			0.upto(unusedProcesses.size() - 1) { i ->
				toRP = unusedProcesses[i].p2c.out()
				toRP.write([new UniversalTerminator()])
			}
		}
	}
}

//reads in the resulting dataSet, forwards to collector, terminates any unused processes