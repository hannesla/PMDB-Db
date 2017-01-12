/**
 * 
 */
package ht.kokoelma;

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
import static ht.kanta.Kanta.alustaKanta;

/**
 * Hallinnoi elokuvien viitteitä 
 * 
 * @author Hannes Laukkanen
 * @version 23.2.2016
 * hannes.v.laukkanen@student.jyu.fi
 * 
 * Alustuksia ja puhdistuksia testiä varten
 * @example
 * <pre name="testJAVA">
 * #import java.io.*;
 * #import java.util.*;
 * 
 * private Elokuvat elokuvat;
 * private String tiedNimi;
 * private File ftied;
 * 
 * @Before
 * public void alusta() throws SailoException { 
 *    tiedNimi = "testi";
 *    ftied = new File(tiedNimi+".db");
 *    ftied.delete();
 *    elokuvat = new Elokuvat(tiedNimi);
 * }   
 *
 * @After
 * public void siivoa() {
 *    ftied.delete();
 * }   
 * </pre>
 */ 
public class Elokuvat {
	private int maxElokuvia = 5;
	private int lkm;
	private Elokuva[] alkiot = new Elokuva[maxElokuvia];


	/**
	 * Palauttaa paljon mahtuu maksimissaan elokuvia nyt käytössä olevaan taulukkoon
	 * @return alkioiden maksimimäärä
	 */
	public int getMaksimi() {
		return maxElokuvia;
	}


	/**
	 * Palatauttaa elokuvien lukumäärän taulukossa
	 * @return elokuvien lukumäärä
	 */
	public int getLkm() {
		return lkm;
	}


