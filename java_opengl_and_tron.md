![http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/real_gl_tron_image.png](http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/real_gl_tron_image.png)

## The Challenge ##

Google and the University of Waterloo a Computer Science Club (Waterloo, Ontario, Canada) have created an AI challenge.  This is more algorithm AI and not Turing Strong AI.  "The objective is to create an AI to play the classic game Tron, Surround, Snafu, Snake, etc."  You must submit your entry by Feb 26th, 2010.  Imagine Tron, there are two opposing players on a N x N (say 50x50) grid and the goal is to trap the other player.  The players can move north, south, east, west in the adjacent squares but into a wall or the other player's previously occupied squares.  I thought I would try my hand at the challenge.  I was actually quite pleased with their handling on the contest.  There are normally about 600+ active bots in the challenge.  The website provides great stats on how your bot is doing.  There is a graph with wins and losses and a javascript application that can replay any game.  I really didn't want to talk about my particular entry, but I wanted to show you how I tested my bot with Java's OpenGL (jogl) library.  The default google challenge starter pack comes with some crude console based game output.  I needed something more visually appealing for my testing.  I created a small testing environment in Java.  I was able to test my particular bot but the bot and the OpenGL initialization and rendering are embedded with the bot.  If I have more time, I hope to separate a running bots from the OpenGL rendering.  It would be nice for the 3D window to bind to a socket connection and wait for input from several bot clients and render the game that way.

http://csclub.uwaterloo.ca/contest/index.php

![http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/my_tron_bot_version_0.png](http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/my_tron_bot_version_0.png)

## OpenGL and jogl (Java Binding for the OpenGL API) ##

There is one benefit when working with Java, you can interact with a number of different mature libraries.  Even though, you normally think of writing OpenGL games or graphics programs with the C++ programming language.  You can just as easily use OpenGL with Java.  OpenGL is a framework and API for writing 2D and 3D graphics.  Most programming languages have a similar interface to OpenGL including initialization, rendering vertices and displaying the content.  OpenGL provides a lot of the legwork for rendering fast through the 3D graphics pipeline on your machine, the core OpenGL libraries do not provide a framework for developing complex games or demos.  OpenGL is as low-level a library as say the Socket API is for writing web applications.  Also, I would strongly encourage you to use OpenGL for both 2D and 3D graphics.  The framework is highly portable and the visualizations are extremely fast.  I created two versions of the tron board, one in 2D and the other in 3D.   Despite your fears about 3D or 2D graphics, OpenGL has always remained fairly simple for getting starting.  Like a lot of other graphics frameworks, OpenGL requires a set of initialization calls and then subsequent double buffering display routines.  In Java, a couple more calls are made to create a GLCanvas object and then attaching the Canvas to a Java Swing JFrame or other Swing widget.

![http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/screenshot_pino_3d1.png](http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/screenshot_pino_3d1.png)

To get started, visit the jogl website and download the jogl libraries.  I am using jogl version 1.1.0.  I needed to ensure that jogl.jar, gluegen-rt.jar gluegen-rt.dll, jogl\_awt.dll, jogl\_cg.dll, jogl.dll were in my runtime classpath.  For Eclipse, I also needed to add the jogl directory to the java.library.path setting.

```
-Djava.library.path=${workspace_loc:pinobot}/lib/jogl
```
Once the libraries are included, you can start with developing your Swing JFrame/main application.

![http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/screenshot_2d_tron.png](http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/screenshot_2d_tron.png)

![http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/screenshot_init_for_opengl.png](http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/screenshot_init_for_opengl.png)

## Jogl Hello World ##

This is the simplest Java OpenGL application you can create:
```
package org.berlin.pino.test.functional.jogl;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCanvas;

public class Basic {
    public static void main(String[] args) {
        
        Frame frame = new Frame("Hello World!");
        GLCanvas canvas = new GLCanvas();        
        frame.add(canvas);
        frame.setSize(300, 300);
        frame.setLocation(400, 400);
        frame.setBackground(Color.white);
        frame.addWindowListener(new WindowAdapter() {           
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }            
        });
        frame.setVisible(true);
    }    
} // End of the class //

```

## Java Open Nehe Game Dev Example, Rendering a Box ##

