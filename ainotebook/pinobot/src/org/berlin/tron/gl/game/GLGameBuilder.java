package org.berlin.tron.gl.game;

public class GLGameBuilder {

    private final GLRenderBoard board;
    
    public GLGameBuilder(final GLRenderBoard board) {
        this.board = board;
    }
    
    public GLGame build() {
        final IBot bot1 = new GLBot(this.board.getBoard());
        final IBot bot2 = new GLBot(this.board.getBoard());
        return new GLGame(this.board, bot1, bot2);
    }
    
} // End of the Class //
