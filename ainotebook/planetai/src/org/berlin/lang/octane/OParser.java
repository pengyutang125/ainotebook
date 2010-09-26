/**
 * Copyright (c) 2006-2010 Berlin Brown and botnode.com  All Rights Reserved
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
 * OParser.java
 * Sep 24, 2010
 * bbrown
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */
package org.berlin.lang.octane;

import java.util.Stack;

import org.berlin.lang.octane.sys.OMathFunctions;
import org.berlin.lang.octane.sys.jsys.OJavaSystem;
import org.berlin.lang.octane.type.ONumber;
import org.berlin.lang.octane.type.OType;
import org.berlin.lang.octane.type.TypeConstants;

/**
 * @author bbrown
 *
 */
public class OParser {

    private final Stack<OType> dataStack = new Stack<OType>();
            
    /**
     * 
     * @param tokenStack
     */
    public void parse(final Stack<OType> tokenStack) {
        
        while (!tokenStack.empty()) {
            
            final OType token = tokenStack.pop();
            if (token.getType() == TypeConstants.NUMBER) {
                dataStack.push(token);
            } else if (token.getType() == TypeConstants.STRING) {
                dataStack.push(token);
            } else if (token.getType() == TypeConstants.WORD) {
                
                System.out.println("PARSE FUNCTION");
                final OMathFunctions math = new OMathFunctions(dataStack, token);
                final OJavaSystem javasys = new OJavaSystem(dataStack, token);
                
                math.registerOps();
                math.execute(token);
                
                javasys.registerCalls();
                javasys.execute(token);
            }
        } // End of the while //
        
        for (final OType token : dataStack) {
            System.out.println("$[data-stack-token]" + token);
        } // End of the for //
        
    }    
    
} // End of the Class
