package org.berlin.tron.gl.game;

import java.util.Timer;

public class GLGame {

    private final IBot bot1;
    private final IBot bot2;
    private final GLRenderBoard board;
    
    public GLGame(final GLRenderBoard board, final IBot bot1, final IBot bot2) {
        this.board  = board;
        this.bot1   = bot1;
        this.bot2   = bot2;
    }
 
    public void setInitBotPos() {
        
        final ITronBoard basicBoard = this.board.getBoard();        
        int sizex = basicBoard.getNumCols();
        int sizey = basicBoard.getNumRows();
        
        // Init position
        this.bot1.makeMove(new Move(2, 2));
        this.bot2.makeMove(new Move(sizex-2, sizey-2));
    }
    
    public void stepGame() {
        
        bot1.makeLogicMove();
        bot2.makeLogicMove();
        
    }
    
    public UpdateStateTask launchTask() {
        
        final UpdateStateTask changeStateTask = new BasicGameState(this, this.board, bot1, bot2);
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(changeStateTask, 
                changeStateTask.getTaskDelayMs(), 
                changeStateTask.getTaskPeriodMs());
        return changeStateTask;
    }
    
} // End of the Class //
