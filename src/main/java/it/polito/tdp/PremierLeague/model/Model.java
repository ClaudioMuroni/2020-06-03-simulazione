package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Map<Integer, Player> mappaGiocatori;
	Graph<Player, DefaultWeightedEdge> grafo;
	List<Player> dreamTeam;
	int titolaritaDT;
	
	public void creaGrafo(double media) {
	
		dao = new PremierLeagueDAO();
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		mappaGiocatori = dao.getGiocatoriByMedia(media);
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

	public List<Player> dreamTeam(int k) {
		
		dreamTeam = new ArrayList<Player>();
		titolaritaDT = 0;
		List<Player> giocatoriDisponibili = new LinkedList<>();
		for(Player p : grafo.vertexSet()) {
			
			giocatoriDisponibili.add(p);
		}
		List<Player> parziale = new ArrayList<>();
		
		ricorsione(parziale, giocatoriDisponibili, k);
		
		return dreamTeam;
	}

	private void ricorsione(List<Player> parziale, List<Player> giocatoriDisponibili, int k) {
		
		if(parziale.size() == k) {
			
			dreamTeam.addAll(parziale);
			return;
		}
		
		//
		
		int max = 0;
		Player giocatore = new Player(0, "");
		List<Player> giocatoriBattuti = new ArrayList<>();
		
		for(Player p : giocatoriDisponibili) {
			
			int sommaE = 0;
			for(DefaultWeightedEdge e : grafo.incomingEdgesOf(p)) {
				
				sommaE += grafo.getEdgeWeight(e);
			}
			
			int sommaU = 0;
			List<Player> giocB = new ArrayList<>();
			for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(p)) {
				
				sommaU += grafo.getEdgeWeight(e);
				giocB.add(grafo.getEdgeTarget(e));
			}
			
			int titolarita = sommaU-sommaE;
			if(titolarita > max) {
				
				max = titolarita;
				giocatore = p;
				giocatoriBattuti.clear();
				giocatoriBattuti.addAll(giocB);
			}
		}
		
		parziale.add(giocatore);
		
		giocatoriDisponibili.remove(giocatore);
		for(Player p : giocatoriBattuti) {
			
			giocatoriDisponibili.remove(p);
		}
		titolaritaDT += max;
		
		ricorsione(parziale, giocatoriDisponibili, k);
	}

	public int getTitolaritaDT() {
		return titolaritaDT;
	}
	
	

}
