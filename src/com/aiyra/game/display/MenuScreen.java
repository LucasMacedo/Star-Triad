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

import com.aiyra.game.properties.Sounds;
import com.aiyra.game.system.Engine;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Musmanno
 */
public class MenuScreen implements Displayable
{
    private Sprite spriteBackground;
    private Sprite pressKeySprite;
    
    private Sprite[] spriteOptions;
    
    private int option;
    
    private Sprite arrowUp;
    private Sprite arrowDown;
    
    public boolean available = false;
    public boolean splash = true;
    
    private int blinkCount = 10;
    private int splashCount = 0;
    
    private Sprite aiyraSprite;
    
    private Sprite onSprite;
    private Sprite offSprite;

    public MenuScreen()
    {
        Image image = Image.createImage(GameDisplay.getInstance().getHeight() + 10, GameDisplay.getInstance().getWidth() + 10);
        Graphics graphics = image.getGraphics();
        
        Image background = null;
        Image arrow = null;
        Image play = null;
        Image rotate = null;
        Image highscore = null;
        Image credits = null;
        Image sound = null;
        Image instructions = null;
        Image pressAnyKey = null;
        Image aiyra = null;
        Image on = null;
        Image off = null;
        
        
        try
        
        {
            background = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaMenu.png"));
            arrow = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/seta.png"));
            play = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/play.png"));
            rotate = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/rotate.png"));
            highscore = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/highscore.png"));
            credits = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/credits.png"));
            sound = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/sound.png"));
            instructions = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/instructions.png"));
            pressAnyKey = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/press any key.png"));
            aiyra = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/aiyra.png"));
            on = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/on.png"));
            off = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/off.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("StarTriad resources could not be loaded.");
        }
        
        graphics.drawImage(background, 0, 0, Graphics.TOP | Graphics.LEFT);
        
        spriteBackground = new Sprite(image);
        
        
        spriteOptions = new Sprite[6];
        
        spriteOptions[0] = new Sprite(play);
        spriteOptions[1] = new Sprite(sound);
        spriteOptions[2] = new Sprite(rotate);
        spriteOptions[3] = new Sprite(instructions);
        spriteOptions[4] = new Sprite(highscore);
        spriteOptions[5] = new Sprite(credits);
        
        for (int i = 0; i < spriteOptions.length; i++)
            spriteOptions[i].defineReferencePixel(spriteOptions[i].getWidth()/2, spriteOptions[i].getHeight()/2);
        
        arrowUp = new Sprite(arrow);
        arrowDown = new Sprite(arrow);
        
        pressKeySprite = new Sprite(pressAnyKey);
        pressKeySprite.defineReferencePixel(pressKeySprite.getWidth()/2, pressKeySprite.getHeight()/2);
        aiyraSprite = new Sprite(aiyra);
        
        onSprite = new Sprite(on);
        offSprite = new Sprite(off);
        
        changeRotation(GameDisplay.DISPLAY_ROTATION);
    }
    
