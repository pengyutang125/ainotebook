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

import java.util.Map;
import java.util.Stack;


/**
 * Default google challenge AI bot.  Search for a valid mode.
 * This bot can also be used for checking valid moves and scoring them.
 * 
 * @author BerlinBrown
 *
 */
public class AIBotMinMax extends GLBot {
       
    public AIBotMinMax(final ITronBoard basicBoard) {
        
        super(basicBoard);                      
        this.setVerbose(false);
    }       
    
    /////////////////////////////////////////////////////////////////
    
    public boolean makeLogicMoveAIValidateScores(final Map<Double, Move> scoreMap) {
        
        if (scoreMap == null) {
            return false;
        }            
        // Get the first entry        
        // Select the first valid case
        for (int i = 0; i < 3; i++) {
            final boolean res = makeLogicMoveAIValidateScore(scoreMap.entrySet().iterator().next());
            if (res) {
                return true;
            }
        }
        return false;
    }
    
    public boolean makeLogicMoveAIValidateScore(final Map.Entry<Double, Move> highestMoveEntry) {
        
        if (highestMoveEntry == null) {
            return false;
        }
        final Move highestMove = highestMoveEntry.getValue();
        if (highestMoveEntry.getKey().doubleValue() <= 1.0) {
            this.addMessages("-1000-AI: using default move, invalid score");            
            return false;            
        } // End of the if - else //
        
        // We have our high value move, validate it.
        final Stack<Move> stackMoves = (Stack<Move>) this.getMoves().getMoves();
        if (!this.validateMove(stackMoves, highestMove)) {
            this.addMessages("-2000-AI: using default move, invalid move - attempt=" + highestMove);            
            return false;
        }
        this.addMessages("+5000-AI: valid move");
        this.makeMove(highestMove);
        return true;
    }
    
    /**
     * Test the default move, use to fall back on.
     * 
     * @return
     */
    @Override
    public void makeLogicMove() {

        this.printThoughts();
        this.printMessages();
        
        // If dead, don't make a move //
        if (this.isDead()) {
            return;
        } 
        
        if (this.getVerbose()) {
            System.out.println("Making logic move for aibotscorer - " + this.getMoves().size());
        }
                        
        final Move lastMove = this.getLastMoveNull();
        if (lastMove == null) {
            this.addMessages("-3000-AI: using default move, last move null");
            // Revert back to the default move
            super.makeLogicMove();
            return;
        }
                        
        final FunctionalScoreAllMoves functionalScore = new FunctionalScoreAllMoves(100, this.getBoard(), 
                lastMove.getX(), lastMove.getY(), ITronBoard.PLAYER1);
        functionalScore.setVerbose(this.getVerbose());
        
        final Map<Double, Move> scoreMap = functionalScore.scoreAll();
        if (this.getVerbose()) {
            System.out.println("Logic Move Score " + scoreMap);
            this.addMessages("+6000-AI: last score set = " + scoreMap);
        }
        
        final boolean validScoreCheck = this.makeLogicMoveAIValidateScores(scoreMap);
        if (!validScoreCheck) {
            // If on the valid case, the move has already been made            
            super.makeLogicMove();
            return;
        } // End of if //
    }
       
        
} // End of the Class //
