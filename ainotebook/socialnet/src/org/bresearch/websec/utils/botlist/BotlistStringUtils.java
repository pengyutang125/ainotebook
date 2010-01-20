/* 
 * BotListGenericUtils.java
 * Nov 16, 2007
 */
package org.bresearch.websec.utils.botlist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author bbrown
 */
public class BotlistStringUtils {

    /*
**************     
Example Test Case for map reduce usage:
**************
<pre>
   public static Set mapReduceLinkKeywords(final BotListEntityLinksDAO dao) {
   
        List allterms = new ArrayList();
        List list = readLinksForDay(dao);
        for (Iterator it = list.iterator(); it.hasNext();) {
            BotListEntityLinks link = (BotListEntityLinks) it.next();
            final String keywords = link.getKeywords();
            if (keywords != null) {
                final String[] words = keywords.split(" ");
                if (words != null) {
                    for (int i = 0; i < words.length; i++) {                        
                        // Also check for stop words
                        if ((BotListGenericUtils.STOP_WORDS_MAP.get(words[i]) == null) 
                            && (words[i].length() > 1)) {
                            allterms.add(words[i]);
                        } // End of if - stop word check
                    }
                } // End of if - words null
            } // End of if
        } // End of For 
        return BotListGenericUtils.mapReduce(allterms, 8);
        
    }
</pre>
-------------     
     
     */
    
	public static final String STOP_WORDS [] = {
		"to",
		"the",
		"in",
		"of",
		"am",
		"add",
		"is",
		"for",
		"a",
		"on",
		"by",
		"for",
		"us",
		"we",
		"be",
		"going",
		"way",
		"from",
		"cool",
		"sometimes",
		"too",
		"man",
		"bad",
		"and",
		"new",
		"i",
		"with",
		"it",
		"all",
		"up",
		"at",
		"over",
		"says",
		"more",
		"2007",
		"your",
		"no",
		"call",
		"race"
	};
	public static final Map STOP_WORDS_MAP;
	static {
		STOP_WORDS_MAP = new HashMap();
		for (int ix = 0; ix < STOP_WORDS.length; ix++) {
			STOP_WORDS_MAP.put(STOP_WORDS[ix], "0");
		}
	}
	
	/** 
	 * inner class to sort map. 
	 */
	private static class ValueComparator implements Comparator {
	    
		private Map data = null;
		public ValueComparator(Map _data) {
			super();
			this.data = _data;
		}
		public int compare(Object o1, Object o2) {
			Integer e1;
			Integer e2;
			e1 = (Integer) this.data.get(o1);
			e2 = (Integer) this.data.get(o2);			
			int res = e1.compareTo(e2);			
			return -(res == 0 ? 1 : res);
		}
	}
	
	public Map sortMapByValue(Map inputMap) {
		SortedMap sortedMap = new TreeMap(new BotlistStringUtils.ValueComparator(inputMap));		
		sortedMap.putAll(inputMap);		
		return sortedMap;
	}

	public List<String> buildWordList(final String document) {
	    
	    if (document == null) {
	        // Return empty list.
	        return new ArrayList<String>();
	    }
	    final String [] words = document.split("\\s");	     	 
	    final List<String> list = new ArrayList<String>();
	    for (String word : words) {
	        list.add(word);
	    } // End of the For //
	    return list;
	}
	
	/**
	 * Return a list of key value (instances of map) pairs.
	 * 
	 * @param inputMap
	 * @return
	 */
	public Set keyValueSet(final Map inputMap, final int maxnum) {
	    
		Set set = inputMap.entrySet();
		Set newset = new LinkedHashSet();		
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext(); i++) {
			newset.add(it.next());
			if ((i >= 0) && i >= (maxnum - 1)) {
			    break;
			} // End of the If //
		} // End of the for //
		
		return newset;
	}
	
	/**
	 * Simple Map Reduce; given a list of keywords, map the terms to a count of how
	 * many times the term occurs in the list.
	 *  
	 * @param allterms
	 * @return
	 */
	public Set mapReduce(final List<String> allterms, final int maxnum) {
	    
		Map map = new HashMap();
		for (Iterator<String> x2it = allterms.iterator(); x2it.hasNext();) {
		    
			final String term = (String) x2it.next();
			if (term.length() == 0) {
			    continue;
			}
			Integer ct = (Integer) map.get(term);
			if (ct == null) {
				map.put(term, new Integer(1));
			} else {
				map.put(term, new Integer(ct.intValue() + 1));
			} // End of if - else
			
		} // End of the for
		
		Map sortedMap = this.sortMapByValue(map);	
		return this.keyValueSet(sortedMap, maxnum);
	}
	
} // End of the Class //
