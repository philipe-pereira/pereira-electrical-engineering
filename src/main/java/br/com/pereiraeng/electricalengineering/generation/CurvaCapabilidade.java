package br.com.pereiraeng.electricalengineering.generation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import br.com.pereiraeng.core.collections.MapUtils;
import br.com.pereiraeng.electricalcircuit.solving.CircuitCalc;
import br.com.pereiraeng.math.Multiplicador;
import br.com.pereiraeng.math.Vec;

/**
 * Classe que agrupa métodos que permitem traçar a curva de capabilidade de um
 * gerador síncrono a partir de seus parâmetros, bem como o cálculo do ponto de
 * operação em regime permanente
 * 
 * @author Philipe PEREIRA
 *
 */
public class CurvaCapabilidade {

	private static final Dimension dim = new Dimension(500, 500);

	// ------------------- PARÂMETROS OPERACIONAIS -------------------

	/**
	 * Tensão de linha do barramento infinito, em V
	 */
	private double v;

	// -------------------- PARÂMETROS DA MÁQUINA --------------------

	/**
	 * Tensão nominal do gerador, em V
	 */
	private double vNom;

	/**
	 * Potência aparente máxima do gerador, em VA
	 */
	private double sNom;

	/**
	 * Potência máxima que o gerador pode produzir, em W
	 */
	private double pMax;

	/**
	 * Potência mínima que o gerador pode produzir (essa restrição é comum em
	 * hidrelétricas), em W
	 */
	private double pMin;

	/**
	 * Reatância de eixo direto, em Ohms
	 */
	private double xd;

	/**
	 * Reatância de eixo de quadratura, em Ohms
	 */
	private double xq;

	/**
	 * Reatância do transformador elevador referida ao lado de baixa tensão, em Ohms
	 */
	private double xtr;

	/**
	 * Fator de potência nominal
	 */
	private double cosPhiNom;

	/**
	 * Valor mínimo da tensão de linha em aberto (calculada considerando-se que ela
	 * é obtido com {@link CurvaCapabilidade#I_EXC_MIN uma portentagem} da
	 * {@link CurvaCapabilidade#vsMax corrente de excitação máxima})
	 */
	private double vsMin;

	/**
	 * Valor máximo da tensão de linha em aberto (valor para o qual a potência da
	 * máquina é nominal, com {@link CurvaCapabilidade#cosPhiNom fator de potência}
	 * também nominal)
	 */
	private double vsMax;

	/**
	 * Relação entre a corrente de excitação mínima e a máxima
	 */
	private static final double I_EXC_MIN = 0.10;

	private static final double ESTAB_MARG = 0.20;

	private boolean valid = false;

	/**
	 * Função que estabelece o ponto de operação da máquina a partir do valor da
	 * tensão de linha do barramento infinito
	 * 
	 * @param v tensão de linha do barramento infinito, em V
	 */
	public void setV(double v) {
		this.v = v;
	}

	/**
	 * Função com a qual se estabelece os parâmetros do desenho das curvas de
	 * capabilidade
	 * 
	 * @param vNom      tensão nominal, em V
	 * @param sNom      potência aparente nominal, em V
	 * @param cosPhiNom fator de potência nominal
	 * @param pMin      potência ativa mínima, em W
	 * @param pMax      potência ativa máxima, em W
	 * @param xd        reatância de eixo de direta, em pu
	 * @param xq        reatância de eixo de quadratura, em pu
	 * @param xtr       reatância do transformador elevador, em pu
	 */
	public void set(double vNom, double sNom, double cosPhiNom, double pMin, double pMax, double xd, double xq,
			double xtr) {
		this.vNom = vNom;
		this.sNom = sNom;
		this.pMax = pMax;
		this.pMin = pMin;
		this.cosPhiNom = cosPhiNom;

		if (sNom > 0.) {
			double zb = CircuitCalc.calcZb(vNom, sNom);

			this.xd = xd * zb;
			this.xq = xq * zb;
			this.xtr = xtr * zb;

			// calcular vSmin e vSmax
			if (this.xd > 0. && this.xq > 0.) {
				double[] xs = getVsMax(new double[] { 1.7 * vNom, Math.PI / 4 }, sNom * this.cosPhiNom,
						sNom * Math.sin(Math.acos(this.cosPhiNom)));
				this.vsMax = xs[0];
				this.vsMin = I_EXC_MIN * vsMax;
			}
		}
		valid = true;
	}

