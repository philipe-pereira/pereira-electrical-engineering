package br.com.pereiraeng.electricalengineering.trifasico;

import java.awt.Color;
import java.util.Arrays;

public enum Phases {
	VM, AZ, BR;

	public static final String NEUTRO = "N";

	public static final Color NEUTRO_COLOR = Color.GREEN.darker();

	/**
	 * 
	 * @param l
	 *            <ul>
	 *            <li>A</i>
	 *            <li>B</i>
	 *            <li>C</i>
	 *            </ul>
	 * @return
	 *         <ul>
	 *         <li>VM</i>
	 *         <li>AZ</i>
	 *         <li>BR</i>
	 *         </ul>
	 */
	public static Phases getPhase(char l) {
		int o = l - 'A';
		if (o < 0 || o > 2)
			return null;
		else
			return values()[o];
	}

	public static Phases getPhase(String etiqueta) {
		for (Phases p : Phases.values())
			if (etiqueta.endsWith(p.name()))
				return p;
		// última letra

		char cp = etiqueta.charAt(etiqueta.length() - 2);
		if (Arrays.binarySearch(new char[] { 'F', 'H', 'J', 'K', 'L' }, cp) >= 0) {
			Phases ph = Phases.getPhase(etiqueta.charAt(etiqueta.length() - 1));
			if (ph != null)
				return ph;
		}
		switch (cp) {
		case 'V':
		case 'M':
			return Phases.VM;
		case 'A':
		case 'Z':
			return Phases.AZ;
		case 'B':
		case 'R':
			return Phases.BR;
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param l
	 *            <ul>
	 *            <li>V</i>
	 *            <li>A</i>
	 *            <li>B</i>
	 *            </ul>
	 * @return
	 *         <ul>
	 *         <li>VM</i>
	 *         <li>AZ</i>
	 *         <li>BR</i>
	 *         </ul>
	 */
	public static Phases getPhase2(char l) {
		switch (l) {
		case 'V':
			return Phases.VM;
		case 'A':
			return Phases.AZ;
		case 'B':
			return Phases.BR;
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param ph
	 *            <ul>
	 *            <li>VM</i>
	 *            <li>AZ</i>
	 *            <li>BR</i>
	 *            </ul>
	 * @return
	 *         <ul>
	 *         <li>A</i>
	 *         <li>B</i>
	 *         <li>C</i>
	 *         </ul>
	 */
	public static char getLetter(Phases ph) {
		return (char) (ph.ordinal() + 'A');
	}

	public char getLetter() {
		return (char) (this.ordinal() + 'A');
	}

	public static Color getColor(Phases ph) {
		switch (ph) {
		case AZ:
			return Color.BLUE;
		case BR:
			return Color.DARK_GRAY;
		case VM:
			return Color.RED;
		}
		return null;
	}

	public Color getColor() {
		return getColor(this);
	}
}
