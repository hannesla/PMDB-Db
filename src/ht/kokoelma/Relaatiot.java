/**
 * 
 */
package ht.kokoelma;

import static ht.kanta.Kanta.alustaKanta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import ht.kanta.Kanta;

/**
 * Hallinnoi tietokaannassa olevia relaatioita
 * 
 * @author Hannes Laukkanen
 * @version Apr 19, 2016
 * hannes.v.laukkanen@student.jyu.fi
 * 
 * Alustuksia ja puhdistuksia testiä varten
 * @example
 * <pre name="testJAVA">
 * #import java.io.*;
 * #import java.util.*;
 * 
 * private Relaatiot relaatiot;
 * private String tiedNimi;
 * private File ftied;
 * 
 * @Before
 * public void alusta() throws SailoException { 
 *    tiedNimi = "testi";
 *    ftied = new File(tiedNimi+".db");
 *    ftied.delete();
 *    relaatiot = new Relaatiot(tiedNimi);
 * }   
 *
 * @After
 * public void siivoa() {
 *    ftied.delete();
 * }   
 * </pre>
 */ 
public class Relaatiot {
	private List<Relaatio> relaatiot = new ArrayList<Relaatio>();


	/**
	 * Pääohjelmaa käytetään luokan kokeilemiseen
	 * @param args ei käytetä
	 */
	public static void main(String[] args) {
		try {
			new File("test.db").delete();
			Relaatiot relaatiot = new Relaatiot("test");

			Relaatio testirel = new Relaatio();
			Relaatio testirel2 = new Relaatio();
			
			testirel.asetaTiedot("1", "4", "5");

			testirel2.asetaTiedot("1", "2", "3");

			relaatiot.add(testirel);
			relaatiot.add(testirel2);

			testirel2.tulosta(System.out);

			System.out.println("============= Relaatiot kokeilu =================");

			int i = 0;
			for (Relaatio e:relaatiot.etsi("2", 2)) {
				System.out.println("Relaatio nro: " + i++);
				e.tulosta(System.out);
			}

			new File("test.db").delete();
			
		} catch ( SailoException ex ) {
			System.out.println(ex.getMessage());
		}		
	}


	/**
	 * palauttaa relaatioiden lukumäärän
	 * @return relaatioiden lukumäärä
	 */
	public int getLkm() {
		return relaatiot.size();
	}