    public void changeRotation(int rotation)
    {
        switch (rotation)
        {
            case Sprite.TRANS_ROT90:
                //FIXME: GameDisplay.getInstance().getWidth() - 1 resolve problema de uma linha branca do lado da tela
                spriteBackground.setRefPixelPosition(GameDisplay.getInstance().getWidth() - 1, 0);
                arrowUp.setRefPixelPosition(GameDisplay.getInstance().getWidth() / 3 - 12, GameDisplay.getInstance().getHeight() / 2);
                arrowDown.setRefPixelPosition(GameDisplay.getInstance().getWidth() / 3 - 60, GameDisplay.getInstance().getHeight() / 2 + arrowUp.getWidth());
                arrowDown.setTransform(Sprite.TRANS_ROT270);
                arrowUp.setTransform(Sprite.TRANS_ROT90);
                
                for (int i = 0; i < spriteOptions.length; ++i)
                {
                    spriteOptions[i].setRefPixelPosition(GameDisplay.getInstance().getWidth() / 3 - 35, GameDisplay.getInstance().getHeight()/2 + arrowUp.getWidth()/2);
                    spriteOptions[i].setTransform(rotation);
                }
                
                pressKeySprite.setRefPixelPosition(GameDisplay.getInstance().getWidth() / 3 - 35, GameDisplay.getInstance().getHeight()/2 + arrowUp.getWidth()/2);
                
                aiyraSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth() - 1, 0);
                
                onSprite.setRefPixelPosition(spriteOptions[1].getRefPixelX() + spriteOptions[1].getWidth()/2, spriteOptions[1].getRefPixelY() + (spriteOptions[1].getHeight()/2 + 10));
                offSprite.setRefPixelPosition(spriteOptions[1].getRefPixelX() + spriteOptions[1].getWidth()/2, spriteOptions[1].getRefPixelY() + (spriteOptions[1].getHeight()/2 + 10));
                break;
            case Sprite.TRANS_ROT270:
                spriteBackground.setRefPixelPosition(0, GameDisplay.getInstance().getHeight());
                arrowUp.setRefPixelPosition(GameDisplay.getInstance().getWidth() - (GameDisplay.getInstance().getWidth() / 3 - 12), GameDisplay.getInstance().getHeight() / 2 + arrowDown.getHeight());
                arrowDown.setRefPixelPosition(GameDisplay.getInstance().getWidth() - (GameDisplay.getInstance().getWidth() / 3 - 60), GameDisplay.getInstance().getHeight()/2);
                arrowUp.setTransform(Sprite.TRANS_ROT270);
                arrowDown.setTransform(Sprite.TRANS_ROT90);
                
                for (int i = 0; i < spriteOptions.length; ++i)
                {
                    spriteOptions[i].setRefPixelPosition(GameDisplay.getInstance().getWidth() - (GameDisplay.getInstance().getWidth() / 3 - 35), GameDisplay.getInstance().getHeight()/2 + arrowUp.getWidth()/2);
                    spriteOptions[i].setTransform(rotation);
                }
                
                pressKeySprite.setRefPixelPosition(GameDisplay.getInstance().getWidth() - (GameDisplay.getInstance().getWidth() / 3 - 35), GameDisplay.getInstance().getHeight()/2 + arrowUp.getWidth()/2);
                
                aiyraSprite.setRefPixelPosition(0, GameDisplay.getInstance().getHeight());
                
                onSprite.setRefPixelPosition(spriteOptions[1].getRefPixelX() - spriteOptions[1].getWidth()/2, spriteOptions[1].getRefPixelY() - (spriteOptions[1].getHeight()/2 + 10));
                offSprite.setRefPixelPosition(spriteOptions[1].getRefPixelX() - spriteOptions[1].getWidth()/2, spriteOptions[1].getRefPixelY() - (spriteOptions[1].getHeight()/2 + 10));
                break;
        }
        
