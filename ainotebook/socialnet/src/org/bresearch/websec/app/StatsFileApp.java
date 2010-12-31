/**
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
package org.bresearch.websec.app;

import java.io.File;

import org.bresearch.websec.utils.FileUtil;
import org.bresearch.websec.utils.botlist.report.IReport;
import org.bresearch.websec.utils.botlist.report.ReportDocument;
import org.bresearch.websec.utils.botlist.report.ReportModule;
import org.bresearch.websec.utils.botlist.report.ReportTermsDocument;
import org.bresearch.websec.utils.botlist.report.ReportTermsModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Analyze file.
 * 
 * @author bbrown
 *
 */
public class StatsFileApp {

    public static void main(String [] args) throws Exception {
    	
    	if (args.length != 1) {
    		System.out.println("usage: StatsFileApp <input document>");
    		throw new RuntimeException("Invalid arguments, please provide the input text file to process.  usage: StatsFileApp <input document>");
    	}
    	
        System.out.println("Loading file, " + args[0]);     
        FileUtil util = new FileUtil();
        // Example usage: String data = util.readLinesRaw(new File("./misc/internet/troll1.txt"));        
        String data = util.readLinesRaw(new File(args[0]));
        final Injector injector = Guice.createInjector(new ReportModule(data, true));             
        final IReport report = injector.getInstance(ReportDocument.class);
        
        final Injector injectorTerms = Guice.createInjector(new ReportTermsModule(data));
        final ReportTermsDocument reportTerms = injectorTerms.getInstance(ReportTermsDocument.class);        
        reportTerms.setNumTopWords(2000);        
        
        System.out.println();
        System.out.println(report.toReport());    
        System.out.println(reportTerms.toReport());
        System.out.println("Done");
    }
    
} // End of the Class //
