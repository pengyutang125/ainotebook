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
import java.util.Random;
import java.util.Stack;

public class GLBot implements IBot {

    public static final Random sysRand = new Random(System.currentTimeMillis());
        
    private final ITronBoard board;
    private IBotMoves moves = new BotMoves();    
    private Random random = new Random(System.currentTimeMillis()); 
    
    private IBot otherBot;
    
    private boolean unableToMakeMove = false;
    private boolean dead = false;

    private String name = "(bot:" + sysRand.nextInt() + ")";   
    private String causeDeath = "";
    
    private List<String> messages = new ArrayList<String>();
    private List<MoveThought> thoughts = new ArrayList<MoveThought>();
    
    /**
     * Enable or disable debugging messages.
     */
    private boolean verbose = true;
    
    /**
     * Main constructor for Bot.
     * 
     * @param basicBoard
     */
    public GLBot(final ITronBoard basicBoard) {
        this.board = basicBoard;
    }
    
    public String toString() {
        return "#{Bot-" + this.getName() + " dead?=" + this.isDead() + " move?=" + (!this.isUnableToMakeMove()) + " cause-death=" + this.getCauseDeath() + "}";
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
        
        final MoveThought thought = new MoveThought(move.getX(), move.getY(), move);
        this.thoughts.add(thought);
        if (stack.contains(move)) {
            thought.setThoughtOnMove("- BadMove, I already moved there");
            return false;
        }        
        if (move.getX() < 0) {
            thought.setThoughtOnMove("- BadMove, less than board size X");
            return false;
        }        
        if (move.getY() < 0) {
            thought.setThoughtOnMove("- BadMove, less than board size Y");
            return false;
        }
        if (move.getX() >= this.board.getNumCols()) {
            thought.setThoughtOnMove("- BadMove, greater than board size X");
            return false;
        }        
        if (move.getY() >= this.board.getNumRows()) {
            thought.setThoughtOnMove("- BadMove, greater than board size Y");
            return false;
        }
        
        return this.checkRawMap(board.getBoard(), move, thought);               
    }
    
    public boolean checkRawMap(final byte [] board, final Move newMove, final MoveThought moveThought) {
        
        if (newMove == null) {
            return false;
        }
        
        final int x = newMove.getX();
        final int y = newMove.getY();
        final byte type = this.getBoard().getBoardVal(x, y);
        if (type == ITronBoard.WALL) {
            moveThought.setThoughtOnMove("- BadMove, might hit a wall");
            return false;
        } 
        if ((type == ITronBoard.PLAYER1) || (type == ITronBoard.PLAYER2)) {
            moveThought.setThoughtOnMove("- BadMove, might myself or another player");
            return false;
        }
        return true;
    }
    
    public Move getLastMove() {
        
        final Stack<Move> stack = (Stack<Move>) this.moves.getMoves();        
        final Move lastMove = stack.peek();
        if (lastMove == null) {
            // Ideally the last move shouldn't be null.
            return new Move(-1, -1);
        }
        return lastMove;
    }
    
    public Move checkValidMovesRaw() {
        
        final Stack<Move> stack = (Stack<Move>) this.moves.getMoves();        
        final Move lastMove = stack.peek();
        if (lastMove == null) {
            // Ideally the last move shouldn't be null.
            return new Move(0, 0);
        } // End of the if  //
        
        final List<Move> validMovesList = new ArrayList<Move>();
        final Move north = lastMove.incy();
        final Move south = lastMove.decy();
        final Move east  = lastMove.incx();      
        final Move west  = lastMove.decx();
        
        final boolean nb = validateMove(stack, north);
        final boolean sb = validateMove(stack, south);
        final boolean eb = validateMove(stack, east);
        final boolean wb = validateMove(stack, west);
        
        int validMovesLeft = 0;
        if (nb) {
            validMovesList.add(north);
            validMovesLeft++;
        }
        if (sb) {
            validMovesList.add(south);
            validMovesLeft++;
        }
        if (eb) {
            validMovesList.add(east);
            validMovesLeft++;
        }
        if (wb) {
            validMovesList.add(west);
            validMovesLeft++;
        }
        this.messages.add("Message: (movesleft=" + validMovesLeft + ") Direction Check - " + nb + " " + sb + " " + eb + " " + wb + " // " + east);
        
        // Add to the queue //
        if (validMovesList.size() == 0) {
            this.messages.add("+ Message: [return my move] size is zero, returning north");
            return north;
        } else if (validMovesList.size() == 1) {
            this.messages.add("+ Message: [return my move] size is one, first");
            return validMovesList.get(0);
        } else {
            final int sel = random.nextInt(validMovesList.size());
            return validMovesList.get(sel);
        } // End of if - else //        
    }
    
    public Move checkValidMoves() {
        final Move rawMove = this.checkValidMovesRaw();
        this.messages.add("+ Message: valid move = " + rawMove);
        return rawMove;
    }
    
    public void printMessages() {   
       
        if (!this.getVerbose()) {
            return;
        }
        
        for (String msg : this.messages) {           
            System.out.println(msg);            
        }
    }
    
    public void printThoughts() {
        if (!this.getVerbose()) {
            return;
        }
        for (MoveThought thought : this.thoughts) {
            System.out.println(thought);
        }
    }
    
    public void printAIMap() {
        this.board.printBoard();
    }
    
    public void makeLogicMove() {
     
        this.printAIMap();
        this.printThoughts();
        this.printMessages();
        
        // If dead, don't make a move //
        if (this.isDead()) {
            return;
        }        
        final Move newMove = this.checkValidMoves();
        if (newMove != null) {
            this.makeMove(newMove);
        } else {                       
            this.unableToMakeMove = true;            
            //  Just move north //
            this.makeMove(this.getLastMove().incy());
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

    /**
     * @return the unableToMakeMove
     */
    public boolean isUnableToMakeMove() {
        return unableToMakeMove;
    }

    /**
     * @return the dead
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * @param dead the dead to set
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the causeDeath
     */
    public String getCauseDeath() {
        return causeDeath;
    }

    /**
     * @param causeDeath the causeDeath to set
     */
    public void setCauseDeath(String causeDeath) {
        this.causeDeath = causeDeath;
    }

    /**
     * @return the verbose
     */
    public boolean getVerbose() {
        return verbose;
    }

    /**
     * @param verbose the verbose to set
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
} // End of the Class //
