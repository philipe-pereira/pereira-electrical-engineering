package br.com.pereiraeng.electricalengineering;

import java.awt.Color;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import br.com.pereiraeng.core.ExtendedMath;

/**
 * <p>
 * Enumeração das tensões comumente utilizadas para transmissão e distribuição
 * de eletricidade
 * </p>
 * 
 * Ver ANSI C84.1-1989; IEC 60038
 * 
 * @author Philipe PEREIRA
 *
 */
public enum Tensao implements Serializable {

	T00(0f, false, false, false, Color.BLACK, 'O', 0f), T01(.4f, true, false, false, new Color(181, 134, 8), 'a', 1f),
	T33(.48f, true, false, true, new Color(181, 134, 8), 'a', 1f),
	T02(1f, true, false, false, new Color(181, 134, 8), 'A', 1f),
	T03(2.4f, false, false, true, new Color(181, 134, 8), 'B', 1f),
	T04(3.3f, true, false, false, new Color(181, 134, 8), 'C', 1f),
	T05(4.16f, true, true, true, new Color(181, 134, 8), 'd', 1f),
	T35(4.8f, false, false, true, new Color(181, 134, 8), 'd', 1f),
	T06(5.75f, false, false, false, new Color(181, 134, 8), 'd', 1f),
	T30(6.6f, true, false, false, new Color(181, 134, 8), 'd', 1f),
	T07(6.9f, false, false, true, new Color(181, 134, 8), 'D', 1f),
	T08(10.5f, false, false, false, Color.WHITE, 'E', 1f), T29(11f, true, false, false, Color.WHITE, 'e', 1f),
	T25(12.47f, true, true, true, Color.YELLOW, 'f', 2f), T24(13.2f, true, true, true, Color.YELLOW, 'f', 2f),
	T09(13.8f, true, true, true, Color.YELLOW, 'F', 2f), T10(16.8f, false, false, false, Color.WHITE, 'G', 2f),
	T26(22f, true, false, false, Color.WHITE, 'h', 2f), T11(23f, false, false, true, Color.WHITE, 'H', 2f),
	T27(33f, true, false, false, Color.WHITE, 'h', 2f), T12(34.5f, true, true, true, Color.WHITE, 'H', 2f),
	T28(35f, true, false, false, Color.WHITE, 'h', 2f), T13(45f, true, true, true, Color.WHITE, 'I', 2f),
	T14(69f, true, true, true, new Color(107, 105, 107), 'J', 3f),
	T37(88f, false, false, false, new Color(107, 105, 107), 'j', 3f), T32(115f, true, true, true, Color.RED, 'k', 3f),
	T15(138f, true, true, true, Color.RED, 'K', 3f), T16(161f, true, true, true, new Color(214, 113, 214), 'L', 3f),
	T17(230f, true, true, true, new Color(33, 142, 255), 'M', 4f),
	T18(300f, true, true, false, new Color(191, 105, 0), 'p', 4f),
	T19(345f, true, true, true, new Color(255, 138, 0), 'P', 4f),
	T36(400f, false, false, true, new Color(178, 122, 181), 'q', 4f),
	T20(440f, true, true, false, new Color(178, 122, 181), 'Q', 4f),
	T21(500f, true, true, true, new Color(49, 202, 49), 'U', 4f), T22(765f, true, true, true, Color.WHITE, 'V', 4f),
	T23(1100f, true, true, true, Color.WHITE, 'W', 4f), T34(1200f, true, true, false, Color.WHITE, 'X', 4f),
	T99(-1f, false, false, false, Color.MAGENTA, 'Z', 0f);

	/**
	 * Valor de tensão, em kV
	 */
	private float voltage;

	// normatização

	private boolean iec;

	private boolean f60;

	private boolean ansi;

	// faixas de tensão

	private char letra;

	// representação gráfica

	private Color color;
	private float thickness;

	private static final String UNIT = " kV";

	private Tensao(float v, boolean iec, boolean f60, boolean ansi, Color color, char letra, float thickness) {
		this.voltage = v;

		this.iec = iec;
		this.f60 = f60;
		this.ansi = ansi;

		this.letra = letra;

		this.color = color;
		this.thickness = thickness;
	}

	/**
	 * Função que retorna o valor da tensão, em kV
	 * 
	 * @return valor da tensão, em kV
	 */
	public float getVoltage() {
		return voltage;
	}

	/**
	 * Função que retorna o valor da tensão, em kV
	 * 
	 * @return valor da tensão, em kV
	 */
	public double getVoltageD() {
		return ExtendedMath.round(voltage, 1);
	}

	/**
	 * Função que indica se a tensão é normatizada pela IEC 60038
	 * 
	 * @return <code>true</code> se for normatizada
	 */
	public boolean isIEC() {
		return iec;
	}

	/**
	 * Função que indica se a tensão é da série de 50 ou 60 Hz da norma IEC 60038
	 * 
	 * @return <code>true</code> se for 60 Hz, <code>false</code> se não (não se
	 *         aplica se {@link #isIEC()} for <code>false</code> )
	 */
	public boolean isF60() {
		return f60;
	}

	/**
	 * Função que indica se a tensão é normatizada pela ANSI C84.1
	 * 
	 * @return <code>true</code> se for normatizada
	 */
	public boolean isANSI() {
		return ansi;
	}

