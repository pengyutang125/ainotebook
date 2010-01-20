/* 
 *** Notice Update: 8/14/2007
 *** Copyright 2007 Berlin Brown
 *** Copyright 2006-2007 Newspiritcompany.com
 *** 
 *** This SOURCE FILE is licensed to NEWSPIRITCOMPANY.COM.  Unless
 *** otherwise stated, use or distribution of this program 
 *** for commercial purpose is prohibited.
 *** 
 *** See LICENSE.BOTLIST for more information.
 ***
 *** The SOFTWARE PRODUCT and CODE are protected by copyright and 
 *** other intellectual property laws and treaties. 
 ***  
 *** Unless required by applicable law or agreed to in writing, software
 *** distributed  under the  License is distributed on an "AS IS" BASIS,
 *** WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 *** implied.
 */
package org.bresearch.websec.utils.botlist.text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * This is class is used by botverse.
 * @author Berlin Brown
 */
public class WordProcessor {
	
	public final static String [][] FILTER_SET = {
			{ "&#39;", "'" } ,
			{ "&quot;", "'" },
			{ "&amp;", "&" },
			{ "<b>", "" }, { "</b>", "" },			
			{ "<B>", "" }, { "</B>", "" },
			{ "<strong>", "" }, { "</strong>", "" },			
			{ "<STRONG>", "" }, { "</STRONG>", "" }
	};
	
	/**
	 * When saving a keyword, process the input to 
	 * filter invalid characters and downcase the string.
	 * 
	 * @return
	 */
	public String createKeywords(String value) {
		if (value == null) {
			return value;
		}		
		// Filter out non alphanumeric chars
		String output = value.replaceAll("[^\\s0-9a-zA-Z]", "");		
		return output.trim().toLowerCase();
	}
	
	public String urlEncode(String val) throws UnsupportedEncodingException {
		return URLEncoder.encode(val, "UTF-8");
	}
	public String urlEncodeEnc(String val, String enc) throws UnsupportedEncodingException {
		return URLEncoder.encode(val, enc);
	}
	
	public String urlDecode(String val) throws UnsupportedEncodingException {
		return URLDecoder.decode(val, "UTF-8");
	}
	public String urlDecodeEnc(String val, String enc) throws UnsupportedEncodingException {
		return URLDecoder.decode(val, enc);
	}
	
	/**
	 * Utility for filtering alpha [a-Z], etc text using regular expressions, used with queries
	 * to prevent sql injection hacks.
	 */
	public String filterAlphaText(final String value) {
		if (value == null) {
			return value;
		}		
		// Filter out non alphanumeric chars
		String output = value.replaceAll("[^\\sa-zA-Z]", "");		
		return output.trim();
	}
	
	public String filterAlphaNumeric(String value) {
		if (value == null) {
			return value;
		}		
		// Filter out non alphanumeric chars
		String output = value.replaceAll("[^\\s0-9a-zA-Z]", "");		
		return output.trim();
	}
	public boolean validateFilterAlphaNumeric(String value) {
		if (value == null) {
			return false;
		}		
		// Filter out non alphanumeric chars
		String output = filterAlphaNumeric(value);
		output = output.replaceAll(" ", "");
		return (output.trim().length() == value.length());
	}
	
	
	public String filterNonAscii(String value) {
		if (value == null) {
			return value;
		}		
		// Filter out non alphanumeric chars
		String output = value.replaceAll("\\P{ASCII}+", "");
		for (int i = 0; i < FILTER_SET.length; i++)
			output = output.replaceAll(FILTER_SET[i][0], FILTER_SET[i][1]);
			
		return output.trim();
	}
		
	/**
	 * Utility to create a filename from an input form title.
	 * 
	 * @param value
	 * @return
	 */
	public String createFilenameTitle(String value) {
		if (value == null) {
			return value;
		}		
		// Filter out non alphanumeric chars
		String output = value.replaceAll(" ", "_");
		output = output.replaceAll("[^\\s_0-9a-zA-Z]", "");		
		return output.trim().toLowerCase();
	}
		
}
