package org.berlin.pino.win;

import org.berlin.seesaw.app.ITeeterWindow;
import org.berlin.seesaw.swing.ITeeterButton;
import org.berlin.seesaw.swing.ITeeterPanel;
import org.berlin.seesaw.swing.ITeeterTextArea;
import org.berlin.seesaw.swing.gl.ITeeterGLCanvas;

public interface IBasicWindow extends ITeeterWindow {

    public void setWindowPanel(final ITeeterPanel panel);

    public void setGLCanvas(final ITeeterGLCanvas glCanvas);
    
    public void setChatTextArea(final ITeeterTextArea chatTextArea);
    
    public void setInputTextArea(final ITeeterTextArea inputTextArea);
    
    public void setButtonPanel(final ITeeterPanel buttonPanel);
    
    public void setButtonEnter(final ITeeterButton buttonEnter);
    
    public void setButtonClear(final ITeeterButton buttonClear);
    
    public void setButtonExit(final ITeeterButton buttonExit);
    
} // End of the Interface //
