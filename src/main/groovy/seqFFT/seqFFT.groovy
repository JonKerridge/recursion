package seqFFT

import fastFourierTransform.Complex
import fastFourierTransform.FFT

int n
if (args.size() == 0){
  n = 1048576
}
else {
  n = Integer.parseInt(args[0])
}
Complex[] x = new Complex[n];

for (int i = 0; i < n; i++) {
  x[i] = new Complex(i, 0);
//            x[i] = new Complex(-2*Math.random() + 1, 0);
}
// original checking data
//    x[0] = new Complex(-0.03480425839330703, 0)
//    x[1] = new Complex(0.07910192950176387, 0)
//    x[2] = new Complex(0.7233322451735928, 0)
//    x[3] = new Complex(0.1659819820667019, 0)

//    FFT.show(x, "x");
long startime = System.currentTimeMillis();
// FFT of x
Complex[] y = FFT.fft(x);
//    FFT.show(y, "y = fft(x)");
long endtime = System.currentTimeMillis();
println "seqFFT, $n, ${(int)((endtime - startime)/1000)}"
