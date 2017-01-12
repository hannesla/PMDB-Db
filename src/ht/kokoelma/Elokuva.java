/**
 * 
 */
package ht.kokoelma;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ht.kanta.ElokuvaTarkistus.*;

/**
 * Luokka Yksitt�isten elokuvin luontiin
 * 
 * @author Hannes Laukkanen
 * @version 23.2.2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Elokuva {

	private int     eid;
	private String  nimi = "";
	private int     vuosi;
	private String  avainsanat = "";

	private static int seuraavaEid = 1;


	/**
	 * T�ytt�� olion esimerkkitiedoilla joita siihen voitaisiin sy�tt��
	 * @example
	 * <pre name="test">
	 * Elokuva e1 = new Elokuva(); 
	 * e1.taytaTiedoilla();
	 * e1.getNimi().matches("Interstellar [0-9]{4}") === true;
	 * String v = "" + e1.getVuosi();
	 * v.matches("[0-9]{4}") === true;
	 * e1.getAvainsanat() === "scifi, j�nnitt�v�";
	 * </pre>
	 */
	public void taytaTiedoilla() {		
		nimi = "Interstellar " + arvoValille(1000, 9999);
		vuosi = arvoValille(1900, 2016);
		avainsanat = "scifi, j�nnitt�v�";		
	}


	/**
	 * Kertoo elokuvan id:n
	 * @return elokuvan id (eli eid)
	 */
	public int getEid() {
		return eid;
	}


	/**
	 * palauttaa elokuvan avainsanat
	 * @return avainsanat
	 */
	public String getAvainsanat() {
		return avainsanat;
	}


	/**
	 * 
	 * @return j�senen nimi
	 */
	public String getNimi() {		
		return nimi;
	}


	/**
	 * palauttaa elokuvan julkaisuvuoden
	 * @return elokuvan julkaisuvuosi
	 */
	public int getVuosi() {
		return vuosi;
	}


	/**
	 * antaa elokuvalle yksil�iv�n tunnuksen
	 * @return palauttaa elokuvan id:n
	 *@example
	 * <pre name="test">
	 * Elokuva test1 = new Elokuva();
	 * Elokuva test2 = new Elokuva();
	 * test1.getEid() === 0;
	 * test2.getEid() === 0;
	 * test1.rekisteroi();
	 * test2.rekisteroi();
	 * test2.getEid() - test1.getEid() == 1 === true; // testattava n�in, koska testien suoritusj�rjestyst� ei voi tiet��
	 * </pre>
	 */
	public int rekisteroi() {		
		if (eid > 0) return eid;

		eid = seuraavaEid;
		seuraavaEid++;

		return eid;
	}


	/**
	 * Tulostetaan elokuvan tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println("Elokuvan nimi: " +   nimi + "\n" +
				"Valmistumisvuosi: " + vuosi + "\n\n" +
				"Avainsanat: " + avainsanat + "\n");					
	}


	/**
	 * Tulostaa elokuvan tiedot
	 * @param os Tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));		
	}	


	/**
	 * Palauttaa elokuvan tiedot tiedostoon tallennettavassa muodossa
	 * @example
	 * <pre name="test">
	 *   Elokuva test = new Elokuva();
	 *   test.asetaTiedot("5", "elokuva", "1999", "vainsanat");
	 *   "5|elokuva|1999|vainsanat|".equals(test.toString()) === true;
	 *   "5|elokuva|1939|vansanat|".equals(test.toString()) === false;
	 * </pre>
	 */
	@Override
	public String toString() {
		if (avainsanat.isEmpty()) avainsanat = " ";
		return "" + getEid()  + "|" + getNimi() + "|" + getVuosi() +
				"|" + getAvainsanat() + "|";
	}


	/**
	 * Elokuva-luokan toiminnan kokeilua varten
	 * @param args ei k�ytet�
	 */
	public static void main(String[] args) {
		Elokuva elokuva1 = new Elokuva(); 
		Elokuva elokuva2 = new Elokuva();

		elokuva1.tulosta(System.out);
		elokuva2.tulosta(System.out);

		elokuva1.taytaTiedoilla();
		elokuva1.rekisteroi();

		elokuva2.taytaTiedoilla();
		elokuva2.rekisteroi();

		elokuva1.tulosta(System.out);
		elokuva2.tulosta(System.out);
	}


	/**
	 * Asettaa elokuvalle tiedot
	 * @param annettuEid elokuvan tunnistusnumero
	 * @param annettuNimi elokuvan nimi
	 * @param annettuVuosi valmistumisvuosi
	 * @param annetutAvainsanat elokuvaan liittyv�t avainsanat
	 */
	public void asetaTiedot(String annettuEid, String annettuNimi, String annettuVuosi, String annetutAvainsanat) {
		this.eid = Integer.parseInt(annettuEid);
		this.nimi = annettuNimi;
		this.vuosi = Integer.parseInt(annettuVuosi);
		this.avainsanat = annetutAvainsanat;
		seuraavaEid = this.eid + 1;
	}


	/**
	 * Alustaa luokan seuraavanEid:n yhdeksi
	 */
	public static void alustaSeuraavaEid() {
		seuraavaEid = 1;
	}


	/**
	 * palauttaa kenttien lkm
	 * @return kenttien lkm
	 */
	public int getKenttienLkm() {
		return 4;
	}


	/**
	 * palauttaa ensimm�isen kant�n, joka on mielek�s kutsujalle tiet��
	 * @return ensimm�inen mielek�s kentt�
	 */
	public int ekaKentta() {
		return 1;
	}


	/**
	 * Kertoo kutsujalle mik� tieto laitetaan mihinkin kentt��n 
	 * @param kentanNumero kentt� jonka sis�lt�� kysyt��n
	 * @return kent�n sis�lt�
	 */
	public String getKentanSisalto(int kentanNumero) {
		if (kentanNumero == 1) return nimi;						// switchiksi??
		if (kentanNumero == 2) return "Vuosi:\n" + vuosi; 
		if (kentanNumero == 3) return "Ohjaaja:\n T�H�N TARVITAAN OHJAAJAN HAKEMINEN"; 
		if (kentanNumero == 4) return "Avainsanat:\n" + avainsanat; 
		return "Ei sis�lt�� pyydetyll� arvolla " + kentanNumero;
	}


	/**
	 * Asettaa elokuvan vuoden
	 * @param vuosi joka asetetaan
	 */
	public void setVuosi(String vuosi) {
		if (vuosi.isEmpty()) return;
		this.vuosi = Integer.parseInt(vuosi);
	}


	/**
	 * asettaa elokuvan nimen
	 * @param nimi joka asetetaan
	 */
	public void setElokuvanNimi(String nimi) {
		this.nimi = nimi;		
	}


	/**
	 * asettaa elokuvan avainsanat
	 * @param avainsanat jotka asetetaan
	 */
	public void setAvainsanat(String avainsanat) {
		this.avainsanat = avainsanat + ", ";
	}



	//------------------------------------------------------------------------------------
	// Tietokannalla toteutus alkaa t�st�

	/**
	 * Antaa tietokannan luontilausekkeen elokuvataululle
	 * @return elokuvataulun luontilauseke
	 */
	public String annaLuontilauseke() {
		return "CREATE TABLE Elokuvat (" +
				"eid INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"nimi VARCHAR(100) NOT NULL, " +
				"vuosi INTEGER, " +
				"avainsanat VARCHAR(100) NOT NULL" +
				")";
	}


	/**
	 * Antaa elokuvan lis�yslausekkeen
	 * @param con tietokantayhteys
	 * @return elokuvan lis�yslauseke
	 * @throws SQLException Jos lausekkeen luonnissa on ongelmia
	 */
	public PreparedStatement annaLisayslauseke(Connection con)
			throws SQLException {
		PreparedStatement sql = con.prepareStatement("INSERT INTO Elokuvat" +
				"(eid, nimi, vuosi, avainsanat) " +
				"VALUES (?, ?, ?, ?)");

		if ( eid != 0 ) sql.setInt(1, eid); else sql.setString(1, null);
		sql.setString(2, nimi);
		sql.setInt(3, vuosi);
		sql.setString(4, avainsanat);
		
		return sql;
	}
	
	
	/**
	 * Tarkistetaan onko eid muuttunut lis�yksess�
	 * @param rs lis�yslauseen ResultSet
	 * @throws SQLException jos tulee jotakin vikaa
	 */
	public void tarkistaId(ResultSet rs) throws SQLException {
	    if ( !rs.next() ) return;
	    int id = rs.getInt(1);
	    if ( id == eid ) return;
	    setEid(id);
	}


	/**
	 * asettaa elokuvalle uuden eidin
	 * @param uusiEid annettava uusi numero
	 */
	public void setEid(int uusiEid) {
		eid = uusiEid;
	}
	
	
	/** 
	 * Ottaa elokuvan tiedot ResultSetist�
	 * @param tulokset mist� tiedot otetaan
	 * @throws SQLException jos jokin menee v��rin
	 */
	public void parse(ResultSet tulokset) throws SQLException {
	    setEid(tulokset.getInt("eid"));
	    nimi = tulokset.getString("nimi");
	    vuosi = tulokset.getInt("vuosi");
	    avainsanat = tulokset.getString("avainsanat");
	}


	/**
	 * palauttaa k:nne sis�ll�n aiheen
	 * @param k kuinka mones sis�lt� palautetaan
	 * @return sis�lt�
	 */
	public String getKysymys(int k) {

		if (k == 1) return "eid";
		if (k == 2) return "nimi";
		if (k == 3) return "vuosi";
		if (k == 4) return "avainsanat";
		
		return null;
	}

}
