package org.berlin.pino.test.test;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.berlin.seesaw.app.ITeeterWindow;
import org.berlin.seesaw.app.TeeterWindow;

public class BuildFrameTest extends TestCase {

    public void test1() {
        
        final ITeeterWindow window = new TeeterWindow();
    }
    
    public static void main(String [] args) {
        
        TestRunner.run(BuildFrameTest.class);
    }
    
} // End of the Class //
