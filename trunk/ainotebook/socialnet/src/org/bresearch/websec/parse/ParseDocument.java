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
package org.bresearch.websec.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.lang.english.PosTagger;

import org.bresearch.websec.parse.model.NLPCommonTags;
import org.bresearch.websec.parse.model.NLPWord;
import org.bresearch.websec.parse.model.Sentence;
import org.bresearch.websec.utils.StringUtil;
import org.bresearch.websec.utils.botlist.IBotlistDocument;
import org.bresearch.websec.utils.botlist.text.WordProcessor;

import com.google.inject.Inject;

public class ParseDocument implements IParseDocument {

    private NLPCommonTags tags = (new NLPCommonTags.Builder()).build();
    private IBotlistDocument document;
    private List<Sentence> sentenceList = new ArrayList<Sentence>();
    private String sentPattern = "([A-Z][^\\.?!]+[\\.\\?!])";
    
    private final PosTagger tagger;
    
    @Inject
    public ParseDocument(final IBotlistDocument document, final PosTagger tagger) {
        this.document = document;
        this.tagger = tagger;
    }
    
    public List<Sentence> parse() {
        
        final String formattedDoc = this.document.formatDocument();        
        final Pattern pattern = Pattern.compile(this.getSentPattern(), Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(formattedDoc);
        
        while(matcher.find()) {            
            this.addSentence(this.parseSentence(matcher.group()));
        } // End of the While //
        
        return this.sentenceList;        
    }
    
    public Sentence parseSentence(final String sent) {
        
        final StringUtil util = new StringUtil();
        if (util.isEmpty(sent)) {
            return null;
        }
        
        final String cleanSent = this.formatLine(sent);
        final String tagAll = this.tagger.tag(cleanSent);
        final String words [] = tagAll.split("\\s+");
        
        final Sentence newSent = new Sentence();        
        for (String wordGroup : words) {
            
            final NLPWord word = new NLPWord(this.tags, wordGroup);
            word.parse();
            newSent.addWord(word);
                        
        } // End of the for //
        
        /*
        System.out.println(newSent);        
        System.out.println(newSent.toDistinctFullWordForm());
         */
        
        return newSent;
    }
        
    
    public String formatLine(final String line) {
        return (new WordProcessor()).filterOnlyAlphaNumeric(line);
        
    }
    
    public void addSentence(final Sentence sentence) {
        if (sentence != null) {
            this.sentenceList.add(sentence);
        } // End of the if //
    }

    /**
     * @return the sentPattern
     */
    public String getSentPattern() {
        return sentPattern;
    }

    /**
     * @param sentPattern the sentPattern to set
     */
    public void setSentPattern(String sentPattern) {
        this.sentPattern = sentPattern;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(NLPCommonTags tags) {
        this.tags = tags;
    }

    /**
     * @return the sentenceList
     */
    public List<Sentence> getSentenceList() {
        return sentenceList;
    }

    /**
     * @param sentenceList the sentenceList to set
     */
    public void setSentenceList(List<Sentence> sentenceList) {
        this.sentenceList = sentenceList;
    }
    
} // End of the Class //
