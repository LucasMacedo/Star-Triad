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
import com.aiyra.game.properties.ScenarioProperties;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author bltorres
 */
public class Scenario extends Entity
{
    private int xSpd;
    private int ySpd;
    
    public double fireFactor;
    public double iceFactor;
    public double stoneFactor;
    public double specialFactor;
    public double antimatterFactor;
    
    private static final int BASE_SPEED = 3;
    
    /** Creates a new instance of Scenario */
    public Scenario(int idScenario)
    {
        try
        {
            Image image = Image.createImage(
                this.getClass().getResourceAsStream(
                "/com/aiyra/game/resources/" + ScenarioProperties.backgrounds[idScenario] + ".png"));
            
            this.specialFactor = ScenarioProperties.specialFactors[idScenario];
            this.fireFactor = ScenarioProperties.fireFactors[idScenario];
            this.iceFactor = ScenarioProperties.iceFactors[idScenario];
            this.stoneFactor = ScenarioProperties.stoneFactors[idScenario];
            this.antimatterFactor = ScenarioProperties.antimatterFactors[idScenario];
            
            this.sprite = new Sprite(image);
            
            this.sprite.setTransform(GameDisplay.DISPLAY_ROTATION);
            
            this.xPos = Stage.MIN_XPOS;
            this.yPos = Stage.MIN_YPOS;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_NONE:
                this.xSpd = -BASE_SPEED;
                this.ySpd = 0;
                break;
            case Sprite.TRANS_ROT90:
                this.xSpd = 0;
                this.ySpd = -BASE_SPEED;
                this.xPos = Stage.MAX_XPOS;
                break;
            case Sprite.TRANS_ROT270:
                this.xSpd = 0;
                this.ySpd = BASE_SPEED;
                break;
        }
    }
    
    public void changeSpeed(int newSpeed)
    {
        int speed = BASE_SPEED + newSpeed;
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_NONE:
                this.xSpd = -speed;
                this.ySpd = 0;
                break;
            case Sprite.TRANS_ROT90:
                this.xSpd = 0;
                this.ySpd = -speed;
                break;
            case Sprite.TRANS_ROT270:
                this.xSpd = 0;
                this.ySpd = speed;
                break;
        }
    }
    
    public void update()
    {
        this.xPos += xSpd;
        this.yPos += ySpd;
    }
    
    public void draw(Graphics g)
    {
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_NONE:
                if (this.xPos + this.sprite.getWidth() < Stage.MAX_XPOS) 
                {
                    this.sprite.setRefPixelPosition(this.xPos + this.sprite.getWidth(), this.yPos);
                    this.sprite.paint(g);
                    if (this.sprite.getWidth() + this.xPos <= 0)
                        this.xPos = 0;
                }
                break;
           case Sprite.TRANS_ROT90:
                if (this.yPos + this.sprite.getHeight() < Stage.MAX_YPOS)
                {
                    this.sprite.setRefPixelPosition(this.xPos, this.yPos + this.sprite.getHeight());
                    this.sprite.paint(g);
                    if (this.yPos + this.sprite.getHeight() <= Stage.MIN_YPOS)
                        this.yPos = Stage.MIN_YPOS;
                }
                break;
            case Sprite.TRANS_ROT270:
                if (this.yPos - this.sprite.getHeight() > Stage.MAX_YPOS)
                {
                    this.sprite.setRefPixelPosition(this.xPos, this.yPos - this.sprite.getHeight());
                    this.sprite.paint(g);
                    if (this.yPos - this.sprite.getHeight() >= Stage.MIN_YPOS)
                        this.yPos = Stage.MIN_YPOS;
                }
                break;
        }
        
        sprite.setRefPixelPosition(xPos, yPos);
        sprite.paint(g);
    }
}
