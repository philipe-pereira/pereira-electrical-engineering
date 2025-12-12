package br.com.pereiraeng.electricalengineering.powerquality;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import br.com.pereiraeng.math.timeseries.RegP;

public class PowerFactor {

	/**
	 * 
	 * @param pqi  registro no padrão PQI dos valores de fronteira (potência ativa,
	 *             reativa e corrente)
	 * @param freq frequência de tempo, em minutos, do registro de saída
	 * @return registro com os valores de potência ativa, reativa e o fator de
	 *         potência
	 */
	public static RegP getFP(RegP pq) {
		return getFP(pq, null, null);
	}

	/**
	 * 
	 * @param pqi       registro PQI dos valores de fronteira
	 * @param freq      frequência de tempo, em minutos, do registro de saída
	 * @param perGer    registro de medições de potências ativas das perturbações na
	 *                  fronteira (geração), tendo no {@link RegP#getLabel(int)
	 *                  cabeçalho} os números de referência das usinas
	 * @param influence tabela de dispersão que associa para cada número de
	 *                  referência da usina um vetor com uma posição (com o fator de
	 *                  influência da geração)
	 * @return registro com os valores de potência ativa, reativa e o fator de
	 *         potência
	 */
	public static RegP getFP(RegP pq, RegP perGer, Map<Integer, float[]> influence) {
		if (perGer != null ? influence == null : false)
			return null; // se tem pert. mas não tem fator de influência...
		RegP out = new RegP(perGer != null ? 5 : 3, pq.getFreq());
		RegP.select(out, pq, 0, 1);
		return calculateFP(out, perGer, influence);
	}

	// calculador do FP

	public static RegP calculateFP(RegP out, RegP perGer, Map<Integer, float[]> influence) {
		// se as medições forem zeradas, remove-se
		Iterator<Entry<Integer, float[]>> it = out.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();
			float[] vs = e.getValue();
			if (vs[0] == 0. && vs[1] == 0.)
				it.remove();
		}

		// arcotangente 2
		out.operation('2', true, 2, 0, 1);

		if (perGer != null) {
			// duplica a coluna de MW
			RegP.transfer(out, 0, out, 3);
			for (int i = 0; i < perGer.length(); i++) {
				int ref = Integer.parseInt(perGer.getLabel(i));
				float perc = -influence.get(ref)[0];
				RegP.operation('*', perc, perGer, i);
				out.operation('+', 3, false, perGer, i);
			}
			// arcotangente 2
			out.operation('2', true, 4, 3, 1);
		}

		// depois das operações, alterar os nomes das colunas
		out.setLabels(perGer == null ? new String[] { "MW", "MVAR", "FP" }
				: new String[] { "MW", "MVAR", "FP", "MWc", "FPc" });

		return out;
	}
}
