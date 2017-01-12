/**
 * 
 */
package ht.kokoelma;

import java.io.PrintStream;

/**
 * K‰ytet‰‰n roolien luomiseen
 * 
 * @author Hannes Laukkanen
 * @version Apr 19, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Rooli {
	private String roolinNimi;
	private int rid;

	private static int seuraavaRid = 1;


	/**
	 * Luo roolin nimell‰ varustettuna
	 * @param roolinNimi nimi joka roolille annetaan
	 */
	public Rooli(String roolinNimi) {
		this.roolinNimi = roolinNimi;
	}


	/**
	 * Luo roolin ilman, ett‰ sille m‰‰ritell‰‰n atribuutteja
	 */
	public Rooli() {
		// 
	}


	/**
	 * Kokeillaan rooli-luokkaa
	 * @param args ei k‰ytet‰
	 */
	public static void main(String[] args) {

		Rooli h = new Rooli("rooli");
		Rooli k = new Rooli("toinenrooli");

		h.taytaTiedoilla().rekisteroi();
		k.taytaTiedoilla().rekisteroi();

		h.tulosta(System.out);	
		k.tulosta(System.out);
	}


	/**
	 * Palauttaa roolin
	 * @return rooli
	 */
	public String getRoolinNimi() {
		return roolinNimi;
	}


	/**
	 * palauttaa roolin id:n
	 * @return roolin id
	 */
	public int getRid() {
		return rid;
	}


	/**
	 * tulostaa roolin tiedot
	 * @param out tietovista josta tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(roolinNimi + "\nRid: " + rid);

	}


	/**
	 * antaa roolille tunnusnumeron
	 * @return viite rooliin
	 *@example
	 * <pre name="test">
	 * Rooli test1 = new Rooli();
	 * Rooli test2 = new Rooli();
	 * test1.getRid() === 0;
	 * test2.getRid() === 0;
	 * test1.rekisteroi();
	 * test2.rekisteroi();
	 * test2.getRid() - test1.getRid() == 1 === true; // testattava n‰in, koska testien suoritusj‰rjestyst‰ ei voi tiet‰‰
	 * </pre>
	 */
	public Rooli rekisteroi() {

		rid = seuraavaRid;
		seuraavaRid++;

		return this;
	}


	/**
	 * t‰ytt‰‰ roolin esimerkidatalla kokeilua varten
	 * @return viite rooliin
	 * <pre name="test">
	 * Rooli e1 = new Rooli(); 
	 * e1.taytaTiedoilla();
	 * e1.getRoolinNimi().matches("N‰yttelij‰ [0-9]{4}") === true;
	 * </pre>
	 */
	public Rooli taytaTiedoilla() {
		roolinNimi = "N‰yttelij‰ " + ht.kanta.ElokuvaTarkistus.arvoValille(1000, 9999);
		return this;
	}


	@Override
	public String toString() {
		return "" + getRid()  + "|" + getRoolinNimi() + "|\n";
	}


	/**
	 * Asettaa roolin atribuutit 
	 * @param uusiRid roolin id
	 * @param uusiNimi roolin nimi
	 * @return viite luotuun rooliin
	 */
	public Rooli asetaTiedot(String uusiRid, String uusiNimi) {
		this.rid = Integer.parseInt(uusiRid);
		this.roolinNimi = uusiNimi;		

		return this;		
	}


	/**
	 * Alustaa roolin seuraavaa k‰yttˆ‰ varten
	 */
	public static void alusta() {
		seuraavaRid = 1;		
	}
}