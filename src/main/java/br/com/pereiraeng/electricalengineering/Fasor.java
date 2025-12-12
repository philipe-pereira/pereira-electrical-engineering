package br.com.pereiraeng.electricalengineering;

import br.com.pereiraeng.math.Complex;
import br.com.pereiraeng.physics.Grandeza;

/**
 * Classe do objeto que representa um fasor (i.e. um número complexo que
 * representa uma grandeza que varia senoidalmente com o tempo)
 * 
 * @author Philipe PEREIRA
 *
 */
public class Fasor extends Complex {
	private static final long serialVersionUID = 1L;

	/**
	 * <ol start="0">
	 * <li>o fasor é o número complexo;</i>
	 * <li>o fasor é a {@link #f1 base} mais este número complexo (o que será
	 * desenhado será a diferença, com a base deslocada);</i>
	 * <li>o fasor é a soma de dois números complexos, {@link #f1} e {@link #f2} (o
	 * que será desenhado é a resultante);</i>
	 * <li>o fasor é igual a {@link #f1 outro número complexo};</i>
	 * <li>o fasor é a diferença de dois números complexos, {@link #f1} e
	 * {@link #f2} (o que será desenhado será a diferença, com a base
	 * deslocada);</i>
	 * </ol>
	 */
	protected int mode = 0;

	/**
	 * Dependendo do valor de {@link mode}:
	 * 
	 * <ol start="0">
	 * <li><code>null</code>;</i>
	 * <li>base;</i>
	 * <li>primeira parcela;</i>
	 * <li>outro número complexo;</i>
	 * <li>minuendo.</i>
	 * </ol>
	 */
	protected Fasor f1;

	/**
	 * Dependendo do valor de {@link mode}:
	 * 
	 * <ol start="0">
	 * <li><code>null</code>;</i>
	 * <li><code>null</code>;</i>
	 * <li>segunda parcela;</i>
	 * <li><code>null</code>;</i>
	 * <li>subtraendo.</i>
	 * </ol>
	 */
	protected Fasor f2;

	protected Grandeza grandeza;

	/**
	 * Construtor do objeto que representa um fasor nulo (modo {@link #mode 0})
	 */
	public Fasor() {
		this(0., 0.);
	}

	/**
	 * Construtor do objeto que representa um fasor de tensão (modo {@link #mode 0})
	 * 
	 * @param mod módulo do fasor
	 * @param ang ângulo do fasor, em <strong>graus</strong>
	 */
	public Fasor(double mod, double angDeg) {
		this(mod, angDeg, false, null, null, null, false, null);
	}

	/**
	 * Construtor do objeto que representa um fasor de tensão (modo {@link #mode 0})
	 * 
	 * @param mod módulo do fasor
	 * @param ang ângulo do fasor, em <strong>radianos</strong> ou
	 *            <strong>graus</strong>
	 * @param rad <code>true</code> para radianos, <code>false</code> para graus
	 */
	public Fasor(double mod, double angRadDeg, boolean rad) {
		this(mod, angRadDeg, rad, null, null, null, false, null);
	}

	/**
	 * Construtor do objeto que representa um fasor de tensão (modo {@link #mode 0})
	 * 
	 * @param c número complexo do fasor
	 */
	public Fasor(Complex c) {
		this(c.getMod(), c.getArg(), true, null, null, null, false, null);
	}

	/**
	 * Construtor do objeto que representa um fasor de tensão, indicando-se que o
	 * fasor está deslocado por outro fasor (modo {@link #mode 1})
	 * 
	 * @param c    número complexo do fasor
	 * @param base fasor de base
	 */
	public Fasor(Complex c, Fasor base) {
		this(c.getMod(), Math.toDegrees(c.getArg()), base);
	}

	/**
	 * Construtor do objeto que representa um fasor de tensão, indicando-se que o
	 * fasor está deslocado por outro fasor (modo {@link #mode 1})
	 * 
	 * @param mod    módulo do fasor
	 * @param angDeg ângulo do fasor, em <strong>graus</strong>
	 * @param base   fasor de base
	 */
	public Fasor(double mod, double angDeg, Fasor base) {
		this(mod, angDeg, false, base, null, null, false, null);
	}

