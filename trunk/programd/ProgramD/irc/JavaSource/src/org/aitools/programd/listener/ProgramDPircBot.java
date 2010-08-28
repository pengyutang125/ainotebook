/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.aitools.programd.listener;

import org.aitools.programd.Core;
import org.aitools.programd.util.XMLKit;
import org.apache.log4j.Logger;
import org.jibble.pircbot.PircBot;

/**
 * @author <a href="mailto:noel@aitools.org">Noel Bush</a>
 * @version 1.2
 */
public class ProgramDPircBot extends PircBot
{
    private Core core;

    private Logger logger = Logger.getLogger("programd.listener.irc");

    private String botID;

    /**
     * Creates a new ProgramDPircBot, using the given name.
     * 
     * @param nameToUse
     * @param coreToUse 
     * @param botIDToUse 
     */
    public ProgramDPircBot(String nameToUse, Core coreToUse, String botIDToUse)
    {
        this.core = coreToUse;
        this.botID = botIDToUse;
        this.setName(nameToUse);
    }

    /**
     * @see org.jibble.pircbot.PircBot#onPrivateMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    protected void onPrivateMessage(String sender, @SuppressWarnings("unused")
    String login, @SuppressWarnings("unused")
    String hostname, String message)
    {
        // Be sure the Core is ready.
        while (this.core.getStatus() != Core.Status.READY)
        {
            this.logger.warn("IRC listener: Waiting for Core to become ready.");
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                // Nothing to do.
            }
        }
        String[] response = XMLKit.filterViaHTMLTags(this.core.getResponse(message, sender, this.botID));
        if (response.length > 0)
        {
            for (int line = 0; line < response.length; line++)
            {
                sendMessage(sender, XMLKit.filterWhitespace(response[line]));
            }
        }
    }

    /**
     * Logs PircBot messages to our logger, instead of just dumping them to stdout.
     * Unfortunately we don't know whether the message is just info or is an error.
     * 
     * @see org.jibble.pircbot.PircBot#log(java.lang.String)
     */
    @Override
    public void log(String line)
    {
        this.logger.info(line);
    }
}
