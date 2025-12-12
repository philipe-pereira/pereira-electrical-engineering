package br.com.pereiraeng.electricalengineering.transformador;

public class Tap {

	/**
	 * número de steps para cima
	 */
	private int ti;

	/**
	 * número de steps para baixo
	 */
	private int tn;

	/**
	 * passo para cima (%)
	 */
	private float vi;

	/**
	 * passo para baixo (%)
	 */
	private float vo;

	/**
	 * @param ti
	 *            número de steps para cima
	 * @param tn
	 *            número de steps para baixo
	 * @param vi
	 *            passo para cima (%)
	 * @param vo
	 *            passo para baixo (%)
	 */
	public Tap(int ti, int tn, float vi, float vo) {
		this.ti = ti;
		this.tn = tn;
		this.vi = vi;
		this.vo = vo;
	}

	/**
	 * Função que retorna o número de steps para cima
	 * 
	 * @return número de steps para cima (>= 0)
	 */
	public int getTi() {
		return ti;
	}

	public void setTi(int ti) {
		this.ti = ti;
	}

	/**
	 * Função que retorna o passo para cima, em porcentagem
	 * 
	 * @return passo para cima (%)
	 */
	public float getVi() {
		return vi;
	}

	public void setVi(float vi) {
		this.vi = vi;
	}

	/**
	 * Função que retorna o número de steps para baixo
	 * 
	 * @return número de steps para baixo (>= 0)
	 */
	public int getTn() {
		return tn;
	}

	public void setTn(int tn) {
		this.tn = tn;
	}

	/**
	 * Função que retorna o passo para baixo, em porcentagem
	 * 
	 * @return passo para baixo (%)
	 */
	public float getVo() {
		return vo;
	}

	public void setVo(float vo) {
		this.vo = vo;
	}

	/**
	 * Função que retorna o limite superior, em porcentagem
	 * 
	 * @return limite superior (%, >= 0)
	 */
	public float getVxi() {
		return getTi() * getVi();
	}

	/**
	 * Função que retorna o limite inferior, em porcentagem
	 * 
	 * @return limite inferior (%, <= 0)
	 */
	public float getVxo() {
		return -getTn() * getVo();
	}

	@Override
	public String toString() {
		if (vi == vo) {
			if (ti == tn)
				return String.format("\u00B1%dx%.3g%%", ti, vi);
			else
				return String.format("+%d-%dx%.3g%%", ti, tn, vo);
		} else
			return String.format("+%dx%.3g%%-%dx%.3g%%", ti, vi, tn, vo);
	}

	// --------------------------------------

	/**
	 * Função que retorna se a posição do tap é válida dado o número máximo de
	 * passos para cima ou para baixo
	 * 
	 * @param pos
	 *            inteiro indicando a posição
	 * @return <code>true</code> se é válida, <code>false</code> senão
	 */
	public boolean isIn(int pos) {
		return pos >= -getTn() && pos <= getTi();
	}

	/**
	 * Função que retorna o a nova relação de transformação a ser aplicada na tensão
	 * nominal em função da posição
	 * 
	 * @param pos
	 *            inteiro indicando a posição
	 * @return número decimal que indica a nova relação de transformação
	 */
	public float getV(int pos) {
		return 1f + (pos > 0 ? pos * getVi() : (pos < 0 ? pos * getVo() : 0f)) / 100f;
	}
}
