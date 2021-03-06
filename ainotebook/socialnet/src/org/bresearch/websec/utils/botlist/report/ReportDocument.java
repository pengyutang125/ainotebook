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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.bresearch.websec.utils.botlist.BotlistStringUtils;
import org.bresearch.websec.utils.botlist.IBotlistDocument;
import org.bresearch.websec.utils.botlist.stats.DocumentWordStats;

import com.google.inject.Inject;

public class ReportDocument implements IReport {
    
    private final IBotlistDocument document;
    private BotlistStringUtils stringUtils = new BotlistStringUtils();
    private boolean stopWords; 
    private int wordListSize = 0;
    private int documentSize = 0;    
    private int numTopWords = 20;
    
    private boolean displayDocument = false;
    
    @Inject
    public ReportDocument(final IBotlistDocument document, final boolean stopWords) {
        this.document = document;
        this.stopWords = stopWords;
    }
    
    public ReportStatsDocument toReportDocument() {
                
        final DescriptiveStatistics stats = this.toReportStats(this.stringUtils);
        final DescriptiveStatistics wordSizeStats = this.toReportStatsWordSize(this.stringUtils);        
        final ReportDocument noStopWordsReport = this.build(false);
        final DescriptiveStatistics noStopWordsStats = noStopWordsReport.toReportStats(this.stringUtils);
        final DescriptiveStatistics noStopWordsWordSizeStats = noStopWordsReport.toReportStatsWordSize(this.stringUtils);                
        final Set<Map.Entry<String, Integer>> topWords = this.topWords(this.getNumTopWords(), true, this.stringUtils);
       
        final ReportStatsDocument statsReport = new ReportStatsDocument(
                this.getDocumentSize(),
                this.isStopWords(),
                stats,
                wordSizeStats,
                noStopWordsReport,
                noStopWordsStats,
                noStopWordsWordSizeStats,
                topWords);
        return statsReport;
        
    }
    
    public String toReport() {
        return toReportDocument().toReport();
    }
    
    /**
     * Build a new report document.
     * 
     * @return
     */
    public ReportDocument build(final boolean hasStopWords) {
        final ReportDocument newReport = new ReportDocument(this.document, hasStopWords);       
        return newReport;        
    }
    
    public DescriptiveStatistics toReportStats(final BotlistStringUtils utils) {
        
        final String formatDoc = this.document.formatDocument();
        this.documentSize = formatDoc.length();
        final List<String> listterms = utils.buildWordList(formatDoc);
        this.wordListSize = listterms.size();
        
        // Return a mapreduce entry map
        final Set<Map.Entry<String, Integer>> set = this.stopWords ? utils.mapReduceWithStopWords(listterms, -1) : utils.mapReduce(listterms, -1);        
        final DescriptiveStatistics stats = new DescriptiveStatistics();
        final Double [] arr = utils.mapReduceCount(set, -1);        
        for (int i = 0; i < arr.length; i++) {
            stats.addValue(arr[i]);
        } // End of the For //
        return stats;
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
    
    public DescriptiveStatistics toReportStatsWordSize(final BotlistStringUtils utils) {
        
        final List<String> listterms = utils.buildWordList(this.document.formatDocument());
        this.wordListSize = listterms.size();
                       
        final Set<Map.Entry<String, Integer>> set = this.stopWords ? utils.mapReduceWithStopWords(listterms, -1) : utils.mapReduce(listterms, -1);
        final Double [] wordSizes = utils.mapReduceWordSize(set, -1);
        final DocumentWordStats wordStats = new DocumentWordStats("");
        return wordStats.stats(wordSizes);
    }
    
    /**
     * @return the stopWords
     */
    public boolean isStopWords() {
        return stopWords;
    }

    /**
     * @param stopWords the stopWords to set
     */
    public void setStopWords(boolean stopWords) {
        this.stopWords = stopWords;
    }

    /**
     * @return the wordListSize
     */
    public int getWordListSize() {
        return wordListSize;
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
     * @return the documentSize
     */
    public int getDocumentSize() {
        return documentSize;
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
     * @param displayDocument the displayDocument to set
     */
    public void setDisplayDocument(boolean displayDocument) {
        this.displayDocument = displayDocument;
    }
            
} // End of the Class //
