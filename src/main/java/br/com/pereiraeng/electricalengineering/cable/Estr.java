package br.com.pereiraeng.electricalengineering.cable;

import br.com.pereiraeng.core.StringUtils;

public enum Estr {
	MADEIRA("Madeira"), CONCRETO("Concreto"), METALICA("Metalica"), SUBTERRANEO("Subterraneo"), TRILHO("Trilho");

	private String nome;

	private Estr(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}

	public static int ordinal(String nome) {
		Estr estr = value(nome);
		if (estr != null)
			return estr.ordinal();
		else
			return -1;
	}

	public static Estr value(String nome) {
		nome = StringUtils.removeAccent(nome);
		for (int i = 0; i < Estr.values().length; i++)
			if (Estr.values()[i].nome.equals(nome))
				return Estr.values()[i];
		return null;
	}
}
