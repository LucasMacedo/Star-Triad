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

package com.aiyra.game.system;

import com.aiyra.game.display.Displayable;
import com.aiyra.game.display.GameDisplay;
import com.aiyra.game.properties.ScenarioProperties;
import com.aiyra.game.properties.Sounds;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author bltorres
 */
public class Stage implements Displayable
{
    public static int DEFAULT_HEIGHT = GameDisplay.DEFAULT_HEIGHT;
    
    public static int MIN_YPOS;
    public static int MAX_YPOS;
    public static int MIN_XPOS;
    public static int MAX_XPOS;
    
    private int maxObstacles = 20;
    
    private Vector obstacles = new Vector();
    private Vector discardPile = new Vector();
    
    private Scenario scenario;
    
    private double specialPercentage;
    private double firePercentage;
    private double icePercentage;
    private double stonePercentage;
    private double antimatterPercentage;
    
    public int baseSpeed;
    private int speedCounter;
    public int speedCounterLimit;
    
    public int state;
    
    public static final int DEAD = 0;
    public static final int ALIVE = 1;

    private TimerTask spawnTask;
    private Timer spawnTimer;
    
    public int idScenario;
    public String name;
    
    /** Creates a new instance of Stage */
    public Stage(int idScenario)
    {
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_ROT90:
                MIN_YPOS = 0;
                MAX_YPOS = GameDisplay.getDisplayHeight();
                MIN_XPOS = (GameDisplay.getDisplayWidth() - DEFAULT_HEIGHT) / 2;
                MAX_XPOS = MIN_XPOS + DEFAULT_HEIGHT;
                break;
            case Sprite.TRANS_ROT270:
                MIN_YPOS = GameDisplay.getDisplayHeight();
                MAX_YPOS = 0;
                MIN_XPOS = (GameDisplay.getDisplayWidth() - DEFAULT_HEIGHT) / 2;
                MAX_XPOS = MIN_XPOS + DEFAULT_HEIGHT;
                break;
        }
        
