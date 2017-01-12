/**
 * 
 */
package ht.kokoelma;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import ht.kanta.ElokuvaTarkistus;


/** 
 * Luokka joka huolehtii tietokannan hallinnasta välittäjämetodien avulla
 * 
 * @author Hannes Laukkanen
 * @version 10.5.2016
 * hannes.v.laukkanen@student.jyu.fi
 * 
 * Alustuksia ja puhdistuksia testiä varten
 * @example
 * <pre name="testJAVA">
 * #import java.io.*;
 * #import java.util.*;
 * 
 * private Kokoelma kokoelma;
 * private String tiedNimi;
 * private File ftied;
 * 
 * @Before
 * public void alusta() throws SailoException { 
 * tiedNimi = "testi";
 * ftied = new File(tiedNimi+".db");
 * ftied.delete();
 * kokoelma = new Kokoelma();
 * kokoelma.lueTiedostot("testi");
 * }   
 *
 * @After
 * public void siivoa() {
 *    ftied.delete();
 * }   
 * </pre>
 */ 
public class Kokoelma {
	private Elokuvat elokuvat;
	private Henkilot henkilot;
	private Roolit   roolit   = new Roolit();
	private Relaatiot relaatiot;
	private Elokuva elokuvaKasittelyssa = null; 


	/**
	 * Lisää Elokuvan kokoelmaan
	 * @param elokuva listäätävä elokuva
	 * @return Elokuva joka lisättiin
	 * @throws SailoException elokuvaa ei voi lisätä
	 */
	public Elokuva add(Elokuva elokuva) throws SailoException {
		return elokuvat.add(elokuva);
	}


	/**
	 * Lisää Elokuvan kokoelmaan
	 * @param relaatio listäätävä elokuva
	 * @return Relaatio joka lisättiin
	 * @throws SailoException elokuvaa ei voi lisätä
	 */
	public Relaatio add(Relaatio relaatio) throws SailoException {
		return relaatiot.add(relaatio);
	}


	/**
	 * Lisää Henkilon kokoelmaan
	 * @param henkilo lisättävä henkilo
	 * @return palautaa viitteen lisättyyn henkilöön
	 * @throws SailoException Henkiloa ei voi lisätä
	 */
	public Henkilo add(Henkilo henkilo) throws SailoException {
		return henkilot.add(henkilo);
	}


	/**
	 * Kertoo kuinka monta elokuvaa elokuvat luokassa
	 * @return elokuvien lukumäärä
	 */
	public int getElokuvienLkm() {
		return elokuvat.getLkm();
	}


	/**
	 * Palauttaa elokuvan parametrina saamastaan paikasta
	 * @param i indeksi josta elokuva haetaan
	 * @return elokuva joka paikasta löytyi
	 */
	public Elokuva getElokuva(int i) {
		return elokuvat.get(i);
	}


	/**
	 * Palauttaa relaation annetusta indeksistä
	 * @param i indeksi josta haetaan
	 * @return viite relaatioon
	 */
	public Relaatio getRelaatio(int i) {
		return relaatiot.get(i);
	}


	/**
	 * palauttaa relaatioiden lukumäärän
	 * @return relaatioiden lukumäärä
	 */
	public int getRelaatioidenLkm() {
		return relaatiot.getLkm();
	}


	/**
	 * Lisää roolin tietokantaan
	 * @param rooli lisättävä rooli
	 */
	public void add(Rooli rooli) {
		roolit.add(rooli);
	}


	/**
	 * palauttaa roolin pyydetystä indeksistä
	 * @param i indeksi josta roolia haetaan
	 * @return roolin viite
	 */
	public Rooli getRooli(int i) {
		return roolit.get(i);
	}


	/**
	 * Paluttaa roolien lukumäärän
	 * @return roolien lukumäärä
	 */
	public int getRoolienLkm() {
		return roolit.getLkm();
	}


	/**
	 * Palauttaa henkilön viitteen
	 * @param indeksi paikka josta henkilöä haetaan
	 * @return henkilön viite
	 */
	public Henkilo getHenkilo(int indeksi) {
		return henkilot.get(indeksi);
	}


