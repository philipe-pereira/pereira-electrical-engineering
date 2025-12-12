package br.com.pereiraeng.electricalengineering.generation;

import br.com.pereiraeng.core.EditableFields;

/**
 * Dados da máquina síncrona para regime transitório
 * 
 * <ul>
 * <li>constante de inércia da máquina, em s; </i>
 * <li>constante de amortização; </i>
 * <li>reatâncias de eixo direto e de quadratura, transitória e subtransitória;
 * </i>
 * <li>constantes de tempo de eixo direto e de quadratura, transitória e
 * subtransitória.</i>
 * </ul>
 * 
 * @author Philipe PEREIRA
 *
 */
public class GenDataTrns implements EditableFields {
	public static final String[] FIELDS = { "H (s)", "D", "xd1 (%)", "xq1 (%)", "xd2 (%)", "xq2 (%)", "td1 (s)",
			"tq1 (s)", "td2 (s)", "tq2 (s)" };

	private float h, d, xd1, xq1, xd2, xq2, td1, tq1, td2, tq2;

	/**
	 * 
	 * @param h   constante de inércia da máquina, em s
	 * @param d   constante de amortização
	 * @param xd1
	 * @param xq1
	 * @param xd2
	 * @param xq2
	 * @param td1
	 * @param tq1
	 * @param td2
	 * @param tq2
	 */
	public GenDataTrns(float h, float d, float xd1, float xq1, float xd2, float xq2, float td1, float tq1, float td2,
			float tq2) {
		this.h = h;
		this.d = d;
		this.xd1 = xd1;
		this.xq1 = xq1;
		this.xd2 = xd2;
		this.xq2 = xq2;
		this.td1 = td1;
		this.tq1 = tq1;
		this.td2 = td2;
		this.tq2 = tq2;
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
			return h;
		case 1:
			return d;
		case 2:
			return xd1;
		case 3:
			return xq1;
		case 4:
			return xd2;
		case 5:
			return xq2;
		case 6:
			return td1;
		case 7:
			return tq1;
		case 8:
			return td2;
		case 9:
			return tq2;
		default:
			return null;
		}
	}

	@Override
	public void setField(int index, Object obj) {
		float f = (float) obj;
		switch (index) {
		case 0:
			h = f;
		case 1:
			d = f;
		case 2:
			xd1 = f;
		case 3:
			xq1 = f;
		case 4:
			xd2 = f;
		case 5:
			xq2 = f;
		case 6:
			td1 = f;
		case 7:
			tq1 = f;
		case 8:
			td2 = f;
		case 9:
			tq2 = f;
		}
	}
}
