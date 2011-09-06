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

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

/**
 * Main Class for JFrame Squirm
 */
public class Squirm extends JFrame implements Runnable {

  private static final long serialVersionUID = 1L;

  // THREAD SUPPORT:
  // m_Squirm is the Thread object for the applet
  private Thread m_Squirm = null;

  protected SquirmGrid squirmGrid;
  protected final int grid_size_x = 200;
  protected final int grid_size_y = 200;
  protected final int drawing_size_x = 400;
  protected final int drawing_size_y = 400;
  protected final float scale = drawing_size_x / (float) grid_size_x;

  protected Image offscreenImage = null;
  protected Graphics off_g = null;

  /** Squirm Class Constructor */
  public Squirm() {
    squirmGrid = new SquirmGrid(grid_size_x, grid_size_y);
  }

  /**
   * APPLET INFO SUPPORT: The getAppletInfo() method returns a string describing
   * the applet's author, copyright date, or miscellaneous information.
   */
  public String getAppletInfo() {
    return "Name: Squirm\r\n" + "Author: Tim Hutton";
  }

  /**
   * The init() method is called by the AWT when an applet is first loaded or
   * reloaded. Override this method to perform whatever initialization your
   * applet needs, such as initializing data structures, loading images or
   * fonts, creating frame windows, setting the layout manager, or adding UI
   * components.
   */
  public void init() {
    // If you use a ResourceWizard-generated "control creator" class to
    // arrange controls in your applet, you may want to call its
    // CreateControls() method from within this method. Remove the following
    // call to resize() before adding the call to CreateControls();
    // CreateControls() does its own resizing.
    resize(drawing_size_x, drawing_size_y);

    if (offscreenImage == null) {
      offscreenImage = createImage(drawing_size_x, drawing_size_y);
      // may need gjt.Util.waitForImage(this, offscreenImage);
      off_g = offscreenImage.getGraphics();
    }
  }

  /**
   * Place additional applet clean up code here. destroy() is called when when
   * your applet is terminating and being unloaded.
   */
  public void destroy() {
  }

  /** Squirm Paint Handler */
  public void paint(Graphics g) {
    if (off_g == null) {
      return;
    }
    // Clear the background
    off_g.setColor(Color.white);
    off_g.fillRect(0, 0, drawing_size_x, drawing_size_y);

    // Draw the cells
    squirmGrid.draw(off_g, scale);

    // Show the result.
    g.drawImage(offscreenImage, 0, 0, this);
  }

  /**
   * Override the default update method to call paint rather than clear and
   * paint.
   */
  public void update(Graphics g) {
    paint(g);
  }

  /**
   * The start() method is called when the page containing the applet first
   * appears on the screen. The AppletWizard's initial implementation of this
   * method starts execution of the applet's thread.
   */
  public void start() {
    if (m_Squirm == null) {
      m_Squirm = new Thread(this);
      m_Squirm.start();
    }
  }

  /**
   * The stop() method is called when the page containing the applet is no
   * longer on the screen. The AppletWizard's initial implementation of this
   * method stops execution of the applet's thread.
   */
  public void stop() {
    if (m_Squirm != null) {
      m_Squirm.stop();
      m_Squirm = null;
    }
  }

  /**
   * THREAD SUPPORT The run() method is called when the applet's thread is
   * started. If your applet performs any ongoing activities without waiting for
   * user input, the code for implementing that behavior typically goes here.
   * For example, for an applet that performs animation, the run() method
   * controls the display of images.
   */
  public void run() {
    while (true) {
      try {
        repaint();

        // ask the squirm world to execute one time step
        squirmGrid.doTimeStep();

        Thread.sleep(1);
      } catch (InterruptedException e) {
        // TODO: Place exception-handling code here in case an
        // InterruptedException is thrown by Thread.sleep(),
        // meaning that another thread has interrupted this one
        stop();
      }
    }
  }

  // MOUSE SUPPORT:
  // The mouseDown() method is called if the mouse button is pressed
  // while the mouse cursor is over the applet's portion of the screen.
  public boolean mouseDown(Event evt, int x, int y) {
    return true;
  }

  // MOUSE SUPPORT:
  // The mouseUp() method is called if the mouse button is released
  // while the mouse cursor is over the applet's portion of the screen.
  public boolean mouseUp(Event evt, int x, int y) {
    return true;
  }

  // MOUSE SUPPORT:
  // The mouseDrag() method is called if the mouse cursor moves over the
  // applet's portion of the screen while the mouse button is being held down.
  public boolean mouseDrag(Event evt, int x, int y) {
    return true;
  }

  /**
   * MOUSE SUPPORT: The mouseMove() method is called if the mouse cursor moves
   * over the applet's portion of the screen and the mouse button isn't being
   * held down.
   */
  public boolean mouseMove(Event evt, int x, int y) {
    return true;
  }

  /**
   * MOUSE SUPPORT: The mouseEnter() method is called if the mouse cursor enters
   * the applet's portion of the screen.
   */
  public boolean mouseEnter(Event evt, int x, int y) {
    return true;
  }

  /**
   * MOUSE SUPPORT: The mouseExit() method is called if the mouse cursor leaves
   * the applet's portion of the screen.
   */
  public boolean mouseExit(Event evt, int x, int y) {
    return true;
  }

} // End of the class //
