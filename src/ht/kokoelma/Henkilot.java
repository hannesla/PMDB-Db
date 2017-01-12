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
import java.util.Scanner;

import ht.kanta.Kanta;

/**
 * Tällä hallinnoidaan henkiloita
 * 
 * @author Hannes Laukkanen
 * @version Mar 9, 2016
 * hannes.v.laukkanen@student.jyu.fi
 *
 * Alustuksia ja puhdistuksia testiä varten
 * @example
 * <pre name="testJAVA">
 * #import java.io.*;
 * #import java.util.*;
 * 
 * private Henkilot henkilot;
 * private String tiedNimi;
 * private File ftied;
 * 
 * @Before
 * public void alusta() throws SailoException { 
 *    tiedNimi = "testi";
 *    ftied = new File(tiedNimi+".db");
 *    ftied.delete();
 *    henkilot = new Henkilot(tiedNimi);
 * }   
 *
 * @After
 * public void siivoa() {
 *    ftied.delete();
 * }   
 * </pre>
 */ 
public class Henkilot {

	private int maxHenkiloita = 5;
	private int lkm;
	private Henkilo[] alkiot = new Henkilo[maxHenkiloita];


	/**
	 * Kokeillaan Henkilot-luokkaa
	 * @param args ei käytetä
	 */
	public static void main(String[] args) {
		try {
			new File("test.db").delete();
			Henkilot henkilot = new Henkilot("test");

			Henkilo testihenkilo1 = new Henkilo();
			Henkilo testihenkilo2 = new Henkilo();

			testihenkilo1.asetaTiedot("0", "nimi1");

			testihenkilo2.asetaTiedot("0", "nimi2");

			henkilot.add(testihenkilo1);
			henkilot.add(testihenkilo2);

			testihenkilo2.tulosta(System.out);

			System.out.println("============= Henkilot kokeilu =================");

			int i = 0;
			for (Henkilo h:henkilot.etsi("nimi", 2)) {
				System.out.println("Henkilo nro: " + i++);
				h.tulosta(System.out);
			}

			new File("test.db").delete();
		} catch ( SailoException ex ) {
			System.out.println(ex.getMessage());
		}
	}


	/**
	 * Palauttaa paljon mahtuu maksimissaan henkilöitä nyt käytössä olevaan taulukkoon
	 * @return alkioiden maksimimäärä
	 */
	public int getMaksimi() {
		return maxHenkiloita;
	}


	/**
	 * palauttaa kuinka monta henkilöä olio kattaa
	 * @return henkilöden määrä
	 */
	public int getLkm() {
		return lkm;
	}


