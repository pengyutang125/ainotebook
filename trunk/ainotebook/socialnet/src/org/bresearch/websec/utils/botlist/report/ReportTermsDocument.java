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
package org.bresearch.websec.utils.botlist.report;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bresearch.websec.utils.botlist.BotlistStringUtils;
import org.bresearch.websec.utils.botlist.IBotlistDocument;
import org.bresearch.websec.utils.botlist.text.WordProcessor;

import com.google.inject.Inject;

public class ReportTermsDocument implements IReport {
    
    public static final char NL = '\n';
    
    private final IBotlistDocument document;        
    private BotlistStringUtils stringUtils = new BotlistStringUtils();
    private int numTopWords = 100;    
    private boolean format = true;
    private String separator = "\n";
    
    @Inject
    public ReportTermsDocument(final IBotlistDocument document) {
        this.document = document;
    }
    
    public String toReport() {
        
        final StringBuilder builder = new StringBuilder(40);
        ///////////////////////////////////////////////////
        
        builder.append("Report Words Document - " + (new Date()));                                     
        builder.append(NL);                 
        
        final WordProcessor fill = new WordProcessor();
        // Add the top words:
        builder.append(fill.spaceFill("Top Words" + ":\n", 10, ' '));        
        builder.append(this.topWords()).append('\n');
        builder.append("<End of Report>");
        builder.append(NL);
        return builder.toString();
        
    }
    
    public String topWords() {
        
        final Set<Map.Entry<String, Integer>> topWords = this.topWords(this.getNumTopWords(), true, this.stringUtils);        
        if (format) {
            
            final StringBuilder builder = new StringBuilder(topWords.size() * 20);
            // Iterate through the top words and use a new line
            for (Map.Entry<String, Integer> entry : topWords) {
                builder.append(entry);
                builder.append(this.getSeparator());
            } // End of the For //
            return builder.toString();
            
        } else {
            return topWords.toString(); 
        } // End of the if - else //
    }    
    
    public Set<Map.Entry<String, Integer>> topWords(final int nWords, final boolean locStopWords, final BotlistStringUtils utils) {
        
        final String formatDoc = this.document.formatDocument();
        final List<String> listterms = utils.buildWordList(formatDoc);
        final Set<Map.Entry<String, Integer>> set = locStopWords ? utils.mapReduceWithStopWords(listterms, -1) : utils.mapReduce(listterms, -1);
        final Set<Map.Entry<String, Integer>> newSet = new LinkedHashSet<Map.Entry<String, Integer>>();
        
        final int n = set.size() < nWords ? set.size() : nWords;
        int i = 1;        
        for (Map.Entry<String, Integer> entry : set) {
            newSet.add(entry);
            if (i >= n) {
                break;
            }
            i++;
        } // End of the For //
       
        return newSet;
    }

    /**
     * @return the numTopWords
     */
    public int getNumTopWords() {
        return numTopWords;
    }

    /**
     * @param numTopWords the numTopWords to set
     */
    public void setNumTopWords(int numTopWords) {
        this.numTopWords = numTopWords;
    }

    /**
     * @return the stringUtils
     */
    public BotlistStringUtils getStringUtils() {
        return stringUtils;
    }

    /**
     * @param stringUtils the stringUtils to set
     */
    public void setStringUtils(BotlistStringUtils stringUtils) {
        this.stringUtils = stringUtils;
    }

    /**
     * @return the format
     */
    public boolean isFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(boolean format) {
        this.format = format;
    }

    /**
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }
               
} // End of the class //
