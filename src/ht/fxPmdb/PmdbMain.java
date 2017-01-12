package ht.fxPmdb;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * Pääohjelma PMDB:lle eli elokuvatietokannalle
 * 
 * Ohjelmasta puuttuvat ominaisuudet:
 * - haku näyttelijöiden nimillä
 * - haku avainsanoilla
 * - haku käsikirjoittajilla
 * - paperille tulostaminen 
 * - henkilöihin liittyvät oikeellisuustarkistukset (nimiin ei numeroita tai erikoismerkkejä)
 * - ikkunoiden sulkemisnapit
 * 
 * @author Hannes Laukkanen
 * @version Jan 29, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class PmdbMain extends Application {
	
	@Override
	public void start(Stage primaryStage) {
				
		try {
			FXMLLoader ldr = new FXMLLoader(getClass().getResource("PmdbGUIView.fxml"));
			
			final Pane root = ldr.load();						
			
			final Scene scene = new Scene(root);			
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("PMDB");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Käynnistetään PMDB-ohjelma
	 * @param args ei käytetä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}