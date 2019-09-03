package it.polito.tdp.crimes.model;

import java.time.Month;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String,DefaultWeightedEdge> grafo;
	private List<String> vertici;
	private List<PD> archi;
	private double media;
	

	
	public Model () {
		this.dao = new EventsDao();
		this.grafo = new SimpleWeightedGraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vertici = new LinkedList<>();
		this.archi = new LinkedList<>();
		
	}
	
	public List<String> getCategory() {
		
		return dao.getCategory();
	}

	public List<Month> getMonth() {
		
		return dao.getMonth();
	}

	public void creaGrafo(Month m, String s) {
		
		vertici = dao.getVertici(m,s);
		Graphs.addAllVertices(grafo, vertici);
		archi = dao.getArchi(m,s);
		
		for(PD p : archi) {
			String v1 = p.getV1();
			String v2 = p.getV2();
			double peso = p.getPeso();
			
			Graphs.addEdge(grafo, v1, v2, peso);
		}
	}

	public Graph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<String, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public List<PD> getBest() {
		int tot=0;
		media=0;
		List<PD> best = new LinkedList<>();
		for(PD e : archi) {
			tot +=e.getPeso();
			
		}
		media= tot/archi.size();
		
		for(PD p : archi) {
			
			if(p.getPeso()> media) {
				best.add(p);
				
			}
		}
		return best;
	}

	public List<PD> getArchi() {
		return archi;
	}

	public void setArchi(List<PD> archi) {
		this.archi = archi;
	}

	public double getMedia() {
		return media;
	}

	public void setMedia(double media) {
		this.media = media;
	}
	
	
}
