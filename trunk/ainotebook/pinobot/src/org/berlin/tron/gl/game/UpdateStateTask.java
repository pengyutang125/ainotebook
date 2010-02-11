package org.berlin.tron.gl.game;

import java.util.TimerTask;

public class UpdateStateTask extends TimerTask {

    private final GLRenderBoard board;
    
    public UpdateStateTask(final GLRenderBoard board) {
        this.board = board;
    }
    
    @Override
    public void run() {
        
        if (this.board != null) {
            final ITronBoard basicBoard = new TronBoard(this.board.getBoard().getSize());
            basicBoard.initRandom();
            basicBoard.makeRandomBoard();
            this.board.setBoard(basicBoard);          
        }
    }

} // End of the Class //