	/**
	 * Palauttaa parametrina saamastaan kohdasta taulukossa löytyvän elokuvan viitteen
	 * @param i paikka josta elokuvaa haetaan
	 * @return paikasta löytyneen elokuvan viite
	 * @throws IndexOutOfBoundsException jos i ei ole sopiva taulukon paikka
	 */
	public Elokuva get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > lkm - 1) throw new IndexOutOfBoundsException("Pyydetyssä indeksissä ei alkioita: " + i);

		return alkiot[i];
	}


	/**
	 * Elokuvat luokan kokeilua varten
	 * @param args ei käytetä
	 */
	public static void main(String args[])  {
		try {
			new File("test.db").delete();
			Elokuvat elokuvat = new Elokuvat("test");

			Elokuva testikuva = new Elokuva();
			Elokuva testikuva2 = new Elokuva();

			testikuva.asetaTiedot("0", "nimi1", "2001", "avainsanat1");

			testikuva2.asetaTiedot("0", "nimi2", "2002", "avainsanat2");

			elokuvat.add(testikuva);
			elokuvat.add(testikuva2);

			testikuva2.tulosta(System.out);

			System.out.println("============= Elokuvat kokeilu =================");

			int i = 0;
			for (Elokuva e:elokuvat.etsi("nimi%", 2)) {
				System.out.println("Elokuva nro: " + i++);
				e.tulosta(System.out);
			}

			new File("test.db").delete();
		} catch ( SailoException ex ) {
			System.out.println(ex.getMessage());
		}
	}


	/**
	 * Tallentaa elokuvien tiedot annettuun polkuun
	 * @param tallennusPolku polku johon tallennetaan
	 * @throws SailoException jos tallennuksessa tapahtuu pokkeuksia
	 * @example
	 * <pre name="test">
	 * // tallennus testattu samassa lueTiedoston() kanssa 
	 * </pre>
	 */
	public void tallenna(String tallennusPolku) throws SailoException {
		String tiedNimi = tallennusPolku + "\\" + "elokuvat.dat";
		try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {
			for (Elokuva elokuva : alkiot) {
				if (elokuva == null) break;
				fo.printf(elokuva.toString() + "\n");
			}
		}catch ( FileNotFoundException ex ) {
			throw new SailoException("Tiedoston " + tiedNimi + " avaamisessa tapahtui virhe");
		}
	}


	/**
	 * lukee elokuvat.dat tiedoston annetusta polusta
	 * @param polku mistä luetaan
	 * @throws SailoException jos tiedostoa ei ole
	 */
	public void lueTiedosto(String polku) throws SailoException {
				
		alkiot = new Elokuva[maxElokuvia];
		String tiedNimi = polku + "\\" + "elokuvat.dat";

		try (Scanner fi = new Scanner(new FileInputStream(new File(tiedNimi)))) {

			while (fi.hasNext()) {
				String s = fi.nextLine();

				String[] palat = s.split("\\|");
				//		if (palat.length < 2) continue;

				Elokuva elokuva = new Elokuva();

				try {
					this.add(elokuva); 
				} catch (SailoException e) { // ei heitä enää tätä pokkeusta, koska taulukko on dynaaminen
					System.err.println("Elokuvan lisääminen ei onnistunut: " + e.getMessage());
				}

				elokuva.asetaTiedot(palat[0], palat[1], palat[2], palat[3]);
			}
		} catch ( FileNotFoundException e ) {
			throw new SailoException("Tiedostoa polussa: " + tiedNimi + " ei ole.");
		}
	}


	/**
	 * Alustaa elokuvat käytettäväksi seuraavaa käyttäjää varten
	 */
	public void alusta() {
		Elokuva.alustaSeuraavaEid();
		lkm = 0;
		alkiot = new Elokuva[maxElokuvia];		
	}	


	/**
	 * Metodi avustamaan tiedostojen lukemisen ja tallentamisen testaamista
	 */
	public void testiTallennus() {
		File polku = new File("test");
		File polku2 = new File("test2");

		polku.mkdir();
		polku2.mkdir();

		File ftied = new File("test\\elokuvat.dat");
		File ftied2 = new File("test2\\elokuvat.dat");

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

		File ftied = new File("test\\elokuvat.dat");
		File ftied2 = new File("test2\\elokuvat.dat");

		ftied.delete();
		ftied2.delete();
		polku.delete();
		polku2.delete();
	}


	/**
	 * Poistaa elokuvan elokuvista
	 * @param poistettava elokuva joka poistetaan
	 */
	public void poista(Elokuva poistettava) {		

		Elokuva[] muutettu = new Elokuva[maxElokuvia];

		int i = 0;
		int j = 0;
		Boolean loytyi = false;

		while(i < lkm) {
			if (alkiot[i].getEid() == poistettava.getEid()) {
				i++;
				loytyi = true;
				continue;
			}

			muutettu[j] = alkiot[i];

			i++;
			j++;
		}

		if (loytyi) {
			alkiot = muutettu;
			lkm--;
		}
	}
		
	
	/**
	 * Lisää elokuvan elokuvien taulukkoon
	 * @param elokuva elokuva joka lisätään
	 * @return palauttaa käsitellyn elokuvan
	 * @throws SailoException Poikkeus ilmoittaa, jos taulukkoon yritetään lisätä alkiota silloin, kun se on jo täynnä
	 *@example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Elokuvat hen = new Elokuvat();
	 * hen.getLkm() === 0;
	 * hen.add(new Elokuva());
	 * hen.getLkm() === 1;
	 * hen.add(new Elokuva());
	 * hen.getLkm() === 2;
	 * while (hen.getLkm() <= hen.getMaksimi()-1)  {
	 *  	hen.add(new Elokuva());
	 * }
	 * hen.add(new Elokuva());
	 * </pre>
	 */
	public Elokuva kokoelmaan(Elokuva elokuva) throws SailoException {
		if (lkm >= alkiot.length) {
			maxElokuvia += 5;

			Elokuva[] alkiotUusi = new Elokuva[maxElokuvia];

			for (int i = 0; i < alkiot.length; i++) {
				alkiotUusi[i] = alkiot[i];
			}

			alkiot = alkiotUusi;
		}

		elokuva.rekisteroi();
		alkiot[lkm] = elokuva;
		lkm++;

		return elokuva;
	}


	//------------------------------------------------------------------------------------------
	//Toteutus tietokannoilla alkaa

	private Kanta kanta;
	private static Elokuva apuelokuva = new Elokuva();

	
	/**
	 * Tarkistetaan että kannassa elokuvien tarvitsema taulu
	 * @param nimi tietokannan nimi
	 * @throws SailoException jos jokin menee pieleen
	 */
	public Elokuvat(String nimi) throws SailoException {
		kanta = alustaKanta(nimi);
		try ( Connection con = kanta.annaKantayhteys() ) {
			DatabaseMetaData meta = con.getMetaData();

			try ( ResultSet taulu = meta.getTables(null, null, "Elokuvat", null) ) {
				if ( !taulu.next() ) {
					try ( PreparedStatement sql = con.prepareStatement(apuelokuva.annaLuontilauseke()) ) {
						sql.execute();
					}
				}
			}

		} catch ( SQLException e ) {
			throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
		}
	}


	/**
	 * Lisää uuden elokuvan tietorakenteeseen.
	 * @param elokuva lisätäävän elokuvan viite.
	 * @return elokuva joka lisättiin
	 * @throws SailoException jos tietorakenne on jo täynnä
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 *  Elokuva test1 = new Elokuva(); 
	 *  Elokuva test2 = new Elokuva();
	 *  Elokuva test3 = new Elokuva();
	 *  test1.asetaTiedot("0", "nimi1", "2001", "avainsanat1");
	 *  test2.asetaTiedot("0", "nimi2", "2002", "avainsanat2");
	 *  test3.asetaTiedot("0", "nimi3", "2003", "avainsanat3");
	 *  elokuvat.add(test1); 
	 *  elokuvat.add(test2);
	 *  elokuvat.add(test3);
	 *  
	 *  Collection<Elokuva> loytyneet = elokuvat.etsi("", 1);
	 * 	loytyneet.size() === 3;
	 * 
	 * elokuvat.add(test1); #THROWS SailoException
	 * 
	 *	loytyneet.toString() === "[1|nimi1|2001|avainsanat1|, 2|nimi2|2002|avainsanat2|, 3|nimi3|2003|avainsanat3|]"; 
	 * </pre>
	 */
	public Elokuva add(Elokuva elokuva) throws SailoException {
		try ( Connection con = kanta.annaKantayhteys(); 
				PreparedStatement sql = elokuva.annaLisayslauseke(con) ) {
			sql.executeUpdate();
			try ( ResultSet rs = sql.getGeneratedKeys() ) {
				elokuva.tarkistaId(rs);
			}
		} catch (SQLException e) {
			throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
		}

		return elokuva;
	}


	/**
	 * Palauttaa elokuvat listassa
	 * @param hakuehto hakuehto  
	 * @param k etsittävän kentän indeksi 
	 * @return jäsenet listassa
	 * @throws SailoException jos tietokannan kanssa ongelmia
	 * 
	 *  Collection<Elokuva> loytyneet = elokuvat.etsi("", 1);
	 *  loytyneet.size() === 0;
	 *  
	 *  Elokuva test1 = new Elokuva(); 
	 *  Elokuva test2 = new Elokuva();
	 *  Elokuva test3 = new Elokuva();
	 *  test1.asetaTiedot("0", "nimi1", "2001", "avainsanat1");
	 *  test2.asetaTiedot("0", "nimi2", "2002", "avainsanat2");
	 *  test3.asetaTiedot("0", "nimi3", "2003", "avainsanat3");
	 *  elokuvat.add(test1); 
	 *  elokuvat.add(test2);
	 *  elokuvat.add(test3);
	 *  
	 *  loytyneet.size() === 3;
	 *  
	 *  loytyneet = jasenet.etsi("", 159); #THROWS SailoException
	 */
	public Collection<Elokuva> etsi(String hakuehto, int k) throws SailoException {
		String ehto = hakuehto;
		String kysymys = apuelokuva.getKysymys(k);
		if ( k < 0 ) { kysymys = apuelokuva.getKysymys(0); ehto = ""; }

		try ( Connection con = kanta.annaKantayhteys();
				PreparedStatement sql = con.prepareStatement("SELECT * FROM Elokuvat WHERE " + kysymys + " LIKE ?") ) {
			ArrayList<Elokuva> loytyneet = new ArrayList<Elokuva>();

			sql.setString(1, "%" + ehto + "%");
			try ( ResultSet tulokset = sql.executeQuery() ) {
				while ( tulokset.next() ) {
					Elokuva j = new Elokuva();
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