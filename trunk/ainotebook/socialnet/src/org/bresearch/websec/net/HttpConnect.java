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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpConnect implements IHttpConnect {

    private final ConnectSettingsBean connectSettings;
    private final SystemSettingsBean systemSettings;
    private ConnectResult lastResult = new ConnectResult(-1, ""); 
    
    public HttpConnect(final ConnectSettingsBean settings, final SystemSettingsBean sysSettings) {
        
        this.connectSettings = settings;
        this.systemSettings = sysSettings;        

    }
    
    public void connect(final URLConnectAdapter urlAdapter) {
        
        if (this.connectSettings == null) {
            throw new IllegalStateException("Invalid Connect Settings (is null)");
        }
        
        final HttpURLConnection httpConnection = (HttpURLConnection) urlAdapter.openConnection();        
        BufferedReader in;
        try {
            
            in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));        
            final StringBuilder buf = new StringBuilder(200);
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
                buf.append('\n');
            } // End of the While //
                                    
            final ConnectResult result = new ConnectResult(httpConnection.getResponseCode(), buf.toString());
            
            // Build the header response information //
            final Map<String, List<String>> headerFields = httpConnection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                
                final String key = entry.getKey();
                final List<String> val = entry.getValue();
                if ((val != null) && (val.size() > 1)) {
                    System.out.println("WARN: Invalid header value : " + key + " url=" + this.connectSettings.getUrl());
                }
                if (key != null) {                    
                    result.addHeader(key, val.get(0), val);                                       
                } else {
                    result.addHeader("Status", val.get(0), val);                                       
                }
                
            } // End of the For //            
            this.lastResult = result;
            
        } catch (IOException e) {
            
            throw new ConnectException(e);
            
        } // End of the try - catch //        
    }

    /**
     * @return the connectSettings
     */
    public ConnectSettingsBean getConnectSettings() {
        return connectSettings;
    }

    /**
     * @return the lastResult
     */
    public ConnectResult getLastResult() {
        return lastResult;
    }
    
    public URLConnectAdapter buildURL() {
        
        if (this.connectSettings == null) {
            throw new IllegalStateException("Invalid Connect Settings (is null)");
        }               
        // Build the adapter with the new URL object
        // and our URL string value.
        URLConnectAdapter adapter;
        try {
            adapter = new URLConnectAdapter(new URL(this.connectSettings.getUrl()));
        } catch (MalformedURLException e) {
            throw new ConnectException(e);
        }
        return adapter;
    }
        
    public void buildConnectProperties() {
        
        // Set the system properties //
        if ((this.systemSettings != null) && this.systemSettings.getProxySet()) {
            System.getProperties().put("http.agent", "" + this.systemSettings.getHttpAgent());
            System.getProperties().put("proxySet",   "" + this.systemSettings.getProxySet());
            System.getProperties().put("proxyHost",  "" + this.systemSettings.getProxyHost());
            System.getProperties().put("proxyPort",  "" + this.systemSettings.getProxySet());
        } // End of the if //
    }
            
} // End of the Class //
