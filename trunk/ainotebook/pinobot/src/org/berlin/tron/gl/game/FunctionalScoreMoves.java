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

import java.util.ArrayList;
import java.util.List;

/**
 * @author BerlinBrown
 *
 */
public class FunctionalScoreMoves implements GameWidget {

    public static final int MAX_DEPTH = 10;

    private final ITronBoard board;
    private final int x; 
    private final int y;
    private final byte type;
    private final int depth;
    
    private boolean verbose = false;
    private int totalOperations = 0;
    private int maxOperations = -1;
    
    private int maxDistX = 4;
    private int maxDistY = 5;
    
    private List<Move> prevMoveList = new ArrayList<Move>();
    
    // Check if any path is marked invalid.
    private boolean invalidNorth = false;
    private boolean invalidSouth = false;
    private boolean invalidEast = false;
    private boolean invalidWest = false;
    
    public FunctionalScoreMoves(final int depth, final ITronBoard board, final int x, final int y, final byte type) {       
        this.board = board;
        this.x = x;
        this.y = y;        
        this.type = type;
        this.depth = depth;
    }
    
    public double scoreMoves(final int curDepth, final double lastScore, final int moveX, final int moveY) {               
        
        totalOperations++;
        if (this.getVerbose()) {
            System.out.println("FunctionalScoreMoves - op=" + totalOperations + " depth=" + curDepth + " score=" + lastScore + " x=" + moveX + " y=" + moveY);
        }
        
        if (!this.board.basicValidateBounds(this.type, moveX, moveY)) {         
            return lastScore;
        }
        
        /////////////////////////////////////////
        if (!this.board.basicValidateMove(this.type, moveX, moveY)) {
            return lastScore;
        }
                        
        // Quit on max depth
        if (curDepth >= this.depth) {            
            return lastScore;
        }
        
        if ((this.maxOperations > 0) && (this.totalOperations > this.maxOperations)) {
            return lastScore;
        }
        
        final Move funcMoveObj = new Move(moveX, moveY);
        if (prevMoveList.contains(funcMoveObj)) {            
            return lastScore;
        }
        
        // Used to check prev move.
        prevMoveList.add(new Move(moveX, moveY));
        
        final int distX = Math.abs(this.x - moveX);
        final int distY = Math.abs(this.y - moveY);
        
        if ((distX > this.getMaxDistX()) || (distY > this.getMaxDistY())) {            
            return lastScore;
        }
        
        final DirectionModel around = new DirectionModel(moveX, moveY);        
        final ScoreMoves basicScoreMoves = new ScoreMoves(board, moveX, moveY, this.type);
        final double theScoreCurPosResult = basicScoreMoves.scoreEmptyMovesFull();        

        ///////////////////////////////
        double sumPointsScores = 0.0;        
        final List<Move> myPointsList = new ArrayList<Move>();
        myPointsList.add(around.getNorth());        
        myPointsList.add(around.getSouth());        
        myPointsList.add(around.getEast());        
        myPointsList.add(around.getWest());               
        for (Move forNextPoint : myPointsList) {
            sumPointsScores += scoreMoves(curDepth + 1, (lastScore + theScoreCurPosResult), forNextPoint.getX(), forNextPoint.getY());
        }               
        return sumPointsScores;
    }
    
    public double scoreMoves() {
        return scoreMoves(0, 0.0, this.x, this.y);
    }
    
    /**
     * @return the verbose
     */
    public boolean getVerbose() {
        //return verbose;
        return false;
    }

    /**
     * @param verbose the verbose to set
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * @return the totalOperations
     */
    public int getTotalOperations() {
        return totalOperations;
    }

    /**
     * @return the maxOperations
     */
    public int getMaxOperations() {
        return maxOperations;
    }

    /**
     * @param maxOperations the maxOperations to set
     */
    public void setMaxOperations(int maxOperations) {
        this.maxOperations = maxOperations;
    }

    /**
     * @return the maxDistX
     */
    public int getMaxDistX() {
        return maxDistX;
    }

    /**
     * @param maxDistX the maxDistX to set
     */
    public void setMaxDistX(int maxDistX) {
        this.maxDistX = maxDistX;
    }

    /**
     * @return the maxDistY
     */
    public int getMaxDistY() {
        return maxDistY;
    }

    /**
     * @param maxDistY the maxDistY to set
     */
    public void setMaxDistY(int maxDistY) {
        this.maxDistY = maxDistY;
    }
    
    
} // End of the Class //