        spriteBackground.setTransform(rotation);
        pressKeySprite.setTransform(rotation);
        aiyraSprite.setTransform(rotation);
        onSprite.setTransform(rotation);
        offSprite.setTransform(rotation);
    }

    public void draw(Graphics g)
    {
        GameDisplay.getInstance().getSounds().playMenuMusic();
        
        this.spriteBackground.paint(g);
        
        if (splash)
        {
            splashCount++;
            
            if (splashCount >= 50)
                splash = false;
            
            this.aiyraSprite.paint(g);
        }
        else
        {
            if (available)
            {
                this.spriteOptions[option].paint(g);
                if (this.option > 0)
                    this.arrowUp.paint(g);
                if (this.option + 1 < this.spriteOptions.length)
                this.arrowDown.paint(g);
                
                if (this.option == 1)
                {
                    if (GameDisplay.SOUND)
                        this.onSprite.paint(g);
                    else
                        this.offSprite.paint(g);
                }
            }
            else
            {
                blinkCount++;

                if (blinkCount >= 10)
                    this.pressKeySprite.paint(g);

                if (blinkCount == 20)
                    blinkCount = 0;
            }
        }
    }
    
    public void chooseOption()
    {
        GameDisplay display = GameDisplay.getInstance();
        
        display.getSounds().playSound(Sounds.CHOOSE_OPTION);
        
        switch (option)
        {
            case 0:
                available = false;
                display.resetScreens();
                display.getEngine().initGame();
                break;
            case 1:
                display.getSounds().stopCurrentMusic();
                GameDisplay.SOUND = !GameDisplay.SOUND;
                break;
            case 2:
                if (GameDisplay.DISPLAY_ROTATION == Sprite.TRANS_ROT90)
                {
                    display.setRotation(Sprite.TRANS_ROT270);
                    changeRotation(Sprite.TRANS_ROT270);
                    if (display.interludeScreen != null)
                        display.interludeScreen.changeRotation(Sprite.TRANS_ROT270);
                    if (display.gameOverScreen != null)
                        display.gameOverScreen.changeRotation(Sprite.TRANS_ROT270);
                    if (display.highScoreScreen != null)
                        display.highScoreScreen.changeRotation(Sprite.TRANS_ROT270);
                    if (display.creditsScreen != null)
                        display.creditsScreen.changeRotation(Sprite.TRANS_ROT270);
                }
                else
                {
                    display.setRotation(Sprite.TRANS_ROT90);
                    changeRotation(Sprite.TRANS_ROT90);
                    if (display.interludeScreen != null)
                        display.interludeScreen.changeRotation(Sprite.TRANS_ROT90);
                    if (display.gameOverScreen != null)
                        display.gameOverScreen.changeRotation(Sprite.TRANS_ROT90);
                    if (display.highScoreScreen != null)
                        display.highScoreScreen.changeRotation(Sprite.TRANS_ROT90);
                    if (display.creditsScreen != null)
                        display.creditsScreen.changeRotation(Sprite.TRANS_ROT90);
                }
                break;
            case 3:
            	display.resetScreens();
                display.getEngine().state = Engine.INSTRUCTIONS;
                if (display.instructionsScreen != null)
                {
                    display.instructionsScreen.changeRotation(GameDisplay.DISPLAY_ROTATION);
                    display.instructionsScreen.screen = 0;
                }
                break;
            case 4:
            	display.resetScreens();
                display.getEngine().state = Engine.HIGHSCORE;
                if (display.highScoreScreen != null)
                {
                    display.highScoreScreen.createScoreSprite();
                    display.highScoreScreen.changeRotation(GameDisplay.DISPLAY_ROTATION);
                }
                break;
            case 5:
            	display.resetScreens();
                display.getEngine().state = Engine.CREDITS;
                if (display.creditsScreen != null)
                    display.creditsScreen.changeRotation(GameDisplay.DISPLAY_ROTATION);
                break;
        }
    }
    
    public void changeOption(int direction)
    {
        switch (GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_ROT90:
                if (GameDisplay.LEFT == direction && option < spriteOptions.length - 1)
                {
                    GameDisplay.getInstance().getSounds().playSound(Sounds.CHANGE_OPTION);
                    ++option;
                }
                else if (GameDisplay.RIGHT == direction && option > 0)
                {
                    GameDisplay.getInstance().getSounds().playSound(Sounds.CHANGE_OPTION);
                    --option;
                }
                break;
            case Sprite.TRANS_ROT270:
                if (GameDisplay.LEFT == direction && option > 0)
                {
                    GameDisplay.getInstance().getSounds().playSound(Sounds.CHANGE_OPTION);
                    --option;
                }
                else if (GameDisplay.RIGHT == direction && option < spriteOptions.length - 1)
                {
                    GameDisplay.getInstance().getSounds().playSound(Sounds.CHANGE_OPTION);
                    ++option;
                }
                break;
        }
    }
}