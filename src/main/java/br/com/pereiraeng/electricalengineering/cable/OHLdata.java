package br.com.pereiraeng.electricalengineering.cable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;

import br.com.pereiraeng.electricalengineering.cable.CableEnums.CondMat;
import br.com.pereiraeng.electricalengineering.cable.CableEnums.IsoMat;
import br.com.pereiraeng.electricalengineering.cable.CableEnums.Secoes;

/**
 * Classe que reúne funções que permitem obter os parâmetros de cabos de linhas
 * aéreas
 * 
 * @author Philipe PEREIRA
 *
 */
public class OHLdata {

	private static final String COMMENTS = "//";

	private static String[] ohlTypes;

	public static String[] getOHLtypes() {
		LinkedList<String> out = new LinkedList<>();
		URL url = ClassLoader.getSystemResource("cable" + File.separator + "ohl.txt");
		try {
			InputStream stream = url.openStream(); // TODO essa parte do codigo se repete muito
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String str = null;
			while ((str = reader.readLine()) != null)
				out.add(str.split("\t")[1].toLowerCase());
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ohlTypes = out.toArray(new String[out.size()]);
	}

	public static String[] getCables(String typeName) {
		LinkedList<String> out = new LinkedList<>();
		// quando se abre o programa via jar, o nome do arquivo fica case sensitive
		URL url = ClassLoader.getSystemResource("cable" + File.separator + typeName.toLowerCase() + ".txt");
		try {
			InputStream stream = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String str = null;
			while ((str = reader.readLine()) != null) {
				String[] c = str.split("\t");
				out.add("".equals(c[1]) ? c[2] : c[1] + "-" + c[2]);
			}
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toArray(new String[out.size()]);
	}

	/**
	 * Função que a partir da designações de um dado cabo retorna sua classe
	 * 
	 * @param ohl
	 * @return
	 */
	public static int[] getCableRef(String ohl) {
		int type = -1, cable = -1;

		if (ohlTypes == null)
			getOHLtypes();

		loopClasses: for (int t = 0; t < ohlTypes.length; t++) { // procurar entre todos os tipos de cabo
			try {
				URL url = ClassLoader.getSystemResource("cable" + File.separator + ohlTypes[t] + ".txt");
				InputStream stream = url.openStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String str;
				while ((str = reader.readLine()) != null) {
					String[] data = str.split("\t");
					String name = data[1];
					if (!name.equals("")) { // se a geometria tiver um nome
						if (name.equalsIgnoreCase(ohl)) {
							type = t;
							cable = Integer.parseInt(data[0]);
							break loopClasses;
						}
					}
				}
				reader.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new int[] { type, cable };
	}

	/**
	 * Função que retorna a ampacidade de um dado cabo aéreo
	 * 
	 * @param type  classe do cabo
	 * @param cable numeração do cabo
	 * @return corrente admissível em regime permanente, em A
	 */
	public static int getCurrent(int type, int cable) {
		if (ohlTypes == null)
			getOHLtypes();

		// coluna do arquivo
		int pos = -1;
		switch (type) {
		case 0:
			pos = 14;
			break;
		case 1:
			pos = 23;
			break;
		case 6:
			pos = 8;
			break;
		}

		try {
			URL url = ClassLoader.getSystemResource("cable" + File.separator + ohlTypes[type] + ".txt");
			InputStream stream = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String str;
			while ((str = reader.readLine()) != null) {
				String[] data = str.split("\t");
				if (cable == Integer.parseInt(data[0])) {
					return Integer.parseInt(data[pos].replace(',', '.'));
				}
			}
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Função que retorna a descrição do cabo em função do seu código identificador
	 * 
	 * @param cable código que descrevem o cabo utilizado:
	 *              <ol>
	 *              <li><strong>:</strong> separa as diferentes seções</i>
	 *              <li><strong>;</strong> separa o tipo do cabo da sua bitola do
	 *              cabo (e.g., '1:9' representa o cabo ACSR (1) de pitola 107,22mm2
	 *              - Penguin (9))</i>
	 *              <li><strong>x</strong> indica quantos cabos por fase</i>
	 *              </ol>
	 * @return descrição do cabo
	 */
	public static String getCablingDescription(String cable) {
		String out = "";

		String[] cs = cable.split(":");

		for (int i = 0; i < cs.length; i++) {
			String c = cs[i];

			// multiplicadores
			String[] ds = c.split("x");
			if (ds.length == 2)
				out += (ds[0] + " x ");
			c = ds[ds.length - 1];

			ds = c.split(";");

			if (ds.length == 3) {
				// isolado

				int cnd = Integer.parseInt(ds[0]);
				int iso = Integer.parseInt(ds[1]);
				int sec = Integer.parseInt(ds[2]);

				// tipo de condutor (cobre ou alumínio)
				if (cnd > -1 && cnd < CondMat.values().length)
					out += CondMat.values()[cnd].getShort() + " ";

				// material da isolação
				if (iso > -1 && iso < IsoMat.values().length)
					out += IsoMat.values()[iso].name() + " ";

				// seção transversal do cabo
				if (sec > -1 && sec < Secoes.values().length)
					out += Secoes.values()[sec] + " ";

				out = out.substring(0, out.length() - 1);
			} else if (ds.length == 2) {
				// não isolado

				int cls = Integer.parseInt(ds[0]);
				int num = Integer.parseInt(ds[1]);

				// abrir arquivo com os nomes
				out += getCable(cls, num);
			}

			if (i != cs.length - 1)
				out += "; ";
		}

		return out;
	}

	private static String getCable(int type, int cable) {
		if (ohlTypes == null)
			getOHLtypes();
		// quando se abre o programa via jar, o nome do arquivo fica case sensitive
		URL url = ClassLoader.getSystemResource("cable" + File.separator + ohlTypes[type] + ".txt");
		try {
			InputStream stream = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(stream);
			BufferedReader reader = new BufferedReader(streamReader);
			String str = null;
			while ((str = reader.readLine()) != null) {
				String[] c = str.split("\t");
				if (Integer.parseInt(c[0]) == cable) {
					reader.close();
					return ("".equals(c[1]) ? "" : c[1] + "-") + c[3] + "mm\u00B2";
				}
			}
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Função que retorna a tabela de torres de linhas de distribuição e o material
	 * da qual é feita
	 * 
	 * @return matriz contendo duas colunas, onde na primeira está o nome do tipo de
	 *         torre e na segunda o {@link Estr material da qual a torre é feita}
	 */
	public static String[][] getEstruturas() {
		// quando se abre o programa via jar, o nome do arquivo fica case sensitive
		URL url = ClassLoader.getSystemResource("cable" + File.separator + "estr.txt");

		LinkedList<String[]> out = new LinkedList<>();
		try {
			InputStream stream = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(stream);
			BufferedReader reader = new BufferedReader(streamReader);
			String str = null;
			while ((str = reader.readLine()) != null)
				if (!str.startsWith(COMMENTS))
					out.add(str.split("\t"));
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toArray(new String[out.size()][]);
	}

	/**
	 * Função que carrega a tabela de impedância
	 * 
	 * @param nix2z10 tabela de dispersão a ser preenchida, associando para cada
	 *                chave, na forma da justaposição:
	 *                <ul start="0">
	 *                <li>cabo (1 caracter);</i>
	 *                <li>bitola (2 caracteres);</i>
	 *                <li>fases (1 caracter).</i>
	 *                </ul>
	 *                a um vetor com quatro posições:
	 *                <ol start="0">
	 *                <li>r1, em &#x3A9;/km;</i>
	 *                <li>x1, em &#x3A9;/km;</i>
	 *                <li>r0, em &#x3A9;/km;</i>
	 *                <li>x0, em &#x3A9;/km.</i>
	 *                </ol>
	 */
	public static void loadNix2z10(Map<String, float[]> nix2z10) {
		URL url = ClassLoader.getSystemResource("cable" + File.separator + "imp.txt");
		try {
			InputStream stream = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(stream);
			BufferedReader reader = new BufferedReader(streamReader);
			String str = null;
			while ((str = reader.readLine()) != null) {
				if (!str.startsWith(COMMENTS)) {
					String[] ss = str.split("\t");
					nix2z10.put(ss[0], new float[] { Float.parseFloat(ss[1].replace(',', '.')),
							Float.parseFloat(ss[2].replace(',', '.')), Float.parseFloat(ss[5].replace(',', '.')),
							Float.parseFloat(ss[6].replace(',', '.')) });
				}
			}
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que carrega a tabela de cabos
	 * 
	 * @param nix2cable tabela de dispersão a ser preenchida, associando para
	 *                  cada chave, na forma da justaposição:
	 *                  <ul start="0">
	 *                  <li>cabo (1 caracter);</i>                                  
	 *                  <li>bitola (2 caracteres);</i>                              
	 *                  <li>fases (1 caracter).</i>                                
	 *                  </ul>
	 *                  a uma descrição do cabo
	 * @param fullName  <code>true</code> para o nome completo, <code>false</code>
	 *                  para o código
	 */
	public static void loadNix2cable(Map<String, String> nix2cable, boolean fullName) {
		URL url = ClassLoader.getSystemResource("cable" + File.separator + "imp.txt");
		try {
			InputStream stream = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(stream);
			BufferedReader reader = new BufferedReader(streamReader);
			String str;
			while ((str = reader.readLine()) != null) {
				if (!str.startsWith(COMMENTS)) { // comentários
					String[] ss = str.split("\t");
					nix2cable.put(ss[0], ss[fullName ? 7 : 8]);
				}
			}
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que carrega a tabela de ampacidade dos cabos
	 * 
	 * @param nix2amp tabela de dispersão a ser preenchida, associando para cada
	 *                chave, na forma da justaposição:
	 *                <ul start="0">
	 *                <li>cabo (1 caracter);</i>
	 *                <li>bitola (2 caracteres);</i>
	 *                <li>fases (1 caracter).</i>
	 *                </ul>
	 *                a um vetor com duas posições:
	 *                <ol start="0">
	 *                <li>ampacidade nominal, em A;</i>
	 *                <li>ampacidade de emergência, em A.</i>
	 *                </ol>
	 */
	public static void loadNix2amp(Map<String, float[]> nix2amp) {
		URL url = ClassLoader.getSystemResource("cable" + File.separator + "imp.txt");
		try {
			InputStream stream = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(stream);
			BufferedReader reader = new BufferedReader(streamReader);
			String str;
			while ((str = reader.readLine()) != null) {
				if (!str.startsWith(COMMENTS)) {
					String[] ss = str.split("\t");
					nix2amp.put(ss[0], new float[] { Float.parseFloat(ss[10].replace(',', '.')),
							Float.parseFloat(ss[11].replace(',', '.')) });
				}
			}
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
