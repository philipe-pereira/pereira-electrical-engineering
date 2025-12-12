package br.com.pereiraeng.electricalengineering.generation;

import br.com.pereiraeng.electricalengineering.Tensao;

public class GenDat {

	/**
	 * Tabela com os valores típicos para os parâmetros das máquinas síncronas
	 */
	public static final float[][][][] CONVENTIONAL = new float[][][][] {
			{ { { 176f, 166f, 21f, 13f, 13f }, { 138f, 135f, 26f, 19f, 19f } },
					{ { 195f, 193f, 33f, 28f, 28f }, { 187f, 182f, 41f, 29f, 29f } } },
			{ { { 100f, 60f, 32f, 20f, 20f } }, { { 100f, 60f, 32f, 30f, 40f } } } };

	public Tensao t;

	public GenDataPerm gdp;
	public GenDataTrns gdt;
	public GenDataStrc gds;

	public GenDat() {
	}

	public GenDat(int pp, float[] selected) {
		gdp = new GenDataPerm(pp, 0f, 100f, 100f, 0.92f, selected[0], selected[1], 0f, 0f, 0f);
		gdt = new GenDataTrns(0f, 0f, selected[2], 0f, selected[3], 0f, 0f, 0f, 0f, 0f);
		gds = new GenDataStrc(0f, 0f, 0f);
	}
}
