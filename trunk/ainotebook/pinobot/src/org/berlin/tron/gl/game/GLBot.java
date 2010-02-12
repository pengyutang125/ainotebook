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

import java.util.Random;
import java.util.Stack;

public class GLBot implements IBot {

    private final ITronBoard board;
    private IBotMoves moves = new BotMoves();    
    private Random random = new Random(System.currentTimeMillis()); 
    
    private IBot otherBot;
    
    public GLBot(final ITronBoard basicBoard) {
        this.board = basicBoard;
    }
    
    /**
     * @return the moves
     */
    public IBotMoves getMoves() {        
        return moves;
    }
    
    /**
     * @param moves the moves to set
     */
    public void setMoves(IBotMoves moves) {
        this.moves = moves;
    }    

    public void makeMove(final Move move) {
        this.moves.makeMove(move);
    }    
    
    public void printMoves() {
        this.moves.printMoves();
    }

    /**
     * @return the board
     */
    public ITronBoard getBoard() {
        return board;
    }
    
    public boolean validateMove(final Stack<Move> stack, final Move move) {
        
        if (stack.contains(move)) {
            return false;
        }        
        if (move.getX() < 0) {
            return false;
        }        
        if (move.getY() < 0) {
            return false;
        }
        if (move.getX() >= this.board.getSize()) {
            return false;
        }        
        if (move.getY() >= this.board.getSize()) {
            return false;
        }
        
        return this.checkRawMap(board.getBoard(), move);               
    }
    
    public boolean checkRawMap(final byte [] board, final Move newMove) {
        
        if (newMove == null) {
            return false;
        }
        
        final int x = newMove.getX();
        final int y = newMove.getY();
        final byte type = board[(y * this.board.getSize()) + x];
        if (type == ITronBoard.WALL) {
            return false;
        }        
        return true;
    }
    
    public Move checkValidMoves() {
        
        final Stack<Move> stack = (Stack<Move>) this.moves.getMoves();        
        final Move lastMove = stack.peek();
        if (lastMove == null) {
            // Ideally the last move shouldn't be null.
            return new Move(0, 0);
        }
        // Only check four times.
        Move newMove = null;
        Move realMove = null;
        for (int i = 0; i < 4; i++) {
            
            final int direction = random.nextInt(4);
            if (direction == 0) {
                newMove = lastMove.decx();
                if (!validateMove(stack, newMove)) { continue; }
                realMove = newMove;
            } else if (direction == 1) {
                newMove = lastMove.incx();
                if (!validateMove(stack, newMove)) { continue; }
                realMove = newMove;
            } else if (direction == 2) {
                newMove = lastMove.incy();
                if (!validateMove(stack, newMove)) { continue; }
                realMove = newMove;
            } else {
                newMove = lastMove.decy();
                if (!validateMove(stack, newMove)) { continue; }
                realMove = newMove;
            } // End of if - else direction check //
            
        } // End of the for //
        
        return realMove;
    }
    
    public void makeLogicMove() {
        final Move newMove = this.checkValidMoves();
        if (newMove != null) {
            this.makeMove(newMove);
        } else {
            System.out.println("Bot cannot make another move");
        } // End of the if //
    }

    public Move getOtherBotPos() {
        if (this.getOtherBot() != null) {
            final Stack<Move> stack = (Stack<Move>) this.getOtherBot().getMoves();
            return stack.peek();
        }
        return null;
    }

    /**
     * @return the otherBot
     */
    public IBot getOtherBot() {
        return otherBot;
    }

    /**
     * @param otherBot the otherBot to set
     */
    public void setOtherBot(final IBot otherBot) {
        this.otherBot = otherBot;
    }
    
} // End of the Class //
