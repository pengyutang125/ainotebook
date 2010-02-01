package org.bresearch.websec.parse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bresearch.websec.parse.model.Sentence;

public class NLPDocumentStore {
    
    private final List<Sentence> sentences;
    private Map<String, List<Sentence>> documentStore = new LinkedHashMap<String, List<Sentence>>();
    private int minCommonSentReport = -1;
    
    public NLPDocumentStore(final List<Sentence> sentences) {
        this.sentences = sentences;
    }
    
    public String buildDocument() {
        
        StringBuilder builder = new StringBuilder(100);
        for (Sentence sent : this.sentences) {
            
            final boolean hasOneTerm = sent.hasOnlyOneTerm(sent.toDistinctFullWordForm());
            if (!hasOneTerm) {
                final String distinctForm = sent.toDistinctFullWordForm();
                this.addSentenceStore(distinctForm, sent);
                builder.append(distinctForm);
                builder.append(' ');
            }
        } // End of the for //
        return builder.toString();
    }
    
    public void addSentenceStore(final String nlpTerms, final Sentence sent) {
        
        // First check if the data exists for this term
        final List<Sentence> sents = this.documentStore.get(nlpTerms);
        if (sents == null) {
            
            // Create a new list //
            final List<Sentence> newListSents = new ArrayList<Sentence>();
            newListSents.add(sent);
            this.documentStore.put(nlpTerms, newListSents);                                   
        } else {            
            // Just add the sentence
            sents.add(sent);            
        } // End of the if - else //
    }

    /**
     * @return the documentStore
     */
    public Map<String, List<Sentence>> getDocumentStore() {
        return documentStore;
    }

    /**
     * @param documentStore the documentStore to set
     */
    public void setDocumentStore(Map<String, List<Sentence>> documentStore) {
        this.documentStore = documentStore;
    }
    
    public String cleanNLPSentence(final String sent) {
        
        String str = sent.replaceAll(Sentence.START_TOKEN, "");
        str = str.replaceAll(Sentence.END_TOKEN, "");
        str = str.replaceAll(Sentence.SPACE, " ");
        return str.trim();
        
    }
    
    public void printStore() {
        
        System.out.println("+ <Document Store> :");
        for (Map.Entry<String, List<Sentence>> storeEntry : this.documentStore.entrySet()) {            
            final String formatttedNLPSent = storeEntry.getKey();
            final List<Sentence> valSent = storeEntry.getValue();
            
            if (this.minCommonSentReport >= 2 && (valSent.size() < this.minCommonSentReport)) {
                // Continue with our min common sent threshold //
                continue;
            }
            
            final String flagDupl = (valSent.size() >= 2) ? "***" : "";
            System.out.println(this.cleanNLPSentence(formatttedNLPSent) + " ---> [" + valSent.size()+ flagDupl +  "]");
            for (Sentence printSent : valSent) {
                System.out.println("    " + printSent.toFullWordForm());
            }
            System.out.println();
        } // End of the for //
        System.out.println("<End of Document Store>");
    }

    /**
     * @return the minCommonSentReport
     */
    public int getMinCommonSentReport() {
        return minCommonSentReport;
    }

    /**
     * @param minCommonSentReport the minCommonSentReport to set
     */
    public void setMinCommonSentReport(int minCommonSentReport) {
        this.minCommonSentReport = minCommonSentReport;
    }
    
} // End of the Class //
