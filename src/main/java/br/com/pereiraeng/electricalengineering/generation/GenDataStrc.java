package br.com.pereiraeng.electricalengineering.generation;

/**
 * Dados sobre a saturação da máquina: parâmetros A, B e C da curva de
 * saturação: y=A*exp(B(x-C)).
 * 
 * @author Philipe PEREIRA
 *
 */
public class GenDataStrc {

	private float a, b, c;

	public GenDataStrc(float a, float b, float c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public String getCurva() {
		return String.format("Curva de saturação: y=%g*exp(%g(x-%g))", a, b, c);
	}
}
