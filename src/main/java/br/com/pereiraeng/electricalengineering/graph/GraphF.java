package br.com.pereiraeng.electricalengineering.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.pereiraeng.graph.numbered.EdgeN;
import br.com.pereiraeng.graph.numbered.GraphN;
import br.com.pereiraeng.math.DuplaEmeio;

/**
 * Classe do objeto que representa o grafo de um sistema de potência para o qual
 * o fluxo foi calculado,
 * {@link EdgePFlow#getFlow(VertexPFlow, VertexPFlow, boolean) com o fluxo
 * indicado nos ramos}.
 * 
 * @author Philipe PEREIRA
 *
 */
public class GraphF extends GraphN {

	public GraphF() {
		this(new HashMap<DuplaEmeio, EdgePFlow>());
	}

	/**
	 * Construtor do grafo
	 * 
	 * @param es arestas do grafo
	 */
	public GraphF(Map<DuplaEmeio, EdgePFlow> es) {
		super.setEns(es.values());
	}

	public GraphF(Collection<EdgePFlow> ens) {
		super.setEns(ens);
	}

	public Map<DuplaEmeio, EdgePFlow> getEs() {
		Map<DuplaEmeio, EdgePFlow> es = new HashMap<>();
		for (Entry<DuplaEmeio, ? extends EdgeN> e : super.ens.entrySet())
			es.put(e.getKey(), (EdgePFlow) e.getValue());
		return es;
	}

	public void add(EdgePFlow ecf) {
		super.ens.put(ecf.getNums(), ecf);
	}
}
