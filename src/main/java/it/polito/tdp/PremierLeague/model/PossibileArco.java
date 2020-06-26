package it.polito.tdp.PremierLeague.model;

public class PossibileArco {
	
	int id1, id2, delta;

	public PossibileArco(int id1, int id2, int delta) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.delta = delta;
	}

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}
	
	

}
