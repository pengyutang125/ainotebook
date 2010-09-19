/**
 * Berlin Brown
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
        return Character.isWhitespace(ch) || ch == ',';
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
                    System.out.println("WHITESPACE [push space]");
                    charStack.push((int) ' ');
                }
                
                if (ch == -1) {
                    
                    if (eofIsError) {
                        throw new Exception("EOF while reading");
                    }
                    return eofValue;
                    
                } else if (Character.isDigit(ch)) {
                    
                    System.out.println("NUMBER FOUND == " + Character.digit(ch, 10));
                    charStack.push(Character.digit(ch, 10));
                    
                } else if (ch == '+' || ch == '-') {
                    // Handle +/- for number and integer
                    System.out.println("PLUS MINUS FOUND");
                    final int ch2 = r.read();
                    if (Character.isDigit(ch2)) {
                        unread(r, ch2);                        
                        System.out.println("NUMBER FOUND == [2] " + ch2);
                    } else {
                        unread(r, ch2);
                    } // End of if - else //
                    
                    charStack.push(ch);
                } // End of if, check plus minus            

            } // End of the for //

        } catch (Exception e) {

            final LineNumberingPushbackReader rdr = (LineNumberingPushbackReader) r;
            throw new ReaderException(rdr.getLineNumber(), e);

        } // End of the try - catch //
    }

    /**
     * 
     * @param reader
     * @param path
     * @param filename
     */
    public void load(final Reader reader, final String path, final String filename) throws Exception {

        final Object EOF = new Object();
        final LineNumberingPushbackReader pushbackReader = (reader instanceof LineNumberingPushbackReader) ? (LineNumberingPushbackReader) reader
                : new LineNumberingPushbackReader(reader);
        
        final Stack<Integer> charStack = new Stack<Integer>();
        this.read(pushbackReader, charStack, false, EOF);          
        System.out.println(">>> Parsing: Reading Stack");
        
        Collections.reverse(charStack);        
        final Stack<Integer> inputStack = new Stack<Integer>();
        while(!charStack.isEmpty()) {
            
            final int chartok = charStack.pop();                        
            System.out.println("POPPING VALUE: " + chartok);            
            if ('+' == chartok) {
                System.out.println("PLUS FOUND!!!");
                int sum = 0;
                for (final Integer inputval : inputStack) {
                    sum += inputval;
                } // End of for
                System.out.println("PUSH RETURN ON STACK ==" + sum);
                charStack.push(sum);
                inputStack.clear();
            } else if (' ' == chartok) {
                System.out.println("WHITESPACE FOUND");
            } else {
                inputStack.push(chartok);
            }
            
        } // End of for //
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
