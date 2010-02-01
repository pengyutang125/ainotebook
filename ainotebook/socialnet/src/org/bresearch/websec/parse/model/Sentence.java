/**
 * Copyright (c) 2006-2007 Berlin Brown and botnode.com/Berlin Research  All Rights Reserved
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
 * Date: 12/15/2009 
 *   
 * Home Page: http://botnode.com/
 * 
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */
package org.bresearch.websec.parse.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Natural language sentence found.
 * 
 * @author BerlinBrown
 *
 */
public class Sentence {

    public static final String START_TOKEN = "ASTARTBB";       
    public static final String END_TOKEN = "XXENDY";
    public static final String SPACE = "MMSPACENN"; 
    
    private List<NLPWord> sentence = new ArrayList<NLPWord>();

    public String toString() {
        return this.toNLPForm();
    }
    
    public String toNLPForm() {
        
        int i = 0;
        final StringBuilder builder = new StringBuilder(40);
        for (NLPWord word : sentence) {            
            builder.append(word.getNlpShortDesc()).append(' ');
            i++;
            // only display 10 words
            if (i > 10) {
                break;
            }
        } // End of the for //        
        return builder.toString();
    }
    
    public String toWordForm() {       
        int i = 0;
        final StringBuilder builder = new StringBuilder(40);
        for (NLPWord word : sentence) {            
            builder.append(word.getTerm()).append(' ');
            i++;
            // only display 10 words
            if (i > 10) {
                break;
            }
        } // End of the for //        
        return builder.toString();
    }
    
    public String toDistinctFullWordForm() {               
        final StringBuilder builder = new StringBuilder(40);
        for (NLPWord word : sentence) {
            builder.append(START_TOKEN);
            builder.append(word.getNlpShortDesc());
            builder.append(END_TOKEN);
            builder.append(SPACE);            
        } // End of the for //        
        return builder.toString();
    }
    
    public boolean hasOnlyOneTerm(final String sent) {
        final int len1 = sent.length();
        final String res = sent.replaceAll(SPACE, "");
        final int len2 = res.length();        
        return ((len1 - len2) == 9);        
    }
    
    public void addWord(final NLPWord word) {
        if (word != null) {
            this.sentence.add(word);
        } // End of the if //
    }

    /**
     * @return the sentence
     */
    public List<NLPWord> getSentence() {
        return sentence;
    }

    /**
     * @param sentence the sentence to set
     */
    public void setSentence(List<NLPWord> sentence) {
        this.sentence = sentence;
    }
    
} // End of the Class //
