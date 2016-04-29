# Introduction #

ProgramD is a fully functional AIML bot engine that is implemented with Java.  It supports multiple bots, it is easy to configure and runs in a GUI application and also under a J2EE environment.

AIML, or Artificial Intelligence Markup Language, is an XML dialect for creating natural language software agents.

When the AIML markup language is loaded for your bot, then you chat with the bot.  Once you become familiar with the AIML format, you can then build a simple personal assistant bot.  For Java developers, you may add Java keyword definitions or links to useful resources.

# Quickstart #

To use ProgramD, you can use the mirrored binary/source version provided on this site (programd**.zip) or visit**

http://www.aitools.org/Program_D

The version on this site includes jetty webserver configured with a programd web application.

# Launch the GUI application #

Navigate to:

![http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/launch_simple_gui.png](http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/launch_simple_gui.png)

the HOME\bin directory and click on the simple-gui-console.bat icon.

The ProgramD GUI application should launch.

![http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/programd_running.png](http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/programd_running.png)

At the prompt, enter the commands and dialog to interact with the bot.  I entered "hello" and "java".

# Configure #

The bots are configured in the PROGRAMD/conf/bots.xml file.

In the screenshot below, the ../aiml/alice/**.aiml and ../aiml/java/**.aiml  AIML files are loaded into the system.

![http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/aiml_files_bots.png](http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/aiml_files_bots.png)

The AIML XML dialect looks like other pattern matching formats.  An input pattern is defined and the bot will respond with the matching output.

![http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/aiml_example.png](http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/aiml_example.png)

# Running with jetty web servlet container #

Jetty is provided in the download.  It is configured for the programd webapp.  Navigate to the PROGRAMD/jetty/jetty directory and invoke start.bat or java -jar start.jar.  Jetty will launch.  Use your web browser and navigate to:

http://localhost:8080/programd

![http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/running_jetty2.png](http://ainotebook.googlecode.com/svn/trunk/programd/ProgramD/docs/media/running_jetty2.png)

# Download #

http://code.google.com/p/ainotebook/downloads/detail?name=programd-4.6b-src.zip

# Resources #

http://aitools.org/Main_Page

http://aitools.org/Getting_Started_with_Program_D

http://aitools.org/Free_AIML_sets