![http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/screenshot_text_area_boxes.png](http://ainotebook.googlecode.com/svn/trunk/ainotebook/pinobot/docs/media/screenshot_text_area_boxes.png)

```
package org.berlin.pino.test.jogl.box;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class GLBox implements GLEventListener, 
               MouseListener, MouseMotionListener {

    private final GLU glu = new GLU();
    private float rtri  = 0.0f;
    private float rquad = 0.0f;
    
    public static class Builder {
        
        public GLCanvas buildCanvas() {

            GLCanvas canvas = new GLCanvas();
            canvas.addGLEventListener(new GLBox());
            canvas.setSize(100, 60);
            return canvas;
        }
        
    } // End of the Class //

    public GL renderGLScene(final GLAutoDrawable drawable) {
        
        final float shiftXPos = 1.2f;
        final GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear and depth buffer bit
        gl.glLoadIdentity(); // Reset The View
        gl.glTranslatef(-4.0f + shiftXPos, 0.0f, -7.0f);
        gl.glRotatef(rtri, 0.0f, 1.0f, 1.0f); 
        
        gl.glBegin(GL.GL_QUADS); // Draw A Quad        
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right Of The Quad (Top)
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);  // Top Left Of The Quad (Top)
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);   // Bottom Left Of The Quad (Top)
        gl.glVertex3f(1.0f, 1.0f, 1.0f);    // Bottom Right Of The Quad (Top)
        
	...
	...
        gl.glEnd(); // Done Drawing The Quad

        return gl;

    }
       
    public void display(final GLAutoDrawable drawable) {
                
        final GL gl = this.renderGLScene(drawable);               
        gl.glFlush();
    }
    
    public void displayChanged(final GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        
    }
    
    public void init(final GLAutoDrawable drawable) {
        
        final GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());

        gl.setSwapInterval(1);
        
        gl.glShadeModel(GL.GL_SMOOTH);                            // Enables Smooth Color Shading
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);               // This Will Clear The Background Color 
        gl.glClearDepth(1.0);                                  // Enables Clearing Of The Depth Buffer
        gl.glEnable(GL.GL_DEPTH_TEST);                            // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                             // The Type Of Depth Test To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations
    }

    public void reshape(final GLAutoDrawable drawable, int x, int y, int width, int height) {

        final GL gl = drawable.getGL();
        System.err.println("GL_VENDOR: "   + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: "  + gl.glGetString(GL.GL_VERSION));

        if (height <= 0) {
            height = 1;
        }
        final float h = (float) width / (float) height;
        
        gl.glViewport(0, 0, width, height);  // Reset The Current Viewport And Perspective Transformation
        gl.glMatrixMode(GL.GL_PROJECTION);   // Select The Projection Matrix
        gl.glLoadIdentity();                 // Reset The Projection Matrix
        glu.gluPerspective(45.0f, h, 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
    ...
    ... Click event methods 
    ...
    
} // End of the Class //
```

## My Bot ##

I am using kind of a mishmash of broken algorithms.  Basically, use my post for finding out visualizing my bot.  I would go elsewhere for a good algorithm for beating the google challenge.  My bot is hovering around 290 / 600.  It was something I created over a couple of hours.  I started out with the random, select a valid direction.  Then I went to favoring an order of directions.  It seems that the best algorithm is a combination of a working flood-fill pattern, a min-max strategy in close combat and possibly a good attack strategy in an open environment.  I used a simple min-max strategy that found good paths with open squares.  My score/evaluation was based on 1 point for the the number of open squares for a particular move.  Here is a more robust set of strategies on the Waterloo forum:

"The evaluation function is simple. Partition the board into squares
OUR bot can reach first and squares THEIR bot can reach first.
I think of this as a Voronoi partition of the board.   The score is simply count(OUR squares) - count(THEIR squares)."

http://csclub.uwaterloo.ca/contest/forums/viewtopic.php?f=8&t=190

Basic Favor Order Algorithm:
```
@Override
    public void makeLogicMove() {
        this.printThoughts();
        this.printMessages();
        
        // If dead, don't make a move //
        if (this.isDead()) {
            return;
        }         
        if (this.getVerbose()) {
            System.err.println("Making logic move for aibotscorer - " + this.getMoves().size());
        }                       
        final Move lastMove = this.getLastMoveNull();
        if (lastMove == null) {
            this.addMessages("-3000-AI: using default move, last move null");
            // Revert back to the default move
            super.makeLogicMove();
            return;
        }
                           
        final Move north = lastMove.decy();
        final Move south = lastMove.incy();
        final Move east  = lastMove.incx();
        final Move west  = lastMove.decx();
        
        final Stack<Move> stack = (Stack<Move>) this.getMoves().getMoves();
        final boolean nb = validateMove(stack, north);
        final boolean sb = validateMove(stack, south);
        final boolean eb = validateMove(stack, east);
        final boolean wb = validateMove(stack, west);
        if (wb) {
            this.makeMove(west);
            return;
        }
        if (nb) {
            this.makeMove(north);
            return;
        }        
        if (eb) {
            this.makeMove(east);
            return;
        }                        
        if (sb) {
            this.makeMove(south);
            return;
        }        
        this.makeMove(this.dumbRandomMove());
        return;        
    }       
```

## Source for the Java OpenGL Code and the Tron Bot ##

http://jvmnotebook.googlecode.com/files/simple_tron_opengl.tar.gz

```
// Packages and Code of Interest:
package org.berlin.tron.gl (GLGridApp)
package org.berlin.tron.gl.game (GL3DGridBoard)
```



Note: I did include the source for the bot.  By the time, you figure out how to get it deployed and usable for the Google challenge, you probably would have found it better to have created your own.