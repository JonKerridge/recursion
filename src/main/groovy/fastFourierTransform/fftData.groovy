package fastFourierTransform


import groovy.transform.CompileStatic

@CompileStatic
public class fftData extends GPP_Library.DataClass {
	static int instances
	static int instance = 1
	static int n = 1
	static String init = "initClass"
	static String create = "createInstance"
	Complex[] x
	
	int initClass(List d) {
		instances = d[0]
		n = d[1]
		return completedOK
	}
	
	int createInstance(List d) {
		x = new Complex[n]
		
		for (int i = 0; i < n; i++) {
			x[i] = new Complex(i, 0);
//			x[i] = new Complex(-2*Math.random() + 1, 0);
		}
		
//		x[0] = new Complex(-0.03480425839330703, 0);
//		x[1] = new Complex(0.07910192950176387, 0);
//		x[2] = new Complex(0.7233322451735928, 0);
//		x[3] = new Complex(0.1659819820667019, 0);
		
		return normalContinuation
	}
}

// 		VERIFICATION VALUES
//		x[0] = new Complex(0.03480425839330703, 0);
//		x[1] = new Complex(0.07910192950176387, 0);
//		x[2] = new Complex(0.7233322451735928, 0);
//		x[3] = new Complex(0.1659819820667019, 0);

//  0.9336118983487516
//  -0.7581365035668999 + 0.08688005256493803i
//  0.44344407521182005
//  -0.7581365035668999 - 0.08688005256493803i