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
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Musmanno
 */
public class HighScoreScreen implements Displayable
{
    public static final int NUMBER_OF_SCORES = 5;
    
    private Sprite backgroundSprite;
    //private Sprite highscoreSprite;
    
    private Sprite menuSprite;
    
    private Vector scoresSprites;
    
    private Sprite[] positionSprites = new Sprite[NUMBER_OF_SCORES];

    public HighScoreScreen()
    {
        Image background = null;
        //Image highscore = null;
        Image menu = null;
        
        try
        {
            background = Image.createImage(GameOverScreen.class.getResourceAsStream("/com/aiyra/game/resources/telaHighscore.png"));
            //highscore = Image.createImage(GameOverScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/highscore.png"));
            menu = Image.createImage(InstructionsScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/menu.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        createScoreSprite();
        
        backgroundSprite = new Sprite(background);
        //highscoreSprite = new Sprite(highscore);
        
        menuSprite = new Sprite(menu);
        menuSprite.defineReferencePixel(menuSprite.getWidth()/2, 0);
        menuSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth()/2, GameDisplay.getInstance().getHeight() - (menuSprite.getHeight() + 10));
        
        
        for (int i = 0; i < positionSprites.length; i++)
            positionSprites[i] = Numbers.getNumberSprite(i + 1);
        
        //highscoreSprite.defineReferencePixel(highscoreSprite.getWidth()/2, highscoreSprite.getHeight()/2);
        
        changeRotation(GameDisplay.DISPLAY_ROTATION);
    }
    
    public void createScoreSprite()
    {   
        int[] scores = GameDisplay.getInstance().getEngine().bd.getHighScores();
        
        scoresSprites = new Vector();
        
        for (int i = 0; i < scores.length; ++i)
            scoresSprites.addElement(Numbers.getValueSprite(scores[i]));
        
        for (int i = scores.length; i < NUMBER_OF_SCORES; i++)
            scoresSprites.addElement(Numbers.getValueSprite(0));
    }
    
    public void changeRotation(int rotation)
    {
        switch (rotation)
        {
            case Sprite.TRANS_ROT90:
                backgroundSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth(), 0);
//                highscoreSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth() - GameDisplay.getInstance().getWidth()/6, GameDisplay.getInstance().getHeight()/2);
                
                positionSprites[0].setRefPixelPosition(GameDisplay.getInstance().getWidth() - GameDisplay.getInstance().getWidth()/3, 2 * GameDisplay.getInstance().getHeight()/7);
                
                for (int i = 1; i < positionSprites.length; i++)
                    positionSprites[i].setRefPixelPosition(positionSprites[i - 1].getRefPixelX() - 30, positionSprites[i - 1].getRefPixelY());
                
                for (int i = 0; i < scoresSprites.size(); i++)
                {
                    Sprite[] sprites = (Sprite[])scoresSprites.elementAt(i);
                    sprites[0].setRefPixelPosition(positionSprites[i].getRefPixelX(), positionSprites[i].getRefPixelY() + 60);
                    sprites[0].setTransform(rotation);
                    for (int j = 1; j < sprites.length; j++)
                        sprites[j].setRefPixelPosition(sprites[j - 1].getRefPixelX(), sprites[j - 1].getRefPixelY() + 21);
                }
                break;
            case Sprite.TRANS_ROT270:
                backgroundSprite.setRefPixelPosition(0, GameDisplay.getInstance().getHeight());
//                highscoreSprite.setRefPixelPosition(GameDisplay.getInstance().getWidth()/6, GameDisplay.getInstance().getHeight()/2);
                positionSprites[0].setRefPixelPosition(GameDisplay.getInstance().getWidth()/3, GameDisplay.getInstance().getHeight() - 2 * GameDisplay.getInstance().getHeight()/7);
                
                for (int i = 1; i < positionSprites.length; i++)
                    positionSprites[i].setRefPixelPosition(positionSprites[i - 1].getRefPixelX() + 30, positionSprites[i - 1].getRefPixelY());
                
                for (int i = 0; i < scoresSprites.size(); i++)
                {
                    Sprite[] sprites = (Sprite[])scoresSprites.elementAt(i);
                    sprites[0].setRefPixelPosition(positionSprites[i].getRefPixelX(), positionSprites[i].getRefPixelY() - 60);
                    sprites[0].setTransform(rotation);
                    for (int j = 1; j < sprites.length; j++)
                        sprites[j].setRefPixelPosition(sprites[j - 1].getRefPixelX(), sprites[j - 1].getRefPixelY() - 21);
                }
                break;
        }

        backgroundSprite.setTransform(rotation);
//        highscoreSprite.setTransform(rotation);
        
        for (int i = 0; i < positionSprites.length; i++)
            positionSprites[i].setTransform(rotation);
        
        for (int i = 0; i < scoresSprites.size(); i++)
        {
            Sprite[] sprites = (Sprite[])scoresSprites.elementAt(i);
            for (int j = 1; j < sprites.length; j++)
                sprites[j].setTransform(rotation);
        }
    }

    public void draw(Graphics g)
    {
        this.backgroundSprite.paint(g);
//        this.highscoreSprite.paint(g);
        
        for (int i = 0; i < scoresSprites.size(); i++)
        {
            Sprite[] sprites = (Sprite[])scoresSprites.elementAt(i);
            for (int j = 0; j < sprites.length; j++)
                sprites[j].paint(g);
        }
        
        for (int i = 0; i < positionSprites.length; i++)
            positionSprites[i].paint(g);
        
        this.menuSprite.paint(g);
    }
}