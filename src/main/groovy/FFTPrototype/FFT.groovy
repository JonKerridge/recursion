package FFTPrototype

import groovy.transform.CompileStatic

//@CompileStatic
public class FFT {

    // compute the FFT of x[], assuming its length is a power of 2
    public static Complex[] fft(Complex[] x) {
		
        int n = x.length;
		
        // base case
        if (n == 1) {			
			return [x[0]] as Complex[]
        }

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) { throw new RuntimeException("n is not a power of 2"); }

        // fft of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + n/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
        	System.out.println(x[i]);
        }
        System.out.println();
    }
		
   /***************************************************************************
    *  Test client and sample execution
    *
    *  % java FFT 4
    *  x
    *  -------------------
    *  -0.03480425839330703
    *  0.07910192950176387
    *  0.7233322451735928
    *  0.1659819820667019
    *
    *  y = fft(x)
    *  -------------------
    *  0.9336118983487516
    *  -0.7581365035668999 + 0.08688005256493803i
    *  0.44344407521182005
    *  -0.7581365035668999 - 0.08688005256493803i
    *
    *  z = ifft(y)
    *  -------------------
    *  -0.03480425839330703
    *  0.07910192950176387 + 2.6599344570851287E-18i
    *  0.7233322451735928
    *  0.1659819820667019 - 2.6599344570851287E-18i
    *
    *  c = cconvolve(x, x)
    *  -------------------
    *  0.5506798633981853
    *  0.23461407150576394 - 4.033186818023279E-18i
    *  -0.016542951108772352
    *  0.10288019294318276 + 4.033186818023279E-18i
    *
    *  d = convolve(x, x)
    *  -------------------
    *  0.001211336402308083 - 3.122502256758253E-17i
    *  -0.005506167987577068 - 5.058885073636224E-17i
    *  -0.044092969479563274 + 2.1934338938072244E-18i
    *  0.10288019294318276 - 3.6147323062478115E-17i
    *  0.5494685269958772 + 3.122502256758253E-17i
    *  0.240120239493341 + 4.655566391833896E-17i
    *  0.02755001837079092 - 2.1934338938072244E-18i
    *  4.01805098805014E-17i
    *
    ***************************************************************************/

//    public static void main(String[] args) { 
//        //int n = Integer.parseInt(args[0]);
//    	int n = 4;
//
//        Complex[] x = new Complex[n];
//
//        // original data
////        for (int i = 0; i < n; i++) {
////            x[i] = new Complex(i, 0);
////            x[i] = new Complex(-2*Math.random() + 1, 0);
////        }
//		x[0] = new Complex(-0.03480425839330703, 0)
//		x[1] = new Complex(0.07910192950176387, 0)
//		x[2] = new Complex(0.7233322451735928, 0)
//		x[3] = new Complex(0.1659819820667019, 0)
//		
//        show(x, "x");
//        long start = System.currentTimeMillis();
//        // FFT of original data
//        Complex[] y = fft(x);
//        show(y, "y = fft(x)");
//        long end = System.currentTimeMillis();
//        System.out.println("Sequential FFT - $n\n" + (end - start));
//    }
	
	public static void main(String[] args) {
		long numRuns = 131072;
		long total = 0;
		long avg = 0;		
		
		for (int i = 0; i < numRuns; i++) {
			Complex[] x = new Complex[1];
			x[0] = new Complex(-2*Math.random() + 1, 0);
			
			long start = System.nanoTime();
			Complex[] y = fft(x);
			long end = System.nanoTime();
			total += (end - start);
		}
		avg = total / numRuns;
		System.out.println("total: " + (total / 1000000000) + "s")
		System.out.println("numRuns: " + numRuns)
		System.out.println("avg: " + avg)
		System.out.println("microseconds: " << avg / 1000)
	}
}

/******************************************************************************
 *  Compilation:  javac FFT.java
 *  Execution:    java FFT n
 *  Dependencies: Complex.java
 *
 *  Compute the FFT and inverse FFT of a length n complex sequence
 *  using the radix 2 Cooley-Tukey algorithm.

 *  Bare bones implementation that runs in O(n log n) time. Our goal
 *  is to optimize the clarity of the code, rather than performance.
 *
 *  Limitations
 *  -----------
 *   -  assumes n is a power of 2
 *
 *   -  not the most memory efficient algorithm (because it uses
 *      an object type for representing complex numbers and because
 *      it re-allocates memory for the subarray, instead of doing
 *      in-place or reusing a single temporary array)
 *
 *  For an in-place radix 2 Cooley-Tukey FFT, see
 *  https://introcs.cs.princeton.edu/java/97data/InplaceFFT.java.html
 *
 ******************************************************************************/