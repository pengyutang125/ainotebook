/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright Tim Hutton and Berlin Brown <berlin dot brown at gmail.com> 2011
 *  
 * Tim Hutton is the original author, but a license not provided in source,
 * GPL was used for similar projects.  If Tim or anyone else has questions, please contact Berlin Brown.
 *
 * http://www.sq3.org.uk/Evolution/Squirm3/
 */
package org.squirm.cellblob;

// SquirmCell.java

import java.awt.Graphics;
import java.util.Vector;

public class SquirmCell {

  // / pointer to the creature this cell belongs to
  protected SquirmCreature parent;

  // / the cell's current location
  protected int x, y;

  // encoding of an 8-neighbourhood:
  /*
   * 1 2 3 0 8 4 7 6 5
   */
  public static final int EIGHT_x[] = { -1, -1, 0, 1, 1, 1, 0, -1, 0 };
  public static final int EIGHT_y[] = { 0, -1, -1, -1, 0, 1, 1, 1, 0 };
  
  // / public constructor
  public SquirmCell(SquirmCreature p, int x_loc, int y_loc, final Vector<SquirmCell> cell_list, SquirmCellSlot cell_grid[][]) {
    x = x_loc;
    y = y_loc;
    parent = p;
    cell_list.addElement(this);
    cell_grid[x][y].makeOccupied(this);
  }

  /** access function */
  public int getX() {
    return x;
  }

  /** access function */
  public int getY() {
    return y;
  }

  public void draw(Graphics g, float scale) {
    g.setColor(parent.colour);
    g.fillRect((int) (x * scale), (int) (y * scale), (int) scale, (int) scale);
  }

  public boolean querySameParent(SquirmCell cell) {
    return (cell != this && cell.parent == this.parent);
  }

  public void doTimeStep(int n_x, int n_y, Vector<SquirmCell> cell_list, SquirmCellSlot cell_grid[][]) {
    // for now, if a frozen cell then don't have to do anything
    if (parent.strategy == SquirmCreature.FROZEN)
      return;

    int occ[] = new int[8]; // -1=off grid, 0=empty, 1=friend, 2=enemy
    int tx, ty;
    for (int i = 0; i < 8; i++) {
      tx = x + EIGHT_x[i];
      ty = y + EIGHT_y[i];
      // check whether off grid
      if (tx < 0 || tx >= n_x || ty < 0 || ty >= n_y) {
        occ[i] = -1;
      }
      // check whether is empty
      else if (cell_grid[tx][ty].queryEmpty()) {
        occ[i] = 0;
      }
      // check for friend/enemy
      else {
        occ[i] = cell_grid[tx][ty].getOccupant().querySameParent(this) ? 1 : 2;
      }
    }

    // pick a place to move to based on the occ list
    int which = chooseMove(occ);
    int desired_x, desired_y;
    desired_x = x + EIGHT_x[which];
    desired_y = y + EIGHT_y[which];

    // confirm within bounds of area and free
    if (desired_x >= 0 && desired_x < n_x && desired_y >= 0 && desired_y < n_y
        && cell_grid[desired_x][desired_y].queryEmpty()) {
      moveTo(desired_x, desired_y, cell_grid);
    }
  }

  private int chooseMove(int occ[]) {    
    {
      int i, j;

      // move can be to any empty empty square (0-7) - return 8 if can't move
      // move must maintain connectedness of cells within neighbourhood

      // build yes/no list of which cells are currently connected to the centre
      // cell
      boolean initially_connected[] = new boolean[8];
      for (i = 0; i < 8; i += 2)
        initially_connected[i] = (occ[i] == 1);
      for (i = 1; i < 8; i += 2)
        initially_connected[i] = (occ[i] == 1 && (occ[i - 1] == 1 || occ[(i + 1) % 8] == 1));

      // any move leaves the centre square empty
      // if find a cell that was initially connected, now unconnected then
      // invalid move
      boolean valid_move[] = new boolean[8];
      boolean connected[] = new boolean[8];
      int n_valid_moves = 0;
      boolean change;
      for (i = 0; i < 8; i++) {
        // is square valid at all?
        if (occ[i] == 0) {
          // clear the connected list
          for (j = 0; j < 8; j++)
            connected[j] = false;
          // any cell next to a connected cell is connected
          connected[i] = true; // start here
          do {
            change = false; // until contradicted below
            for (j = 0; j < 8; j++) {
              if (!connected[j] && (connected[(j - 1 + 8) % 8] || connected[(j + 1) % 8]) && occ[j] == 1) {
                connected[j] = true;
                change = true;
              }
            }
          } while (change);
          // if any cell is unconnected now that was connected then invalid move
          valid_move[i] = true; // unless contradicted below
          for (j = 0; j < 8; j++) {
            if (initially_connected[j] && !connected[j]) {
              valid_move[i] = false;
              break;
            }
          }
        } else
          valid_move[i] = false;
        // keep count of how many are valid
        if (valid_move[i])
          n_valid_moves++;
      }

      // build a simple array of the available moves
      int choice[] = new int[n_valid_moves + 1];
      choice[0] = 8; // no move is always an option
      int count = 1;
      for (i = 0; i < 8; i++)
        if (valid_move[i])
          choice[count++] = i;

      // ask the parent creature which one we should move to
      return parent.getMoveOpinion(this, choice, n_valid_moves + 1, valid_move);

    }
  }

  private void moveTo(int new_x, int new_y, SquirmCellSlot cell_grid[][]) {
    // move there
    cell_grid[x][y].makeEmpty();
    x = new_x;
    y = new_y;
    cell_grid[x][y].makeOccupied(this);
  }
  
} // End of the class //