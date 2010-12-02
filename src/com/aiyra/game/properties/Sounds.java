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

package com.aiyra.game.properties;

import com.aiyra.game.display.GameDisplay;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

/**
 *
 * @author Musmanno
 */
public class Sounds
{
        
    public static final int DASH = 0;
    public static final int EXPLOSION = 1;
    public static final int CHANGE_OPTION = 2;
    public static final int CHOOSE_OPTION = 3;
    public static final int HYPERSPEED = 4;
    
    public static final int MUSIC1 = 0;
    public static final int MUSIC2 = 1;
    public static final int MUSIC3 = 2;
    
    private Player dash;
    private Player explosion;
    private Player changeOption;
    private Player chooseOption;
    private Player hyperSpeed;
    
    private Player music1;
    private Player music2;
    private Player music3;
    
    private int currentMusic;
    
    public Sounds()
    {
        try
        {
            dash = Manager.createPlayer(this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/audio/dash.mid"), "audio/midi");
            dash.prefetch();
            
            explosion = Manager.createPlayer(this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/audio/explosion.mid"), "audio/midi");
            explosion.prefetch();
            
            hyperSpeed = Manager.createPlayer(this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/audio/hyper.mid"), "audio/midi");
            hyperSpeed.prefetch();
            
            changeOption = Manager.createPlayer(this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/audio/changeOption.mid"), "audio/midi");
            changeOption.prefetch();
            
            chooseOption = Manager.createPlayer(this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/audio/chooseOption.mid"), "audio/midi");
            chooseOption.prefetch();
            
            music1 = Manager.createPlayer(this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/audio/music1.mid"), "audio/midi");            
            
            music2 = Manager.createPlayer(this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/audio/music2.mid"), "audio/midi");
            
            music3 = Manager.createPlayer(this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/audio/music3.mid"), "audio/midi");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void playSound(int effect)
    {
        if (GameDisplay.SOUND)
        {
            try
            {
                switch (effect)
                {
                    case Sounds.DASH:
                        dash.start();
                        dash.setMediaTime(0);
                        break;
                    case Sounds.EXPLOSION:
                        explosion.start();
                        explosion.setMediaTime(0);
                        break;
                    case Sounds.CHANGE_OPTION:
                        changeOption.start();
                        changeOption.setMediaTime(0);
                        break;
                    case Sounds.CHOOSE_OPTION:
                        chooseOption.start();
                        chooseOption.setMediaTime(0);
                        break;
                    case Sounds.HYPERSPEED:
                        hyperSpeed.start();
                        hyperSpeed.setMediaTime(0);
                        break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void stopSound(int effect)
    {
        if (GameDisplay.SOUND)
        {
            try
            {
                switch (effect)
                {
                    case Sounds.DASH:
                        dash.stop();
                        dash.setMediaTime(0);
                        break;
                    case Sounds.EXPLOSION:
                        explosion.stop();
                        explosion.setMediaTime(0);
                        break;
                    case Sounds.CHANGE_OPTION:
                        changeOption.stop();
                        changeOption.setMediaTime(0);
                        break;
                    case Sounds.CHOOSE_OPTION:
                        chooseOption.stop();
                        chooseOption.setMediaTime(0);
                        break;
                    case Sounds.HYPERSPEED:
                        hyperSpeed.stop();
                        hyperSpeed.setMediaTime(0);
                        break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void playMusic(int music)
    {
        stopMusic(music);
        
        if (GameDisplay.SOUND)
        {
        	if (currentMusic == music)
        		music = music >= MUSIC3 ? MUSIC1 : music++;
            currentMusic = music;
            
            try
            {
                switch (music)
                {
                    case Sounds.MUSIC1:
                        music1.setLoopCount(-1);
                        music1.start();
                        music1.setMediaTime(0);
                        break;
                    case Sounds.MUSIC2:
                        music2.setLoopCount(-1);
                        music2.start();
                        music2.setMediaTime(0);
                        break;
                    case Sounds.MUSIC3:
                        music3.setLoopCount(-1);
                        music3.start();
                        music3.setMediaTime(0);
                        break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void stopMusic(int music)
    {
        if (GameDisplay.SOUND)
        {
            try
            {
                switch (music)
                {
                    case Sounds.MUSIC1:
                        if (music1.getState() == Player.STARTED)
                        {
                            music1.stop();
                            music1.setMediaTime(0);
                        }
                        break;
                    case Sounds.MUSIC2:
                        if (music2.getState() == Player.STARTED)
                        {
                            music2.stop();
                            music2.setMediaTime(0);
                        }
                        break;
                    case Sounds.MUSIC3:
                        if (music3.getState() == Player.STARTED)
                        {
                            music3.stop();
                            music3.setMediaTime(0);
                        }
                        break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void stopCurrentMusic()
    {
        stopMusic(currentMusic);
    }
    
    public void playMenuMusic()
    {
        if (music2.getState() != Player.STARTED)
        {
            playMusic(MUSIC2);
            currentMusic = MUSIC2;
        }
    }
}
