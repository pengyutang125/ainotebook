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
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class ReportStatsDocument implements IReport {

    public static final char NL = '\n';
    
    private static final int LEN_LABEL = 35;
    private static final int LEN_VAL   = 18;   
    private static final String LABELS [] = {
        "N distinct words",        
        "Mean Word Count Freq",
        "Mean Word Size",
        "N Characters (word count)",        
        "Word Count Standard Dev",
        "Word Size Standard Dev",
        "Top Words",
        "Document Size",
        "Sum Total Words",
        "Percent Unique Words",
    };
        
    private final DescriptiveStatistics stats;
    private final DescriptiveStatistics wordSizeStats;
    private final ReportDocument noStopWordsReport;
    private final DescriptiveStatistics noStopWordsStats;
    private final DescriptiveStatistics noStopWordsWordSizeStats;
    private final Set<Map.Entry<String, Integer>> topWords;
    
    private final boolean stopWords;
    private final int documentSize;    
    private boolean displayDocument = false;
    
    public ReportStatsDocument(
                    final int documentSize,
                    final boolean stopWords,
                    final DescriptiveStatistics stats,
                    final DescriptiveStatistics wordSizeStats,
                    final ReportDocument noStopWordsReport,
                    final DescriptiveStatistics noStopWordsStats,
                    final DescriptiveStatistics noStopWordsWordSizeStats,
                    final Set<Map.Entry<String, Integer>> topWords) {
        
        this.documentSize = documentSize;
        this.stopWords = stopWords;
        this.stats = stats;
        this.wordSizeStats = wordSizeStats; 
        this.noStopWordsReport = noStopWordsReport;
        this.noStopWordsStats = noStopWordsStats;
        this.noStopWordsWordSizeStats = noStopWordsWordSizeStats;
        this.topWords = topWords;
        
    }
    
    public String toReport() {
        
        final StringBuilder builder = new StringBuilder(40);
        ///////////////////////////////////////////////////
        
        builder.append("Report Document - " + (new Date()) + " - document-size=" + this.getDocumentSize());                
        builder.append(NL);                 
        builder.append(field("" + this.documentSize, 7, LEN_LABEL, LEN_VAL, ' '));
        // Next Section Includes Stop Words
        builder.append(noStopWordsReport.isStopWords() ? "<(1) Common Stop Words Removed!>" : "<(1) No Stop Word Removal>").append(NL);
        final double percentUnique = noStopWordsStats.getN() / noStopWordsStats.getSum(); 
        builder.append(field("" + noStopWordsStats.getN(), 0, LEN_LABEL, LEN_VAL, ' '));
        builder.append(field("" + noStopWordsStats.getSum(), 8, LEN_LABEL, LEN_VAL, ' '));
        builder.append(field("" + percentUnique, 9, LEN_LABEL, LEN_VAL, ' '));
        builder.append(field("" + noStopWordsWordSizeStats.getMean(), 2, LEN_LABEL, LEN_VAL, ' '));
        builder.append(field("" + noStopWordsWordSizeStats.getSum(), 3, LEN_LABEL, LEN_VAL, ' '));
        
        // Next Section Includes Stop Words
        builder.append(this.stopWords ? "<(2) Common Stop Words Removed!>" : "<(2) No Stop Word Removal>").append(NL);
        builder.append(field("" + stats.getN(), 0, LEN_LABEL, LEN_VAL, ' '));        
        builder.append(field("" + stats.getSum(), 8, LEN_LABEL, LEN_VAL, ' '));
        builder.append(field("" + stats.getMean(), 1, LEN_LABEL, LEN_VAL, ' '));
        builder.append(field("" + stats.getStandardDeviation(), 4, LEN_LABEL, LEN_VAL, ' '));        
        builder.append(field("" + wordSizeStats.getMean(), 2, LEN_LABEL, LEN_VAL, ' '));
        builder.append(field("" + wordSizeStats.getSum(), 3, LEN_LABEL, LEN_VAL, ' '));
        builder.append(field("" + wordSizeStats.getStandardDeviation(), 5, LEN_LABEL, LEN_VAL, ' '));
        
        // Add the top words:
        builder.append(spaceFill(LABELS[6] + ':', LEN_LABEL, ' '));        
        builder.append(topWords).append('\n');
        builder.append("<End of Report>");
        builder.append(NL);
        return builder.toString();
        
    }
      
    
    /**
     * Left justified space fill.
     * 
     * @param data
     * @param len
     * @return
     */
    public String spaceFill(final Object dataObj, final int len, final char spaceChar) {
        if (dataObj == null) {
            return "";
        }
        final String data = dataObj.toString();        
        if (data.length() < len) {
            
            // Fill a buffer with empty spaces //
            final StringBuilder spaces = new StringBuilder(len + 2);
            spaces.append(data);
            final int spacesLeft = len - data.length();
            for (int i = 0; i < spacesLeft; i++) {
                spaces.append(spaceChar);
            }
            return spaces.toString();
        } else {
            return data;
        } // End of the if - else //
    }
    
    
    public String field(String data, int labelId, int len, int len2, char spaceChar) {
        final StringBuilder builder = new StringBuilder(40);
        
        builder.append(spaceFill(LABELS[labelId] + ':', len, spaceChar));
        builder.append('[');
        builder.append(spaceFill(data, len2, spaceChar));
        builder.append(']');
        builder.append(NL);
        return builder.toString();
    }
    
    /**
     * @return the stopWords
     */
    public boolean isStopWords() {
        return stopWords;
    }

    /**
     * @return the documentSize
     */
    public int getDocumentSize() {
        return documentSize;
    }

    /**
     * @return the stats
     */
    public DescriptiveStatistics getStats() {
        return stats;
    }

    /**
     * @return the noStopWordsReport
     */
    public ReportDocument getNoStopWordsReport() {
        return noStopWordsReport;
    }

    /**
     * @return the noStopWordsStats
     */
    public DescriptiveStatistics getNoStopWordsStats() {
        return noStopWordsStats;
    }

    /**
     * @return the noStopWordsWordSizeStats
     */
    public DescriptiveStatistics getNoStopWordsWordSizeStats() {
        return noStopWordsWordSizeStats;
    }

    /**
     * @return the topWords
     */
    public Set<Map.Entry<String, Integer>> getTopWords() {
        return topWords;
    }

    /**
     * @param displayDocument the displayDocument to set
     */
    public void setDisplayDocument(boolean displayDocument) {
        this.displayDocument = displayDocument;
    }
        
} // End of the class //
