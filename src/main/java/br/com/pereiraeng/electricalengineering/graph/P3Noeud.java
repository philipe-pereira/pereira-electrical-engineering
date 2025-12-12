package br.com.pereiraeng.electricalengineering.graph;

import br.com.pereiraeng.modelling.modelutils.graph.elt.PNoeud;

/**
 * <p>
 * Nó de sistema de transferência de potência elétrica que representa uma trinca
 * de nós, cada um deles com uma tensão alternada e defasada de um nó para o
 * outro.
 * </p>
 * 
 * <p>
 * Pode ter como valor, três tensões em um ponto do sistema trifásico (e.g., o
 * ponto de uma linha de transmissão onde iniciou-se um curto-circuito)
 * </p>
 * 
 * @author Philipe PEREIRA
 * @version September 28th, 2020
 *
 */
public interface P3Noeud extends PNoeud, VertexPFlow {
}
