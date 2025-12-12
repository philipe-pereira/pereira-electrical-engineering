package br.com.pereiraeng.electricalengineering.graph;

import br.com.pereiraeng.graph.numbered.EdgeN;

/**
 * Aresta numerada pela qual há um fluxo de energia elétrica com variação na
 * potência ativa ou reativa (i.e., há uma impdência entre elas)
 * 
 * @author Philipe PEREIRA
 *
 */
public interface EdgePFlow extends EdgeN, GraphPFlow {

	/**
	 * Função que retorna o valor do fluxo entre dois vértices
	 * 
	 * @param vf vértice de partida do fluxo
	 * @param vt vértice de chegada do fluxo
	 * @param p  <code>true</code> par potência ativa, <code>false</code> par
	 *           reativa
	 * @return valor do fluxo de potência, sendo que o sinal positivo indica que sai
	 *         da partida, negativo chega na partida
	 */
	public float getFlow(VertexPFlow vf, VertexPFlow vt, boolean p);

}
