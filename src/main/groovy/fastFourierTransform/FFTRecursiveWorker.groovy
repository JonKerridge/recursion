package fastFourierTransform

import GPP_Library.DataClass

// see
// https://en.wikipedia.org/wiki/Cooley%E2%80%93Tukey_FFT_algorithm
// and refined in https://introcs.cs.princeton.edu/java/97data/FFT.java.html

//@CompileStatic
class FFTRecursiveWorker extends DataClass {
	// recursive the only difference is that the code is split
	// into two methods fftrun([ List dataset, List [id] ]) and
	// combine([id, n, [even, odd] ])

	static String workMethod1 = "fftRun"
	static String workMethod2 = "combine"
	
	List fftRun(List params){ //dataSet list, param list (id)
		
		int n = ((fftData)((List)params[0])[0]).x.length
		
		//test basecase
		if (n != 1) {
			
			// radix 2 Cooley-Tukey FFT
			if (n % 2 != 0)
				throw new RuntimeException("n is not a power of 2")
			
			//fft of even terms
			Complex[] even = new Complex[n/2]
			for (int k = 0; k < n/2; k++) {
				even[k] = ((fftData)((List)params[0])[0]).x[2*k]
			}
			
			//fft of odd terms
			Complex[] odd = new Complex[n/2] //reuse array
			for (int k = 0; k < n/2; k++) {
				odd[k] = ((fftData)((List)params[0])[0]).x[2*k + 1]
			}
			
			def evenData = new fftData()
			evenData.x = even
			def oddData = new fftData()
			oddData.x = odd
			
			return [true, [ [[evenData], [0]], [[oddData], [1]] ], [params[1][0], n]] //bool, new params, return params
		} else { //base case
			return [false, [params[0], params[1][0]]]
		}
	}
	
	List combine(List params) { //id, n, [even, odd]
				
		Complex[] y = new Complex[params[1]]
		for (int k = 0; k < params[1]/2; k++) {
			double kth         = -2 * k * Math.PI / params[1]
			Complex wk         = new Complex(Math.cos(kth), Math.sin(kth))
            y[k]       		   = (params[2][0].x)[k].plus(wk.times((params[3][0].x)[k]))
            y[k + params[1]/2] = (params[2][0].x)[k].minus(wk.times((params[3][0].x)[k]))
        }
		def returnData = new fftData()
		returnData.x = y
        return [[returnData], params[0]]
	}
}