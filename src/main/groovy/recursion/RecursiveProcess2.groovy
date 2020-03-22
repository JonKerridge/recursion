package recursion

import GPP_Library.DataClass
import GPP_Library.LocalDetails
import GPP_Library.UniversalTerminator
import jcsp.lang.Any2AnyChannel
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class RecursiveProcess2 extends DataClass implements CSProcess {

	ChannelOutput toParent
	ChannelInput fromParent
	ChannelOutput toChildren
	ChannelInput fromChildren
	Any2AnyChannel p2c
	
	String workMethod1
	String workMethod2
	def parameters = null
	def returnParameters = []
	def returnParametersTemp = []
	def returnParamCount = 2
	def returnMessage = []
	Integer splitCount
		
	LocalDetails lDetails = null
	
	Object dataset = []
	
	int depth
	
	boolean maxDepthReached = false	
	boolean used = false
		
	void run() {
		if (depth > 0)
			parameters = fromParent.read()
		
		used = true
		if ( ! ( parameters[0] instanceof UniversalTerminator) ) {
			def wc = null
			Class workerClass = Class.forName(lDetails.lName)
			wc = workerClass.newInstance()
			
			List newParameters = wc.&"$workMethod1"(parameters)
			
			//If new parameters[0] is true - no base case reached
			if (newParameters[0]) {
				if (!maxDepthReached) { //pass to next depth
					// Send new parameters to child RPs
					1.upto(splitCount) { i ->
						toChildren.write(newParameters[1][i - 1])
					}
					
					returnParameters = newParameters[2]
					
					// Wait for finish message from child RPs
					int readCount = 0
					while (readCount < splitCount) {
						returnParametersTemp << fromChildren.read()
						++readCount
					}
					
					//put return parameters into correct order					
					int sortCount = 0
					
					while (sortCount < returnParamCount) {						
						for (int i = 0; i < returnParamCount; i++) {
							for (int j = 0; j < returnParamCount; j++) {
								if (returnParametersTemp[j][1] == i) {									
									returnParameters << returnParametersTemp[j][0]
									sortCount++
									break
								}
							}
						}
					}
					//call second work method
					returnMessage = wc.&"$workMethod2"(returnParameters)
					
					if (depth != 0)
						toParent.write(returnMessage)
					else
						toParent.write(returnMessage[0])
					
				} else { //cycle in this process (sequential)
					def params1 = []
					1.upto(splitCount) { i ->
						params1 << newParameters[1][i - 1]
					}
					
					def params2Temp1 = [newParameters[2]]
					def params2Temp2 = [[]]
					
					//while there are newParams to be processed
					while (params1.size() > 0) {
						def newParams = wc.&"$workMethod1"(params1[0])
						if (newParams[0]) {
							1.upto(splitCount) { i ->
								params1 << newParams[1][i - 1]
							}
							params2Temp1 << newParams[2]
						} else {
							if ((params2Temp2.last() as List).size() == returnParamCount)
								params2Temp2.add([])
								
							params2Temp2.last() << newParams[1]
						}
						params1.remove(params1[0])
					}
					
					//2nd user function
					def params2 = []
					
					int idx1 = params2Temp1.size()
					int idx2 = params2Temp2.size()
					while (params2Temp1.size() > 0) {
						
						idx1 = idx1 - splitCount
						idx2 = idx2 - returnParamCount
						1.upto(splitCount) { i ->
							if (params2Temp1.size() > 0) {
								if (idx1 < 0)
									idx1 = 0
								if (idx2 < 0)
									idx2 = 0
								params2 = params2Temp1[idx1]
								params2Temp1.remove(idx1)
								
								for (int j = 0; j < returnParamCount; j++)
									params2 << params2Temp2[idx2][j][0]
								
								params2Temp2.remove(idx2)
								if (i == 1)
									params2Temp2.add([])
									
								params2Temp2.last() << wc.&"$workMethod2"(params2)
							}
						}
					}
					if (depth != 0)
						toParent.write(params2Temp2[0][0])
					else
						toParent.write(params2Temp2[0][0][0])
				}
			} else { //base case
				toParent.write(newParameters[1])
			}
		}
	}
}