	public void clear() {
		valid = false;
	}

	// ----------------------------- OPERAÇÃO -----------------------------

	// ----------------- CÁLCULO DA TENSÃO EM ABERTO NOMINAL -----------------

	private static final int ITERMAX = 100;

	/**
	 * Função que retorna o valor da tensão em vazio para a qual se obtem, com a
	 * máquina gerando potência ativa nominal, o fator de potência nominal. Esse
	 * valor de tensão é obtido para o valor de corrente de excitação dito nominal
	 * 
	 * @param x0 valor inicial tensão em aberto e ângulo de potência
	 * @param p  potência reativa nominal
	 * @param q  potência reativa nominal
	 * @return valor encontrado para a tensão em aberto e ângulo de potência, ou
	 *         <code>null</code> se não encontrada uma solução
	 */
	public double[] getVsMax(double[] x0, double p, double q) {
		double[] x = null;

		double xql = xq + xtr;
		double xdl = xd + xtr;
		double A = Math.pow(vNom, 2) * (1 / xql - 1 / xdl);
		double b = vNom / xdl;

		double[] x1 = x0;
		for (int i = 0; i < ITERMAX; i++) {
			x = Vec.sub(x1, Vec.transf(Vec.inverte2(jacobiana(x1, A, b)), f(x1, A, b, xql, p, q)));
			if (Vec.equals(x, x1))
				return x;
			else
				x1 = x;
		}
		return null;
	}

	/**
	 * Função que retorna o vetor do sistema de equações algébricas não-lineares que
	 * se quer resolver
	 * 
	 * @param x   vetor com os parâmetros (tensão em aberto e ângulo de potência)
	 * @param A   v^2*(1/xq-1/xd)
	 * @param b   v/xd
	 * @param xql reatância de quadratura
	 * @param p   potência ativa
	 * @param q   potência reativa
	 * @return vetor com o valores das expressões
	 */
	private double[] f(double[] x, double A, double b, double xql, double p, double q) {
		double rho = A * Math.cos(x[1]) + b * x[0];

		return new double[] { rho * Math.sin(x[1]) - p, rho * Math.cos(x[1]) - q - Math.pow(vNom, 2) / xql };
	}

	/**
	 * Função que retorna a matriz jacobiana do vetor de equações
	 * {@link CurvaCapabilidade#f f}.
	 * 
	 * @param x vetor com os parâmetros (tensão em aberto e ângulo de potência)
	 * @param A v^2*(1/xq-1/xd)
	 * @param b v/xd
	 * @return &#x2202; {@link CurvaCapabilidade#f f} / &#x2202; x
	 */
	private double[][] jacobiana(double[] x, double A, double b) {
		double[][] out = new double[2][2];

		out[0][0] = b * Math.sin(x[1]);
		out[0][1] = A * Math.cos(2 * x[1]) + b * x[0] * Math.cos(x[1]);
		out[1][0] = b * Math.cos(x[1]);
		out[1][1] = -Math.sin(x[1]) * (2 * A * Math.cos(x[1]) + b * x[0]);

		return out;
	}

	// --------------------------- FAIXA DE REATIVO ---------------------------

	public static float[] getRange(float p, GenDataPerm genData) {
		Map<Integer, GenDataPerm> data = new HashMap<>(1);
		data.put(-1, genData);
		return getRange(p, data);
	}

