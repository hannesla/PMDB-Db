/**
 * 
 */
package ht.kokoelma;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Luokka jolla yhdistetään elokuvat ja henkilöt sekä kunkin henkilön roolit elokuvissa
 * 
 * @author Hannes Laukkanen
 * @version Mar 9, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Relaatio {
	private int eid;
	private int hid;
	private int rid;


	/**
	 * Kokeillaan Relaatio luokkaa luokan pääohjelmassa 
	 * @param args ei käytetä
	 */
	public static void main(String[] args) {
		Relaatio h = new Relaatio();
		Relaatio k = new Relaatio();

		h.liitosElokuva().liitosHenkilo().liitosRooli();
		k.liitosElokuva().liitosHenkilo().liitosRooli();

		h.tulosta(System.out);	
		k.tulosta(System.out);	
	}


	/**
	 * Tulostaa Relaation tiedot
	 * @param out tuolostettava tietovirta
	 */
	public void tulosta(PrintStream out) {		
		out.println("Eid: " + eid + "\n" +
				"Hid: " + hid + "\n" + 
				"Rid: " + rid + "\n");		
	}


	/**
	 * Relaatioon liittyvän henkilön id
	 * @return henkilön id
	 */
	public Relaatio liitosHenkilo() {
		hid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * Relaatioon liittyvän elokuvan id
	 * @return elokuvan id
	 */
	public Relaatio liitosElokuva() {
		eid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * relaatioon liittyvän henkilön roolin id
	 * @return roolin id
	 */
	public Relaatio liitosRooli() {
		rid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * Asetetaan elokuvan, henkilön ja henkilön roolin id arvo relaatioon
	 * @param eid elokuvan id
	 * @param hid henkilön id
	 * @param rid roolin id
	 */
	public void set(int eid, int hid, int rid) {
		this.eid = eid;
		this.hid = hid;
		this.rid = rid;		
	}


	/**
	 * palauttaa elokuvan id:n
	 * @return elokuvan id
	 */
	public int getEid() {
		return eid;
	}


	/**
	 * Palauttaa henkilön id:n 
	 * @return henkilön id
	 */
	public int getHid() {
		return hid;
	}


	/**
	 * Palauttaa relaation tiedot tiedostoon tallennettavassa muodossa
	 */
	@Override
	public String toString() {
		return "" + getEid()  + "|" + getHid() + "|" + getRid() + "|";
	}


	/**
	 * palautaa relaation roolin id:n
	 * @return roolin id
	 */
	public int getRid() {
		return rid;
	}


	/**
	 * asettaa relaation tiedot
	 * @param uusiEid elokuvan id
	 * @param uusiHid henkilön id
	 * @param uusiRid roolin id
	 * @return tämän relaation
	 */
	public Relaatio asetaTiedot(String uusiEid, String uusiHid, String uusiRid) { // TODO: parametrit inteiksi
		this.eid = Integer.parseInt(uusiEid);
		this.hid = Integer.parseInt(uusiHid);
		this.rid = Integer.parseInt(uusiRid);		

		return this;
	}


	//------------------------------------------------------------------------------------
	// Tietokannalla toteutus alkaa tästä

	/**
	 * Antaa tietokannan luontilausekkeen taululle
	 * @return relaatiotaulun luontilauseke
	 */
	public String annaLuontilauseke() {
		return "CREATE TABLE Relaatiot (" +
				"eid INTEGER, " +
				"hid INTEGER, " +
				"rid INTEGER " +
				// "FOREIGN KEY (jasenID) REFERENCES Jasenet(jasenID)" + // TODO: tarvitaanko?
				")";
	}


	/**
	 * Antaa relaation lisäyslausekkeen
	 * @param con tietokantayhteys
	 * @return relaation lisäyslauseke
	 * @throws SQLException Jos lausekkeen luonnissa on ongelmia
	 */
	public PreparedStatement annaLisayslauseke(Connection con)
			throws SQLException {
		PreparedStatement sql = con.prepareStatement("INSERT INTO Relaatiot" +
				"(eid, hid, rid) " +
				"VALUES (?, ?, ?)");

		sql.setInt(1, eid);
		sql.setInt(2, hid);
		sql.setInt(3, rid);

		return sql;
	}


	/** 
	 * Ottaa relaation tiedot ResultSetistä
	 * @param tulokset mistä tiedot otetaan
	 * @throws SQLException jos jokin menee väärin
	 */
	public void parse(ResultSet tulokset) throws SQLException {
		eid = tulokset.getInt("eid");
		hid = tulokset.getInt("hid");
		rid = tulokset.getInt("rid");
	}


	/**
	 * palauttaa k:nne sisällön aiheen
	 * @param k kuinka mones sisältö palautetaan
	 * @return sisältö
	 */
	public String getKysymys(int k) {

		if (k == 1) return "eid";
		if (k == 2) return "hid";
		if (k == 3) return "rid";

		return null;
	}

}

