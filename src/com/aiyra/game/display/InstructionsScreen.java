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

package com.aiyra.game.display;

import com.aiyra.game.system.Engine;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Musmanno
 */
public class InstructionsScreen implements Displayable
{
	private Sprite background0Sprite;
    private Sprite background1Sprite;
    private Sprite background2Sprite;
    
    public int screen = 0;
    
    private Sprite nextSprite;
    private Sprite menuSprite;

    public InstructionsScreen()
    {
    	Image background0 = null;
        Image background1 = null;
        Image background2 = null;
        Image next = null;
        Image menu = null;
        
        try
        {
        	background0 = Image.createImage(InstructionsScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaIntro.png"));
            background1 = Image.createImage(InstructionsScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaInstrucoes.png"));
            background2 = Image.createImage(InstructionsScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaInstrucoes2.png"));
            next = Image.createImage(InstructionsScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/next.png"));
            menu = Image.createImage(InstructionsScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/menu.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        background0Sprite = new Sprite(background0);
        background1Sprite = new Sprite(background1);
        background2Sprite = new Sprite(background2);
        
        nextSprite = new Sprite(next);
        menuSprite = new Sprite(menu);
        
        nextSprite.defineReferencePixel(nextSprite.getWidth()/2, 0);
        nextSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth()/2, GameDisplay.getInstance().getHeight() - (nextSprite.getHeight() + 10));
        
        menuSprite.defineReferencePixel(menuSprite.getWidth()/2, 0);
        menuSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth()/2, GameDisplay.getInstance().getHeight() - (menuSprite.getHeight() + 10));
        
        changeRotation(GameDisplay.DISPLAY_ROTATION);
    }
    
    public void changeRotation(int rotation)
    {
        switch (rotation)
        {
            case Sprite.TRANS_ROT90:
            	background0Sprite.setRefPixelPosition(GameDisplay.getInstance().getWidth(), 0);
                background1Sprite.setRefPixelPosition(GameDisplay.getInstance().getWidth(), 0);
                background2Sprite.setRefPixelPosition(GameDisplay.getInstance().getWidth(), 0);
                break;
            case Sprite.TRANS_ROT270:
            	background0Sprite.setRefPixelPosition(0, GameDisplay.getInstance().getHeight());
                background1Sprite.setRefPixelPosition(0, GameDisplay.getInstance().getHeight());
                background2Sprite.setRefPixelPosition(0, GameDisplay.getInstance().getHeight());
                break;
        }

        background0Sprite.setTransform(rotation);
        background1Sprite.setTransform(rotation);
        background2Sprite.setTransform(rotation);
    }

    public void draw(Graphics g)
    {
    	switch (screen) {
		case 0:
			this.background0Sprite.paint(g);
            this.nextSprite.paint(g);
			break;
		case 1:
			this.background1Sprite.paint(g);
            this.nextSprite.paint(g);
			break;
		case 2:
			this.background2Sprite.paint(g);
            menuSprite.paint(g);	
			break;		
		}        
    }
    
    public void changeScreen()
    {
        if (this.screen < 2)
            this.screen++;
        else
            GameDisplay.getInstance().getEngine().state = Engine.MENU;
    }
}