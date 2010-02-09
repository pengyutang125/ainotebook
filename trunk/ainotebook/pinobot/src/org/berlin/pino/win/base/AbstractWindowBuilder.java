package org.berlin.pino.win.base;

import org.berlin.seesaw.app.ITeeterWindowBuilder;

public abstract class AbstractWindowBuilder implements ITeeterWindowBuilder {

    private final IBasicWindow basicWindow;
    
    public AbstractWindowBuilder(final IBasicWindow basicWindow) {
        this.basicWindow = basicWindow;
    }
            
    public abstract IBasicWindow build();

    /**
     * @return the basicWindow
     */
    public IBasicWindow getBasicWindow() {
        return this.basicWindow;
    }
    
} // End of the Class //
