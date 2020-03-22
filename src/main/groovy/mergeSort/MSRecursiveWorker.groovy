package mergeSort

class MSRecursiveWorker extends GPP_Library.DataClass {
     
    static String workMethod1 = "sort1"
    static String workMethod2 = "sort2"
 
    // Main function that sorts arr[l..r] using
    // merge()
    List sort1(List<Integer> params) {
        if (params[0] < params[1]) {
            // Find the middle point
            int middle = (int)((params[0] + params[1])/2)
             
            return [true, [params[0], middle], [middle + 1, params[1]], [params[0], middle, params[1]]]
        } else {
            return [false]
        }
    }
     
    void sort2(List dataset, List<Integer> params) {
        //Merge sorted halves
        merge(dataset[0], params[0], params[1], params[2])
    }
     
    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    void merge(msData obj, int left, int middle, int right) {
        // Find sizes of two subarrays to be merged
        int size1 = middle - left + 1
        int size2 = right - middle
        List<msData> tempL = []
        List<msData> tempR = []
		
        /*Copy data to temp arrays*/
        for (int i=0; i < size1; ++i)
            tempL[i] = obj.array[left + i]
        for (int j=0; j < size2; ++j)
            tempR[j] = obj.array[middle + 1 + j] 
  
        /* Merge the temp arrays */
  
        // Initial indexes of first and second subarrays
        int i = 0, j = 0
  
        // Initial index of merged subarray array
        int k = left
        while (i < size1 && j < size2) {
            if (tempL[i] <= tempR[j]) {
                obj.array[k] = tempL[i]
                i++
            }
            else {
                obj.array[k] = tempR[j]
                j++
            }
            k++
        }
  
        /* Copy remaining elements of L[] if any */
        while (i < size1) {
            obj.array[k] = tempL[i]
            i++
            k++
        }
  
        /* Copy remaining elements of R[] if any */
        while (j < size2) {
            obj.array[k] = tempR[j]
            j++
            k++
        }
    }
}