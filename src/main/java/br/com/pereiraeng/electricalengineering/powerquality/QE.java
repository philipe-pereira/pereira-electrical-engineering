package br.com.pereiraeng.electricalengineering.powerquality;

import java.util.Arrays;
import java.util.Map.Entry;

import br.com.pereiraeng.core.ExtendedMath;
import br.com.pereiraeng.core.collections.ArrayUtils;
import br.com.pereiraeng.math.Complex;
import br.com.pereiraeng.math.timeseries.SrT;

public class QE {

	/**
	 * Função que calcula a taxa de distorção harmônica de um dado sinal
	 * 
	 * @param espectro
	 *            conjunto de números complexos que indicam a composição
	 *            espectral do sinal
	 * @param base
	 *            índice do componente de referência
	 * @return taxa de distorção calculada
	 */
	public static double tdh(Complex[] espectro, int base) {
		double m = 0;
		for (int i = base + 1; i < espectro.length; i++)
			m += Math.pow(espectro[i].getMod(), 2);
		return Math.sqrt(m) / espectro[base].getMod();
	}

	/**
	 * Função que retorna a taxa de desequilíbrio de corrente para um dado
	 * circuito polifásicos
	 * 
	 * @param i
	 *            vetor com os módulos das correntes (esse vetor deve conter 2
	 *            ou 3 posições)
	 * @return taxa de desequilíbrio
	 */
	public static double deseqI(double... i) {
		double sum = 0.;
		for (int j = 0; j < i.length; j++)
			sum += i[j];
		if (sum == 0.)
			return 0.;
		if (i.length == 3)
			return Math.sqrt((i[0] * i[0] + i[1] * i[1] + i[2] * i[2]) - (i[0] * i[1] + i[1] * i[2] + i[0] * i[2]))
					/ sum;
		else if (i.length == 2)
			return Math.abs(i[0] - i[1]) / sum;
		else
			return 0.;
	}

	public static float deseqI(float... i) {
		return (float) deseqI(ArrayUtils.castDouble(i));
	}

	/**
	 * Função que calcula os valores para os desesquilíbrios de uma série de
	 * grandezas
	 * 
	 * @param reg
	 *            registro que contém as grandezas cujo desequilíbrio será
	 *            calculado
	 * @param pos
	 *            vetor contendo as posições do registro que serão avaliadas
	 *            (esse vetor deve ter tamanho 2 ou 3)
	 * @return registro com uma única posição contendo a
	 *         {@link QE#deseqI(double...) taxa de desequilíbrio} das grandezas
	 *         selecionadas
	 */
	public static <T extends Number> SrT<T> getDeseq(SrT<T> reg, int... pos) {
		SrT<T> out = new SrT<>(1);

		for (Entry<T, float[]> e : reg.entrySet()) {
			float[] values = e.getValue();

			double[] vs = new double[pos.length];
			int k = 0;
			for (int i = 0; i < pos.length; i++) {
				double d = values[pos[i]];
				if (d > 0. && !Double.isNaN(d))
					vs[k++] = d;
			}

			out.put(e.getKey(), 0, (float) QE.deseqI(Arrays.copyOf(vs, k)));
		}
		return out;
	}

	public static <T extends Number> SrT<T> getIn(SrT<T> reg, int... pos) {
		if (pos.length == 0)
			pos = new int[] { 0, 1, 2 };
		else if (pos.length != 3)
			throw new IllegalArgumentException("Corrente de neutro deve ser calculado para sistemas trifásico.");

		SrT<T> out = new SrT<>(1);

		for (Entry<T, float[]> e : reg.entrySet()) {
			float[] values = e.getValue();
			out.put(e.getKey(), 0, (float) in(values[pos[0]], values[pos[1]], values[pos[2]]));
		}
		return out;
	}

	public static double in(double a, double b, double c) {
		return Math.sqrt(Math.pow(a - 0.5 * b - 0.5 * c, 2) + Math.pow(ExtendedMath.SQRT3_2 * b - ExtendedMath.SQRT3_2 * c, 2));
	}

	public static double in(double... i) {
		return in(i[0], i[1], i[2]);
	}

	public static float in(float... i) {
		return (float) in(i[0], i[1], i[2]);
	}
}
