/**
 * 
 */
package ht.fxPmdb;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Ikkunan josta "kirjaudutaan" eli kerrotaan, mink‰ tunnuksen elokuvat ladataan 
 * 
 * @author Hannes Laukkanen
 * @version Feb 12, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class KirjautuminenController implements ModalControllerInterface<String> {

	private String vastaus = null;

	@FXML private TextField textVastaus;

	
	/**
	 * asettaa vastauksen arvoksi k‰ytt‰j‰n kirjoittaman merkkijonon ja sulkee dialogin
	 */
	@FXML private void handleKirjaudu() {
		vastaus = textVastaus.getText();
		ModalController.closeStage(textVastaus);
	}


	/**
	 * sulkee dialogin, ei avaa tiedostoa
	 */
	@FXML private void handleCancel() {
		vastaus = null;
		ModalController.closeStage(textVastaus);
	}


	/**
	 * palautaa vastauksen
	 */
	@Override
	public String getResult() {
		return vastaus;
	}
	

	/**
	 * Asettaa oletus tekstin ikkunaan
	 */
	@Override
	public void setDefault(String oletus) {
		textVastaus.setText(oletus);
	}


	/**
	 * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
	 */
	@Override
	public void handleShown() {
		textVastaus.requestFocus();
	}


    /**
     * Kysyy mink‰ tunnuksen elokuvat ladataan
     * @param modalityStage mille ikkunoille ollaa modaalisia
     * @param oletus teksti
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     */
	public static String kysyTunnus(Stage modalityStage, String oletus ){
		return ModalController.showModal(
				KirjautuminenController.class.getResource("KirjautuminenView.fxml"),
				"PMDB", modalityStage, oletus);
	}

}
