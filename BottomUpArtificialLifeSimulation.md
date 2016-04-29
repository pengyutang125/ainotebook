### Download ###

http://ainotebook.googlecode.com/files/bottom-alife-demo.zip

Simple Cell Demo.  The system is slightly interesting.  You can monitor the balance with the number of live cells. You may notice a shift in mutations as cells grow away from the center. With only a few mutations, the color of cells tend to shift in color over time.  It takes many iterations for emergent behavior to emerge. (Written in Scala/Java)

The field of artificial intelligence in computer science focuses on many
different areas of computing from computer vision to natural language
processing. These top-down approaches typically concentrate on human behavior
or other animal functions. In this code-base we look at a bottom-up approach to
artificial life and how emergent cell behavior can produce interesting results.
With this bottom-up alife approach, we are not interested in solving any
particular task, but we are interested in observing the adaptive nature of the
entities in our simulation. We also wanted to introduce those more familiar with
software engineering to biological systems and evolutionary theory concepts.

Conway's Game of Life cellular automaton is one of the most prominent examples
of cellular automata theory. The one dimensional program consists of a cell grid
typically with several dozen or more rows and similar number of columns. Each
cell on the grid has an on or off Boolean state. Every cell on the grid survives
or dies to the next generation depending on the game of life rules. If there are
too many neighbors surrounding a cell then the cell dies due to overcrowding. If
there is only one neighbor cell, the base cell dies due to under-population.
Activity on a particular cell is not interesting but when you run the entire
system for many generations, a group of patterns begin to form.

You may notice some common patterns in the figure. After so many iterations
through the game of life rules, only a few cells tend to stay alive. We started
with a large random number of alive cells and over time those cells died off. In
a controlled environment you may begin with carefully placed live cells and
monitor the patterns that emerge to model some other natural phenomena.

Summary

Moving forward if we want to study, analyze and work with artificial agents, we
might consider systems that have evolved behavior over a series of steps. We
might not build a specific tool with a specific purpose but the creature that is
built from the system may produce interesting properties which are unlike the
clean-room created software that we create today.

With this artificial life approach, but we also want to study the simple life
forms first before moving too fast forward like human behavior.


**keywords:** gameoflife, dna, java, scala

### Screenshots ###

![http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/Edit1ClassSimpleLifeDiagram.png](http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/Edit1ClassSimpleLifeDiagram.png)

![http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/Edit1PackageFullDiagram.png](http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/Edit1PackageFullDiagram.png)

![http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/moreScreenShotThruDemo.png](http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/moreScreenShotThruDemo.png)

![http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/screenshotDLife.png](http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/screenshotDLife.png)

![http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/screenshotLifeMoreReplication.png](http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/screenshotLifeMoreReplication.png)

![http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/withScreenshotMutation.png](http://doingitwrongnotebook.googlecode.com/svn/trunk/doingitwrong_phase2/scala2/GameOfLife/docs/media/withScreenshotMutation.png)

![http://doingitwrongnotebook.googlecode.com/svn/trunk/media/render_artificial_life.png](http://doingitwrongnotebook.googlecode.com/svn/trunk/media/render_artificial_life.png)