        this.idScenario = idScenario;
        this.name = ScenarioProperties.names[this.idScenario];
        init();
    }
    
    public void draw(Graphics g)
    {
        this.scenario.draw(g);
        for (int i = 0; i < this.obstacles.size(); ++i)
            ((Entity) this.obstacles.elementAt(i)).draw(g);
    }
    
    public void generateObstacle()
    {
        if (this.obstacles.size() < maxObstacles)
        {
            int type = 0;
            int size = 0;
            int ySpd = 0;
            float rndPercentage = (Math.abs(Engine.rnd.nextInt())%101)/100f;
            int rndInt = Math.abs(Engine.rnd.nextInt()) % 3;
            int v = Obstacle.VERTICAL_BASIC_SPEED + rndInt;
            
            if (rndPercentage > specialPercentage)
            {
                type = Obstacle.SPECIAL;
                rndPercentage = (Math.abs(Engine.rnd.nextInt())%101)/100f;
                if (rndPercentage > 0.67)
                    type += Obstacle.FIRE;
                else if (rndPercentage > 0.33)
                    type += Obstacle.ICE;
                else
                    type += Obstacle.STONE;
            }
            else if (rndPercentage > firePercentage)
                type = Obstacle.FIRE;
            else if (rndPercentage > icePercentage)
                type = Obstacle.ICE;
            else if (rndPercentage > stonePercentage)
                type = Obstacle.STONE;
            else if (rndPercentage > antimatterPercentage)
                type = Obstacle.ANTIMATTER;
            
            rndPercentage = (Math.abs(Engine.rnd.nextInt())%101)/100f;
            if (rndPercentage > 0.50)
                size = Obstacle.SMALL;
            else if (rndPercentage > 0.15)
                size = Obstacle.MEDIUM;
            else
                size = Obstacle.BIG;
            
            Obstacle o = null;
            
            rndInt = Math.abs(Engine.rnd.nextInt())%3;
            if (rndInt == 0)
                ySpd = v;
            else
                ySpd = -v;

            int xSpd = Math.abs(Engine.rnd.nextInt())%9 + Obstacle.HORIZONTAL_BASIC_SPEED + baseSpeed;
            
            rndInt = Math.abs(Engine.rnd.nextInt())%3;
            if (rndInt == 0)
                o = new Obstacle(type, size, ySpd, baseSpeed == 0? xSpd : xSpd/2);
            else
                o = new Obstacle(type, size, 0, xSpd);

            this.obstacles.addElement(o);
        }
    }
    
    public void update()
    {
        if (this.speedCounter >= this.speedCounterLimit)
        {
            this.speedCounter = 0;
            ++this.baseSpeed;
            this.scenario.changeSpeed(this.baseSpeed);
        }
        else
            ++this.speedCounter;
        
        this.scenario.update();
        
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_ROT90:
                for (int i = 0; i < this.obstacles.size(); ++i)
                {
                    Obstacle currEntity = ((Obstacle) this.obstacles.elementAt(i));

                    currEntity.update();

                    if (currEntity.status() == Obstacle.DISCARD || currEntity.yPos + currEntity.sprite.getHeight() < MIN_YPOS || currEntity.xPos + currEntity.sprite.getWidth() + 10 < MIN_XPOS || currEntity.xPos - currEntity.sprite.getWidth() - 10 > MAX_XPOS)
                        discardPile.addElement(currEntity);
                }
                break;
            case Sprite.TRANS_ROT270:
                for (int i = 0; i < this.obstacles.size(); ++i)
                {
                    Obstacle currEntity = ((Obstacle) this.obstacles.elementAt(i));

                    currEntity.update();

                    if (currEntity.status() == Obstacle.DISCARD || currEntity.yPos - currEntity.sprite.getHeight() > MIN_YPOS || currEntity.xPos + currEntity.sprite.getWidth() + 10 < MIN_XPOS || currEntity.xPos - 10 > MAX_XPOS)
                        discardPile.addElement(currEntity);
                }
                break;
        }
        
        for (int i = 0; i < discardPile.size(); ++i)
            this.obstacles.removeElement(discardPile.elementAt(i));
        
        this.discardPile.removeAllElements();
    }
    
    public Vector getVisibleObstacles()
    {
        return this.obstacles;
    }
    
    public Obstacle getObstacleAt(int i)
    {
        return (Obstacle) this.obstacles.elementAt(i);
    }
    
    public void destroyElement(int i)
    {
        ((Obstacle) this.obstacles.elementAt(i)).destroy();
    }
    
    public void init()
    {
        if (this.state != ALIVE)
        {
            this.spawnTask = new TimerTask()
            {
                public void run()
                {
                    generateObstacle();
                }
            };

            this.spawnTimer = new Timer();
            spawnTimer.schedule(spawnTask, 0, 500);

            this.scenario = new Scenario(idScenario);
            
            specialPercentage = 1.0 - this.scenario.specialFactor;
            firePercentage = specialPercentage - this.scenario.fireFactor;
            icePercentage = firePercentage - this.scenario.iceFactor;
            stonePercentage = icePercentage - this.scenario.stoneFactor;
            antimatterPercentage = stonePercentage - this.scenario.antimatterFactor;

            GameDisplay.getInstance().getSounds().stopCurrentMusic();
            GameDisplay.getInstance().getSounds().playMusic(Math.abs(Engine.rnd.nextInt())%(Sounds.MUSIC3 + 1));
            

            this.speedCounterLimit = 200;
            
            this.state = ALIVE;
        }
    }
    
    public void destroy()
    {
        if (this.state == ALIVE)
        {
            GameDisplay.getInstance().getSounds().stopCurrentMusic();

            this.obstacles.removeAllElements();
            this.discardPile.removeAllElements();
            this.spawnTimer.cancel();
            this.spawnTimer = null;
            this.spawnTask = null;

            this.state = DEAD;
        }
    }
}
