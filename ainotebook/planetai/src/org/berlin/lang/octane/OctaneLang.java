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
 * Test.java
 * Sep 19, 2010
 * bbrown
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */
package org.berlin.lang.octane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Stack;

import org.berlin.lang.octane.type.ONumber;
import org.berlin.lang.octane.type.OString;
import org.berlin.lang.octane.type.OType;
import org.berlin.lang.octane.type.OWord;
import org.berlin.lang.octane.type.TypeConstants;

/**
 * 
 * @author bbrown
 * 
 */
public class OctaneLang {

    /**
     * 
     * @author bbrown
     * 
     */
    public static class ReaderException extends Exception {

        /**
         * Serial Version id. 
         */
        private static final long serialVersionUID = 1L;
        
        final int line;

        public ReaderException(int line, Throwable cause) {
            super(cause);
            this.line = line;
        }
    }

    /**
     * 
     * @param ch
     * @return
     */
    public static boolean isWhitespace(int ch) {
        return Character.isWhitespace(ch) || ch == ',' || ch == '{' || ch == '}';
    }

    /**
     * 
     * @param r
     * @param ch
     * @throws IOException
     */
    public static void unread(final PushbackReader r, int ch) throws IOException {
        if (ch != -1) {
            r.unread(ch);
        }
    }
    
    /**
     * 
     * @param r
     * @param eofIsError
     * @param eofValue
     * @return
     * @throws Exception
     */
    public Object read(final PushbackReader r, final Stack<Integer> charStack, final boolean eofIsError, final Object eofValue) throws Exception {
        
        try {
            for (;;) {

                int ch = r.read();
                boolean hasWhiteSpace = false;
                while (isWhitespace(ch)) {
                    ch = r.read();
                    hasWhiteSpace = true;
                } // End of the while //
                
                if (hasWhiteSpace) {
                    charStack.push((int) TypeConstants.WHITESPACE);
                }
                
                if (TypeConstants.COMMENT_START == ch) {                                        
                    // Loop until next end comment                    
                    while((TypeConstants.COMMENT_END != ch) && (ch != TypeConstants.END)) {
                        ch = r.read();                        
                    } // End of while //                                        
                }
                
                if (ch == TypeConstants.END) {                    
                    if (eofIsError) {
                        throw new Exception("EOF while reading");
                    }
                    return eofValue;
                    
                } else if (Character.isLetterOrDigit(ch)) {  
                    
                    charStack.push(ch);
                    
                } else if ((ch == TypeConstants.POINT) || (ch == TypeConstants.DOUBLE_QUOTE) || (ch == '_')) {                   
                    charStack.push(ch);                               
                } else {
                    System.out.println("INVALID INPUT");
                } // End of the if - else //
                
            } // End of the for //

        } catch (Exception e) {

            final LineNumberingPushbackReader rdr = (LineNumberingPushbackReader) r;
            throw new ReaderException(rdr.getLineNumber(), e);

        } // End of the try - catch //
    }

    /**
     * Whitespace found.
     */
    public void readOnWhitespace(final Stack<Integer> charStack, final Stack<Integer> inputStack, final Stack<Integer> doubleRightStack, final Stack<OType> tokenStack) {
                
        boolean hasLetter = false;                
        for (final int ci : inputStack) {                    
            if ((ci != TypeConstants.POINT) && (!Character.isDigit(ci))) {
                hasLetter = true;
            } 
        } // End of the for //
        final boolean isNum = !hasLetter;                
        if (isNum) {
                final StringBuilder buf = new StringBuilder(inputStack.size());
                for (final int rc : inputStack) {
                    buf.append(String.valueOf(Character.digit(rc, 10)));
                }
                if (buf.length() != 0) {
                    if (doubleRightStack.size() != 0) {
                        buf.append(TypeConstants.POINT);
                        for (final int rc : doubleRightStack) {                                                       
                            buf.append(String.valueOf(Character.digit(rc, 10)));
                        } // End of the for //
                    } // End of the if - right
                    
                    final double num = Double.valueOf(buf.toString());                    
                    tokenStack.add(new ONumber(num));
                } // End of if //
        } else {
            
            final StringBuilder buf = new StringBuilder(inputStack.size());
            for (final int rc : inputStack) {
                buf.append((char) rc);
            }
            if (buf.length() != 0) {
                tokenStack.add(new OWord(buf.toString()));
            }
                                
        } // End of the if - else //
        
        // Clear the right value        
        inputStack.clear(); 
        doubleRightStack.clear();        
        
    }
            