	/**
	 * Palauttaa henkilöiden lukumäärän
	 * @return henkilöiden lukumäärä
	 */
	public int getHenkiloidenLkm() {
		return henkilot.getLkm();
	}


	/**
	 * tallentaa kokoelman tiedot
	 * @param tallennusPolku polku johon tiedostot tallennetaan
	 * @throws SailoException jos tallennus ei onnistunut
	 */
	public void tallenna(String tallennusPolku) throws SailoException {
		elokuvat.tallenna(tallennusPolku);
		henkilot.tallenna(tallennusPolku);
		roolit.tallenna(tallennusPolku);
		relaatiot.tallenna(tallennusPolku);		
	}


	/**
	 * alustaa tietokannan seuraavaa käyttäjää varten
	 * @return viite alustettuun Kokoelmaan
	 */
	public Kokoelma alusta() {
		return new Kokoelma();
	}


	/**
	 * Valitsee elokuvan kokoelmasta valmiiksi kasittelyä varten
	 * @param valittu elokuva joka valitaan kasittelyyn
	 */
	public void valitseElokuva(Elokuva valittu) {
		elokuvaKasittelyssa = valittu;
	}


	/**
	 * kertoo kasittelyssa olevan elokuvan
	 * @return elokuva jota kasitellään
	 */
	public Elokuva getElokuvaKasittelyssa() {
		return elokuvaKasittelyssa;
	}


	/**
	 * asettaa tiedot, jotta henkilö liittyy elokuvaan 
	 * @param henkilonNimi Henkilön nimi
	 * @param eid elokuvan id johon liitetään
	 * @param rid henkilön roolin id elokuvassa
	 * @throws SailoException jos henkilön tietojen asettaminen ei onnistunut
	 */
	public void setHenkilo(String henkilonNimi, int eid, int rid) throws SailoException {											
		Henkilo henkilo = henkilot.etsiHenkilo(henkilonNimi);

		if (henkilo == null) {

			henkilo = new Henkilo(henkilonNimi).rekisteroi();

			try {
				henkilot.kokoelmaan(henkilo);
			} catch (SailoException e) { // TODO: Poikkeus nousemaan käyttöliittymään
				System.err.println(e.getMessage()); 
			}
		}

		Relaatio relaatio = relaatiot.etsiRelaatio(eid, henkilo.getHid(), rid);

		if (relaatio != null) return;

		relaatio = new Relaatio();

		if (rid == 1) relaatiot.poista(eid, rid); // TODO: ratkaisu jossa selvitettäisiin erikseen, onko rid sellainen että niitä voi olla vain yksi per elokuva

		relaatio.asetaTiedot("" + eid, "" + henkilo.getHid(), "" + rid);

		//	relaatiot.kokoelmaan(relaatio);
	}



	/**
	 * Asettaa elokuvalle vuoden
	 * @param vuosi joka asetetaan
	 */
	public void setVuosi(String vuosi) {
		elokuvaKasittelyssa.setVuosi(vuosi);		
	}


	/**
	 * Asettaa elokuvalle nimen
	 * @param nimi joka asetetaan
	 */
	public void setElokuvanNimi(String nimi) {
		elokuvaKasittelyssa.setElokuvanNimi(nimi);
	}


	/**
	 * Poistaa henkilön elokuvasta
	 * @param poistettavanNimi henkilön nimi
	 * @param rooliJostaPoistetaan rooli josta poistetaan
	 */
	public void poistaHenkiloElokuvasta(String poistettavanNimi, int rooliJostaPoistetaan) {
		Henkilo poistettava = henkilot.etsiHenkilo(poistettavanNimi);
		relaatiot.poista(elokuvaKasittelyssa.getEid(), poistettava.getHid(), rooliJostaPoistetaan);
	}


	/**
	 * Poistaa elokuvan
	 * @param poistettava elokuva joka poistetaan
	 */
	public void poistaElokuva(Elokuva poistettava) {			
		relaatiot.poista(poistettava.getEid());
		elokuvat.poista(poistettava);
	}


