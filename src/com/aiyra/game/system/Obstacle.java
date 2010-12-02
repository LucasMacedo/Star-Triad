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
import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author bltorres
 */
public class Obstacle extends Actor
{
    public static final int SMALL = 0;
    public static final int MEDIUM = 1;
    public static final int BIG = 2;
    
    public static final int ALIVE = 0;
    public static final int DESTROYED = 1;
    public static final int DISCARD = 2;
    
    public static final int VERTICAL_BASIC_SPEED = 2;
    public static final int HORIZONTAL_BASIC_SPEED = 2;
    
    private int size;
    
    private final int sizeOffsetOnList = 4;
    
    private int animationCounter = 0;
    
    private int animationFlagNumber = 1;
    
    private static final String[] fnames = {
        "bloco_fogo.png",
        "bloco_pedra.png",
        "bloco_gelo.png",
        "bloco_antimateria.png",
        "bloco_energia.png",
        "bloco_fogo_2.png",
        "bloco_fogo_3.png",
        "bloco_pedra_2.png",
        "bloco_pedra_3.png",
        "bloco_gelo_2.png",
        "bloco_gelo_3.png"
    };
    
    private static final int[][] imgSizes = {
        { 20, 20 },
        { 20, 20 },
        { 20, 20 },
        { 20, 20 },
        { 47, 45 },
        { 30, 30 },
        { 40, 40 },
        { 30, 30 },
        { 40, 40 },
        { 30, 30 },
        { 40, 40 }
    };
    
    private static final int[][] colRectStartPoints = {
        { 0, 0 },
        { 0, 0 },
        { 0, 0 },
        { 0, 0 },
        { 0, 0 },
        { 0, 0 },
        { 0, 0 },
        { 0, 0 },
        { 0, 0 },
        { 0, 0 },
        { 0, 0 }
    };
    
    private static final int[][] colRectSize = {
        { 20, 20 },
        { 20, 20 },
        { 20, 20 },
        { 20, 20 },
        { 55, 53 },
        { 30, 30 },
        { 40, 40 },
        { 30, 30 },
        { 40, 40 },
        { 30, 30 },
        { 40, 40 }
    };
    
    public static final Image[] images = new Image[11];
    
    static
    {
        try {
            for (int i = 0; i < fnames.length; ++i)
                images[i] = Image.createImage(Obstacle.class.getResourceAsStream("/com/aiyra/game/resources/" + fnames[i]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private int status = ALIVE;
    
    /** Creates a new instance of Obstacle */
    public Obstacle(int type, int size, int ySpd, int xSpd)
    {
        super(type);
        this.size = size;
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_ROT90:
                this.xSpd = -ySpd;
                break;
            case Sprite.TRANS_ROT270:
                this.xSpd = ySpd;
                break;
        }
        
        makeObstacle(type, xSpd);
    }
    
    private void makeObstacle(int type, int xSpd)
    {
        this.type = type;
        if (this.type >= SPECIAL)
        {
            this.sprite = new Sprite(images[SPECIAL], imgSizes[SPECIAL][0], imgSizes[SPECIAL][1]);
            this.sprite.setFrame(this.type - SPECIAL);
        }
        else
        {
            int imgIndex = this.type;
            if (this.size > 0 && this.type != 3)
                imgIndex += (sizeOffsetOnList + this.type) + this.size;
            this.sprite = new Sprite(images[imgIndex], imgSizes[imgIndex][0], imgSizes[imgIndex][1]);
            this.sprite.defineCollisionRectangle(colRectStartPoints[imgIndex][0], colRectStartPoints[imgIndex][1], colRectSize[imgIndex][0], colRectSize[imgIndex][1]);
        }
        
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_ROT90:
                if (this.xSpd == 0)
                {
                    int rndPos = Math.abs(rnd.nextInt())%(Stage.MAX_XPOS - this.sprite.getWidth());
                    
                    while (rndPos < Stage.MIN_XPOS)
                        rndPos = Math.abs(rnd.nextInt())%(Stage.MAX_XPOS - this.sprite.getWidth());
                    
                    this.xPos = rndPos;
                    this.yPos = Stage.MAX_YPOS + this.sprite.getHeight();
                }
                else
                {
                    this.xPos = (this.xSpd > 0) ? (Stage.MIN_XPOS - this.sprite.getWidth()) : Stage.MAX_XPOS;
                    this.yPos = Stage.MAX_YPOS / 3 + Math.abs(rnd.nextInt())%(2 * Stage.MAX_YPOS / 3);
                }
                this.ySpd = -xSpd;
                break;
            case Sprite.TRANS_ROT270:
                if (this.xSpd == 0)
                {
                    int rndPos = Math.abs(rnd.nextInt())%(Stage.MAX_XPOS - this.sprite.getWidth());
                    
                    while (rndPos < Stage.MIN_XPOS)
                        rndPos = Math.abs(rnd.nextInt())%(Stage.MAX_XPOS - this.sprite.getWidth());
                    
                    this.xPos = rndPos;
                    this.yPos = Stage.MAX_YPOS - this.sprite.getHeight();
                }
                else
                {
                    this.xPos = (this.xSpd > 0) ? (Stage.MIN_XPOS - this.sprite.getWidth()) : Stage.MAX_XPOS;
                    this.yPos = Stage.MIN_YPOS / 3 - Math.abs(rnd.nextInt())%(2 * Stage.MIN_YPOS / 3);
                }
                this.ySpd = xSpd;
                break;
        }
    }
    
    public int status()
    {
        return this.status;
    }
    
    public void destroy() 
    {
        if (this.type < SPECIAL)
        {
            this.status = DESTROYED;
            
            switch (GameDisplay.DISPLAY_ROTATION)
            {
                case Sprite.TRANS_ROT90:
                    this.xPos += 15 + this.sprite.getWidth();
                    this.yPos -= 15 + this.sprite.getHeight(); 
                    break;
                case Sprite.TRANS_ROT270:
                    this.xPos -= this.sprite.getWidth();
                    this.yPos += 15 + this.sprite.getHeight();
                    break;
            }
            
            this.sprite.setImage(explosion[0], 93, 64);
            this.sprite.setTransform(GameDisplay.DISPLAY_ROTATION);
            
            this.discardTimer.schedule(discardTask, 0, 150);
        }
        else
            this.status = DISCARD;
    }
    
    protected void discard()
    {
        this.sprite.nextFrame();
        if (this.sprite.getFrame() == 0)
        {
            this.status = DISCARD;
            this.discardTimer.cancel();
        }
    }
    
    public void update()
    {
        if (Math.abs(this.xAcc) > MAX_XACC)
            this.xAcc = MAX_XACC * (this.xAcc / this.xAcc);
        if (Math.abs(this.yAcc) > MAX_YACC)
            this.yAcc = MAX_YACC * (this.yAcc / this.yAcc);
        
        if (Math.abs(this.xSpd + this.xAcc) <= this.MAX_XSPD)
            this.xSpd += this.xAcc;
        if (Math.abs(this.ySpd + this.yAcc) <= this.MAX_YSPD)
            this.ySpd += this.yAcc;
        
        this.xPos += this.xSpd;
        this.yPos += this.ySpd;
        
        animationCounter++;
        
        if ((animationCounter >= animationFlagNumber) && (this.type < SPECIAL) && (this.status != DESTROYED))
        {
            changeFrame();
            animationCounter = 0;
        }
    }
    
    public void changeFrame()
    {
        this.sprite.nextFrame();
    }
}
