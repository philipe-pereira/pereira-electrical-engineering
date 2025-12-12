package br.com.pereiraeng.electricalengineering.generation;

import br.com.pereiraeng.core.EditableFields;

/**
 * Dados da máquina síncrona para regime permanente
 * 
 * <ul>
 * <li>o número de pares de pólos;</i>
 * <li>a geração mínima, em MW (aplicável a máquinas de hidrelétricas);</i>
 * <li>geração máxima, em MW;</i>
 * <li>potência aparente máxima, em MVA (limitação da corrente de armadura);</i>
 * <li>fator de potência nominal;</i>
 * <li>reatância de eixo direto, em pu;</i>
 * <li>reatância de eixo de quadratura, em pu;</i>
 * <li>reatância de dispersão, em pu;</i>
 * <li>resistência de armadura, em pu;</i>
 * <li>reatância de dispersão do trafo, em pu.</i>
 * </ul>
 * 
 * @author Philipe PEREIRA
 *
 */
public class GenDataPerm implements EditableFields {

	public static final String[] FIELDS = { "PP", "Wmin (MW)", "Wmax (MW)", "S (MVA)", "FP", "xd (%)", "xq (%)",
			"xl (%)", "ra (%)", "xtr (%)" };

	private int pp;

	private float pm, pM, S, fp, xd, xq, xl, ra, xtr;

	/**
	 * 
	 * @param pp  o número de pares de pólos
	 * @param pm  a geração mínima, em MW (aplicável a máquinas de
	 *            hidrelétricas);</i>
	 *            <li>geração máxima, em MW
	 * @param pM  geração máxima, em MW
	 * @param s   potência aparente máxima, em MVA (limitação da corrente de
	 *            armadura)
	 * @param fp  fator de potência nominal
	 * @param xd  reatância de eixo direto, em pu
	 * @param xq  reatância de eixo de quadratura, em pu
	 * @param xl  reatância de dispersão, em pu
	 * @param ra  resistência de armadura, em pu
	 * @param xtr reatância de dispersão do trafo, em pu
	 */
	public GenDataPerm(int pp, float pm, float pM, float s, float fp, float xd, float xq, float xl, float ra,
			float xtr) {
		this.pp = pp;
		this.pm = pm;
		this.pM = pM;
		this.S = s;
		this.fp = fp;
		this.xd = xd;
		this.xq = xq;
		this.xl = xl;
		this.ra = ra;
		this.xtr = xtr;
	}

	@Override
	public int getFieldCount() {
		return FIELDS.length;
	}

	@Override
	public String getFieldName(int index) {
		return FIELDS[index];
	}

	@Override
	public Object getField(int index) {
		switch (index) {
		case 0:
			return pp;
		case 1:
			return pm;
		case 2:
			return pM;
		case 3:
			return S;
		case 4:
			return fp;
		case 5:
			return xd;
		case 6:
			return xq;
		case 7:
			return xl;
		case 8:
			return ra;
		case 9:
			return xtr;
		default:
			return null;
		}
	}

	@Override
	public void setField(int index, Object obj) {
		switch (index) {
		case 0:
			pp = (int) obj;
		case 1:
			pm = (float) obj;
		case 2:
			pM = (float) obj;
		case 3:
			S = (float) obj;
		case 4:
			fp = (float) obj;
		case 5:
			xd = (float) obj;
		case 6:
			xq = (float) obj;
		case 7:
			xl = (float) obj;
		case 8:
			ra = (float) obj;
		case 9:
			xtr = (float) obj;
		}
	}
}
