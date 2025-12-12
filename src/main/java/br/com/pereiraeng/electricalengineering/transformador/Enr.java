package br.com.pereiraeng.electricalengineering.transformador;

import java.util.Locale;

import br.com.pereiraeng.electricalengineering.Tensao;


/**
 * Classe dos objetos que representam os enrolamentos de transformadores em que
 * é possível alterar o número de espiras e, consequentemente, a relação de
 * transformação (tap do enrolamento).
 * 
 * @author Philipe PEREIRA
 *
 */
public class Enr {

	/**
	 * Enumeração dos enrolamentos em que é possível ajustar a relação de
	 * transformação (primário, secundário ou terciário)
	 * 
	 * @author Philipe PEREIRA
	 *
	 */
	public enum EnrPos {
		PRI, SEC, TER;
	}

	public static final int UNKNOWN = -1024, NOT_APPLY = -2048;

	/**
	 * Posição do enrolamento no trafo
	 */
	private final EnrPos enr;

	/**
	 * Tensão padronizada
	 */
	private final Tensao tensao;

	/**
	 * tensão nominal, em kV
	 */
	private float vn;

	private Tap fixo, movel;

	/**
	 * inteiro que indica o tap atual (ou {@link #UNKNOWN} para indicar que não
	 * se sabe onde está)
	 */
	private int current = NOT_APPLY;

	public Enr(EnrPos enr, Tensao tensao) {
		this.enr = enr;
		this.tensao = tensao;
	}

	public EnrPos getEnr() {
		return enr;
	}

	public Tensao getTensao() {
		return tensao;
	}

	public float getVn() {
		return vn;
	}

	public Tap getFixo() {
		return fixo;
	}

	public Tap getMovel() {
		return movel;
	}

	public void clear() {
		fixo = null;
		current = NOT_APPLY;
		movel = null;
	}

	public int getCurrent() {
		return current;
	}

	@Override
	public String toString() {
		return String.format("%.1f%s%s", vn, hasFixo() ? fixo : "", hasMovel() ? movel : "");
	}

	public boolean hasFixo() {
		return fixo != null;
	}

	public boolean hasMovel() {
		return movel != null;
	}

	public void setVn(float vn0, float vn1) {
		this.vn = tensao.isSame(vn0) ? vn0 : vn1;
	}

	/**
	 * um tap <strong>fixo (a vazio)</strong>
	 * 
	 * @param ti
	 *            número de steps para cima
	 * @param vi
	 *            passo para cima (%)
	 * @param tn
	 *            número de steps para baixo
	 * @param vo
	 *            passo para baixo (%)
	 * @param current
	 *            inteiro que indica o tap atual
	 */
	public void setTv(int ti, float vi, int tn, float vo, int current) {
		this.fixo = new Tap(ti, tn, vi, vo);
		this.setCurrent(current);
	}

	/**
	 * um tap <strong>móvel (em carga)</strong>.
	 * 
	 * @param ti
	 *            número de steps para cima
	 * @param vi
	 *            passo para cima (%)
	 * @param tn
	 *            número de steps para baixo
	 * @param vo
	 *            passo para baixo (%)
	 */
	public void setTc(int ti, float vi, int tn, float vo) {
		this.movel = new Tap(ti, tn, vi, vo);
	}

	// -----------------------------------------------

	public void setCurrent(int current) {
		if (!hasFixo())
			throw new IllegalArgumentException("Este enrolamento não possui tap fixo");
		if (current == UNKNOWN ? true : fixo.isIn(current))
			this.current = current;
		else
			throw new IllegalArgumentException("Posição inacessível ao trafo");
	}

	public float getVp() {
		float vn = getVn();
		if (hasFixo())
			vn *= fixo.getV(current);
		return vn;
	}

	public float[] getVx() {
		float vp = getVp();
		float[] out = new float[] { vp, vp };
		if (hasMovel()) {
			out[0] *= (1f + movel.getVxo() / 100f);
			out[1] *= (1f + movel.getVxi() / 100f);
		}
		return out;
	}

	// -----------------------------------------------

	public static String getSQL(int nreg, Enr[] enrs) {
		int tv = 0, ti = 0, tn = 0;
		float vi = 0f, vo = 0f;
		int tc = 0, ci = 0, cn = 0;
		float si = 0f, so = 0f;
		for (int i = 0; i < enrs.length; i++) {
			if (enrs[i].hasFixo()) { // tap fixo
				if (tv == 0)
					tv = i + 1;
				else
					tv = 4;
				Tap tap = enrs[i].getFixo();
				ti = tap.getTi();
				tn = tap.getTn();
				vi = tap.getVi();
				vo = tap.getVo();
			}
			if (enrs[i].hasMovel()) { // tap móvel
				tc = i + 1;
				Tap tap = enrs[i].getMovel();
				ci = tap.getTi();
				cn = tap.getTn();
				si = tap.getVi();
				so = tap.getVo();
			}
		}
		return String.format(Locale.US,
				"UPDATE `ttraf` SET `val_TV`=%d,`val_TI`=%d,`val_VI`=%f,`val_TN`=%d,`val_VO`=%f,`num_TC`=%d,`val_CI`=%d,`val_SI`=%f,`val_CN`=%d,`val_SO`=%f WHERE `cod_TRAF`=%d",
				tv, ti, vi, tn, vo, tc, ci, si, cn, so, nreg);
	}
}