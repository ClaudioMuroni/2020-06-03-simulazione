package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Graph<Player, DefaultWeightedEdge> grafo;

	public void creaGrafo(double media) {
	
		dao = new PremierLeagueDAO();
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Map<Integer, Player> mappaGiocatori = dao.getGiocatoriByMedia(media);
		Graphs.addAllVertices(grafo, mappaGiocatori.values());
		
		//System.out.println(grafo);
		
		List<PossibileArco> listaPossibiliArchi = dao.getAllPossibiliArchi();
		
		for(PossibileArco pa : listaPossibiliArchi) {
			
			if ( mappaGiocatori.containsKey(pa.getId1()) && mappaGiocatori.containsKey(pa.getId2()) ) {
				
				Graphs.addEdge(grafo, mappaGiocatori.get(pa.getId1()), mappaGiocatori.get(pa.getId2()), pa.getDelta());
			}
		}
		
		System.out.println(grafo);
	}

	public List<Player> cercaTopPlayer() {
		
		Player top = new Player(0, "");
		int max = 0;
		
		for (Player p : grafo.vertexSet()) {
			
			if (grafo.outDegreeOf(p) > max) {
				
				top = p;
				max = grafo.outDegreeOf(p);
			}
		}
		
		List<Player> result = new ArrayList<>();
		result.add(top);
		
		Map<Double, Player> mappaGiocatoriBattuti = new TreeMap<>();
		for (DefaultWeightedEdge e : grafo.outgoingEdgesOf(top)) {
			
			mappaGiocatoriBattuti.put(-grafo.getEdgeWeight(e), grafo.getEdgeTarget(e));
		}
		
		result.addAll(mappaGiocatoriBattuti.values());
		
		return result;
	}

}
