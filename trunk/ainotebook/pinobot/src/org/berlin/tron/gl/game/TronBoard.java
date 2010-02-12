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

/**
 * Board can only consist of:
 * 
 * <pre>
 * 0    - Empty
 * 255  - Player 1
 * 128  - Player 2
 * 1    - Wall
 * </pre>
 * 
 * @author BerlinBrown
 *
 */
public class TronBoard implements ITronBoard {
    
    private final int size;
    private final byte board [];
    
    private Random random = new Random(); 
    
    public TronBoard(final int size) {
        this.size = size;
        this.board = new byte [size * size];
    }
    
    public String toString() {
        return "TronBoard: size=" + this.size + " obj=" + super.toString();
    }
    
    public void initRandom() {
        try {
            //this.random = SecureRandom.getInstance("SHA1PRNG");
            this.random = new Random(System.currentTimeMillis());
        } catch(Exception e) {
            e.printStackTrace();
            this.random = new Random();
        }
    }
    
    /**
     * Ensure that a bot cannot end up on a wall or other player.
     * He must be dead and not render on top of another object.
     */
    public boolean validateBoardMove(final byte onMoveType, final IBot bot, final int x, final int y) {
        if (bot == null) {
            return false;
        }
        if ((x < 0) || (y < 0) || (x >= this.getNumCols()) || (y >= this.getNumRows())) {
            bot.setCauseDeath("Hit Bounds at : x=" + x + "y=" + y);
            bot.setDead(true);
            return false;
        }
        final byte existType = this.getBoardVal(x, y);
        
        System.out.println("validate board move: ");
        System.out.println(this);
        this.printBoard();        
        if (existType != ITronBoard.EMPTY) {
            bot.setCauseDeath("Ran into something - " + existType + " (" + x + "," + y+ ")");
            bot.setDead(true);
            return false;
        }
        
        return true;
    }
          
    public void marshalMoves(final byte type, final IBot bot) {
        
        if (bot == null) {
            return;
        }
        
        final Move lastBotMove = bot.getLastMove();
        Move invalidMove = null;
        final int lastx = lastBotMove.getX();
        final int lasty = lastBotMove.getY();        
        final boolean validMove = validateBoardMove(type, bot, lastx, lasty);        
        if (!validMove) {
            invalidMove = lastBotMove;
        }
        // Continue to setting moves
        final IBotMoves moves = bot.getMoves();
        System.out.println("--->>>");
        bot.printMoves();
        for (Move curmove : moves.getMoves()) {            
            final int x = curmove.getX();
            final int y = curmove.getY();            
            if ((invalidMove != null) && (invalidMove.equals(curmove))) {
                // Don't draw
            } else {
                System.out.println("Setting board ->" + x + " / " + y);
                this.setBoardVal(type, x, y);
            }
        } // End of the For //
        
    }
    
    public void setRandomObject(final int x, final int y) {
        
        synchronized(this.board) {
            final int randVal = this.random.nextInt(12);        
            if (randVal == 1) {
                this.board[(y * size) + x] = PLAYER1;
            } else if (randVal == 2) {
                this.board[(y * size) + x] = PLAYER2;
            } else if (randVal == 3) {
                this.board[(y * size) + x] = WALL;
            } else {
                this.board[(y * size) + x] = EMPTY;
            } // End of the if - else //

        }
    }
    
    public void makeRandomBoard() {        
        for (int i = 0; i < size; i++) {            
            for (int j = 0; j < size; j++) {                
                this.setRandomObject(i, j);
            }
        } // End of the For //
    }
    
    public void clearBoard() {
        
        synchronized(this.board) {
            for (int i = 0; i < size; i++) {            
                for (int j = 0; j < size; j++) {
                    this.board[(i * size) + j] = EMPTY;
                }
            } // End of the For //
        }
    }

    public void printBoard() {
        for (int j = 0; j < size; j++) {            
            for (int i = 0; i < size; i++) {
                
                final byte p = this.board[(j * size) + i];
                if (p == PLAYER1) {
                    System.out.print("[#-x=" + i + " y=" + j + "],");
                } else if (p == PLAYER2) {
                    System.out.print("[+-x=" + i + " y=" + j + "],");                    
                } else if (p == WALL) {   
                    System.out.print("[x-x=" + i + " y=" + j + "],");
                } else {
                    System.out.print("[.-x=" + i + " y=" + j + "],");                    
                } // End of the if - else //                
            } // End of the for //
            System.out.println();
        } // End of the For //
    }
    
    /**
     * @return the board
     */
    public byte [] getBoard() {
        synchronized(this.board) {
            return board;
        }
    }

    public void setBoardVal(final byte type, final int x, final int y) {
        synchronized(this.board) {
            this.board[(y * size) + x] = type;
        }
    }
    
    public byte getBoardVal(final int x, final int y) {
        synchronized(this.board) {
            return this.board[(y * size) + x];
        }
    }
    
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
    
    public int getNumRows() {
        return size;
    }
    
    public int getNumCols() {
        return size;
    }
    
} // End of the Class //
