/**
 * 
 */
package ht.kanta;

/**
 * Aliojelmia joita tietokanta hyödyntää
 *  
 *   Puuttuvat ominaisuudet:
 * - henkilöihin liittyvät oikeellisuustarkistukset (nimiin ei numeroita tai erikoismerkkejä)
 *  
 * @author Hannes Laukkanen
 * @version 23.2.2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class ElokuvaTarkistus {	
	
	
	/**
	 * Arpoo luvun annetulle välille
	 * @param alaraja luvun on oltava tätä suurempi
	 * @param ylaraja luvun on oltava tätä pienempi
	 * @return arvottu luku
	 */
	public static int arvoValille(int alaraja, int ylaraja) {
		double n = (ylaraja - alaraja) * Math.random() + alaraja;		
		return (int)Math.round(n);
	}

	
	/**
	 * tarkistaa onko elokuvan nimi sopiva tietokantaan
	 * @param ehdotus nimi jota on ehdotettu
	 * @return null jos ok, muuten syy miksi ei ole sopiva
	 * @example
	 * <pre name="test">
	 * tarkistaNimi("") === "Elokuvalla on oltava nimi";
	 * tarkistaNimi("Mitä tahansa muuta") === null;
	 * </pre>
	 */
	public static String tarkistaNimi(String ehdotus) {
		if (ehdotus.isEmpty()) return "Elokuvalla on oltava nimi";
		return null;
		
	}

	
	/**
	 * Tarkistaa onko ehdotettu valmistumisvuosi sopiva tietokantaan
	 * @param ehdotus vuosi jota ehdotettu
	 * @return null jos ok, muuten syy miksi ei kelpaa
	 * @example
	 * <pre name="test">
	 * tarkistaVuosi("1999") === null;
	 * tarkistaVuosi("1") === "vuoden on oltava väliltä 1895 - 2025";
	 * tarkistaVuosi("2026") === "vuoden on oltava väliltä 1895 - 2025";
	 * tarkistaVuosi("2025") === null;
	 * tarkistaVuosi("1895") === null;
	 * </pre>
	 */
	public static String tarkistaVuosi(String ehdotus) {		
		int vuosi = Integer.parseInt(ehdotus);
		
		if (vuosi < 1895 || vuosi > 2025) return "vuoden on oltava väliltä 1895 - 2025";
		
		return null;
	}
}