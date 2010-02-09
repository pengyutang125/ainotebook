package org.berlin.pino.test.functional.teeter;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.berlin.seesaw.swing.BaseWorker;
import org.berlin.seesaw.swing.ITeeterButton;
import org.berlin.seesaw.swing.ITeeterEventWorker;
import org.berlin.seesaw.swing.TeeterButton;

public class TeeterPanelTest {

    public static ITeeterButton createEnterButton() {
        
        final ITeeterEventWorker eventWorker = new BaseWorker() {
            public void execute() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Tested - Enter!");
            }
        };        
        return new TeeterButton(new JButton("Enter"), eventWorker);
    }
    
    public static ITeeterButton createExitButton() {
        
        final ITeeterEventWorker eventWorker = new BaseWorker() {
            public void execute() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Tested - Exit!");
            }
        };        
        return new TeeterButton(new JButton("Exit"), eventWorker);                
    }
    
    public static ITeeterButton createClearButton() {
        
        final ITeeterEventWorker eventWorker = new BaseWorker() {
            public void execute() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Tested - Clear!");
            }
        };        
        return new TeeterButton(new JButton("Clear"), eventWorker);
    }
    
    public static ITeeterPanel buildPanel() {
        final 
    }
    
    public static void main(String [] args) {
        
        ///////////////
        final JFrame frame = new JFrame("Hello World!");               
        frame.add(buildMyComponent());
        frame.setSize(300, 300);
        frame.setLocation(400, 400);
        frame.setBackground(Color.white);                
        
        ///////////////////////////////
        frame.addWindowListener(new WindowAdapter() {            
            public void windowClosing(WindowEvent e) {
                System.out.println("Exiting...");
                System.exit(0);
            }            
        });

        frame.setVisible(true);        
    }
    
} // End of the Class //
