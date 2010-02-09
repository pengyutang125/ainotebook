package org.berlin.pino.win;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.berlin.seesaw.swing.BaseWorker;
import org.berlin.seesaw.swing.ITeeterButton;
import org.berlin.seesaw.swing.ITeeterEventWorker;
import org.berlin.seesaw.swing.ITeeterPanel;
import org.berlin.seesaw.swing.TeeterButton;
import org.berlin.seesaw.swing.layout.DefaultTeeterLayout;
import org.berlin.seesaw.swing.layout.ITeeterLayout;

/**
 */
public class PinoWindowBuilder extends AbstractWindowBuilder {
     
    /**
     * Constructor for PinoWindowBuilder.
     * @param basicWindow IBasicWindow
     */
    public PinoWindowBuilder(final IBasicWindow basicWindow) {
        super(basicWindow);
    }
    
    /**
     * Method createEnterButton.
     * @return ITeeterButton
     */
    public ITeeterButton createEnterButton() {
        
        final ITeeterEventWorker eventWorker = new BaseWorker() {
            public void execute() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Tested - Enter!");
            }
        };        
        final ITeeterButton button = new TeeterButton(new JButton("Enter"), eventWorker);
        button.addEventHandler();
        return button;
    }
    
    /**
     * Method createClearButton.
     * @return ITeeterButton
     */
    public ITeeterButton createClearButton() {
        
        final ITeeterEventWorker eventWorker = new BaseWorker() {
            public void execute() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Tested - Clear!");
            }
        };        
        final ITeeterButton button = new TeeterButton(new JButton("Clear"), eventWorker);
        button.addEventHandler();
        return button;
    }
    
    /**
     * Method createExitButton.
     * @return ITeeterButton
     */
    public ITeeterButton createExitButton() {
        
        final ITeeterEventWorker eventWorker = new BaseWorker() {
            public void execute() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Tested - Exit!");
            }
        };        
        final ITeeterButton button = new TeeterButton(new JButton("Exit"), eventWorker);
        button.addEventHandler();
        return button;
    }

    
    public PinoWindowBuilder withButtonPanel() {
        
        final ITeeterButton enterButton = this.createEnterButton();
        final ITeeterButton clearButton = this.createClearButton();
        final ITeeterButton exitButton = this.createExitButton();
        
        final ITeeterLayout layout = new DefaultTeeterLayout();
        layout.defaultSettings();
        
        final JPanel swingPanel = new JPanel(layout.getLayout());
        final ITeeterPanel panel = new CommandButtonPanel(swingPanel, layout, enterButton, clearButton, exitButton);
        panel.constructView();
        
        // Set the components on the window //
        this.getBasicWindow().setButtonEnter(enterButton);
        this.getBasicWindow().setButtonClear(clearButton);
        this.getBasicWindow().setButtonExit(exitButton);
        this.getBasicWindow().setButtonPanel(panel);
        
        return this;
    }
    
    public PinoWindowBuilder withMainPanel() {
              
        this.withButtonPanel();
        
        // Create a new panel 
        // with the default layout
        final ITeeterLayout layout = new DefaultTeeterLayout();
        layout.defaultSettings();
        
        final JPanel swingPanel = new JPanel(layout.getLayout());
        final ITeeterPanel panel = new CommandButtonPanel(swingPanel, layout, enterButton, clearButton, exitButton);
      
        return this;
    }
    
    /**
     * Method build.
     * @return IBasicWindow
     * @see org.berlin.seesaw.app.ITeeterWindowBuilder#build()
     */
    @Override
    public IBasicWindow build() {
        this.withMainPanel();
        return this.getBasicWindow();
    }        

} // End of the Class //