	/**
	 * palauttaa pyydetyn relaation viitteen
	 * @param i indeksi josta pyydetään
	 * @return relaation viite
	 * @throws IndexOutOfBoundsException jos epäkelpo indeksi
	 */
	public Relaatio get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > getLkm() - 1) throw new IndexOutOfBoundsException();

		return relaatiot.get(i);
	}


	/**
	 * Tallentaa relaatiot annettuun polkuun
	 * @param polku johon tallennetaan
	 * @throws SailoException jos tellantamisessa ongelmia
	 * @example
	 * <pre name="test">
	 * // tallennus testattu samassa lueTiedoston() kanssa 
	 * </pre>
	 */
	public void tallenna(String polku) throws SailoException {
		String tiedNimi = polku + "\\" + "relaatiot.dat";
		try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {            	
			for (Relaatio rela : relaatiot) {
				if (rela == null) break;
				fo.printf(rela.toString() + "\n");
			}
		} catch (FileNotFoundException ex) {
			throw new SailoException("Tiedoston " + tiedNimi + " avaamisessa tapahtui virhe");
		}

	}


	/**
	 * Lukee relaatiot.dat tiedoston annetusta polusta
	 * @param polku polku josta luetaan
	 * @throws SailoException jos tiedostoa ei ole
	 */
	public void lueTiedosto(String polku) throws SailoException {
		relaatiot = new ArrayList<Relaatio>();

		String tiedNimi = polku + "\\" + "relaatiot.dat";

		try (Scanner fi = new Scanner(new FileInputStream(new File(tiedNimi)))) {
			while ( fi.hasNext() ) {

				String s = fi.nextLine();
				String[] palat = s.split("\\|");

				Relaatio relaatio = new Relaatio();        		

				this.add(relaatio);

				relaatio.asetaTiedot(palat[0], palat[1], palat[2]);


			}
		} catch ( FileNotFoundException e ) {
			throw new SailoException("Tiedostoa polussa: " + tiedNimi + " ei ole.");
		}
	}

	/**
	 * Alustaa relaatio seuraavaa käyttäjää varten
	 */
	public void alusta() {
		relaatiot = new ArrayList<Relaatio>();		
	}


	/**
	 * Metodi avustamaan tiedostojen lukemisen ja tallentamisen testaamista
	 */
	public void testiTallennus() {
		File polku = new File("test");
		File polku2 = new File("test2");

		polku.mkdir();
		polku2.mkdir();

		File ftied = new File("test\\relaatiot.dat");
		File ftied2 = new File("test2\\relaatiot.dat");

		try {
			ftied.createNewFile();
			ftied2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}	


	/**
	 * Poistaa testeissä käytetyt tiedostot
	 */
	public void poistaTestitiedostot() {
		File polku = new File("test");
		File polku2 = new File("test2");

		File ftied = new File("test\\relaatiot.dat");
		File ftied2 = new File("test2\\relaatiot.dat");

		ftied.delete();
		ftied2.delete();
		polku.delete();
		polku2.delete();
	}


	/**
	 * Etsii relaation jossa id:t vastaavat kutsun parametreja
	 * @param eid relaation id
	 * @param hid henkilön id
	 * @param rid roolin id
	 * @return löytynyt relaatio tai null jos ei löytynyt
	 */
	public Relaatio etsiRelaatio(int eid, int hid, int rid) {

		for (Relaatio relaatio : relaatiot) {
			if (relaatio.getEid() == eid && relaatio.getHid() == hid && relaatio.getRid() == rid) return relaatio;
		}

		return null;
	}


	/**
	 * Poistaa relaation, jonka id:t vastaavat parametrina saatuja
	 * @param eid relaatio id
	 * @param hid henkilö id
	 * @param rid rooli id
	 */
	public void poista(int eid, int hid, int rid) {

		int i = -1;
		boolean loytyi = false;

		for (Relaatio relaatio : relaatiot) {
			if (relaatio.getEid() == eid && relaatio.getHid() == hid && relaatio.getRid() == rid) {
				i = relaatiot.indexOf(relaatio);
				loytyi = true;
			}		
		}

		if (loytyi) relaatiot.remove(i);
	}


	/**
	 * poistaa relaation relaation id:n ja roolin id:n perusteella (eli henkilö poistetaan suorittamasta
	 * roolia relaatiossa)
	 * @param eid relaation id
	 * @param rid roolin id
	 */
	public void poista(int eid, int rid) {

		int i = -1;
		boolean loytyi = false;

		for (Relaatio relaatio : relaatiot) {
			if (relaatio.getEid() == eid && relaatio.getRid() == rid) {
				i = relaatiot.indexOf(relaatio);
				loytyi = true;
			}		
		}

		if (loytyi) relaatiot.remove(i);

	}


	/**
	 * poistaa kaikki relaatioan liittyvät relaatiot
	 * @param eid relaation id
	 */
	public void poista(int eid) { // Ongelmana oli, että poiston jälkeen siirtyi seuraavaan indeksiin hypäten yhden yli

		int jaljella = relaatiot.size();
		int i = 0;

		while (i < jaljella) {		
			if (relaatiot.get(i).getEid() == eid) {
				relaatiot.remove(relaatiot.get(i));
				jaljella--;
				continue;
			}

			i++;
		}
	}


	/**
	 * Etsii relaation relaation id:n ja roolin id:n perusteella
	 * @param eid relaation id
	 * @param rid roolin id
	 * @return löytynyt relaatio tai null jos relaatiota ei ole 
	 */
	public Relaatio etsiRelaatio(int eid, int rid) {
		for (Relaatio r : relaatiot) {
			if (r.getEid() == eid && r.getRid() == rid) return r;
		}

		return null;
	}


	//------------------------------------------------------------------------------------------
	//Toteutus tietokannoilla alkaa

	private Kanta kanta;
	private static Relaatio apurelaatio = new Relaatio();

	/**
	 * Tarkistetaan että kannassa elokuvien tarvitsema taulu
	 * @param nimi tietokannan nimi
	 * @throws SailoException jos jokin menee pieleen
	 */
	public Relaatiot(String nimi) throws SailoException {
		kanta = alustaKanta(nimi);
		try ( Connection con = kanta.annaKantayhteys() ) {
			DatabaseMetaData meta = con.getMetaData();

			try ( ResultSet taulu = meta.getTables(null, null, "Relaatiot", null) ) {
				if ( !taulu.next() ) {
					try ( PreparedStatement sql = con.prepareStatement(apurelaatio.annaLuontilauseke()) ) {
						sql.execute();
					}
				}
			}

		} catch ( SQLException e ) {
			throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
		}
	}


	/**
	 * Lisää uuden relaation tietorakenteeseen.
	 * @param relaatio lisätäävän relaation viite.
	 * @return viite relaatioon joka lisättiin
	 * @throws SailoException jos tietorakenne on jo täynnä
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 *  Relaatio test1 = new Relaatio(); 
	 *  Relaatio test2 = new Relaatio();
	 *  Relaatio test3 = new Relaatio();
	 *  test1.asetaTiedot("1", "1", "1");
	 *  test2.asetaTiedot("2", "20", "2");
	 *  test3.asetaTiedot("3", "23", "3");
	 *  relaatiot.add(test1); 
	 *  relaatiot.add(test2);
	 *  relaatiot.add(test3);
	 *  
	 *  Collection<Relaatio> loytyneet = relaatiot.etsi("", 1);
	 * 	loytyneet.size() === 3;
	 * 
	 *	loytyneet.toString() === "[1|1|1|, 2|20|2|, 3|23|3|]"; 
	 * </pre>
	 */
	public Relaatio add(Relaatio relaatio) throws SailoException {
		try ( Connection con = kanta.annaKantayhteys(); 
				PreparedStatement sql = relaatio.annaLisayslauseke(con) ) {
			sql.executeUpdate();
			
			/* try ( ResultSet rs = sql.getGeneratedKeys() ) {
				relaatio.tarkistaId(rs);
			} */  

		} catch (SQLException e) {
			throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
		}

		return relaatio;

	}


	/**
	 * Palauttaa relaatiot listassa
	 * @param hakuehto hakuehto  
	 * @param k etsittävän kentän indeksi 
	 * @return jäsenet listassa
	 * @throws SailoException jos tietokannan kanssa ongelmia
	 * 
	 *  Collection<Relaatio> loytyneet = relaatiot.etsi("", 1);
	 *  loytyneet.size() === 0;
	 *  
	 *  Relaatio test1 = new Relaatio(); 
	 *  Relaatio test2 = new Relaatio();
	 *  Relaatio test3 = new Relaatio();
	 *  test1.asetaTiedot("1", "1", "1");
	 *  test2.asetaTiedot("2", "20", "2");
	 *  test3.asetaTiedot("3", "23", "3");
	 *  relaatiot.add(test1); 
	 *  relaatiot.add(test2);
	 *  relaatiot.add(test3);
	 *  
	 *  loytyneet.size() === 3;
	 *  
	 *  loytyneet = jasenet.etsi("", 159); #THROWS SailoException
	 */
	public Collection<Relaatio> etsi(String hakuehto, int k) throws SailoException {
		String ehto = hakuehto;
		String kysymys = apurelaatio.getKysymys(k);
		if ( k < 0 ) { kysymys = apurelaatio.getKysymys(0); ehto = ""; }

		try ( Connection con = kanta.annaKantayhteys();
				PreparedStatement sql = con.prepareStatement("SELECT * FROM Relaatiot WHERE " + kysymys + " LIKE ?") ) {
			ArrayList<Relaatio> loytyneet = new ArrayList<Relaatio>();

			sql.setString(1, "%" + ehto + "%");
			try ( ResultSet tulokset = sql.executeQuery() ) {
				while ( tulokset.next() ) {
					Relaatio j = new Relaatio();
					j.parse(tulokset);
					loytyneet.add(j);
				}
			}
			return loytyneet;
		} catch ( SQLException e ) {
			throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
		}
	}
}