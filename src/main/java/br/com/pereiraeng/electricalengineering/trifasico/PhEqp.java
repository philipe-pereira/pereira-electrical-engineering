package br.com.pereiraeng.electricalengineering.trifasico;

public enum PhEqp {
	A, B, C, AB, AC, BC, ABC;

	/**
	 * Função que analisa se dois equipamentos tem o mesmo número de fases
	 * 
	 * @param ph1 inteiro que indica alguma das combinações das fases
	 * @param ph2 inteiro que indica alguma das combinações das fases
	 * @return <code>true</code> se tem o mesmo número, <code>false</code> senão
	 */
	public static boolean sameNumPh(int ph1, int ph2) {
		return getNumPh(ph1) == getNumPh(ph2);
	}

	/**
	 * Função que retorna o número de fase do equipamento
	 * 
	 * @param ph item da enumeração
	 * @return número de fases
	 */
	private static int getNumPh(int ph) {
		return PhEqp.values()[ph - 1].name().length();
	}

	/**
	 * Função que retorna os mesmo valores da tabela TDSR_FASE
	 * 
	 * @param i número de referência (NUM_FASE)
	 * @return fases do vão
	 */
	public static String getPhs(int i) {
		return values()[i - 1].name();
	}

	public float[] sec2pri(float da, float db, float dc) {
		switch (this) {
		case A:
			return new float[] { da + db, 0f, 0f };
		case B:
			return new float[] { 0f, da + db, 0f };
		case C:
			return new float[] { 0f, 0f, da + db };
		case AC:
			return new float[] { da, 0f, db };
		case BC:
			return new float[] { 0f, da, db };
		case AB:
			return new float[] { da, db, 0f };
		case ABC:
			return new float[] { da, db, dc };
		}
		return null;
	}
}