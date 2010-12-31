/**
 * Copyright (c) 2006-2011 Berlin Brown.  All Rights Reserved
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
 * Home Page: http://code.google.com/u/berlin.brown/
 * 
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */
package org.bresearch.websec.net;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConnectResult {

    private final int responseCode;
    private final String htmlData;
    private final List<Header> headerData = new LinkedList<Header>();
    private final Map<String, Header> headerDataMap = new LinkedHashMap<String, Header>();
    
    public ConnectResult(final int responseCode, final String data) {
        
        this.responseCode = responseCode;
        this.htmlData = data;
        
    }
    
    public String toString() {
        
        return "ConnectResult:ResponseCode:" + this.responseCode;
        
    }
       
    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @return the htmlData
     */
    public String getHtmlData() {
        return htmlData;
    }
    
    public void addHeader(final String headerKey, final String headerValue, final List<String> list) {
        final Header header = new Header(headerKey, headerValue, list);
        this.headerData.add(header);
        this.headerDataMap.put(headerKey.toLowerCase().trim(), header);
    }
    
    public Header getHeader(final String key) {
        return this.headerDataMap.get(key);
    }
    
    public String buildHeaderResponse() {
        
        final StringBuilder buf = new StringBuilder();
        for (Header header : this.headerData) {
            buf.append(header);
            buf.append('\n');
        } // End of the For //
        return buf.toString();
    }
   
    /////////////////////////////////////////////////////////////////
       
    public static final class Header {
    
        private final String headerKey;
        private final String headerVal;
        private final List<String> headerList;
        
        public Header(final String hdr, final String val, final List<String> headerList) {
            this.headerKey = hdr;
            this.headerVal = val;
            this.headerList = headerList;
        }

        public String toString() {
            return this.headerKey + ": " + this.headerVal;
        }

        /**
         * @return the headerKey
         */
        public String getHeaderKey() {
            return headerKey;
        }


        /**
         * @return the headerVal
         */
        public String getHeaderVal() {
            return headerVal;
        }
        
    } // End of the Header //
    
} // End of the Class //