	/**
	 * Pyyttää tarkistamaan, onko elokuvan nimi oikeanlainen
	 * @param ehdotus nimi jota on ehdotettu
	 * @return null jos sopiva, muuten syy miksi ei ole sopiva
	 */
	public String tarkistaNimi(String ehdotus) {
		return ElokuvaTarkistus.tarkistaNimi(ehdotus);
	}


	/**
	 * Pyytää tarkistamaan, onko elokuvalle ehdotettu vuosi sopiva
	 * @param ehdotus vuosi jota ehdotetaan
	 * @return null jos sopiva, muuten syy miksi ei ole sopiva
	 */
	public String tarkistaVuosi(String ehdotus) {
		return ElokuvaTarkistus.tarkistaVuosi(ehdotus);
	}


	/**
	 * Selvittää kuka on ohjannut elokuvan
	 * @param elokuva jonka ohjaajaa selvitetään
	 * @return ohjaajan nimi
	 * @throws SailoException jos etsinnässä tulee virhe
	 */
	public String selvitaOhjaaja(Elokuva elokuva) throws SailoException {

		Collection<Relaatio> relaatioEhdokkaat = etsiRelaatioita("" + elokuva.getEid(), 1);

		int ohjaajanId = 0;

		for (Relaatio r : relaatioEhdokkaat) {
			if (r.getRid() == 1) {
				ohjaajanId = r.getHid();
				break;
			}
		}

		String etsitty = etsiHenkiloita("" + ohjaajanId, 1).iterator().next().getNimi();

		return etsitty; 

	}


	/**
	 * Tulostaa tiivistetyt tiedot eolokuvasta tietovirtaan
	 * @param os tietovirta johon tulostetaan
	 * @param tulostettava elokuva jonka tiedot tulostetaan
	 * @throws SailoException jos ohjaajan selvittäminen epäonnistuu
	 */
	public void tulostaTiivistelma(PrintStream os, Elokuva tulostettava) throws SailoException {
		os.println("Elokuvan nimi:\n"+   tulostettava.getNimi() + "\n\n" +
				"Valmistumisvuosi:\n" + tulostettava.getVuosi() + "\n\n" +
				"Ohjaaja:\n" + selvitaOhjaaja(tulostettava) + "\n\n" + 
				"Avainsanat:\n" + tulostettava.getAvainsanat());
	}


	/**
	 * etsii henkilön nimen perusteella
	 * @param nimi henkilön nimi
	 * @return viite henkilöön tai null jos ei löydy
	 */
	public Henkilo getHenkilo(String nimi) {
		return henkilot.etsiHenkilo(nimi);
	}


	//---------------------------------------------------------------------------------------------------
	// tietokanta versio alkaa