    /**
     * 
     * @param pushbackReader
     */
    public void readCharStack(final LineNumberingPushbackReader pushbackReader) throws Exception {
        
        final Object EOF = new Object();
        final Stack<Integer> charStack = new Stack<Integer>();
        final Stack<Integer> inputStack = new Stack<Integer>();
        final Stack<Integer> doubleRightStack = new Stack<Integer>();        
        final Stack<OType>   tokenStack = new Stack<OType>();
        
        this.read(pushbackReader, charStack, false, EOF);          
        System.out.println(">>> Parsing: Reading Stack");
        
        Collections.reverse(charStack);        
        
        boolean hasRightVal = false;
        while(!charStack.isEmpty()) {
            
            final int chartok = charStack.pop();                  
            
            if (TypeConstants.WHITESPACE == chartok) {
                
                readOnWhitespace(charStack, inputStack, doubleRightStack, tokenStack);
                hasRightVal = false;
                
            } else if (TypeConstants.DOUBLE_QUOTE == chartok) {
                
                final StringBuilder buf = new StringBuilder();
                // Loop until next double quote is found
                int tokforstr = -1;
                while((TypeConstants.DOUBLE_QUOTE != tokforstr) && !charStack.isEmpty()) {                    
                    tokforstr = charStack.pop();
                    if (TypeConstants.DOUBLE_QUOTE != tokforstr) {
                        buf.append((char) tokforstr);
                    }
                } // End of while //
                
                tokenStack.push(new OString(buf.toString()));                        
                                
            } else if (Character.isDigit(chartok)) {
                if (hasRightVal) {                    
                    doubleRightStack.push(chartok);
                } else {
                    inputStack.push(chartok);                    
                }                                
            } else if (Character.isLetter(chartok) || '_' == chartok) {
                
                inputStack.push(chartok);
                
            } else if (TypeConstants.POINT == chartok) {
                hasRightVal = true;
                doubleRightStack.clear();
            } else {                                               
                System.out.println(String.format("<OTHER CHARACTER  %s>", chartok));
            } // End of the if - else //
            
        } // End of for //
        
        System.out.println(">>>>>>>>>>>> Printing Tokens in TOKEN STACK <<");
        Collections.reverse(tokenStack);
        // Copy the token stack stack to the history stack
        final Stack<OType> historyTokenStack = (Stack<OType>) tokenStack.clone();        
        
        for (final OType token : tokenStack) {
            System.out.println("$[token]" + token);
        } // End of the for //
                
        System.out.println(">>>>>>>>>>>> Parsing Tokens");
        final OParser parser = new OParser();
        parser.parse(tokenStack);
        
    }
    
    /**    
     * 
     * @param reader
     * @param path
     * @param filename
     */
    public void load(final Reader reader, final String path, final String filename) throws Exception {
        
        final LineNumberingPushbackReader pushbackReader = (reader instanceof LineNumberingPushbackReader) ? (LineNumberingPushbackReader) reader
                : new LineNumberingPushbackReader(reader);
        this.readCharStack(pushbackReader);
        
    }

    /**
     * 
     * @param filename
     * @throws Exception
     */
    public void loadFile(final String filename) throws Exception {

        final FileInputStream f = new FileInputStream(filename);
        try {
            final File file = new File(filename);
            load(new InputStreamReader(f, "UTF-8"), file.getAbsolutePath(), file.getName());

        } finally {
            f.close();
        }
    }
    
    /**
     * 
     * @param args
     * @throws Exception
     */
    public static final void main(final String[] args) throws Exception {
        
        System.out.println(">>Running Octane Lang");
        final OctaneLang lang = new OctaneLang();
        lang.loadFile("test1.oct");
        
    }

} // End of the Class //
