/*
 *            `.                               OBS: Se voc� fala somente portugu�s, h� uma tradu��o deste cabe�alho e       
 *           o/-`                              das licen�as na pasta "/licenses_pt-br" na raiz do pacote.    
 *           +ooo/-.                               
 *           -oooooo+-                         PROJECT: STARTRIAD: A SpaceShooter with a lot of Space and no Shooter! =D    
 *            +ooooooo/                        DESCRIPTION:  This file is part of a package that compose a JME game.     
 *            `+ooooooo-                       The purpose of this release is to help people wanting to learn how to make    
 *             `:+ooooo/                       games, or even port this game to their cellphones.    
 *    `.`````    `.:/++- `.---..`              Author: Aiyra (Laubisch-Cordeiro Computa��o e Tecnologia LTDA, Brazil),    
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

package com.aiyra.game.display;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Musmanno
 */
public class CreditsScreen implements Displayable
{
    private Sprite backgroundSprite;

    public CreditsScreen()
    {
        Image background = null;
        
        try
        {
            background = Image.createImage(CreditsScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaCreditos.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        backgroundSprite = new Sprite(background);
        changeRotation(GameDisplay.DISPLAY_ROTATION);
    }
    
    public void changeRotation(int rotation)
    {
        switch (rotation)
        {
            case Sprite.TRANS_ROT90:
                backgroundSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth(), 0);
                break;
            case Sprite.TRANS_ROT270:
                backgroundSprite.setRefPixelPosition(0, GameDisplay.getInstance().getHeight());
                break;
        }

        backgroundSprite.setTransform(rotation);
    }

    public void draw(Graphics g)
    {
        this.backgroundSprite.paint(g);
    }
}