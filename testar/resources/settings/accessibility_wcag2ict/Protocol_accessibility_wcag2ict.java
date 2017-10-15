/**********************************************************************************************
 *                                                                                            *
 * COPYRIGHT (2017):                                                                          *
 * Universitat Politecnica de Valencia                                                        *
 * Camino de Vera, s/n                                                                        *
 * 46022 Valencia, Spain                                                                      *
 * www.upv.es                                                                                 *
 *                                                                                            * 
 * D I S C L A I M E R:                                                                       *
 * This software has been developed by the Universitat Politecnica de Valencia (UPV)          *
 * in the context of the STaQ (Software Testing and Quality) research group: staq.dsic.upv.es *
 * This software is distributed FREE of charge under the TESTAR license, as an open           *
 * source project under the BSD3 license (http://opensource.org/licenses/BSD-3-Clause)        *                                                                                        * 
 *                                                                                            *
 **********************************************************************************************/

import nl.ou.testar.a11y.protocols.AccessibilityProtocol;
import nl.ou.testar.a11y.wcag2.WCAG2ICT;

/**
 * An accessibility evaluation protocol based on WCAG2ICT
 * @author Davy Kager
 */
public class Protocol_accessibility_wcag2ict extends AccessibilityProtocol {

	/**
	 * Constructs a new protocol
	 */
	public Protocol_accessibility_wcag2ict() {
		super(new WCAG2ICT());
	}

}