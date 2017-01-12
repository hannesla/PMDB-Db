/**
 * 
 */
package ht.kokoelma;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Luokka roolien hallintaan
 * 
 * @author Hannes Laukkanen
 * @version Apr 19, 2016
 * hannes.v.laukkanen@student.jyu.fi 
 */
public class Roolit {
	private List<Rooli> roolit = new ArrayList<Rooli>();


	/**
	 * Luo rooleja tietokannan k‰yttˆˆn
	 */
	public Roolit() {
		alusta();
		roolit.add(new Rooli("ohjaaja").rekisteroi());
		roolit.add(new Rooli("n‰yttelij‰").rekisteroi());
		roolit.add(new Rooli("k‰sikirjoittaja").rekisteroi());
	}


	/**
	 * Alustaa roolit seuraavaa k‰yttˆ‰ varten
	 */
	public void alusta() {
		roolit = new ArrayList<>();
		Rooli.alusta();
	}


	/**
	 * @param args ei k‰ytet‰
	 * Kokeillaan roolit luokkaa p‰‰ohjelmassa
	 */
	public static void main(String[] args) {
		Roolit roolit = new Roolit();

		roolit.add(new Rooli("rooli").taytaTiedoilla().rekisteroi());
		roolit.add(new Rooli("toinenrooli").taytaTiedoilla().rekisteroi());

		for (int i = 0; i < roolit.getLkm(); i++) {
			roolit.get(i).tulosta(System.out);
			System.out.println("Testataan roolit-luokkaa");
		}
	}


	/**
	 * palauttaa roolien lukum‰‰r‰n
	 * @return roolien lukum‰‰r‰
	 */
	public int getLkm() {
		return roolit.size();
	}


	/**
	 * palauttaa viitteen annetussa indeksiss‰ olevaan rooliin
	 * @param i roolin indeksi
	 * @return viite rooliin
	 * @throws IndexOutOfBoundsException jos ep‰kelpo indeksi
	 * @example
	 * <pre name="test">
	 * #THROWS IndexOutOfBoundsException
	 * Roolit rel = new Roolit();
	 * rel.get(4); #THROWS IndexOutOfBoundsException
	 * Rooli r = new Rooli();
	 * rel.add(r);
	 * rel.get(3) == r === true;
	 * rel.get(0) == new Rooli() === false; 
	 * rel.get(5); #THROWS IndexOutOfBoundsException
	 * </pre>
	 */
	public Rooli get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > getLkm() - 1) throw new IndexOutOfBoundsException();
		return roolit.get(i);
	}


	/**
	 * Lis‰‰ roolin listaan
	 * @param rooli rooli joka lis‰t‰‰n
	 * @example
	 * <pre name="test">
	 * Roolit rel = new Roolit();
	 * rel.getLkm() === 3; // nykyisell‰‰n luo roolit luonnin yhteydess‰
	 * rel.add(new Rooli());
	 * rel.getLkm() === 4;
	 * </pre> 
	 */
	public void add(Rooli rooli) {
		roolit.add(rooli);		
	}


	/**
	 * Tallentaa roolin tiedot annettuun polkuun
	 * @param polku polku johon tallennetaan
	 */
	public void tallenna(String polku) {
		String tiedNimi = polku + "\\" + "roolit.dat";
		try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {
			for (Rooli rooli : roolit) {
				if (rooli == null) break;
				fo.printf(rooli.toString());
			}
		} catch (FileNotFoundException ex) {
			System.err.println("Tiedosto ei aukea: " + ex.getMessage());
		}

	}


	/**
	 * Kertoo mik‰ id vastaa kysytty‰ roolia
	 * @param roolinNimi roolin nimi jonka id:ta kysyt‰‰n
	 * @return roolin id (0 jos roolia ei ole)
	 */
	public int roolinId(String roolinNimi) {	
		for (Rooli r : roolit) {
			if (r.getRoolinNimi().equals(roolinNimi)) return r.getRid();
		}

		return 0;
	}
}