package org.berlin.pino.win;

import javax.swing.JPanel;

import org.berlin.seesaw.swing.ITeeterButton;
import org.berlin.seesaw.swing.ITeeterPanel;
import org.berlin.seesaw.swing.ITeeterTextArea;
import org.berlin.seesaw.swing.gl.ITeeterGLCanvas;
import org.berlin.seesaw.swing.layout.ITeeterLayout;

/**
 * 
 * @author NNN 
 *
 */
public class BasicWindow implements IBasicWindow {

    private ITeeterLayout   layout;
    
    private ITeeterPanel    windowPanel;
    private ITeeterGLCanvas glCanvas;  
    private ITeeterTextArea chatTextArea;   
    private ITeeterTextArea inputTextArea;
    
    private ITeeterPanel    buttonPanel;
    private ITeeterButton   buttonEnter;    
    private ITeeterButton   buttonClear;    
    private ITeeterButton   buttonExit;
    
    ///////////////////////////////////////////////////////
    
    public String toString() {
        return String.format("#{Basic Window: %s}", this.windowPanel);
    }
    
    /**
     * @param windowPanel the windowPanel to set
     * @see org.berlin.pino.win.IBasicWindow#setWindowPanel(ITeeterPanel)
     */
    public void setWindowPanel(final ITeeterPanel windowPanel) {
        this.windowPanel = windowPanel;
    }
    /**
     * @param glCanvas the glCanvas to set
     * @see org.berlin.pino.win.IBasicWindow#setGLCanvas(ITeeterGLCanvas)
     */
    public void setGLCanvas(final ITeeterGLCanvas glCanvas) {
        this.glCanvas = glCanvas;
    }
    /**
     * @param chatTextArea the chatTextArea to set
     * @see org.berlin.pino.win.IBasicWindow#setChatTextArea(ITeeterTextArea)
     */
    public void setChatTextArea(final ITeeterTextArea chatTextArea) {
        this.chatTextArea = chatTextArea;
    }
    /**
     * @param inputTextArea the inputTextArea to set
     * @see org.berlin.pino.win.IBasicWindow#setInputTextArea(ITeeterTextArea)
     */
    public void setInputTextArea(final ITeeterTextArea inputTextArea) {
        this.inputTextArea = inputTextArea;
    }
    /**
     * @param buttonPanel the buttonPanel to set
     * @see org.berlin.pino.win.IBasicWindow#setButtonPanel(ITeeterPanel)
     */
    public void setButtonPanel(final ITeeterPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }
    /**
     * @param buttonEnter the buttonEnter to set
     * @see org.berlin.pino.win.IBasicWindow#setButtonEnter(ITeeterButton)
     */
    public void setButtonEnter(final ITeeterButton buttonEnter) {
        this.buttonEnter = buttonEnter;
    }
    /**
     * @param buttonClear the buttonClear to set
     * @see org.berlin.pino.win.IBasicWindow#setButtonClear(ITeeterButton)
     */
    public void setButtonClear(final ITeeterButton buttonClear) {
        this.buttonClear = buttonClear;
    }
    /**
     * @param buttonExit the buttonExit to set
     * @see org.berlin.pino.win.IBasicWindow#setButtonExit(ITeeterButton)
     */
    public void setButtonExit(final ITeeterButton buttonExit) {
        this.buttonExit = buttonExit;
    }
    public JPanel getComponent() {
        return this.windowPanel.getComponent();
    }

    /**
     * @param layout the layout to set
     */
    public void setLayout(ITeeterLayout layout) {
        this.layout = layout;
    }
        
    
} // End of the Class //
