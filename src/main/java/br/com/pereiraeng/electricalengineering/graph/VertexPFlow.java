package br.com.pereiraeng.electricalengineering.graph;

import br.com.pereiraeng.electricalengineering.Fasor;
import br.com.pereiraeng.graph.numbered.VertexN;

/**
 * <p>
 * Nó que, num dado momento, pode ter como valor uma {@link Fasor tensão
 * alternada cossenoidal} elétrica num sistema de transferência de potência.
 * </p>
 * 
 * @author Philipe PEREIRA
 *
 */
public interface VertexPFlow extends VertexN, GraphPFlow {

	/**
	 * Função que retorna o fasor de tensão neste vértice
	 * 
	 * @return fasor de tensão
	 */
	public Fasor getV();
}