	/**
	 * Construtor do objeto que representa um fasor de tensão, indicando-se que o
	 * fasor é a soma ({@link #mode 2}) ou diferença ({@link #mode 4}) de dois
	 * outros
	 * 
	 * @param f1  primeiro parcela (sum é <code>true</code>) ou minuendo (sum é
	 *            <code>false</code>)
	 * @param f2  segunda parcela (sum é <code>true</code>) ou subtraendo (sum é
	 *            <code>false</code>)
	 * @param sum <code>true</code> para que este fasor represente a soma de dois
	 *            número complexos, <code>false</code> para que ele representa a
	 *            diferença
	 */
	public Fasor(Fasor f1, Fasor f2, boolean sum) {
		this(Double.NaN, Double.NaN, true, null, f1, f2, sum, null);
	}

	/**
	 * Construtor do objeto que representa um fasor de tensão, indicando-se que o
	 * fasor é igual a outro (modo {@link #mode 3})
	 * 
	 * @param other fasor que é igual a este
	 */
	public Fasor(Fasor other) {
		this(Double.NaN, Double.NaN, true, null, null, null, false, other);
	}

	/**
	 * 
	 * @param mod       módulo do fasor
	 * @param angRadDeg ângulo do fasor, em <strong>graus</strong>
	 * @param rad       <code>true</code> para radianos, <code>false</code> para
	 *                  graus
	 * @param base      fasor de base
	 * @param f1
	 * @param f2
	 * @param sum       <code>true</code> para que este fasor represente a soma de
	 *                  dois número complexos, <code>false</code> para que ele
	 *                  representa a diferença
	 * @param other
	 */
	private Fasor(double mod, double angRadDeg, boolean rad, Fasor base, Fasor f1, Fasor f2, boolean sum, Fasor other) {
		super(mod, rad ? angRadDeg : Math.toRadians(angRadDeg), false);
		this.setBase(base, true);
		this.setSum(f1, f2, sum);
		this.setOther(other);
	}

	/**
	 * Função que retorna o inteiro que designa o modo como o fasor está
	 * representado
	 * 
	 * @return
	 *         <ol start="0">
	 *         <li>o fasor é o número complexo;</i>
	 *         <li>o fasor é a {@link #f1 base} mais este número complexo (o que
	 *         será desenhado será a diferença, com a base deslocada);</i>
	 *         <li>o fasor é a soma de dois números complexos, {@link #f1} e
	 *         {@link #f2} (o que será desenhado é a resultante);</i>
	 *         <li>o fasor é igual a {@link #f1 outro número complexo};</i>
	 *         <li>o fasor é a diferença de dois números complexos, {@link #f1} e
	 *         {@link #f2} (o que será desenhado será a diferença, com a base
	 *         deslocada);</i>
	 *         </ol>
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Função que estabelece a grandeza que varia cossenoidalmente no tempo
	 * representada por este fasor. Caso seja indicada, isso altera a forma como a
	 * ponta do vetor será desenhada, segundo a convenção adotada no livro
	 * <i>Electric Machinery</i>, do autor Fitzgerald
	 * 
	 * @param grandeza
	 *                 <ul>
	 *                 <li>{@link Grandeza#TENSAO}: seta triangular cheia;</i>
	 *                 <li>{@link Grandeza#CORRENTE}: seta triangular oca;</i>
	 *                 <li>{@link Grandeza#FLUXO_MAG}: seta losangular cheia;</i>
	 *                 <li>para <code>null</code> ou qualquer outra grandeza: seta
	 *                 triangular cheia.</i>
	 *                 </ul>
	 */
	public void setGrandeza(Grandeza grandeza) {
		this.grandeza = grandeza;
	}

	/**
	 * Função que retorna a grandeza que varia cossenoidalmente no tempo
	 * representada por este fasor.
	 * 
	 * @return
	 *         <ul>
	 *         <li>{@link Ana#TENSAO}: seta triangular cheia;</i>
	 *         <li>{@link Ana#CORRENTE}: seta triangular oca;</i>
	 *         <li>{@link Ana#FLUXO_MAG}: seta losangular cheia;</i>
	 *         <li>para <code>null</code> ou qualquer outra grandeza: seta
	 *         triangular cheia.</i>
	 *         </ul>
	 */
	public Grandeza getGrandeza() {
		return grandeza;
	}

