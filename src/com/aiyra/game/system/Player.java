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

import com.aiyra.game.display.GameDisplay;
import com.aiyra.game.properties.Sounds;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author bltorres
 */
public class Player extends Actor
{
    private final class MergeTask extends TimerTask
    {
        public void run()
        {
            animateMerge();
        }
    }
    
    public static final int ACCEL_HORIZONTAL = 3;
    public static final int ACCEL_VERTICAL = 2;
    
    public static final int UP = 0;
    public static final int NORMAL = 1;
    public static final int DOWN = 2;
    public static final int LAST = 6;
    
    public static final int[][] DMG_TABLE = {
        { 1, 3, 3 }, // FIRE
        { 3, 1, 3 }, // ROCK
        { 3, 3, 1 }  // ICE
    };
    
    private static final String[] fnames = {
        "nave_fogo.png",
        "nave_pedra.png",
        "nave_gelo.png"
    };
    
    private static final int[][] imgSizes = {
        { 40, 25 },
        { 35, 30 },
        { 45, 20 }
    };
    
    public static final Image[] images = new Image[3];
    
    private Timer eventTimer;
    private MergeTask mergeTask;
    
    private int lastMergeXPos;
    private int lastMergeYPos;
    
    static
    {
        try
        {
            for (int i = 0; i < fnames.length; ++i)
            {
                images[i] = Image.createImage(Player.class.getResourceAsStream(
                        "/com/aiyra/game/resources/" + fnames[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static final int MAX_LIFE = 10;
    public int life;
    
    public boolean invincible = false;
    public boolean hyperSpeed = false;
    
    /** Creates a new instance of Player */
    public Player()
    {
        super();
        this.type = STONE;
        this.sprite = new Sprite(images[this.type], imgSizes[this.type][0],
                imgSizes[this.type][1]);
        this.sprite.setFrame(NORMAL);
        
        init();
    }
    
    Timer animationTimer;
    TimerTask animationTask;
    
    public void animateDash(int dashFrameDelay)
    {
        animationTimer = new Timer();
        animationTask = new TimerTask()
        {
            int factor = 1;
            
            public void run()
            {
                sprite.setFrame(sprite.getFrame() + factor);
                
                if (sprite.getFrame() == LAST || sprite.getFrame() == DOWN)
                    factor *= -1;
                
                if (sprite.getFrame() <= DOWN)
                    sprite.setFrame(DOWN + 1);
            }
        };
        
        animationTimer.schedule(animationTask, 0, dashFrameDelay);
    }
    
    public void destroyDashAnimation()
    {
        animationTimer.cancel();
        animationTask.cancel();
        animationTimer = null;
        animationTask = null;
    }
    
    public void init()
    {
        life = MAX_LIFE;
        this.invincible = false;
        this.hyperSpeed = false;
        
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_ROT90:
                this.sprite.setTransform(Sprite.TRANS_ROT90);
                this.xPos = Stage.MIN_XPOS + Stage.DEFAULT_HEIGHT / 2;
                this.yPos = Stage.MIN_YPOS;
                break;
            case Sprite.TRANS_ROT270:
                this.sprite.setTransform(Sprite.TRANS_ROT270);
                this.xPos = Stage.MIN_XPOS + Stage.DEFAULT_HEIGHT / 2;
                this.yPos = Stage.MIN_YPOS;
                break;
        }
        
        this.sprite.setRefPixelPosition(xPos, yPos);
    }
    
    public void setDirection(int direction)
    {
        this.sprite.setFrame(direction);
    }
    
    public int collidesWith(Vector obstacles)
    {
        if (obstacles != null) {
            for (int i = 0; i < obstacles.size(); ++i)
            {
                Obstacle o = (Obstacle) obstacles.elementAt(i);
                
                if (o.status() == Obstacle.ALIVE && this.sprite.collidesWith(o.sprite, true))
                    return i;
            }
        }
        
        return -1;
    }
    
    public boolean processHit(Obstacle obstacle)
    {
        if (this.invincible)
            return false;
        
        if ((obstacle.getType() & SPECIAL) != 0)
        {
            GameDisplay.getInstance().getSounds().playSound(Sounds.CHANGE_OPTION);
            processMerge(obstacle.getType() - SPECIAL);
        }
        else
        {
            GameDisplay.getInstance().getSounds().playSound(Sounds.EXPLOSION);
            
            if (obstacle.getType() == ANTIMATTER)
                this.life = 0;
            else
                 this.life -= DMG_TABLE[this.type][obstacle.getType()];
        }
        
        
        boolean died = (this.life <= 0);

        if (died)
        {
            this.sprite.setImage(explosion[0], 93, 64);
            this.discardTimer.schedule(discardTask, 0, 150);
        }
        
        return died;
    }
    
    public void processMerge(int type)
    {
        this.invincible = true;
        if (type == this.type)
        {
            this.life = MAX_LIFE;
            this.invincible = false;
        }
        else
        {
            
            this.type = type;
            
            switch (GameDisplay.DISPLAY_ROTATION)
            {
                case Sprite.TRANS_ROT90:
                    lastMergeXPos = this.sprite.getWidth();
                    lastMergeYPos = -this.sprite.getHeight();
                    break;
                case Sprite.TRANS_ROT270:
                    lastMergeXPos = -this.sprite.getWidth();
                    lastMergeYPos = this.sprite.getHeight();
                    break;
            }
            
            this.xPos += this.lastMergeXPos;
            this.yPos += this.lastMergeYPos;
            
            this.sprite.setImage(explosion[0], 93, 64);
            
            this.eventTimer = new Timer();
            this.mergeTask = new MergeTask();
            this.eventTimer.schedule(mergeTask, 50, 100);
        }
    }
    
    public void animateMerge()
    {
        GameDisplay.getInstance().getSounds().playSound(Sounds.CHANGE_OPTION);
        this.sprite.nextFrame();
        
        if (this.sprite.getFrame() == 0)
        {
            this.eventTimer.cancel();
            
            this.xPos -= this.lastMergeXPos;
            this.yPos -= this.lastMergeYPos;
            
            this.sprite = new Sprite(images[this.type], imgSizes[this.type][0], imgSizes[this.type][1]);
            this.sprite.setFrame(NORMAL);
            this.sprite.setTransform(GameDisplay.DISPLAY_ROTATION);
            
            this.invincible = false;
        }
    }
    
    public void update()
    {
        super.update();

        if (!hyperSpeed)
        {
            switch (GameDisplay.DISPLAY_ROTATION)
            {
                case Sprite.TRANS_ROT90:
                    this.xPos = Math.min(Math.max(this.xPos, Stage.MIN_XPOS + this.sprite.getWidth()), Stage.MAX_XPOS);
                    this.yPos = Math.min(Math.max(this.yPos, Stage.MIN_YPOS), Stage.MAX_YPOS - this.sprite.getHeight());

                    if (this.xPos == Stage.MIN_XPOS + this.sprite.getWidth() || this.xPos == Stage.MAX_XPOS)
                        this.xSpd = 0;
                    if (this.yPos == Stage.MIN_YPOS || this.yPos == Stage.MAX_YPOS - this.sprite.getHeight())
                        this.ySpd = 0;
                    break;
                case Sprite.TRANS_ROT270:
                    this.xPos = Math.min(Math.max(this.xPos, Stage.MIN_XPOS), Stage.MAX_XPOS - this.sprite.getWidth());
                    this.yPos = Math.min(Math.max(this.yPos, Stage.MAX_YPOS + this.sprite.getHeight()), Stage.MIN_YPOS);

                    if (this.xPos == Stage.MIN_XPOS || this.xPos == Stage.MAX_XPOS - this.sprite.getWidth())
                        this.xSpd = 0;
                    if (this.yPos == Stage.MAX_YPOS + this.sprite.getHeight() || this.yPos == Stage.MIN_YPOS)
                        this.ySpd = 0;
                    break;
            }
        }
        
        if (this.xAcc == 0 && Math.abs(this.xSpd) > 0)
        {
            if (this.xSpd > 0)
                this.xSpd -= 1;
            else
                this.xSpd += 1;
        }
        if (this.yAcc == 0 && Math.abs(this.ySpd) > 0)
        {
            if (this.ySpd > 0)
                this.ySpd -= 1;
            else
                this.ySpd += 1;
        }
    }
    
    protected void discard()
    {
        this.sprite.nextFrame();
        if (this.sprite.getFrame() == 0) 
            this.discardTimer.cancel();
    }
}
