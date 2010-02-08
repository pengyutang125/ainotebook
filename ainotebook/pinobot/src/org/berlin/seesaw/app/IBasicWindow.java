package org.berlin.seesaw.app;

import org.berlin.seesaw.swing.ITeeterPanel;
import org.berlin.seesaw.swing.gl.ITeeterGLCanvas;

public interface IBasicWindow extends ITeeterWindow {

    public void setWindowPanel(final ITeeterPanel panel);

    public void setGLCanvas(final ITeeterGLCanvas glCanvas);
    
    public void setChatTextArea(final ITeeterTextArea chatTextArea);
    
    public void setInputTextArea(final ITeeterTextArea inputTextArea);
    
    public void setButtonPanel(final ITeeterButton buttonPanel);
    
    public void setButtonEnter(final ButtonEnter);
    
    public void setButtonClear(final ButtonClear);
    
    public void setButtonExit(final ButtonExit);
    
} // End of the Interface //
