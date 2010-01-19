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
package org.bresearch.websec.net.conf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bresearch.websec.utils.FileUtil;

public class URLLoader {
    
    private final String fileListPath;
    private List<URL> urlList = new ArrayList<URL>();
    private String data = "";   
    private Throwable lastError = null;
    
    public URLLoader(final String fileList) {
        this.fileListPath = fileList;
    }
    
    /**
     * Build a list of URLs.
     */
    public void loadFile() {
        
        final FileUtil util = new FileUtil();                
        try {
            final List<String> lines = util.readLinesList(new File(this.fileListPath), true);            
            if (lines != null) {
                for (String strURL : lines) {
                    urlList.add(new URL(strURL));
                } // End of the For //                
            } // End of the if //
            
        } catch (IOException e) {
            this.lastError = e;
            e.printStackTrace();
        } // End of the try - catch //
                       
    }

    /**
     * @return the fileListPath
     */
    public String getFileListPath() {
        return fileListPath;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @return the lastError
     */
    public Throwable getLastError() {
        return lastError;
    }

    /**
     * @return the urlList
     */
    public List<URL> getUrlList() {
        return urlList;
    }

    /**
     * @param urlList the urlList to set
     */
    public void setUrlList(List<URL> urlList) {
        this.urlList = urlList;
    }    
    
} // End of the Class //