	// override sobre as funções que determinam se este é um número complexo ou não

	@Override
	protected double getX0() {
		double out = 0.;
		switch (this.mode) {
		case 0:
			out = super.getX0();
			break;
		case 1:
			if (super.isCartesian()) // retorna parte real da resultante
				out = super.getX0() + f1.getRe();
			else // retorna módulo da resultante
				out = Math.hypot(super.getX0() * Math.cos(super.getX1()) + f1.getRe(),
						super.getX0() * Math.sin(super.getX1()) + f1.getIm());
			break;
		case 2:
			if (super.isCartesian())
				out = f1.getRe() + f2.getRe();
			else
				out = Math.hypot(f1.getRe() + f2.getRe(), f1.getIm() + f2.getIm());
			break;
		case 3:
			if (super.isCartesian())
				out = f1.getRe();
			else
				out = f1.getMod();
			break;
		case 4:
			if (super.isCartesian())
				out = f2.getRe();
			else
				out = Math.hypot(f2.getRe(), f2.getIm());
			break;
		}
		return out;
	}

	@Override
	protected double getX1() {
		double out = 0.;
		switch (this.mode) {
		case 0:
			out = super.getX1();
			break;
		case 1:
			if (super.isCartesian()) // retorna parte imaginária da resultante
				out = super.getX1() + f1.getIm();
			else // retorna argumento da resultante
				out = Math.atan2(super.getX0() * Math.sin(super.getX1()) + f1.getIm(),
						super.getX0() * Math.cos(super.getX1()) + f1.getRe());
			break;
		case 2:
			if (super.isCartesian())
				out = f1.getIm() + f2.getIm();
			else
				out = Math.atan2(f1.getIm() + f2.getIm(), f1.getRe() + f2.getRe());
			break;
		case 3:
			if (super.isCartesian())
				out = f1.getIm();
			else
				out = f1.getArg();
			break;
		case 4:
			if (super.isCartesian())
				out = f2.getIm();
			else
				out = Math.atan2(f2.getIm(), f2.getRe());
			break;
		}
		return out;
	}

	// base (mode 1)

	/**
	 * Função que indica qual o fasor sobre o qual é somado este para se ter o fasor
	 * resultante
	 * 
	 * @param base   fasor a ser somado com este
	 * @param change <code>true</code> para que o fasor resultante seja a soma deste
	 *               com o argumento, <code>false</code> para que o fasor resultante
	 *               continue sendo este
	 */
	public void setBase(Fasor base, boolean change) {
		if (base != null) {
			if (!change)
				// para o fasor resultante não ser alterado, deve se alterar este fasor, de modo
				// que a soma do novo com a base seja o atual
				this.sub(base);
			this.f1 = base;
			this.mode = 1;
		}
	}

	public Fasor getBase() {
		if (mode != 1)
			throw new IllegalArgumentException("Este fasor não é baseado em outro.");
		return f1;
	}

	// resultante ou diferença (mode 2 ou 4)

	public void setSum(Fasor f1, Fasor f2, boolean sum) {
		if (f1 != null && f2 != null) {
			this.f1 = f1;
			this.f2 = f2;
			this.mode = sum ? 2 : 4;
		}
	}

	public Fasor getSum1() {
		if (mode != 2 && mode != 4)
			throw new IllegalArgumentException("Este fasor não é a composição de outros.");
		return f1;
	}

	public Fasor getSum2() {
		if (mode != 2 && mode != 4)
			throw new IllegalArgumentException("Este fasor não é a composição de outros.");
		return f2;
	}

	// outro (mode 3)

	public void setOther(Fasor other) {
		if (other != null) {
			this.f1 = other;
			this.mode = 3;
		}
	}

	public Fasor getOther() {
		if (mode != 3)
			throw new IllegalArgumentException("Este fasor não é igual a um outro.");
		return f1;
	}
}