	/**
	 * Luo tietokannan. Jos annettu tiedosto on jo olemassa ja
	 * sisältää tarvitut taulut, ei luoda mitään
	 * @param nimi tietokannan nimi
	 * @throws SailoException jos tietokannan luominen epäonnistuu
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 *  Elokuva e1 = new Elokuva(), e2 = new Elokuva();
	 *  e1.asetaTiedot("0", "nimi1", "2001", "annetutAvainsanat1");
	 *  e2.asetaTiedot("0", "nimi2", "2002", "annetutAvainsanat2");
	 *  Relaatio r1 = new Relaatio(), r2 = new Relaatio(), r3 = new Relaatio(), r4 = new Relaatio();
	 *  r1.asetaTiedot("1", "1", "1");
	 *  r2.asetaTiedot("1", "2", "2");
	 *  r3.asetaTiedot("2", "3", "2");
	 *  r4.asetaTiedot("2", "2", "3");
	 *  
	 *  Henkilo h1 = new Henkilo(), h2 = new Henkilo(), h3 = new Henkilo();
	 *  h1.asetaTiedot("0", "henk1");
	 *  h2.asetaTiedot("0", "henk2");
	 *  h3.asetaTiedot("0", "henk3");
	 *  
	 *  kokoelma.add(e1);
	 *  kokoelma.add(e2);
	 *  e2.getEid() === 2;
	 *  
	 *  kokoelma.add(r1);
	 * 	kokoelma.add(r2);
	 * 	kokoelma.add(r3);
	 * 	kokoelma.add(r4); 
	 *	kokoelma.add(h1);
	 *	kokoelma.add(h2);
	 *  kokoelma.add(h3);
	 *
	 *	Collection<Elokuva> eloHaku = kokoelma.etsiElokuvia("nimi%", 2);
	 *  eloHaku.size() === 2;
	 *  eloHaku = kokoelma.etsiElokuvia("jotain jota ei löydy", 2);
	 *  eloHaku.size() === 0;
	 *
	 *  Collection<Henkilo> henkHaku = kokoelma.etsiHenkiloita("henk%", 2);
	 *  henkHaku.size() === 3;
	 *  henkHaku = kokoelma.etsiHenkiloita("henk1", 2);
	 *  henkHaku.size() === 1;
	 *  henkHaku = kokoelma.etsiHenkiloita("jotain jota ei löydy", 2);
	 *  henkHaku.size() === 0;
	 *  
	 *	Collection<Relaatio> relHaku = kokoelma.etsiRelaatioita("2", 1);
	 *  relHaku.size() === 2;
	 *  relHaku = kokoelma.etsiRelaatioita("jotain jota ei löydy", 2);
	 *  relHaku.size() === 0;
	 *  
	 *  Collection<String> henkilot = kokoelma.selvitaNayttelijat(e2);
	 *  henkilot.iterator().next() === "henk3";
	 *  
	 *  henkilot = kokoelma.selvitaKasikirjoittajat(e2);
	 *  henkilot.iterator().next() === "henk2";
	 *</pre>
	 */
	public void lueTiedostot(String nimi) throws SailoException {
		elokuvat = new Elokuvat(nimi);
		henkilot = new Henkilot(nimi);
		relaatiot = new Relaatiot(nimi);
	}


	/**
	 * tallentaisi tiedot tiedotoon, ei käytössä tietokantaversiossa 
	 * @throws SailoException ei tee, koska ei käytössä
	 */
	public void tallenna() throws SailoException {
		return;
	}


	/**
	 * Testiohjelma kerhosta
	 * @param args ei käytössä
	 */
	public static void main(String args[]) {

		try {
			new File("kokeilu.db").delete();
			Kokoelma kokoelma = new Kokoelma();
			kokoelma.lueTiedostot("kokeilu");

			Elokuva e1 = new Elokuva(), e2 = new Elokuva();
			e1.asetaTiedot("0", "nimi1", "2001", "annetutAvainsanat1");
			e2.asetaTiedot("0", "nimi2", "2002", "annetutAvainsanat2");

			Relaatio r1 = new Relaatio(), r2 = new Relaatio(), r3 = new Relaatio(), r4 = new Relaatio();
			r1.asetaTiedot("1", "1", "1");
			r2.asetaTiedot("1", "2", "2");
			r3.asetaTiedot("2", "3", "2");
			r4.asetaTiedot("2", "2", "3");

			Henkilo h1 = new Henkilo(), h2 = new Henkilo();
			h1.asetaTiedot("0", "henk1");
			h2.asetaTiedot("0", "henk2");

			kokoelma.add(e1);
			kokoelma.add(e2);
			kokoelma.add(r1);
			kokoelma.add(r2);
			kokoelma.add(r3);
			kokoelma.add(r4);
			kokoelma.add(h1);
			kokoelma.add(h2);

			System.out.println("============= Kokoelman kokeilu =================");

			Collection<Elokuva> loytyneetElokuvat = kokoelma.etsiElokuvia("nimi%", 2);
			int i = 0;
			for (Elokuva e : loytyneetElokuvat) {
				System.out.println("Elokuva paikassa: " + i);
				e.tulosta(System.out);

				Collection<Relaatio> loytyneetRelaatiot = kokoelma.etsiRelaatioita("" + e.getEid(), 1);

				for (Relaatio r : loytyneetRelaatiot) {
					Collection<Henkilo> loytyneetHenkilot = kokoelma.etsiHenkiloita("" + r.getHid(), 1);	

					for (Henkilo h : loytyneetHenkilot) {
						h.tulosta(System.out);
					}
				}

				System.out.println();

				i++;
			}   			
		} catch ( SailoException ex ) {
			System.out.println(ex.getMessage());
		}

		new File("kokeilu.db").delete();
	}


