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
 * Luokka jolla yhdistet��n elokuvat ja henkil�t sek� kunkin henkil�n roolit elokuvissa
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
	 * Kokeillaan Relaatio luokkaa luokan p��ohjelmassa 
	 * @param args ei k�ytet�
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
	 * Relaatioon liittyv�n henkil�n id
	 * @return henkil�n id
	 */
	public Relaatio liitosHenkilo() {
		hid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * Relaatioon liittyv�n elokuvan id
	 * @return elokuvan id
	 */
	public Relaatio liitosElokuva() {
		eid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * relaatioon liittyv�n henkil�n roolin id
	 * @return roolin id
	 */
	public Relaatio liitosRooli() {
		rid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * Asetetaan elokuvan, henkil�n ja henkil�n roolin id arvo relaatioon
	 * @param eid elokuvan id
	 * @param hid henkil�n id
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
	 * Palauttaa henkil�n id:n 
	 * @return henkil�n id
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
	 * @param uusiHid henkil�n id
	 * @param uusiRid roolin id
	 * @return t�m�n relaation
	 */
	public Relaatio asetaTiedot(String uusiEid, String uusiHid, String uusiRid) { // TODO: parametrit inteiksi
		this.eid = Integer.parseInt(uusiEid);
		this.hid = Integer.parseInt(uusiHid);
		this.rid = Integer.parseInt(uusiRid);		

		return this;
	}


	//------------------------------------------------------------------------------------
	// Tietokannalla toteutus alkaa t�st�

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
	 * Antaa relaation lis�yslausekkeen
	 * @param con tietokantayhteys
	 * @return relaation lis�yslauseke
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
	 * Ottaa relaation tiedot ResultSetist�
	 * @param tulokset mist� tiedot otetaan
	 * @throws SQLException jos jokin menee v��rin
	 */
	public void parse(ResultSet tulokset) throws SQLException {
		eid = tulokset.getInt("eid");
		hid = tulokset.getInt("hid");
		rid = tulokset.getInt("rid");
	}


	/**
	 * palauttaa k:nne sis�ll�n aiheen
	 * @param k kuinka mones sis�lt� palautetaan
	 * @return sis�lt�
	 */
	public String getKysymys(int k) {

		if (k == 1) return "eid";
		if (k == 2) return "hid";
		if (k == 3) return "rid";

		return null;
	}

}

