/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Security;

import java.io.InputStream;

/**
 * Interface to define detection methods.
 *
 */
public interface DocumentDetector {
	/**
	 * Method to verify if the specified file contains a document that:<br>
	 * <ul>
	 * <li>Do not contains potential malicious content</li>
	 * <li>Is part of the supported accepted format</li>
	 * </ul>
	 * 
	 * @param f File to validate
	 * 
	 * @return TRUE only if the file fill the 2 rules above
	 */
	//boolean isSafe(File f);
        boolean isSafe(InputStream istr);
}