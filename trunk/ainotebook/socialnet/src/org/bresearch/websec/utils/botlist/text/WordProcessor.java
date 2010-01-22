/**
 * Copyright (c) 2006-2010 Berlin Brown and botnode.com/Berlin Research  All Rights Reserved
 *
 * http://www.opensource.org/licenses/bsd-license.php

 * All rights reserved.

 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:

 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * * Neither the name of the Botnode.com (Berlin Brown) nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Date: 1/23/2010 
 * Description: Social Networking Site Document Analysis
 * Home Page: http://botnode.com/
 * 
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */
package org.bresearch.websec.utils.botlist.text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public String filterOnlyAlphaNumeric(final String strData) {
	    final String trim2 = strData.trim().toLowerCase();
	    // Use the not char to replace everything but he alpha numeric chars.
	    final Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
	    final Matcher m = pattern.matcher(trim2);
	    final String res = m.replaceAll(" ");        
	    final String res2 = res.trim();
	    return res2;
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
