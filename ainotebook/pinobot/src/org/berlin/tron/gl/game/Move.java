/**
 * Copyright (c) 2006-2007 Berlin Brown and botnode.com/Berlin Research  All Rights Reserved
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
 * Date: 12/15/2009 
 *   
 * Home Page: http://botnode.com/
 * 
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 * 
 * Simple Java OpenGL
 */
package org.berlin.tron.gl.game;

/**
 * Basic X,Y Move.
 * 
 * @author BerlinBrown
 *
 */
public class Move implements IMove {

    private final int x;
    private final int y;
    private final long moveTimeMs;    
    private String direction = "";
    
    public Move(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.moveTimeMs = System.currentTimeMillis();
    }
    
    public String toString() {
        return "${Move x=" + this.x + " y=" + this.y +  "[" + this.moveTimeMs + "]}";
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
    
    public Move incx() {
        direction = EAST;
        return new Move(this.x + 1, this.y);
    }
    public Move incy() {
        direction = SOUTH;
        return new Move(this.x, this.y + 1);
    }
    
    public Move decx() {
        direction = WEST;
        return new Move(this.x - 1, this.y);
    }
    public Move decy() {
        direction = NORTH;
        return new Move(this.x, this.y - 1);        
    }
    
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Move)) {
            return false;
        }
        final Move m = (Move) obj;
        if ((m.getX() == this.x) && (m.getY() == this.y)) {
            return true;            
        }
        return false;                
    }
    
    /**
     * Create hashcode when this object is used as
     * a key with map to void collisions.
     */ 
    public int hashCode() {
        
        int hash = 1;
        hash = hash * 31 + this.toString().hashCode();
        hash = hash * 31 + ("" + this.x).hashCode();                      
        return hash;
    }

    /**
     * @return the moveTimeMs
     */
    public long getMoveTimeMs() {
        return moveTimeMs;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
} // End of the Class //
