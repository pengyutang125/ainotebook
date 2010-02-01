/**
 *
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

public class NLPWord {

    private String term = "";
    private String nlpShortDesc = "";
    private String nlpLongDesc = "";
    
    private final String termGroup;
    private final NLPCommonTags tags;
    
    public NLPWord(final NLPCommonTags tags, final String termGroup) {
        
        this.termGroup = termGroup;
        this.tags = tags;
    }
    
    public String toString() {
        return "{NLPWord: term=" + this.term + " , nlp=" + this.nlpShortDesc + "}";
    }
    
    public String toReport() {
        return "{NLPWord: term=" + this.term + " , nlp=" + this.nlpShortDesc + " nlplong=" + this.nlpLongDesc + "}";
    }
    
    public void parse() {
     
        final String [] group = this.termGroup.split("/");
        if (group.length == 2) {
            this.term = group[0];
            this.nlpShortDesc = group[1];
            final NLPWordTag wordTag = tags.getTag(this.nlpShortDesc);
            if (wordTag != null) {
                this.nlpLongDesc = wordTag.toString();
            } // End of the if //
            
        } // End of the if //     
    }
    
    /**
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @return the nlpShortDesc
     */
    public String getNlpShortDesc() {
        return nlpShortDesc;
    }

    /**
     * @return the nlpLongDesc
     */
    public String getNlpLongDesc() {
        return nlpLongDesc;
    }          
        
} // End of the class //
