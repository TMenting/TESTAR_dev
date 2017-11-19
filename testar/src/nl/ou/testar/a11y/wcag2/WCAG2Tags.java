/******************************************************************************************
 * COPYRIGHT:                                                                             *
 * Universitat Politecnica de Valencia 2013                                               *
 * Camino de Vera, s/n                                                                    *
 * 46022 Valencia, Spain                                                                  *
 * www.upv.es                                                                             *
 *                                                                                        * 
 * D I S C L A I M E R:                                                                   *
 * This software has been developed by the Universitat Politecnica de Valencia (UPV)      *
 * in the context of the european funded FITTEST project (contract number ICT257574)      *
 * of which the UPV is the coordinator. As the sole developer of this source code,        *
 * following the signed FITTEST Consortium Agreement, the UPV should decide upon an       *
 * appropriate license under which the source code will be distributed after termination  *
 * of the project. Until this time, this code can be used by the partners of the          *
 * FITTEST project for executing the tasks that are outlined in the Description of Work   *
 * (DoW) that is annexed to the contract with the EU.                                     *
 *                                                                                        * 
 * Although it has already been decided that this code will be distributed under an open  *
 * source license, the exact license has not been decided upon and will be announced      *
 * before the end of the project. Beware of any restrictions regarding the use of this    *
 * work that might arise from the open source license it might fall under! It is the      *
 * UPV's intention to make this work accessible, free of any charge.                      *
 *****************************************************************************************/

package nl.ou.testar.a11y.wcag2;

import org.fruit.alayer.Tag;
import org.fruit.alayer.TagsBase;

/**
 * WCAG2ICT tags
 * @author Davy Kager
 *
 */
public final class WCAG2Tags extends TagsBase {
	
	private WCAG2Tags() {}
	
	public static final Tag<Boolean> WCAG2KeyboardVisited = from("WCAG2KeyboardVisited", Boolean.class);
	public static final Tag<Boolean> WCAG2IsWindow = from("WCAG2IsWindow", Boolean.class);
	public static final Tag<String> WCAG2Violations = from("WCAG2Violations", String.class);

}
