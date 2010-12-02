/*
 *            `.                               OBS: Se você fala somente português, há uma tradução deste cabeçalho e       
 *           o/-`                              das licenças na pasta "/licenses_pt-br" na raiz do pacote.    
 *           +ooo/-.                               
 *           -oooooo+-                         PROJECT: STARTRIAD: A SpaceShooter with a lot of Space and no Shooter! =D    
 *            +ooooooo/                        DESCRIPTION:  This file is part of a package that compose a JME game.     
 *            `+ooooooo-                       The purpose of this release is to help people wanting to learn how to make    
 *             `:+ooooo/                       games, or even port this game to their cellphones.    
 *    `.`````    `.:/++- `.---..`              Author: Aiyra (Laubisch-Cordeiro Computação e Tecnologia LTDA, Brazil),    
 *    `/ossssoo:`    `-/ossyyysss+:`           Distributed by Aiyra http://www.aiyra.com,    
 *      -oyyyyyys:  -osyyyyyyyyyyyys/`         Copyright (C) 2006.    
 *        ./ossso+`:syyyyyyyyyyyyyyyyo`        Version: $Id: Release Version, v 0.9 2010/12/02 15:38    
 *           ```` -yyyyyyyyyyyyyyyyyyyo        This code is distributed under the terms of The Q Public License 1.0.     
 *       .`-::-.` +yyyyyyyyyyyyyyyyyyyy`       For information about how you can use or modify this code, see    
 *     .:osyyyys- +yyyyyyyyyyyyyyyyyyyy`       http://opensource.org/licenses/qtpl.php or the file QPL1.txt that is in     
 *     `.:/++/:.  -yyyyyyyyyyyyyyyyyyyo        the code package.    
 *                 /syyyyyyyyyyyyyyyyo`        The media content of this package (music, sound effects, images, icons,     
 *                  -osyyyyyyyyyyyys/`         animations and game mechanics) is distributed under the term of the    
 *                    -/ossyyysss+:`           Creative Commons Licence 3.0. For information about how you can use or    
 *                       `.----.``             modify this content, see http://creativecommons.org/licenses/by/3.0/    
 *                                             or the file CC3.txt that is in the code package. 
 *                                             If you like this game, source code, art or anything, please visit 
 *                                             our website www.startriad.com and donate (if possible) =)
 */

package com.aiyra.game;

import com.aiyra.game.display.Commands;
import com.aiyra.game.display.GameDisplay;
import com.aiyra.game.system.Engine;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;

/**
 *
 * @author  Bruno Torres
 * @version
 */
public class Triade extends MIDlet implements CommandListener
{
    public Display display;
    
    public Engine engine;
    
    public GameDisplay canvas;
    
    public Triade()
    {
        this.canvas = GameDisplay.getInstance();
        this.canvas.setFullScreenMode(true);
        this.engine = new Engine(this.canvas);

        this.display = Display.getDisplay(this);
        
        this.canvas.addCommand(Commands.exitCommand);
        this.canvas.addCommand(Commands.menuCommand);
        this.canvas.setCommandListener(this);
    }
    
    public void startApp()
    {
        this.display.setCurrent(canvas);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public void commandAction(Command command, Displayable displayable)
    {
        if (Commands.exitCommand.equals(command))
        {
            try
            {
                this.engine.destroy();
                this.engine = null;
                this.canvas.resetScreens();
                destroyApp(true);
                notifyDestroyed();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        else if (Commands.menuCommand.equals(command))
        {
            this.engine.state = Engine.MENU;
            this.engine.destroyStages();
        }
    }
}