	/**
	 * Função que retorna um vetor com as tensão definidas em norma
	 * 
	 * @param iec <code>true</code> para as tensão da norma IEC 60038,
	 *            <code>false</code> ANSI C84.1
	 * @return vetor de tensão
	 */
	public static Tensao[] getFormats(boolean iec) {
		List<Tensao> out = new LinkedList<>();
		for (int i = 0; i < Tensao.values().length; i++) {
			Tensao t = Tensao.get(i);
			if (iec ? t.isIEC() : t.isANSI())
				out.add(t);
		}
		return out.toArray(new Tensao[out.size()]);
	}

	// ------------------------------------------

	public char getLetra() {
		return letra;
	}

	// ------------------------------------------

	public Color getColor() {
		return color;
	}

	public float getThickness() {
		return thickness;
	}

	// ------------------------------------------

	public static Tensao valor(Number vnom) {
		return valor(vnom.floatValue());
	}

	/**
	 * Função que retorna o item da enumeração correspondente a um dado valor de
	 * tensão em kV.
	 * 
	 * O limite de tensão pode variar na faixa de mais ou menos 300 V ou mais ou
	 * menos 10%
	 * 
	 * @param vnom valor de tensão, em kV e variável <code>double</code>
	 * @return o item da enumeração
	 */
	public static Tensao valor(double vnom) {
		return valor((float) vnom);
	}

	/**
	 * Função que retorna o item da enumeração correspondente a um dado valor de
	 * tensão em kV.
	 * 
	 * O limite de tensão pode variar na faixa de mais ou menos 300 V ou mais ou
	 * menos 10%
	 * 
	 * @param v valor de tensão, em kV e variável <code>float</code>
	 * @return o item da enumeração
	 */
	public static Tensao valor(float v) {
		Tensao out = null;
		float min = Float.POSITIVE_INFINITY;
		for (Tensao t : Tensao.values()) {
			float dv = Math.abs(t.getVoltage() - v);
			if (dv < min) {
				out = t;
				min = dv;
			}
		}
		return out;
	}

	public static Tensao valor(String text) {
		// pré-tratamento
		if (text.endsWith(UNIT))
			text = text.substring(0, text.length() - 3);
		text = text.replace(',', '.');
		// parse
		Tensao out = null;
		try {
			float t = Float.parseFloat(text);
			out = Tensao.valor(t);
		} catch (NumberFormatException e) {
		}
		return out;
	}

	public static Tensao letra(char letra) {
		for (Tensao t : Tensao.values())
			if (letra == t.letra)
				return t;
		return null;
	}

	public static TreeSet<Tensao> letras(char letra) {
		TreeSet<Tensao> out = new TreeSet<Tensao>();
		for (Tensao t : Tensao.values()) {
			if (letra == t.letra)
				out.add(t);
		}
		return out;
	}

	/**
	 * Função que retorna a tensão designada por um número inteiro. É a função
	 * inversa de {@link #get(Tensao)}.
	 * 
	 * @param n inteiro que designa um nível de tensão
	 * @return nível de tensão
	 */
	public static Tensao get(int n) {
		return Tensao.valueOf(String.format("T%02d", n));
	}

	/**
	 * Função que retorna a tensão designada por um número inteiro. É a função
	 * inversa de {@link #get(int)}.
	 * 
	 * @param t nível de tensão
	 * @return inteiro que designa o nível de tensão
	 */
	public static int get(Tensao t) {
		return Integer.parseInt(t.name().substring(1));
	}

	/**
	 * Função que retorna o inteiro que designa este nível de tensão
	 * 
	 * @return inteiro que designa este nível de tensão
	 */
	public int get() {
		return get(this);
	}

	/**
	 * Função que retorna a lista de tensões ordenadas pelo {@link #get() número
	 * inteiro que as designa}
	 * 
	 * @return vetor de tensões
	 */
	public static Tensao[] getValues() {
		TreeMap<Integer, Tensao> orderedVoltages = new TreeMap<>();
		Tensao[] allVoltages = values();
		for (int i = 0; i < allVoltages.length; i++)
			orderedVoltages.put(allVoltages[i].get(), allVoltages[i]);
		return orderedVoltages.values().toArray(new Tensao[orderedVoltages.size()]);
	}

	public static Map<Character, Color> colors(Tensao... values) {
		Map<Character, Color> out = new LinkedHashMap<>();
		for (int i = 0; i < values.length; i++)
			out.put(values[i].getLetra(), values[i].getColor());
		return out;
	}

	public boolean isSame(float v) {
		return (v > this.voltage * 0.9 && v < this.voltage * 1.1)
				|| (v > this.voltage - 0.3f && v < this.voltage + 0.3f);
	}

	@Override
	public String toString() {
		return String.format("%.1f%s", voltage, UNIT);
	}

	/**
	 * Função que indica se um nível de tensão faz com que o equipamento seja da
	 * rede básica ou não
	 * 
	 * @return <code>true</code> se a tensão for maior ou igual a 230 kV,
	 *         <code>false</code> senão
	 */
	public boolean isRB() {
		return getVoltage() > 229f;
	}

	/**
	 * Função que retorna a impedância de base associada a este nível de tensão
	 * 
	 * @param sb potência de base, em MVA
	 * @return impedância de base, em Ohms
	 */
	public float getZb(float sb) {
		return (float) (Math.pow(getVoltage(), 2) / sb);
	}
}
