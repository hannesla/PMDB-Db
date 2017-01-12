package ht.kanta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Luokka SQLite yhteyden muodostamiseksi.
 * 
 * @author Hannes Laukkanen
 * @version 4.5.2016
 * hannes.v.laukkanen@student.jyu.fi
 * 
 * @author vesal
 * @version 7.2.2015
 *
 */
public class Kanta {
	private static HashMap<String, Kanta> kannat = new HashMap<String, Kanta>();
	private String tiedostonPerusNimi = "";

	
	/** 
	 * Alustetaan kanta
	 * @param nimi tiedoston perusnimi
	 */
	private Kanta(String nimi) {
		setTiedosto(nimi);
	}


	/**
	 * Alustetaan kantayhteys
	 * @param nimi kannan nimi
	 * @return kannan tiedot jolla voidaan operoida
	 */
	public static Kanta alustaKanta(String nimi) {
		if ( kannat.containsKey(nimi) ) return kannat.get(nimi); 
		Kanta uusi = new Kanta(nimi);
		kannat.put(nimi, uusi);
		return uusi;
	}


	/**
	 * Asettaa kannan perusnimen
	 * @param nimi uusi nimi
	 */
	public void setTiedosto(String nimi) {
		tiedostonPerusNimi = nimi;
	}


	/**
	 * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonNimi() {
		return tiedostonPerusNimi + ".db";
	}


	/**
	 * Antaa tietokantayhteyden
	 * @return tietokantayhteys
	 * @throws SQLException Jos tietokantayhteyden avaamisessa on ongelmia
	 */
	public Connection annaKantayhteys() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + getTiedostonNimi());
	}
}