	public static float[] getRange(float ptotal, Map<Integer, GenDataPerm> data) {
		if (Float.isNaN(ptotal))
			return null;
		if (ptotal <= 0f)
			return new float[] { 0f, 0f };

		float qmin = 0f, qmax = 0f;

		Map<Integer, Float> ps = null;
		if (data.size() == 1) {
			// se só há uma máquina, toda a potência ativa é dela
			ps = new HashMap<Integer, Float>(1);
			ps.put(data.entrySet().iterator().next().getKey(), ptotal);
		} else
			// se há mais de uma máquina, distribuir a potência
			ps = getPs(ptotal, data);

		for (Entry<Integer, GenDataPerm> e : data.entrySet()) {
			float pi = ps.get(e.getKey());
			GenDataPerm d = e.getValue();

			// CONFERE se respeita os limites
			float pmin = (float) d.getField(1), pmax = (float) d.getField(2);
			if (pi < pmin * 0.1f) {
				System.err.println(String.format("Gerador %d operando fora da faixa de P: gerando %f MW < %f",
						e.getKey(), pi, pmin));
				// considerar que está desligado
				continue;
			} else if (pi > 1.1 * pmax) {
				System.err.println(String.format("Gerador %d operando fora da faixa de P: gerando %f MW > %f",
						e.getKey(), pi, pmax));
			}

			// corrente de armadura
			float s = (float) d.getField(3);
			float ql = (float) Math.sqrt(s * s - pi * pi);

			if (!Float.isNaN(ql)) {
				qmax += ql;
				qmin -= ql;
			}
		}

		return new float[] { qmin, qmax };
	}

	/**
	 * Função que faz a distribuição de potência ativa entre os geradores
	 * 
	 * @param p
	 * @param data
	 * @return
	 */
	private static Map<Integer, Float> getPs(float p, Map<Integer, GenDataPerm> data) {
		Map<Integer, GenDataPerm> ds = MapUtils.sortMap(data, new Comparator<GenDataPerm>() {
			@Override
			public int compare(GenDataPerm o1, GenDataPerm o2) {
				return (int) (((float) o2.getField(2)) - ((float) o1.getField(2)));
			}
		});

		Map<Integer, Float> out = new HashMap<Integer, Float>();
		for (Entry<Integer, GenDataPerm> e : ds.entrySet()) {
			GenDataPerm d = e.getValue();
			float pmin = (float) d.getField(1), pmax = (float) d.getField(2);

			if (p > pmin) {
				float pi = p > pmax ? pmax : p;
				out.put(e.getKey(), pi);
				p -= pi;
			}
		}

		if (p != 0f)
			System.err.println(String.format("Sobrou %fMW no grupo de geradores %s", p, data.keySet().toString()));

		return out;
	}

	// ----------------------------- PARTE GRÁFICA -----------------------------

	/**
	 * Função que retorna um objeto responsável por desenhar a curva de capabilidade
	 * de um dos gerados da usina cujas características foram estam estabelecidas no
	 * objeto
	 * 
	 * @return painel com o desenho da curva
	 */
	public CurvaCap getChart() {
		return new CurvaCap();
	}

	/**
	 * Classe que representa o objeto gráfico que desenha a curva de capabilidade
	 * 
	 * @author Philipe PEREIRA
	 *
	 */
	public class CurvaCap extends JPanel implements MouseMotionListener {
		private static final long serialVersionUID = 1L;

		/**
		 * coordenada em pixels do eixo vertical
		 */
		private static final int x0 = 250;

		/**
		 * coordenada em pixels do eixo horizontal
		 */
		private static final int y0 = 250;

		// fonctions 'mouvement'
		protected transient int x_mouse, y_mouse;

		public CurvaCap() {
			setOpaque(true);
			setBackground(Color.WHITE);
			setPreferredSize(dim);
			addMouseMotionListener(this);
		}

