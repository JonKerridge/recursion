package recursion

import GPP_Library.DataClass
import GPP_Library.LocalDetails
import GPP_Library.UniversalTerminator
import jcsp.lang.Any2AnyChannel
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class RecursiveProcess0 extends DataClass implements CSProcess {

	ChannelOutput toParent
	ChannelInput fromParent
	ChannelOutput toChildren
	ChannelInput fromChildren
//	List<Any2AnyChannel> p2c
	
	String workMethod1
	def parameters = []
	def buffer = []
	Integer splitCount
		
	LocalDetails lDetails
	
	Object dataSet
	
	int depth
	
	boolean maxDepthReached = false
	boolean used = false
	
	int id
		
	void run() {
		
		if (depth > 0)
			buffer = fromParent.read()
		
		used = true
		
		if ( ! ( buffer[0] instanceof UniversalTerminator) ) {
			if (dataSet != null)
				parameters.add(0, dataSet)
				
			parameters << buffer
			
			def wc = null
			Class workerClass = Class.forName(lDetails.lName)
			wc = workerClass.newInstance()
			List newParameters = wc.&"$workMethod1"(parameters)
			
			//If new params[0] is true - no base case reached
			if (newParameters[0]) {
				if (!maxDepthReached) { //pass to next depth
					1.upto(splitCount) { i ->
						def param = []
						0.upto((newParameters[i] as List).size() - 1) { j ->
							param << newParameters[i][j]
						}
						toChildren.write(param)
					}
					// Wait for finish message from child RPs
					int readCount = 0
					while (readCount < splitCount) {
						fromChildren.read()
						++readCount
					}
					
				} else { //cycle in this process
					
					def params = [newParameters[1], newParameters[2]]
					
					while (params.size() > 0) {
						def param = [dataSet]
						def param2 = []
						0.upto((params[0] as List).size() - 1) { i ->
							param2 << params[0][i]
						}
						param << param2
						def newParams = wc.&"$workMethod1"(param)
						if (newParams[0]) {
							1.upto(splitCount) { i ->
								params << newParams[i]
							}
						}
						params.remove(params[0])
					}
				}
			}
			if (depth != 0)
				toParent.write()
			else
				toParent.write(dataSet)
		}
	}
}