/**
 * 
 */
package ht.kokoelma;

/** 
 * Luokka jota k‰ytet‰‰n poikkeuksien hallintaan
 * 
 * @author Hannes Laukkanen
 * @version 23.2.2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class SailoException extends Exception {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Muodostaa poikkeuksen k‰ytt‰en parametrina saatua viesti‰ 
	 * @param viesti teksti joka poikkeukseen liitet‰‰n
	 */
	public SailoException(String viesti) {
		super(viesti);
	}
}