		@Override
		protected void paintComponent(Graphics gr) {
			Graphics2D g = (Graphics2D) gr;

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

			super.paintComponent(g);

			if (valid) {
				// ---------------- grade -------------------

				double ppVA = x0 / sNom;
				double order = Math.pow(10, (int) Math.log10(sNom));

				// eixos
				g.drawLine(0, y0, 600, y0);
				g.drawLine(x0, 0, x0, 600);

				g.fillOval(x0 - 3, y0 - 3, 6, 6);

				// grade
				g.setColor(Color.LIGHT_GRAY);
				int n = (int) (sNom / order);
				for (int i = -n; i <= n; i++) {
					if (i == 0)
						continue;
					g.drawLine((int) (x0 + i * ppVA * order), 0, (int) (x0 + i * ppVA * order), 600);
				}

				for (int i = -n; i <= n; i++) {
					if (i == 0)
						continue;
					g.drawLine(0, (int) (y0 + i * ppVA * order), 600, (int) (y0 + i * ppVA * order));
				}

				// ---------------- turbina -------------------

				// potência máxima e mínima (pMax e pMin)
				double r = pMax * ppVA;
				g.setColor(Color.RED);
				g.drawLine(0, (int) (y0 - r), 600, (int) (y0 - r));

				r = pMin * ppVA;
				g.setColor(Color.GREEN);
				g.drawLine(0, (int) (y0 - r), 600, (int) (y0 - r));

				// ---------------- corrente de estator -------------------

				// corrente de armadura (sNom)

				r = ppVA * sNom * (v / vNom);
				g.setColor(Color.BLUE);
				g.drawOval((int) (x0 - r), (int) (y0 - r), (int) (2 * r), (int) (2 * r));

				// ---------------- corrente de rotor -------------------

				// corrente de excitação mínima

				double xql = xq + xtr;
				double xdl = xd + xtr;

				int OSl = x0 - (int) ((Math.pow(v, 2) / xql) * ppVA);
				g.fillOval(OSl - 3, y0 - 3, 6, 6);
				int A = (int) ((Math.pow(v, 2) * (1 / xql - 1 / xdl)) * ppVA);

				int B = (int) ((v * vsMin / xdl) * ppVA);

				n = 100;
				double step = 2 * Math.PI / n;
				double delta = 0;
				int[][] xs = new int[2][n];
				for (int i = 0; i < n; i++) {
					double rho = A * Math.cos(delta) + B;
					xs[0][i] = (int) (rho * Math.cos(delta) + OSl);
					xs[1][i] = (int) (rho * Math.sin(delta) + y0);
					delta += step;
				}

				g.setColor(Color.ORANGE);
				g.drawPolygon(xs[0], xs[1], xs[0].length);

				// corrente de excitação máxima

				B = (int) ((v * vsMax / xdl) * ppVA);
				delta = 0;
				for (int i = 0; i < n; i++) {
					double rho = A * Math.cos(delta) + B;
					xs[0][i] = (int) (rho * Math.cos(delta) + OSl);
					xs[1][i] = (int) (rho * Math.sin(delta) + y0);
					delta += step;
				}

				g.drawPolygon(xs[0], xs[1], xs[0].length);

				// ---------------- estabilidade ----------------

				xs = new int[2][n / 2];
				// pi / 2
				delta = 1.5707963267958966;
				for (int i = 0; i < n / 2; i++) {
					double rho = A * (Math.pow(Math.sin(delta), 2) / Math.cos(delta));
					xs[0][i] = (int) (rho * Math.cos(delta) + OSl + ESTAB_MARG * ppVA * sNom);
					xs[1][i] = (int) (rho * Math.sin(delta) + y0);
					delta += step;
				}

				g.setColor(Color.MAGENTA);
				g.drawPolyline(xs[0], xs[1], xs[0].length);

				// ---------------- track ----------------

				double p = (y0 - this.y_mouse) / ppVA;
				double q = (this.x_mouse - x0) / ppVA;
				g.setColor(Color.ORANGE);
				g.drawString(Multiplicador.getMult(p, 3, Multiplicador.QUILO, Multiplicador.MEGA) + "W", this.x_mouse,
						this.y_mouse + 30);
				g.setColor(Color.GREEN);
				g.drawString(Multiplicador.getMult(q, 3, Multiplicador.QUILO, Multiplicador.MEGA) + "VAr", this.x_mouse,
						this.y_mouse + 45);
			}
		}

		@Override
		public void mouseDragged(MouseEvent event) {
		}

		@Override
		public void mouseMoved(MouseEvent event) {
			this.x_mouse = event.getX();
			this.y_mouse = event.getY();
			super.repaint();
		}
	}
}
