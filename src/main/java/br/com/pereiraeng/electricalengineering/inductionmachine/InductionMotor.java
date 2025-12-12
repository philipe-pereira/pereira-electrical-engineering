package br.com.pereiraeng.electricalengineering.inductionmachine;

import br.com.pereiraeng.electricalcircuit.solving.CircuitCalc;
import br.com.pereiraeng.math.Complex;

public class InductionMotor {

	/**
	 * Função que calcula, para vários valores de escorregamento, o valor do torque
	 * produzido pelo motor síncrono
	 * 
	 * @param nf número de fases
	 * @param v1 tensão de fase de armadura
	 * @param r1 resistência dos enrolamento da armadura
	 * @param r2 resistência dos enrolamentos do rotor
	 * @param x1 reatância de dispersão dos enrolamentos da armadura
	 * @param x2 reatância de dispersão dos enrolamentos do rotor
	 * @param xm reatância de magnetização
	 * @param fo frequência na qual as reatâncias foram medidas, em Hertz
	 * @param fs frequência síncrona, em Hertz
	 * @param s  vetor com os escorregamentos da máquina
	 * @param p  número de pólos
	 * @return
	 */
	public static double[][] tm(int nf, double v1, double r1, double r2, double x1, double x2, double xm, double fo,
			double fs, double[] s, int p) {
		double[] ts = new double[s.length], fm = new double[s.length];
		// pares de pólos
		int pp = p / 2;
		// rotação síncrona
		double ns = fs / pp;

		for (int i = 0; i < s.length; i++) {
			fm[i] = (1 - s[i]) * ns;

			Complex zm = new Complex(0, xm * fs / fo);
			Complex z1 = new Complex(r1, x1 * fs / fo);

			Complex z1eq = CircuitCalc.parallel(z1, zm);

			Complex v1eq = Complex.mult(new Complex(v1, 0), Complex.div(zm, Complex.sum(z1, zm)));

			double v1eq2 = v1eq.getMod();

			double r1eq = z1eq.getRe();
			double x1eq = z1eq.getIm();

			if (s[i] != 0.)
				ts[i] = (1 / (2 * Math.PI * fs)) * (nf * v1eq2 * (r2 / s[i]))
						/ (Math.pow(r1eq + r2 / s[i], 2) + Math.pow(x1eq + x2 * fs / fo, 2));
			else
				ts[i] = 0.;
		}
		return new double[][] { fm, ts };
	}
}
