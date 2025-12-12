package br.com.pereiraeng.electricalengineering.trifasico;

import br.com.pereiraeng.math.Complex;
import br.com.pereiraeng.math.Vec;
import br.com.pereiraeng.core.ExtendedMath;

/**
 * Classe que contem funções e objetos que auxiliam cálculos envolvendo
 * transformações de sistema trifásicos
 * 
 * @author Philipe Pereira
 * 
 */
public class TransfTF {

	/**
	 * Função que retorna as matrizes de transformação de Fortescue para sistemas
	 * com um dado número de fases
	 * 
	 * @param fases número de fases do sistema
	 * @param inv   se <code>true</code> a função retorna a matriz da tranformação
	 *              inversa, senão a matriz da transformação direta
	 * @return matriz complexa da transformação
	 */
	public static Complex[][] getFortescue(int fases, boolean inv) {
		switch (fases) {
		case 2:
			if (inv) {
				return new Complex[][] { { new Complex(0.5, 0), new Complex(0.5, 0) },
						{ new Complex(0.5, 0), new Complex(-0.5, 0) } };
			} else {
				return new Complex[][] { { new Complex(1, 0), new Complex(1, 0) },
						{ new Complex(1, 0), new Complex(-1, 0) } };
			}
		default:
			return getFortescue3(inv);
		}
	}

	/**
	 * Função que retorna as matrizes de transformação de Fortescue para sistemas
	 * trifásicos
	 * 
	 * @param inv se <code>true</code> a função retorna a matriz da tranformação
	 *            inversa (fases->modal), senão a matriz da transformação direta
	 *            (modal->fases)
	 * @return matriz complexa da transformação
	 */
	public static Complex[][] getFortescue3(boolean inv) {
		return getFortescue3(inv, true);
	}

	/**
	 * Função que retorna as matrizes de transformação de Fortescue para sistemas
	 * trifásicos
	 * 
	 * @param inv          se <code>true</code> a função retorna a matriz da
	 *                     tranformação inversa (fases->modal), senão a matriz da
	 *                     transformação direta (modal->fases)
	 * @param convencional se <code>true</code> a matriz será a matriz
	 *                     convencionalmente apresentada nos livros de sistema de
	 *                     potência, com os vetores modais na ordem [0,1,2]. Se
	 *                     <code>false</code> a matriz é da transformação na ordem
	 *                     [1,2,0].
	 * @return matriz complexa da transformação
	 */
	public static Complex[][] getFortescue3(boolean inv, boolean convencional) {
		Complex[][] out = new Complex[3][3];

		if (inv) {
			// A é a soma das três componentes
			for (int i = 0; i < 3; i++)
				out[i][0] = new Complex(1 / 3., 0);

			// sequência zero
			for (int i = 1; i < 3; i++)
				out[convencional ? 0 : 2][i] = new Complex(1 / 3., 0);

			// alphas
			putAlphas(out, inv, convencional ? 1 : 0, 1);
		} else {
			// A é a soma das três componentes
			for (int i = 0; i < 3; i++)
				out[0][i] = new Complex(1, 0);

			// sequência zero
			for (int i = 1; i < 3; i++)
				out[i][convencional ? 0 : 2] = new Complex(1, 0);

			// alphas
			putAlphas(out, inv, 1, convencional ? 1 : 0);
		}

		return out;
	}

	private static void putAlphas(Complex[][] out, boolean inv, int i, int j) {
		Complex alpha = inv ? Complex.mult(1 / 3., Complex.ALPHA) : Complex.ALPHA;
		Complex alpha2 = inv ? Complex.mult(1 / 3., Complex.ALPHA_2) : Complex.ALPHA_2;

		for (int k = 0; k < 2; k++) {
			for (int l = 0; l < 2; l++) {
				out[k + i][l + j] = (inv ^ k == l) ? alpha2 : alpha;
			}
		}
	}

	/**
	 * 
	 * @param theta ângulo de referência da transformação
	 * @param inv   se <code>true</code> a função retorna a matriz da tranformação
	 *              inversa (fases->dq0), senão a matriz da transformação direta
	 *              (dq0->fases)
	 * @return matriz
	 */
	public static Complex[][] getParkKrause(double theta, boolean inv) {
		if (inv) {
			Complex[][] out = new Complex[][] {
					{ new Complex(Math.cos(theta), 0), new Complex(Math.cos(theta - ExtendedMath.PI_23), 0),
							new Complex(Math.cos(theta + ExtendedMath.PI_23), 0) },
					{ new Complex(-Math.sin(theta), 0), new Complex(-Math.sin(theta - ExtendedMath.PI_23), 0),
							new Complex(-Math.sin(theta + ExtendedMath.PI_23), 0) },
					{ new Complex(0.5, 0), new Complex(0.5, 0), new Complex(0.5, 0) } };

			return Vec.mult(new Complex(0.66666666667, 0.), out);
		} else {
			return new Complex[][] {
					{ new Complex(Math.cos(theta), 0.), new Complex(-Math.sin(theta), 0.), new Complex(1., 0.) },
					{ new Complex(Math.cos(theta - ExtendedMath.PI_23), 0.),
							new Complex(-Math.sin(theta - ExtendedMath.PI_23), 0.), new Complex(1., 0.) },
					{ new Complex(Math.cos(theta + ExtendedMath.PI_23), 0.),
							new Complex(-Math.sin(theta + ExtendedMath.PI_23), 0.), new Complex(1., 0.) } };
		}
	}
}
