/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.aitools.programd.listener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aitools.programd.Core;
import org.aitools.programd.bot.Bot;
import org.apache.log4j.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

/**
 * This is totally new and works with the PircBot API.
 * Note that this currently does *not* support the ShellCommandable interface.
 * 
 * @author <a href="mailto:noel@aitools.org">Noel Bush</a>
 * @version 1.2
 */

public class IRCListener implements Listener
{
    /** The Core object in use. */
    protected Core core;
    
    /** The PircBot that does the work. */
    protected PircBot pircBot;

    /** The id of the bot for which this listener works. */
    protected String botID;

    /** The parameters that can be set for this listener. */
    protected Map<String, String> parameters;

    /** The logger to use. */
    protected Logger logger = Logger.getLogger("programd.listener.irc");
    
    /** The host to connect to. */
    private String host;

    /** The nickname to use. */
    private String nick;
    
    /** The password to use. */
    private String password;

    /** The channel to connect to. */
    private String channel;

    /** The port to use. */
    private int port;

    /**
     * Creates a new IRCListener chat listener for a given bot.
     * 
     * @param coreToUse the Core object in use
     * @param bot the bot for whom to listen
     * @param parametersToUse the parameters for the listener and their default
     *            values
     * @throws InvalidListenerParameterException
     */
    public IRCListener(Core coreToUse, Bot bot, HashMap<String, String> parametersToUse) throws InvalidListenerParameterException
    {
        this.core = coreToUse;
        this.botID = bot.getID();
        this.parameters = parametersToUse;
        this.host = this.parameters.get("host");
        try
        {
            this.port = Integer.parseInt(this.parameters.get("port"));
        }
        catch (NumberFormatException e)
        {
            throw new InvalidListenerParameterException("Invalid port specification (try a number!)");
        }
        this.nick = this.parameters.get("nick");
        this.password = this.parameters.get("password");
        this.channel = this.parameters.get("channel");
    }

    /**
     * @see org.aitools.programd.listener.Listener#checkParameters()
     */
    public void checkParameters() throws InvalidListenerParameterException
    {
        if (this.host.length() == 0)
        {
            throw new InvalidListenerParameterException("No host specified.");
        }
        if (this.port <= 0)
        {
            throw new InvalidListenerParameterException("Invalid port.");
        }
        if (this.nick.length() == 0)
        {
            throw new InvalidListenerParameterException("No nick specified.");
        }
        if (this.password.length() == 0)
        {
            throw new InvalidListenerParameterException("No password specified.");
        }
        if (this.channel.length() == 0)
        {
            throw new InvalidListenerParameterException("No channel specified.");
        }
    }

    /**
     * @see org.aitools.programd.util.ManagedProcess#shutdown()
     */
    public void shutdown()
    {
        this.pircBot.disconnect();
    }

    /**
     * Connects to the given host and begins listening.
     */
    public void run()
    {
        this.logger.info(String.format("Starting for \"%s.\".", this.botID));
        this.pircBot = new ProgramDPircBot(this.nick, this.core, this.botID);
        this.pircBot.setVerbose(true);
        try
        {
            this.pircBot.connect(this.host, this.port, this.password);
        }
        catch (NickAlreadyInUseException e)
        {
            this.logger.error(String.format("Cannot connect: the nickname \"%s\" is already in use.  You must assign another.", this.nick));
            return;
        }
        catch (IOException e)
        {
            this.logger.error(String.format("Cannot connect: IO Exception when connecting: %s", e.getMessage()), e);
            return;
        }
        catch (IrcException e)
        {
            this.logger.error(String.format("Cannot connect: IRC Exception when connecting: %s", e.getMessage()), e);
            return;
        }
        this.pircBot.joinChannel(this.channel);
    }
}