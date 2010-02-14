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

/**
 * Default google challenge AI bot.  Search for a valid mode.
 * This bot can also be used for checking valid moves and scoring them.
 * 
 * @author BerlinBrown
 *
 */
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
    
    private double score = 0.0;
    /**
     * See perMove score methods.
     */
    private double moveScore = 0.0;
    private double moveScoreChecksForAvg = 0.0;
    
    /**
     * Enable or disable debugging messages.
     */
    private boolean verbose = false;
    
    /**
     * Main constructor for Bot.
     * 
     * @param basicBoard
     */
    public GLBot(final ITronBoard basicBoard) {
        this.board = basicBoard;
    }
    
    public String toString() {
        return "#{Bot-" + this.getName() + " score=" + this.getScore() + " dead?=" + this.isDead() + " move?=" + (!this.isUnableToMakeMove()) + " cause-death=" + this.getCauseDeath() + "}";
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
    
    public void addThoughts(final MoveThought moveThought) {
        this.thoughts.add(moveThought);
    }
    public void addMessages(final String msg) {
        this.addMessages(msg);
    }
    
    public boolean validateMove(final Stack<Move> stack, final Move move) {
        
        if (stack.size() == 0) {
            return false;
        }
        
        final MoveThought thought = new MoveThought(move.getX(), move.getY(), move);
        this.thoughts.add(thought);
        
        if (stack.contains(move)) {
            this.moveScoreChecksForAvg += 1.0;
            this.score += IMove.NEG_THOUGHT_MOVE_PREV_SCORE;
            thought.setThoughtOnMove("- BadMove, I already moved there - " + this.score);
            return false;
        }        
        if (move.getX() < 0) {
            this.moveScoreChecksForAvg += 1.0;
            this.score += IMove.NEG_THOUGHT_MOVE_SCORE;
            thought.setThoughtOnMove("- BadMove, less than board size X - " + this.score);
            return false;
        }        
        if (move.getY() < 0) {
            this.moveScoreChecksForAvg += 1.0;
            this.score += IMove.NEG_THOUGHT_MOVE_SCORE;
            thought.setThoughtOnMove("- BadMove, less than board size Y - " + this.score);
            return false;
        }
        if (move.getX() >= this.board.getNumCols()) {
            this.moveScoreChecksForAvg += 1.0;
            this.score += IMove.NEG_THOUGHT_MOVE_SCORE;
            thought.setThoughtOnMove("- BadMove, greater than board size X - " + this.score);
            return false;
        }        
        if (move.getY() >= this.board.getNumRows()) {
            this.moveScoreChecksForAvg += 1.0;
            this.score += IMove.NEG_THOUGHT_MOVE_SCORE;
            thought.setThoughtOnMove("- BadMove, greater than board size Y - " + this.score);
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
            this.moveScoreChecksForAvg += 1.0;
            this.score += IMove.NEG_THOUGHT_MOVE_WALL_SCORE;
            moveThought.setThoughtOnMove("- BadMove, might hit a wall - " + this.score);
            return false;
        } 
        if ((type == ITronBoard.PLAYER1) || (type == ITronBoard.PLAYER2)) {
            this.moveScoreChecksForAvg += 1.0;
            this.score += IMove.NEG_THOUGHT_MOVE_PLAYER;
            moveThought.setThoughtOnMove("- BadMove, might myself or another player - " + this.score);
            return false;
        }
        return true;
    }
    
    public Move getLastMove() {
        
        final Stack<Move> stack = (Stack<Move>) this.moves.getMoves();        
        final Move lastMove = (stack.size() == 0) ? null : stack.peek();
        if (lastMove == null) {
            // Ideally the last move shouldn't be null.
            return new Move(-1, -1);
        }
        return lastMove;
    }
    
    /**
     * Get Last Move, but allow for null.
     * 
     * @return
     */
    public Move getLastMoveNull() {
        final Stack<Move> stack = (Stack<Move>) this.moves.getMoves();        
        final Move lastMove = (stack.size() == 0) ? null : stack.peek();
        if (lastMove == null) {
            // Ideally the last move shouldn't be null.
            return null;
        } // End of the if  //
        return lastMove;
    }
    
    /**
     * Check for empty squares.
     * 
     * @return
     */
    public List<Move> checkValidMovesRaw() {
        
        final Stack<Move> stack = (Stack<Move>) this.moves.getMoves();        
        final Move lastMove = (stack.size() == 0) ? null : stack.peek();
        if (lastMove == null) {
            // Ideally the last move shouldn't be null.
            return null;
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
            this.score += IMove.POS_VALID_MOVE;
            this.moveScoreChecksForAvg += 1.0;
            validMovesList.add(north);
            validMovesLeft++;
        }
        if (sb) {
            this.score += IMove.POS_VALID_MOVE;
            this.moveScoreChecksForAvg += 1.0;
            validMovesList.add(south);
            validMovesLeft++;
        }
        if (eb) {
            this.score += IMove.POS_VALID_MOVE;
            this.moveScoreChecksForAvg += 1.0;
            validMovesList.add(east);
            validMovesLeft++;
        }
        if (wb) {
            this.score += IMove.POS_VALID_MOVE;
            this.moveScoreChecksForAvg += 1.0;
            validMovesList.add(west);
            validMovesLeft++;
        }
                
        // The move score is the score over the average number of checks. 
        this.moveScoreChecksForAvg = (this.moveScoreChecksForAvg <= 0) ? 1.0 : this.moveScoreChecksForAvg;
        this.moveScore = this.score / this.moveScoreChecksForAvg;        
        this.messages.add("Message: (movesleft=" + validMovesLeft + ") Direction Check - " 
                + nb + " " + sb + " " + eb + " " + wb + " // " + this.score + " //chks=" + this.moveScore);        
        return validMovesList;
    }
    
    /**
     * CHeck the valid moves.
     */
    public Move checkValidMoves() {
        
        final List<Move> validMovesList = this.checkValidMovesRaw();
        Move rawMove = new Move(0, 0);
        
        if (validMovesList == null) {
            this.messages.add("+ Message: valid move = " + rawMove);
            return rawMove;
        }
        // Add to the queue //
        if (validMovesList.size() == 0) {
            this.score += IMove.NEG_NO_MOVES;
            this.moveScoreChecksForAvg += 1.0;
            // Invalid state 
            this.messages.add("+ Message: [return my move] size is zero, returning null");
            // Move north
            // !IMPORTANT! - may throw nullpointer, not check last move
            rawMove = this.getLastMove().incy();
            
        } else if (validMovesList.size() == 1) {
            
            this.score += IMove.NEG_THOUGHT_ONLY_ONE_MOVE;
            this.moveScoreChecksForAvg += 1.0;
            this.messages.add("+ Message: [return my move] size is one, first - " + this.moveScore);
            rawMove = validMovesList.get(0);
            
        } else {
            
            this.messages.add("+ Message: [return my move] " + this.moveScore);
            final int sel = random.nextInt(validMovesList.size());
            rawMove = validMovesList.get(sel);
            
        } // End of if - else //     
                
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
    
    /**
     * Make a logic move basd on the alternatives.
     */
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

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    public void incScore(double score) {
        this.score += score;
    }

    
    /**
     * @return the moveScore
     */
    public double getPerMoveScore() {
        return moveScore;
    }

    /**
     * @param moveScore the moveScore to set
     */
    public void setPerMoveScore(double moveScore) {
        this.moveScore = moveScore;
    }

    /**
     * @param unableToMakeMove the unableToMakeMove to set
     */
    public void setUnableToMakeMove(boolean unableToMakeMove) {
        this.unableToMakeMove = unableToMakeMove;
    }
    
} // End of the Class //
