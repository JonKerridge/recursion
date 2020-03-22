package seqMS

public class msWorker extends GPP_Library.DataClass {
	
	List <msData> msBuffer = []
	static String init = "nullInitialise"
	static String inFunction = "iFunc"
	static String workFunction = "wFunc"
	static String outFunction = "oFunc"
	
	int iFunc (List d, msData o) {
		msBuffer << o		
		return completedOK
	}
	
	int wFunc() {
		mergeSort(msBuffer, 0, msBuffer.size() - 1)
		return completedOK
	}
	
	/* l is for left index and r is right index of the
	 sub-array of arr to be sorted */
  void mergeSort(List<msData> arr, int l, int r) {
	  if (l < r)
	  {
		  // Same as (l+r)/2, but avoids overflow for
		  // large l and h
		  int m = l+(r-l)/2;
   
		  // Sort first and second halves
		  mergeSort(arr, l, m);
		  mergeSort(arr, m+1, r);
   
		  merge(arr, l, m, r);
	  }
  }
  
	void merge(List<msData> arr, int l, int m, int r)
	{
		int i, j, k;
		int n1 = m - l + 1;
		int n2 =  r - m;
	 
		/* create temp arrays */
		def L = [n1], R = [n2];
	 
		/* Copy data to temp arrays L[] and R[] */
		for (i = 0; i < n1; i++)
			L[i] = arr[l + i];
		for (j = 0; j < n2; j++)
			R[j] = arr[m + 1+ j];
	 
		/* Merge the temp arrays back into arr[l..r]*/
		i = 0; // Initial index of first subarray
		j = 0; // Initial index of second subarray
		k = l; // Initial index of merged subarray
		while (i < n1 && j < n2)
		{
			if (L[i].index <= R[j].index)
			{
				arr[k] = L[i];
				i++;
			}
			else
			{
				arr[k] = R[j];
				j++;
			}
			k++;
		}
	 
		/* Copy the remaining elements of L[], if there
		   are any */
		while (i < n1)
		{
			arr[k] = L[i];
			i++;
			k++;
		}
	 
		/* Copy the remaining elements of R[], if there
		   are any */
		while (j < n2)
		{
			arr[k] = R[j];
			j++;
			k++;
		}
	}
}
