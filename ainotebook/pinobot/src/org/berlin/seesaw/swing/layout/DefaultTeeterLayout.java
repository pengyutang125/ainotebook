/**
 * Copyright (c) 2006-2010 Berlin Brown and botnode.com/Berlin Research  All Rights Reserved
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
 * Date: 2/15/2010 
 *   
 * Home Page: http://botnode.com/
 * 
 * Description: Seesaw (Teeter Totter) Swing Framework
 * 
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */
package org.berlin.seesaw.swing.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class DefaultTeeterLayout implements ITeeterLayout {

    private GridBagLayout layout = new GridBagLayout();   
    private GridBagConstraints constraints = new GridBagConstraints();
    
    public void defaultSettings() {        

        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridy = 0;
        constraints.gridx = 0;
        // Specifies how to distribute extra horizontal space.
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE;
        
    }
    
    /**
     * Move the position right.
     */
    public GridBagConstraints moveRight() {
        constraints.gridx = constraints.gridx + 1;
        return constraints;
    }
    
    /**
     * @param layout the layout to set
     */
    public void setLayout(final GridBagLayout layout) {
        this.layout = layout;
    }

    /**
     * @return the layout
     */
    public GridBagLayout getLayout() {
        return layout;
    }

    /**
     * @return the constraints
     */
    public GridBagConstraints getConstraints() {
        return constraints;
    }

    /**
     * @param constraints the constraints to set
     */
    public void setConstraints(final GridBagConstraints constraints) {
        this.constraints = constraints;
    } 
    
} // End of the Class //