	/**
	 * palauttaa viitteen henkilöön pyydetystä paikasta
	 * @param i indeksi josta henkilöä pyydetään
	 * @return viite henkilöön
	 * @throws IndexOutOfBoundsException jos indeksi on alle 0 tai suurempi kuin alkioita taulukossa
	 */
	public Henkilo get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > this.lkm - 1) throw new IndexOutOfBoundsException("Indeksi on epäkelpo");

		return alkiot[i];		
	}


	/**
	 * tallentaa henkilöt annettuun polkuun
	 * @param tallennusPolku polku johon tallennetaan
	 * @throws SailoException jos tallentamisessa ongelmia
	 * @example
	 * <pre name="test">
	 * // tallennus testattu samassa lueTiedoston() kanssa 
	 * </pre>
	 */
	public void tallenna(String tallennusPolku) throws SailoException {
		String tiedNimi = tallennusPolku + "\\" + "henkilot.dat";
		try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {
			for (Henkilo henkilo : alkiot) {
				if (henkilo == null) break;
				fo.printf(henkilo.toString() + "\n");
			}
		} catch (FileNotFoundException ex) {
			throw new SailoException("Tiedoston " + tiedNimi + " avaamisessa tapahtui virhe");
		}
	}


	/**
	 * lukee henkilöt annetusta polusta
	 * @param polku polku josta luetaan
	 * @throws SailoException jos tiedostoa ei ole
	 */
	public void lueTiedosto(String polku) throws SailoException {	        
		alkiot = new Henkilo[maxHenkiloita];
		String tiedNimi = polku + "\\" + "henkilot.dat";

		try (Scanner fi = new Scanner(new FileInputStream(new File(tiedNimi)))) {                  
			while (fi.hasNext()) {
				String s = fi.nextLine();
				String[] palat = s.split("\\|");

				Henkilo henkilo = new Henkilo();

				try {
					this.add(henkilo);
				} catch (SailoException e) {
					System.err.println("Tiedoston avaaminen ei onnistunut: " + e.getMessage());
				}

				henkilo.asetaTiedot(palat[0], palat[1]);

			}
		} catch ( FileNotFoundException e ) {
			throw new SailoException("Tiedostoa polussa: " + tiedNimi + " ei ole.");
		}
	}

	
	/**
	 * Alustaa henkilöt seuraavaa käyttäjän latausta varten
	 */
	public void alusta() {
		Henkilo.alustaSeuraavaHid();
		lkm = 0;
		alkiot = new Henkilo[maxHenkiloita];		
	}


	/**
	 * Metodi avustamaan tiedostojen lukemisen ja tallentamisen testaamista
	 */
	public void testiTallennus() {
		File polku = new File("test");
		File polku2 = new File("test2");

		polku.mkdir();
		polku2.mkdir();

		File ftied = new File("test\\henkilot.dat");
		File ftied2 = new File("test2\\henkilot.dat");

		try {
			ftied.createNewFile();
			ftied2.createNewFile();
		} catch (IOException e) {
			System.err.println("tiedostojen luonnissa tapahtui virhe: " + e.getMessage());
		}
	}	


	/**
	 * Poistaa testeissä käytetyt tiedostot
	 */
	public void poistaTestitiedostot() {
		File polku = new File("test");
		File polku2 = new File("test2");

		File ftied = new File("test\\henkilot.dat");
		File ftied2 = new File("test2\\henkilot.dat");

		ftied.delete();
		ftied2.delete();
		polku.delete();
		polku2.delete();
	}


	/**
	 * selvittää löytyykö henkilö henkilöistä
	 * @param henkilo henkilön nimi
	 * @return löytyikö vai ei
	 */
	public boolean loytyyko(String henkilo) {

		for (int i = 0; i < lkm; i++) {
			if (henkilo.equals(get(i).getNimi())) return true;
		}

		return false;
	}


	/**
	 * Etstii henkilöä nimeltä
	 * @param nimi henkilön nimi
	 * @return löytynyt henkilö tai null jos ei löytynyt
	 */
	public Henkilo etsiHenkilo(String nimi) {		
		for (int i = 0; i < lkm; i++) {
			if (nimi.equals(get(i).getNimi())) return get(i);
		}

		return null;
	}


	/**
	 * etsii henkilön id:n perusteella
	 * @param hid henkilön id
	 * @return löytynyt henkilö tai null jos ei löytynyt
	 */
	public Henkilo etsiHenkilo(int hid) {

		for (int i = 0; i < lkm; i++) {
			if (alkiot[i].getHid() == hid) return get(i);
		}

		return null;
	}

	//------------------------------------------------------------------------------------------
	//Toteutus tietokannoilla alkaa

	private Kanta kanta;
	private static Henkilo apuhenkilo = new Henkilo();

	/**
	 * Tarkistetaan että kannassa henkiloiden tarvitsema taulu
	 * @param nimi tietokannan nimi
	 * @throws SailoException jos jokin menee pieleen
	 */
	public Henkilot(String nimi) throws SailoException {
		kanta = alustaKanta(nimi);
		try ( Connection con = kanta.annaKantayhteys() ) {
			DatabaseMetaData meta = con.getMetaData();

			try ( ResultSet taulu = meta.getTables(null, null, "Henkilot", null) ) {
				if ( !taulu.next() ) {
					try ( PreparedStatement sql = con.prepareStatement(apuhenkilo.annaLuontilauseke()) ) {
						sql.execute();
					}
				}
			}

		} catch ( SQLException e ) {
			throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
		}
	}


	/**
	 * Lisää uuden henkilon tietorakenteeseen.
	 * @param henkilo lisätäävän henkilon viite.
	 * @return palauttaa viitteen henkilöön, joka lisättiin
	 * @throws SailoException jos tietorakenne on jo täynnä
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 *  Henkilo test1 = new Henkilo(); 
	 *  Henkilo test2 = new Henkilo();
	 *  Henkilo test3 = new Henkilo();
	 *  test1.asetaTiedot("0", "nimi1");
	 *  test2.asetaTiedot("0", "nimi2");
	 *  test3.asetaTiedot("0", "nimi3");
	 *  henkilot.add(test1); 
	 *  henkilot.add(test2);
	 *  henkilot.add(test3);
	 *  
	 *  Collection<Henkilo> loytyneet = henkilot.etsi("", 1);
	 * 	loytyneet.size() === 3;
	 * 
	 * henkilot.add(test1); #THROWS SailoException
	 * 
	 *	loytyneet.toString() === "[1|nimi1|, 2|nimi2|, 3|nimi3|]"; 
	 * </pre>
	 */
	public Henkilo add(Henkilo henkilo) throws SailoException {
		try ( Connection con = kanta.annaKantayhteys(); 
				PreparedStatement sql = henkilo.annaLisayslauseke(con) ) {
			sql.executeUpdate();
			try ( ResultSet rs = sql.getGeneratedKeys() ) {
				henkilo.tarkistaId(rs);
			}   

		} catch (SQLException e) {
			throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
		}

		return henkilo;

	}


	/**
	 * Palauttaa henkilot listassa
	 * @param hakuehto hakuehto  
	 * @param k etsittävän kentän indeksi 
	 * @return jäsenet listassa
	 * @throws SailoException jos tietokannan kanssa ongelmia
	 * 
	 *  Collection<Henkilo> loytyneet = henkilot.etsi("", 1);
	 *  loytyneet.size() === 0;
	 *  
	 *  Henkilo test1 = new Henkilo(); 
	 *  Henkilo test2 = new Henkilo();
	 *  Henkilo test3 = new Henkilo();
	 *  test1.asetaTiedot("0", "nimi1");
	 *  test2.asetaTiedot("0", "nimi2");
	 *  test3.asetaTiedot("0", "nimi3");
	 *  henkilot.add(test1); 
	 *  henkilot.add(test2);
	 *  henkilot.add(test3);
	 *  
	 *  loytyneet.size() === 3;
	 *  
	 *  loytyneet = jasenet.etsi("", 159); #THROWS SailoException
	 */
	public Collection<Henkilo> etsi(String hakuehto, int k) throws SailoException {
		String ehto = hakuehto;
		String kysymys = apuhenkilo.getKysymys(k);
		if ( k < 0 ) { kysymys = apuhenkilo.getKysymys(0); ehto = ""; }

		try ( Connection con = kanta.annaKantayhteys();
				PreparedStatement sql = con.prepareStatement("SELECT * FROM Henkilot WHERE " + kysymys + " LIKE ?") ) {
			ArrayList<Henkilo> loytyneet = new ArrayList<Henkilo>();

			sql.setString(1, "%" + ehto + "%");
			try ( ResultSet tulokset = sql.executeQuery() ) {
				while ( tulokset.next() ) {
					Henkilo j = new Henkilo();
					j.parse(tulokset);
					loytyneet.add(j);
				}
			}
			return loytyneet;
		} catch ( SQLException e ) {
			throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
		}
	}


	/**
	 * Lisää henkilön
	 * @param henkilo henkilö joka lisätään
	 * @return palauttaa viitteen käsiteltyyn henkilöön
	 * @throws SailoException jos taulukkoon ei mahdu enempää henkilöitä
	 *@example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Henkilot hen = new Henkilot();
	 * hen.getLkm() === 0;
	 * hen.add(new Henkilo());
	 * hen.getLkm() === 1;
	 * hen.add(new Henkilo());
	 * hen.getLkm() === 2;
	 * while (hen.getLkm() <= hen.getMaksimi()-1)  {
	 *  	hen.add(new Henkilo());
	 * }
	 * hen.add(new Henkilo());
	 * </pre>
	 */
	public Henkilo kokoelmaan(Henkilo henkilo) throws SailoException {
		if (lkm >= alkiot.length) {
			maxHenkiloita += 5;

			Henkilo[] alkiotUusi = new Henkilo[maxHenkiloita];

			for (int i = 0; i < alkiot.length; i++) {
				alkiotUusi[i] = alkiot[i];
			}

			alkiot = alkiotUusi;
		}	

		alkiot[lkm] = henkilo;		
		lkm++;

		return henkilo;
	}
	
}