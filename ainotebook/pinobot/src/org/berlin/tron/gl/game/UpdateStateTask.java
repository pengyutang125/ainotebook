package org.berlin.tron.gl.game;

import java.util.TimerTask;

public class UpdateStateTask extends TimerTask {

    private final GLRenderBoard glRenderBoard;
    
    private int taskDelayMs  = 100;
    private int taskPeriodMs = 100;
    
    private long lastRunStart = 0;
    private long lastRunEnd = 0;
    
    public UpdateStateTask(final GLRenderBoard board) {
        this.glRenderBoard = board;
        this.lastRunStart = System.currentTimeMillis();        
    }
    
    @Override
    public void run() {
        
        if (this.glRenderBoard != null) {
            
            final ITronBoard basicBoard = new TronBoard(glRenderBoard.getBoard().getSize());
            basicBoard.makeRandomBoard();           
            glRenderBoard.setBoard(basicBoard);
            
            this.lastRunEnd = System.currentTimeMillis();
            final long diff = this.lastRunEnd - this.lastRunStart;
            System.out.println("Task Updated - " + diff);
            this.lastRunStart = this.lastRunEnd;
            
        } // End of the If //
    }

    /**
     * @return the taskDelayMs
     */
    public int getTaskDelayMs() {
        return taskDelayMs;
    }

    /**
     * @param taskDelayMs the taskDelayMs to set
     */
    public void setTaskDelayMs(int taskDelayMs) {
        this.taskDelayMs = taskDelayMs;
    }

    /**
     * @return the taskPeriodMs
     */
    public int getTaskPeriodMs() {
        return taskPeriodMs;
    }

    /**
     * @param taskPeriodMs the taskPeriodMs to set
     */
    public void setTaskPeriodMs(int taskPeriodMs) {
        this.taskPeriodMs = taskPeriodMs;
    }

} // End of the Class //
