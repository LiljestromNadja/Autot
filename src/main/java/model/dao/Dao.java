package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Auto;

public class Dao {
	private Connection con = null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep = null;
	private String sql;
	private String db = "Autot.sqlite";

	private Connection yhdista() {
		Connection con = null;
		String path = System.getProperty("catalina.base");
		path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); // Eclipsessa
		//System.out.println(path); //Tästä näet mihin kansioon laitat tietokanta-tiedostosi
		// path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
		String url = "jdbc:sqlite:" + path + db;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			System.out.println("Yhteys avattu.");
		} catch (Exception e) {
			System.out.println("Yhteyden avaus epäonnistui.");
			e.printStackTrace();
		}
		return con;
	}

	private void sulje() {		
		if (stmtPrep != null) {
			try {
				stmtPrep.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
				System.out.println("Yhteys suljettu.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	public ArrayList<Auto> getAllItems() {
		ArrayList<Auto> autot = new ArrayList<Auto>();
		sql = "SELECT * FROM autot ORDER BY id DESC"; //Suurin id tulee ensimmäisenä
		try {
			con = yhdista();
			if (con != null) { // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);
				rs = stmtPrep.executeQuery();
				if (rs != null) { // jos kysely onnistui
					while (rs.next()) {
						Auto auto = new Auto();
						auto.setId(rs.getInt(1));//1=sarakenumero, voisi myös lukea sarakkeen nimi lainausmerkeissä
						auto.setRekno(rs.getString(2));//2=sarakenumero, voisi myös lukea sarakkeen nimi lainausmerkeissä
						auto.setMerkki(rs.getString(3));
						auto.setMalli(rs.getString(4));
						auto.setVuosi(rs.getInt(5));
						autot.add(auto);//auto lisätään autot arraylistiin
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sulje();//Joka tapauksessa yhteys suljetaan!! 
		}
		return autot;
	}
	
	public ArrayList<Auto> getAllitems(String searchStr) {//Metodeja voi kuormittaa kunhan parametreissa on eroja 
		ArrayList<Auto> autot = new ArrayList<Auto>();
				sql = "SELECT * FROM autot WHERE rekno LIKE ? OR merkki LIKE ? OR malli LIKE ? ORDER BY id DESC";
				try {
					con = yhdista();
					if (con != null) { // jos yhteys onnistui
						stmtPrep = con.prepareStatement(sql);
						stmtPrep.setString(1,"%" + searchStr + "%"); //Kohdistuu ekaan kysymysmerkkiin sql-lauseessa
						stmtPrep.setString(2,"%" + searchStr + "%");//Toka kysymysmerkkki
						stmtPrep.setString(3,"%" + searchStr + "%");//Kolmas HUOM!! % -merkki on "laaja jokeri" eli edessä ja takana voi olla mitä vain 
						rs = stmtPrep.executeQuery();
						if (rs != null) { // jos kysely onnistui
							while (rs.next()) {
								Auto auto = new Auto();
								auto.setId(rs.getInt(1));//1=sarakenumero, voisi myös lukea sarakkeen nimi lainausmerkeissä
								auto.setRekno(rs.getString(2));//2=sarakenumero, voisi myös lukea sarakkeen nimi lainausmerkeissä
								auto.setMerkki(rs.getString(3));
								auto.setMalli(rs.getString(4));
								auto.setVuosi(rs.getInt(5));
								autot.add(auto);//auto lisätään autot arraylistiin
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sulje();//Joka tapauksessa yhteys suljetaan!! 
				}
				return autot;
				
	}
	
	public boolean addItem(Auto auto) {
		boolean paluuArvo = true;
		sql = "INSERT INTO autot(rekno, merkki, malli, vuosi)VALUES(?,?,?,?)";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setString(1, auto.getRekno()); //huom ID:tä ei anneta
			stmtPrep.setString(2, auto.getMerkki());
			stmtPrep.setString(3, auto.getMalli());
			stmtPrep.setInt(4, auto.getVuosi());
			stmtPrep.executeUpdate();		
		} catch (Exception e) {
			paluuArvo=false;
			e.printStackTrace();
		} finally {
			sulje();
		}
		return paluuArvo;
	}
	
	public boolean removeItem(int id) { // Todellisuudessa tiedot ensisijaisesti merkitään poistetuiksi, DELETEä ajetaan hyvin harvoin, viite-eheys
		boolean paluuArvo = true;
		sql = "DELETE FROM autot WHERE id=?";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setInt(1, id);
			stmtPrep.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		} finally {
			sulje();
		}
		return paluuArvo;
	}
}