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

import com.aiyra.game.properties.Numbers;
import com.aiyra.game.system.Stage;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Musmanno
 */
public class GameOverScreen implements Displayable
{
    private Sprite backgroundSprite;
    private Sprite scoreSprite;
    
    private Sprite pressOKSprite;
    
    private Sprite[] pointsSprite;
    
    private int blinkCount = 10;
    
    public static final int LOSS = 0;
    public static final int HALF_WIN = 1;
    public static final int WIN = 2;

    public GameOverScreen(int gameOverType)
    {
        Image background = null;
        Image score = null;
        Image pressOK = null;
        
        try
        {
        	switch(gameOverType)
        	{
        		case LOSS:
        			background = Image.createImage(GameOverScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaGameOver.png"));
        			break;
        		case HALF_WIN:
        			background = Image.createImage(GameOverScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaHalfWin.png"));
        			break;
        		case WIN:
        			background = Image.createImage(GameOverScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaWin.png"));
        			break;
        		default:
        			throw new RuntimeException("Unexpected Error. Wrong value to gameOverType variable.");
        	}
            
            score = Image.createImage(GameOverScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/score_gameover.png"));
            pressOK = Image.createImage(GameOverScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/press ok.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        createScoreSprite();
        
        backgroundSprite = new Sprite(background);
        scoreSprite = new Sprite(score);
        pressOKSprite = new Sprite(pressOK);
        
        scoreSprite.defineReferencePixel(scoreSprite.getWidth()/2, 0);
        pressOKSprite.defineReferencePixel(pressOKSprite.getWidth()/2, 0);
        
        changeRotation(GameDisplay.DISPLAY_ROTATION);
    }
    
    public void createScoreSprite()
    {
        pointsSprite = Numbers.getValueSprite(GameDisplay.getInstance().getEngine().score);
        
        for (int i = 0; i < pointsSprite.length; i++)
            pointsSprite[i].setTransform(GameDisplay.DISPLAY_ROTATION);
    }
    
    public void changeRotation(int rotation)
    {
        switch (rotation)
        {
            case Sprite.TRANS_ROT90:
                backgroundSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth(), 0);
                scoreSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth()/3, GameDisplay.getInstance().getHeight()/3);
                pressOKSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth()/5, GameDisplay.getInstance().getHeight()/2);
                
                pointsSprite[0].setRefPixelPosition(GameDisplay.getInstance().getWidth()/3, GameDisplay.getInstance().getHeight() - GameDisplay.getInstance().getHeight()/3);
                
                for (int i = 1; i < pointsSprite.length; i++)
                    pointsSprite[i].setRefPixelPosition(pointsSprite[i - 1].getRefPixelX(), pointsSprite[i - 1].getRefPixelY() + 21);
                break;
            case Sprite.TRANS_ROT270:
                backgroundSprite.setRefPixelPosition(0, GameDisplay.getInstance().getHeight());
                scoreSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth() - GameDisplay.getInstance().getWidth()/3, GameDisplay.getInstance().getHeight() - GameDisplay.getInstance().getHeight()/3);
                pressOKSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth() - GameDisplay.getInstance().getWidth()/5, GameDisplay.getInstance().getHeight()/2);
                
                pointsSprite[0].setRefPixelPosition(GameDisplay.getInstance().getWidth() - GameDisplay.getInstance().getWidth()/3, GameDisplay.getInstance().getHeight()/3);
                
                for (int i = 1; i < pointsSprite.length; i++)
                    pointsSprite[i].setRefPixelPosition(pointsSprite[i - 1].getRefPixelX(), pointsSprite[i - 1].getRefPixelY() - 21);
                break;
        }

        backgroundSprite.setTransform(rotation);
        scoreSprite.setTransform(rotation);
        pressOKSprite.setTransform(rotation);
    }

    public void draw(Graphics g)
    {
        this.backgroundSprite.paint(g);
        this.scoreSprite.paint(g);        

        blinkCount++;
        
        if (blinkCount >= 10)
            this.pressOKSprite.paint(g);
        
        if (blinkCount == 20)
            blinkCount = 0;
        
        
        for (int i = 0; i < pointsSprite.length; i++)
            pointsSprite[i].paint(g);
        
        switch(GameDisplay.DISPLAY_ROTATION)
        {
            case Sprite.TRANS_ROT90:
                g.setColor(0x00000000);
                //FIXME: Stage.MIN_XPOS + 1 conserta o problema da divisï¿½o inteira
                g.fillRect(0, 0, Stage.MIN_XPOS + 1, GameDisplay.getInstance().getHeight());
                g.fillRect(Stage.MAX_XPOS, 0, GameDisplay.getInstance().getWidth() - Stage.MAX_XPOS, GameDisplay.getInstance().getHeight());
                break;
            case Sprite.TRANS_ROT270:
                g.setColor(0x00000000);
                g.fillRect(0, 0, Stage.MIN_XPOS, GameDisplay.getInstance().getHeight());
                g.fillRect(Stage.MAX_XPOS, 0, GameDisplay.getInstance().getWidth() - Stage.MAX_XPOS, GameDisplay.getInstance().getHeight());
                break;
        }
    }
}