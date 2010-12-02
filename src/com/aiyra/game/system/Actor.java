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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author bltorres
 */
public abstract class Actor extends Entity {
    
    public static final int FIRE = 0;
    public static final int STONE = 1;
    public static final int ICE = 2;
    
    public static final int ANTIMATTER = 3;
    
    public static final int SPECIAL = 4;
    
    protected static final Random rnd = new Random();
    
    protected Timer discardTimer = new Timer();
    
    protected TimerTask discardTask = new TimerTask() 
    {
        public void run() 
        {
            discard();
        }
    };
    
    protected abstract void discard();
    
    public static final Image[] explosion = new Image[1];
    
    static {
        try {
            explosion[0] = Image.createImage(Obstacle.class.getResourceAsStream("/com/aiyra/game/resources/explosao.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected int type = 0;
    
    public int xSpd;
    
    public int ySpd;
    
    public int xAcc;
    
    public int yAcc;
    
    public int MAX_XSPD;
    
    public int MAX_YSPD;
    
    public int MAX_XACC;
    
    public int MAX_YACC;
    
    /** Creates a new instance of Actor */
    public Actor(int type)
    {
        super();
        this.type = type;
    }
    
    public Actor()
    {
        this.type = Math.abs(rnd.nextInt())%3;
        
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_NONE:
                this.MAX_XSPD = 10;
                this.MAX_YSPD = 8;
                this.MAX_XACC = 5;
                this.MAX_YACC = 3;
                break;
            case Sprite.TRANS_ROT90:
            case Sprite.TRANS_ROT270:
                this.MAX_XSPD = 8;
                this.MAX_YSPD = 10;
                this.MAX_XACC = 3;
                this.MAX_YACC = 5;
                break;
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
    }
    
    public int getType()
    {
        return this.type;
    }
}
