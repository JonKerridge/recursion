package quickSort
//@CompileStatic
class QSRecursiveWorker extends GPP_Library.DataClass {
	
	static String workMethod = "quickSortRun"
	
	// params contains [dataSet, [parameters]]
	List quickSortRun(List params) {
		
		// If statement tests base case
		if (params[1][0] < params[1][1]) {
			
			// No base case reached
			Integer splitPoint = partition(params[0][0], params[1][0], params[1][1])
			return [true, [params[1][0], splitPoint-1], [splitPoint+1, params[1][1]]]
		} else {
			
			// Base case reached
			return [false]
		}
	}

	Integer partition(qsData obj, Integer first, Integer last) {
		Integer pivotValue = obj.array[first]
		Integer left = first+1
		Integer right = last
		boolean done = false
		while (!done ) {
			 while ((left <= right) && (obj.array[left]<=pivotValue)) left = left + 1
			 while ((obj.array[right] >= pivotValue) && (right >= left)) right = right - 1
			 if (right < left)
				 done = true
			 else
				obj.array.swap(left, right)
		}
		obj.array.swap(first, right)
		return right
	 }
}