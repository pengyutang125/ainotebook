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
package org.bresearch.websec.parse;

import java.io.IOException;

import opennlp.tools.lang.english.PosTagger;
import opennlp.tools.postag.POSDictionary;

import org.bresearch.websec.parse.model.NLPDocument;
import org.bresearch.websec.utils.botlist.IBotlistDocument;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ParseDocumentModule extends AbstractModule {

    private final String document;
    private final String modelFilePath;
    private final String dictFilePath;
    
    public ParseDocumentModule(final String docString, final String modelPath, final String dictPath) {
        this.document = docString;
        this.modelFilePath = modelPath;
        this.dictFilePath = dictPath;
    }
    
    @Override
    protected void configure() {     
                
    }
    
    @Provides
    IBotlistDocument provideDocument() {
        final IBotlistDocument doc = new NLPDocument(this.document);        
        return doc;
    }
    
    @Provides
    PosTagger provideTagger() throws IOException {
        
        /*
         * final String modelPath = "../socialnet/models/tag.bin.gz";
         * final String dictFile = "../socialnet/models/tagdict";
         */
        
        final POSDictionary dict = new POSDictionary(this.dictFilePath, true);
        final PosTagger tagger = new PosTagger(this.modelFilePath, dict);
        return tagger;
    }
        
} // End of the Class //
