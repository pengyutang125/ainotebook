package org.bresearch.websec.parse.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class NLPCommonTags {

    private Map<String, NLPWordTag> tagData = new LinkedHashMap<String, NLPWordTag>();

    public void addTagData(final NLPWordTag tag) {
        this.tagData.put(tag.getKeyword(), tag);
    }
    
    public NLPWordTag getTag(final String tagKey) {
        return this.tagData.get(tagKey);
    }
    
    public static class Builder {
        public NLPCommonTags build() {
            
            final NLPCommonTags tags = new NLPCommonTags();
                        
            tags.addTagData(new NLPWordTag("ABL",  "determiner/pronoun"    ,"quite such rather"));
            tags.addTagData(new NLPWordTag("AT",   "article"               ,"the an no a"));
            tags.addTagData(new NLPWordTag("NN",   "noun, singular, common","failure burden court fire"));
            tags.addTagData(new NLPWordTag("UH",   "interjection"          ,"Hurrah bang"));
            tags.addTagData(new NLPWordTag("VB",   "verb, base"            ,"find"));
            tags.addTagData(new NLPWordTag("JJ",   "adjective"             ,"recent over-all"));
            tags.addTagData(new NLPWordTag("VBZ",  "verb, present tense, 3rd person singular","deserves believes"));
            tags.addTagData(new NLPWordTag("CC",   "conjuction"            ,"and"));
            tags.addTagData(new NLPWordTag("PRP$", "possessive pronoun"    ,"my his"));
            tags.addTagData(new NLPWordTag("DT" ,  "determiner"            ,"the"));
            tags.addTagData(new NLPWordTag("CD" ,  "cardinal number"       ,"1 2 3"));
            tags.addTagData(new NLPWordTag("EX" ,  "existential there"     ,"there is"));
            tags.addTagData(new NLPWordTag("JJR",  "adjective, comparative","greener"));
            tags.addTagData(new NLPWordTag("JJS",  "adjective, superlative","greenest"));
            tags.addTagData(new NLPWordTag("MD",   "modal"                 ,"could, will"));
            tags.addTagData(new NLPWordTag("RP" ,  "particle"              ,"give up"));
            tags.addTagData(new NLPWordTag("NNS",  "noun plural"           ,"tables"));                       
            tags.addTagData(new NLPWordTag("NNP",  "proper noun, singular" ,"John"));
            tags.addTagData(new NLPWordTag("VB",   "verb, base form"       ,"take"));
            tags.addTagData(new NLPWordTag("NNPS", "proper noun, plural"   ,"Vikings"));                       
            tags.addTagData(new NLPWordTag("VBD",  "verb, past tense"      ,"took"));
            tags.addTagData(new NLPWordTag("PDT",  "predeterminer"         ,"both the boys"));                 
            tags.addTagData(new NLPWordTag("VBG",  "verb, gerund/present participle"    ,"taking"));
            tags.addTagData(new NLPWordTag("POS",  "possessive ending"     ,"friend's"));                     
            tags.addTagData(new NLPWordTag("VBN",  "verb, past participle" ,"taken"));
            tags.addTagData(new NLPWordTag("PRP",  "personal pronoun"      ,"I, he, it"));                     
            tags.addTagData(new NLPWordTag("VBP",  "verb, sing. present, non-3d"        ,"take"));
            tags.addTagData(new NLPWordTag("RB" ,  "adverb"        ,"however, usually, naturally, here, good"));     
            tags.addTagData(new NLPWordTag("WDT",  "wh-determiner"         ,"which"));
            tags.addTagData(new NLPWordTag("RBR",  "adverb, comparative"   ,"better"));
            tags.addTagData(new NLPWordTag("WP",   "wh-pronoun"            ,"who, what" ));
            tags.addTagData(new NLPWordTag("RBS",  "adverb, superlative"   ,"best" ));
            tags.addTagData(new NLPWordTag("WP$",  "possessive wh-pronoun" ,"whose" ));
            tags.addTagData(new NLPWordTag("WRB",  "wh-abverb"             ,"where, when" ));
            return tags;
        }
    } // End of the Class //
    
        
}
