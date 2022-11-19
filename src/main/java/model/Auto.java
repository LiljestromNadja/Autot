package model;

public class Auto { // Tämä on bean luokka
	private int id, vuosi;
	private String rekno, merkki, malli;
	public Auto() {
		
	}
	public Auto(int id, int vuosi, String rekno, String merkki, String malli) {
		//super();
		this.id = id;
		this.vuosi = vuosi;
		this.rekno = rekno;
		this.merkki = merkki;
		this.malli = malli;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVuosi() {
		return vuosi;
	}
	public void setVuosi(int vuosi) {
		this.vuosi = vuosi;
	}
	public String getRekno() {
		return rekno;
	}
	public void setRekno(String rekno) {
		this.rekno = rekno;
	}
	public String getMerkki() {
		return merkki;
	}
	public void setMerkki(String merkki) {
		this.merkki = merkki;
	}
	public String getMalli() {
		return malli;
	}
	public void setMalli(String malli) {
		this.malli = malli;
	}
	@Override
	public String toString() {
		return "Auto [id=" + id + ", vuosi=" + vuosi + ", rekno=" + rekno + ", merkki=" + merkki + ", malli=" + malli
				+ "]";
	}
	
	
	
	
}
