package org.berlin.pino.win;

import org.berlin.seesaw.swing.ITeeterButton;
import org.berlin.seesaw.swing.ITeeterPanel;
import org.berlin.seesaw.swing.ITeeterTextArea;
import org.berlin.seesaw.swing.gl.ITeeterGLCanvas;

public class BasicWindow implements IBasicWindow {

    private ITeeterPanel    windowPanel;
    private ITeeterGLCanvas glCanvas;  
    private ITeeterTextArea chatTextArea;   
    private ITeeterTextArea inputTextArea;    
    private ITeeterPanel    buttonPanel;
    private ITeeterButton   buttonEnter;    
    private ITeeterButton   buttonClear;    
    private ITeeterButton   buttonExit;

    private BasicWindow(final Builder builder) {
        
        this.windowPanel   = builder.windowPanel;
        this.glCanvas      = builder.glCanvas;
        this.chatTextArea  = builder.chatTextArea;
        this.inputTextArea = builder.inputTextArea;
        this.buttonPanel   = builder.buttonPanel;
        this.buttonEnter   = builder.buttonEnter;
        this.buttonClear   = builder.buttonClear;
        this.buttonExit    = builder.buttonExit;

    }
    
    /**
     * @param windowPanel the windowPanel to set
     */
    public void setWindowPanel(final ITeeterPanel windowPanel) {
        this.windowPanel = windowPanel;
    }
    /**
     * @param glCanvas the glCanvas to set
     */
    public void setGLCanvas(final ITeeterGLCanvas glCanvas) {
        this.glCanvas = glCanvas;
    }
    /**
     * @param chatTextArea the chatTextArea to set
     */
    public void setChatTextArea(final ITeeterTextArea chatTextArea) {
        this.chatTextArea = chatTextArea;
    }
    /**
     * @param inputTextArea the inputTextArea to set
     */
    public void setInputTextArea(final ITeeterTextArea inputTextArea) {
        this.inputTextArea = inputTextArea;
    }
    /**
     * @param buttonPanel the buttonPanel to set
     */
    public void setButtonPanel(final ITeeterPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }
    /**
     * @param buttonEnter the buttonEnter to set
     */
    public void setButtonEnter(final ITeeterButton buttonEnter) {
        this.buttonEnter = buttonEnter;
    }
    /**
     * @param buttonClear the buttonClear to set
     */
    public void setButtonClear(final ITeeterButton buttonClear) {
        this.buttonClear = buttonClear;
    }
    /**
     * @param buttonExit the buttonExit to set
     */
    public void setButtonExit(final ITeeterButton buttonExit) {
        this.buttonExit = buttonExit;
    }
    
    /**
     * Builder class.
     *
     */
    public static class Builder {
        
        private ITeeterPanel windowPanel;
        private ITeeterGLCanvas glCanvas;  
        private ITeeterTextArea chatTextArea;   
        private ITeeterTextArea inputTextArea;
        
        private ITeeterPanel buttonPanel;
        private ITeeterButton buttonEnter;    
        private ITeeterButton buttonClear;    
        private ITeeterButton buttonExit;
        
        public IBasicWindow build() {
            
            // Transfer the data from this builder 
            // to the newly created object.
            return new BasicWindow(this);
            
        }
                
        /**
         * Create the panel and the buttons.
         * 
         * @return
         */
        public Builder withButtons(final ITeeterPanel buttonPanel, final ITeeterButton buttonEnter, 
                            final ITeeterButton buttonClear, final ITeeterButton buttonExit) {
            
            this.buttonPanel = buttonPanel;
            this.buttonEnter = buttonEnter;
            this.buttonClear = buttonClear;
            this.buttonExit  = buttonExit;
            return this;
        }
        
    } // End of the Class //
    
} // End of the Class //
