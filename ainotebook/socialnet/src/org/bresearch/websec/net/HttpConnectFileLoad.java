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
package org.bresearch.websec.net;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bresearch.websec.net.ConnectResult.Header;
import org.bresearch.websec.net.conf.URLLoader;

public class HttpConnectFileLoad {
   
    private static final String DATA_KEY = "__HTTP_DATA__"; 
    
    private final URLLoader loader;
    private final List<String> targetHeaders;       
    
    /**
     * Return a Map of Maps.
     */
    private Map<URL, Map<String, String>> results = new LinkedHashMap<URL, Map<String, String>>();
    
    public HttpConnectFileLoad(final URLLoader loader, final List<String> headers) {
        
        this.loader = loader;        
        this.targetHeaders = headers;
    }

    public void connect() {
                    
        if (this.loader == null) {           
            throw new IllegalStateException("Invalid URLLoader (is null)");
        }        

        // Each url should return a set of headers //
        
        final List<URL> list = loader.getUrlList();
        for (URL url : list) {
            
            final String ustr = url.toString();                                    
            final ConnectSettingsBean settings = new ConnectSettingsBean(ustr);
            final IHttpConnect connection = new HttpConnect(settings, null);
            connection.buildConnectProperties();
            connection.connect(connection.buildURL());
            final ConnectResult result = connection.getLastResult();
        
            // Iterate over our target headers
            final Map<String, String> mapHeaderRes = new LinkedHashMap<String, String>();
            for (String theader : this.targetHeaders) {
                final Header header = result.getHeader(theader);
                if (header != null) {
                    mapHeaderRes.put(header.getHeaderKey(), header.getHeaderVal());                    
                } // End of the if //
                
            } // End of the For //
            
            mapHeaderRes.put(DATA_KEY, result.getHtmlData());
            // After map created, add            
            // The key is the URL object.
            results.put(url, mapHeaderRes);            
            
        } // End of the for //
                
    }

    /**
     * @return the results
     */
    public Map<URL, Map<String, String>> getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(Map<URL, Map<String, String>> results) {
        this.results = results;
    }

    /**
     * @return the targetHeaders
     */
    public List<String> getTargetHeaders() {
        return targetHeaders;
    }
    
    /**
     * Use the results to format the header results to a string. 
     * 
     * @return
     */
    public String prettyFormatHeaders() {
                      
        final StringBuilder buf = new StringBuilder(100);
                
        for (Map.Entry<URL, Map<String, String>> entry : results.entrySet()) {
            final URL u = entry.getKey();
            final Map<String, String> mHeaders = entry.getValue();
                                   
            // Then iterate over the headers
            for (Map.Entry<String, String> entryHeader : mHeaders.entrySet()) {
                
                final String header = entryHeader.getKey();
                final String val = entryHeader.getValue();
                if (!DATA_KEY.equalsIgnoreCase(header)) {
                    buf.append(u).append(": ");
                    buf.append(header).append(": ");                
                    buf.append(val).append('\n');
                } // End of the if //
                
            } // End of inner for //
            
        } // End of the for //
        
        return buf.toString();
    }
   
} // End of the Class //
