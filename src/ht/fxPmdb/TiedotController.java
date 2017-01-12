/**
 * 
 */
package ht.fxPmdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import ht.kokoelma.Elokuva;
import ht.kokoelma.Henkilo;

import ht.kokoelma.Kokoelma;
import ht.kokoelma.Relaatio;
import ht.kokoelma.SailoException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/**
 * N‰ytt‰‰ enemm‰n tietoja elokuvasta ja mahdollistaa tietojen muokkauksen
 * 
 * Ohjelmasta puuttuvat ominaisuudet:
 * - henkilˆihin liittyv‰t oikeellisuustarkistukset (nimiin ei numeroita tai erikoismerkkej‰)
 * - ikkunoiden sulkemisnapit
 * - avainnojen tulosteesta ',' pois lopusta
 * 
 * @author handu
 * @version 10.5.2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class TiedotController implements ModalControllerInterface<Kokoelma>, Initializable {

	// Tekstikent‰t
	@FXML private GridPane gridTiedot;	
	@FXML private TextField textNimi;
	@FXML private TextField textVuosi;
	@FXML private TextArea textNayttelijat;
	@FXML private TextArea textAvainsanat;
	@FXML private TextArea textKasikirjoittajat;
	@FXML private TextField textOhjaaja;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		//
	}	


	@FXML void muokkaaAvainsanatHandler() {
		avainsanojenMuokkaus();
	}


	@FXML void muokkaaKasikirjoittajatHandler() {
		kasikirjoittajienMuokkaus();
	}


	@FXML void muokkaaNayttelijatHandler() {
		nayttelijoidenMuokkaus();
	}


	@FXML void tallennaJaPoistuHandler() {
		Stage stage = (Stage) gridTiedot.getScene().getWindow();
		stage.close();
	}


	// =========================================================================================
	// Oma koodi	

	private Kokoelma kasittelyssa = null;
	private String ohjaajanNimi = "ei ohjaajaa";
	private String elokuvanNimi = "nimetˆn";
	private String vuosi = "2000";
	private String avainsanat = "";
	private List<String> nayttelijat = new ArrayList<>();
	private List<String> kasikirjoittajat = new ArrayList<>();
	private boolean onUusi = false; 

	/**
	 * Avaa ikkunan josta voidaan lis‰t‰ ja poistaa tietoja
	 */
	private void nayttelijoidenMuokkaus() {

		if (kasittelyssa.getElokuvaKasittelyssa().getEid() != 0 ){
			Dialogs.showMessageDialog("Tietokantaversiossa tallennetun elokuvan tietoja ei voi muuttaa");
			return;
		}

		TextInputDialog dialog = new TextInputDialog("kirjoita...");
		dialog.setHeaderText(null);
		dialog.setTitle("Lis‰‰minen");
		dialog.setContentText("Lis‰tt‰v‰:");
		Optional<String> answer = dialog.showAndWait();

		if (!answer.isPresent()) return;  // jos k‰ytt‰j‰ painaa cancel

		nayttelijat.add(answer.get());

		textNayttelijat.clear();

		for (String nimi : nayttelijat) {
			textNayttelijat.setText(textNayttelijat.getText() + nimi + "\n");
		}
	}


	/**
	 * Muokkaa k‰sikirjoittajia
	 */
	public void kasikirjoittajienMuokkaus() {

		if (kasittelyssa.getElokuvaKasittelyssa().getEid() != 0 ){
			Dialogs.showMessageDialog("Tietokantaversiossa tallennetun elokuvan tietoja ei voi muuttaa");
			return;
		}

		TextInputDialog dialog = new TextInputDialog("kirjoita...");
		dialog.setHeaderText(null);
		dialog.setTitle("Lis‰‰minen");
		dialog.setContentText("Lis‰tt‰v‰:");
		Optional<String> answer = dialog.showAndWait();

		if (!answer.isPresent()) return;  // jos k‰ytt‰j‰ painaa cancel

		kasikirjoittajat.add(answer.get());

		textKasikirjoittajat.clear();

		for (String nimi : kasikirjoittajat) {
			textKasikirjoittajat.setText(textKasikirjoittajat.getText() + nimi + "\n");
		}
	}


	/**
	 * Muokkaa avainsanoja
	 */
	public void avainsanojenMuokkaus() {

		if (kasittelyssa.getElokuvaKasittelyssa().getEid() != 0 ){
			Dialogs.showMessageDialog("Tietokantaversiossa tallennetun elokuvan tietoja ei voi muuttaa");
			return;
		}

		TextInputDialog dialog = new TextInputDialog("kirjoita...");
		dialog.setHeaderText(null);
		dialog.setTitle("Lis‰‰minen");
		dialog.setContentText("Lis‰tt‰v‰:");
		Optional<String> answer = dialog.showAndWait();

		if (!answer.isPresent()) return;  // jos k‰ytt‰j‰ painaa cancel

		avainsanat = textAvainsanat.getText() + " " + answer.get();

		textAvainsanat.setText(avainsanat);
	}


	/**
	 * Luo ikkunan
	 * @param modalityStage modaalisuus
	 * @param kasiteltava oletus k‰ytt‰j‰
	 * @return ikkunan vaikutus
	 */
	public static Kokoelma elokuvanTiedot(Stage modalityStage, Kokoelma kasiteltava ){		
		return ModalController.showModal(
				TiedotController.class.getResource("TiedotView.fxml"),
				"Elokuvan tiedot", modalityStage, kasiteltava);
	}


	/**
	 * kysyy kokoelmalta, onko nimi sopiva kokoelmaan
	 */
	public void tarkistaNimi() {

		String ongelma = kasittelyssa.tarkistaNimi(textNimi.getText());
		if (ongelma == null) return;

		vaadiNimi(ongelma);	
	}


	/**
	 * Avaa ikkunan jossa k‰ytt‰j‰‰ pyydet‰‰n antamaan sopiva nimi elokuvalle
	 * @param ongelma miksi aiemmin annettu nimi ei kelpaa
	 */
	public void vaadiNimi(String ongelma) {

		String nimi = "";

		while(nimi.equals("")) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setHeaderText(null);
			dialog.setTitle(ongelma);
			dialog.setContentText("Elokuvan nimi:");
			Optional<String> answer = dialog.showAndWait();

			if (!answer.isPresent()) nimi = "nimetˆn elokuva";
			else nimi = answer.get();		
		}

		kasittelyssa.getElokuvaKasittelyssa().setElokuvanNimi(nimi);		
	}


	/**
	 * Asettaa kursorin nimi kentt‰‰n
	 */
	@Override
	public void handleShown() {
		//	System.out.println(kasittelyssa.toString());
		textNimi.requestFocus();		
	}


	/**
	 * ottaa kokoelman kasitteleltavaksi ja asettaa nykyiset tiedot
	 */
	@Override
	public void setDefault(Kokoelma auki) {
		kasittelyssa = auki;

		alusta();
	}


	/**
	 * asettaa kokoelmassa olevat t‰m‰n hetkiset tiedot ikkunaan
	 * @throws SailoException jos tietojen hakemisessa on ongelma
	 */
	public void asetaTiedot() throws SailoException {
		textNimi.setText(kasittelyssa.getElokuvaKasittelyssa().getNimi());
		textVuosi.setText("" + kasittelyssa.getElokuvaKasittelyssa().getVuosi());

		try {		
			String asetetaanOhjaajaksi = kasittelyssa.selvitaOhjaaja(kasittelyssa.getElokuvaKasittelyssa());
			if (asetetaanOhjaajaksi != null) textOhjaaja.setText(asetetaanOhjaajaksi);

			StringBuilder nimiLista = new StringBuilder();

			for (String nimi : kasittelyssa.selvitaNayttelijat(kasittelyssa.getElokuvaKasittelyssa())){
				nimiLista.append(nimi + "\n");
			}

			textNayttelijat.setText(nimiLista.toString());

			nimiLista = new StringBuilder();

			for (String nimi : kasittelyssa.selvitaKasikirjoittajat(kasittelyssa.getElokuvaKasittelyssa())){
				nimiLista.append(nimi + "\n");
			}

			textKasikirjoittajat.setText(nimiLista.toString());		

		} catch (SailoException e) {
			System.err.println("Henkiˆiden selvitt‰misess‰ tapahtui virhe: " + e.getMessage());
		}

		textAvainsanat.setText(kasittelyssa.getElokuvaKasittelyssa().getAvainsanat());
	}


	/**
	 * selvitt‰‰ elokuvan avainsanat
	 * @return avainsanat
	 */
	public String selvitaAvainsanat() {
		return kasittelyssa.getElokuvaKasittelyssa().getAvainsanat();
	}

	
	/**
	 * selvitt‰‰ elokuvan ohjaajan
	 * @return elokuvan ohjaaja
	 */
	public String selvitaOhjaaja() {

		int eid = kasittelyssa.getElokuvaKasittelyssa().getEid();
		int hid = -1;
		boolean loytyiko = false;

		for (int i = 0; i < kasittelyssa.getRelaatioidenLkm(); i++) {
			if (kasittelyssa.getRelaatio(i).getEid() == eid && kasittelyssa.getRelaatio(i).getRid() == 1) {
				hid = kasittelyssa.getRelaatio(i).getHid();
				loytyiko = true;
				break;
			}
		}

		if (!loytyiko) return "";

		for (int i = 0; i < kasittelyssa.getHenkiloidenLkm(); i++) {
			if (kasittelyssa.getHenkilo(i).getHid() == hid) return kasittelyssa.getHenkilo(i).getNimi();
		}		

		return "";
	}


	/**
	 * Selvitt‰‰ ollaanko lis‰‰m‰ss‰ elokuvaa vai tarkastelemassa vanhaan ja alustaa ikkunan
	 * sen mukaan.
	 */
	public void alusta() {

		onUusi = (kasittelyssa.getElokuvaKasittelyssa().getEid() == 0);

		if (onUusi) {

			textNimi.setEditable(true);
			textVuosi.setEditable(true);
			textOhjaaja.setEditable(true);

			textNimi.setText("");
			textVuosi.setText("");
			textOhjaaja.setText("");
			textAvainsanat.setText("");
			textKasikirjoittajat.setText("");
			textNayttelijat.setText("");
		} else {
			try {
				asetaTiedot();
			} catch (SailoException e) {
				System.err.println("Ongelmia elokuvan tietojen haussa: " + e.getMessage());
			}
		}
	}


	/**
	 * Palauttaa vastauksen
	 */
	@Override
	public Kokoelma getResult() {

		if (!onUusi) return null;

		if (!textNimi.getText().isEmpty()) elokuvanNimi = textNimi.getText();
		if (!textVuosi.getText().isEmpty()) vuosi = textVuosi.getText();
		if (!textOhjaaja.getText().isEmpty()) ohjaajanNimi = textOhjaaja.getText();
		if (!textAvainsanat.getText().isEmpty()) avainsanat = textAvainsanat.getText();

		vieTietokantaan();

		return kasittelyssa;
	}


	/**
	 * vie lomakkeella olevat tiedot tietokantaan
	 */
	public void vieTietokantaan() {
		try {
			Elokuva uusiElokuva = kasittelyssa.getElokuvaKasittelyssa();
			uusiElokuva.asetaTiedot("" + 0, elokuvanNimi, vuosi, avainsanat);		
			kasittelyssa.add(uusiElokuva);

			int eid = uusiElokuva.getEid();

			Collection<Henkilo> samannimiset = kasittelyssa.etsiHenkiloita(ohjaajanNimi, 2);

			Henkilo ohjaaja;

			if (samannimiset.isEmpty()) {
				ohjaaja = new Henkilo();
				ohjaaja.asetaTiedot("" + 0, ohjaajanNimi);
				kasittelyssa.add(ohjaaja);
			} else {
				ohjaaja = samannimiset.iterator().next();
			}

			Relaatio r = new Relaatio();
			r.asetaTiedot("" + eid, "" + ohjaaja.getHid(), "" + 1);
			kasittelyssa.add(r);

			for (String nimi : nayttelijat) {				
				Henkilo nayttelija; 

				samannimiset = kasittelyssa.etsiHenkiloita(nimi, 2);

				if (samannimiset.isEmpty()) {
					nayttelija = new Henkilo();
					nayttelija.asetaTiedot("" + 0, nimi);
					kasittelyssa.add(nayttelija);
				} else {
					nayttelija = samannimiset.iterator().next();
				}

				r = new Relaatio();
				r.asetaTiedot("" + eid, "" + nayttelija.getHid(), "" + 2);
				kasittelyssa.add(r);
			}


			for (String nimi : kasikirjoittajat) {
				Henkilo kasikirjoittaja; 

				samannimiset = kasittelyssa.etsiHenkiloita(nimi, 2);

				if (samannimiset.isEmpty() ) {
					kasikirjoittaja = new Henkilo();
					kasikirjoittaja.asetaTiedot("" + 0, nimi);
					kasittelyssa.add(kasikirjoittaja);
				} else {
					kasikirjoittaja = samannimiset.iterator().next();
				}

				r = new Relaatio();
				r.asetaTiedot("" + eid, "" + kasikirjoittaja.getHid(), "" + 3);
				kasittelyssa.add(r);
			}	

		} catch (Exception e) {
			System.err.println("Tietokantaan lis‰‰misess‰ tapahtui virhe: " + e.getMessage());
		}
	}
}