	/**
	 * etsii henkilöitä tietyn tietueen merkkijonomuodon perusteella (ks. lueTiedostot() -testi)
	 * @param hakuehto merkkijono jota haetaan
	 * @param k tietue josta haetaan
	 * @return löytyneet tietueet
	 * @throws SailoException jos haussa tapahtui virhe
	 */
	public Collection<Henkilo> etsiHenkiloita(String hakuehto, int k) throws SailoException {
		Collection<Henkilo> loytyneet = henkilot.etsi(hakuehto, k);

		return loytyneet;
	}


	/**
	 * pyytää relaatioilta hakuehdot täyttäviä relaatioita (ks. lueTiedostot() -testi)
	 * @param hakuehto ehto jolla haetaan
	 * @param k mones kysymys johon ehtoa testataan
	 * @return mitä löytyi
	 * @throws SailoException jos tulee ongelmia haun kanssa
	 */
	public Collection<Relaatio> etsiRelaatioita(String hakuehto, int k) throws SailoException {
		Collection<Relaatio> loytyneet = relaatiot.etsi(hakuehto, k);

		return loytyneet;
	}


	/**
	 * pyytää elokuvista hakuehdot täyttäviä elokuvia (ks. lueTiedostot() -testi)
	 * @param hakuehto ehto jolla haetaan 
	 * @param k mones kysymys johon ehtoa testataan
	 * @return mitä löytyi
	 * @throws SailoException jos tulee ongelmia haun kanssa
	 */
	public Collection<Elokuva> etsiElokuvia(String hakuehto, int k) throws SailoException {
		Collection<Elokuva> loytyneet = elokuvat.etsi(hakuehto, k);

		return loytyneet;
	}


	/**
	 * selvittää elokuvassa näyttelevät henkilöt (ks. lueTiedostot() -testi)
	 * @param tutkittava elokuva jonka näyttelijöitä selvitetään
	 * @return näyttelijöiden nimet
	 * @throws SailoException jos näyttelijöitä ei saatu selvitettyä
	 */
	public Collection<String> selvitaNayttelijat(Elokuva tutkittava) throws SailoException {
		Collection<Relaatio> sopivatRelaatiot = etsiRelaatioita("" + tutkittava.getEid(), 1);		
		Collection<String> nayttelijat = new ArrayList<String>();
		
		for (Relaatio r : sopivatRelaatiot) {
			if (r.getRid() == 2) {			
				nayttelijat.add(etsiHenkiloita("" + r.getHid(), 1).iterator().next().getNimi());
			}
		}				

		return nayttelijat;
	}


	/**
	 * selvittää elokuvan käsikirjoittajat (ks. lueTiedostot() -testi)
	 * @param tutkittava minkä elokuvan käsikirjoittajia selvitetään
	 * @return elokuvan käsikirjoittajien nimet
	 * @throws SailoException jos selvitys ei onnistunut
	 */
	public Collection<String> selvitaKasikirjoittajat(Elokuva tutkittava) throws SailoException {
		Collection<Relaatio> sopivatRelaatiot = etsiRelaatioita("" + tutkittava.getEid(), 1);		
		Collection<String> kasikirjoittajat = new ArrayList<String>();

		for (Relaatio r : sopivatRelaatiot) {
			if (r.getRid() == 3) {
				kasikirjoittajat.add(etsiHenkiloita("" + r.getHid(), 1).iterator().next().getNimi());
			}
		}				

		return kasikirjoittajat;
	}
}
