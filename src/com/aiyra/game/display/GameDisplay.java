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

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

import com.aiyra.game.properties.Sounds;
import com.aiyra.game.system.Engine;
import com.aiyra.game.system.Stage;

/**
 *
 * @author bltorres
 */
public class GameDisplay extends GameCanvas
{
    public static int DISPLAY_ROTATION = Sprite.TRANS_ROT270;
    
    public static boolean SOUND = true;
    
    public static final int DEFAULT_HEIGHT = 188;
    
    private static final GameDisplay instance = new GameDisplay();
    
    private Engine engine;
    
    private Sounds sounds = new Sounds();
    
    private Image timeImage = Image.createImage(30, 20);
    private Graphics timeImageGraphics = timeImage.getGraphics();
    private Sprite timeSprite = new Sprite(timeImage);
    
    private Image stageImg = Image.createImage(30,20);
    private Graphics stageImgGraphics = stageImg.getGraphics();
    private Sprite stageSprite = new Sprite(stageImg);
    
    private final int lifeBarSize = 152;
    private final int dashBarSize = 69;
    
    private static Image lifeBar;
    private static Image dashBar;
    
    private final int DEFAULT_LIFE_BAR_X_ROT90 = 5;
    private final int DEFAULT_LIFE_BAR_X_ROT270 = this.getWidth() - DEFAULT_LIFE_BAR_X_ROT90;
    private final int DEFAULT_LIFE_BAR_Y_ROT270 = 20;
    private final int DEFAULT_LIFE_BAR_Y_ROT90 = this.getHeight() - DEFAULT_LIFE_BAR_Y_ROT270 - lifeBarSize;
    
    private final int DEFAULT_DASH_BAR_X_ROT90 = DEFAULT_LIFE_BAR_X_ROT90;
    private final int DEFAULT_DASH_BAR_X_ROT270 = DEFAULT_LIFE_BAR_X_ROT270;
    private final int DEFAULT_DASH_BAR_Y_ROT270 = this.getHeight() * 7 / 10;    
    private final int DEFAULT_DASH_BAR_Y_ROT90 = this.getHeight() - DEFAULT_DASH_BAR_Y_ROT270 - dashBarSize;
    
    private int lifeBarX = DEFAULT_LIFE_BAR_X_ROT270;
    private int lifeBarY = DEFAULT_LIFE_BAR_Y_ROT270;
    
    private int dashBarX = DEFAULT_DASH_BAR_X_ROT270;
    private int dashBarY = DEFAULT_DASH_BAR_Y_ROT270;
    
    private static Sprite lifeBarSprite;
    private static Sprite dashBarSprite;
            
    static
    {
        try
        {
            lifeBar = Image.createImage(GameDisplay.class.getResourceAsStream("/com/aiyra/game/resources/energia.png"));
            lifeBarSprite = new Sprite(lifeBar);
            lifeBarSprite.setTransform(Sprite.TRANS_ROT90);
            
            dashBar = Image.createImage(GameDisplay.class.getResourceAsStream("/com/aiyra/game/resources/energia2.png"));
            dashBarSprite = new Sprite(dashBar);
            dashBarSprite.setTransform(Sprite.TRANS_ROT90);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public GameOverScreen gameOverScreen;
    public InterludeScreen interludeScreen;
    public MenuScreen menuScreen;
    public HighScoreScreen highScoreScreen;
    public CreditsScreen creditsScreen;
    public InstructionsScreen instructionsScreen;
    
    private Sprite energy;
    private Sprite power;
    private Sprite score;
    private Sprite stage;
    private Sprite secretStage;
    
    public static GameDisplay getInstance()
    {
        return instance;
    }
    
    public static int getDisplayWidth()
    {
        return instance.getWidth();
    }
    
    public static int getDisplayHeight()
    {
        return instance.getHeight();
    }
    
    /** Creates a new instance of WorldDisplay */
    private GameDisplay()
    {
        super(false);
        
        Image energyImg = null;
        Image powerImg = null;
        Image scoreImg = null;
        Image stageImg = null;
        Image secretStageImg = null;
        
        try
        {
            powerImg = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/boost.png"));
            energyImg = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/life.png"));
            scoreImg = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/score_ingame.png"));
            stageImg = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/stage.png"));
            secretStageImg = Image.createImage(MenuScreen.class.getResourceAsStream("/com/aiyra/game/resources/fonts/secret_stage.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        energy = new Sprite(energyImg);
        power = new Sprite(powerImg);
        score = new Sprite(scoreImg);
        stage = new Sprite(stageImg);
        secretStage = new Sprite(secretStageImg);
    }
    
    public void setEngine(Engine engine)
    {
        this.engine = engine;
    }
    
    public Engine getEngine()
    {
        return engine;
    }
    
    /**
     * Draws all visible elements in positive z-axis display order.
     * The elements must be drawn from background to foreground, in order to
     * avoid incorrectly drawn objects.
     *
     * @param g the Graphics object used to paint the elements
     */
    public void paint(Graphics g)
    {
        switch (this.engine.state)
        {
            case Engine.PLAYING:
                paintPlaying(g);
                break;
            case Engine.DEAD:
                paintDead(g);
                break;
            case Engine.WIN:
                paintWin(g);
                break;
            case Engine.HALF_WIN:
                paintHalfWin(g);
                break;
            case Engine.INTERLUDE:
                paintInterlude(g);
                break;
            case Engine.MENU:
                paintMenu(g);
                break;
            case Engine.HIGHSCORE:
                paintHighScore(g);
                break;
            case Engine.CREDITS:
                paintCredits(g);
                break;
            case Engine.INSTRUCTIONS:
                paintInstructions(g);
                break;
        }
    }
    
    private void paintDead(Graphics g)
    {
        if (gameOverScreen == null)
            gameOverScreen = new GameOverScreen(GameOverScreen.LOSS);
        gameOverScreen.draw(g);
    }
    
    private void paintWin(Graphics g)
    {
        if (gameOverScreen == null)
            gameOverScreen = new GameOverScreen(GameOverScreen.WIN);
        gameOverScreen.draw(g);
    }
    
    private void paintHalfWin(Graphics g)
    {
        if (gameOverScreen == null)
            gameOverScreen = new GameOverScreen(GameOverScreen.HALF_WIN);
        gameOverScreen.draw(g);
    }
    
    private void paintHighScore(Graphics g)
    {
        if (highScoreScreen == null)
            highScoreScreen = new HighScoreScreen();
        highScoreScreen.draw(g);
    }
    
    private void paintCredits(Graphics g)
    {
        if (creditsScreen == null)
            creditsScreen = new CreditsScreen();
        creditsScreen.draw(g);
    }
    
    private void paintInstructions(Graphics g)
    {
        if (instructionsScreen == null)
            instructionsScreen = new InstructionsScreen();
        instructionsScreen.draw(g);
    }
    
    private void paintInterlude(Graphics g)
    {
        if (interludeScreen == null)
            interludeScreen = new InterludeScreen();
        interludeScreen.draw(g);
    }
    
    private void paintMenu(Graphics g)
    {
        if (menuScreen == null)
            menuScreen = new MenuScreen();
        menuScreen.draw(g);
    }
    
    private void paintPlaying(Graphics g)
    {
        this.engine.drawElements(g);
        
        switch(DISPLAY_ROTATION)
        {
            case Sprite.TRANS_ROT90:
                g.setColor(0x00000000);
                //FIXME: Stage.MIN_XPOS + 1 conserta o problema da divisï¿½o inteira
                g.fillRect(0, 0, Stage.MIN_XPOS + 1, this.getHeight());
                g.fillRect(Stage.MAX_XPOS, 0, this.getWidth() - Stage.MAX_XPOS, this.getHeight());
                                
                updateTimeImage(g, Stage.MAX_XPOS + timeImage.getHeight(), Stage.MAX_YPOS - timeImage.getWidth());
                
                dashBarSprite.setRefPixelPosition(dashBarX, dashBarY + dashBarSize);
                dashBarSprite.paint(g);
                g.setColor(0x00000000);
                g.fillRect(dashBarX + 2, dashBarY + ((int) (dashBarSize * (engine.dashPercentage / 100))), 5, dashBarSize - 3 - ((int) (dashBarSize * (engine.dashPercentage / 100))));
                g.setColor(0x00FFFFFF);
                g.drawLine(dashBarX, dashBarY + 2, dashBarX, dashBarY + dashBarSize - 2);
                g.drawLine(dashBarX, dashBarY + 2, dashBarX + 8, dashBarY + 2);
                
                lifeBarSprite.setRefPixelPosition(lifeBarX, lifeBarY + lifeBarSize);
                lifeBarSprite.paint(g);
                g.setColor(0x00000000);
                g.fillRect(lifeBarX + 2, lifeBarY + (engine.getPlayerLife() * (lifeBarSize / engine.getPlayerMaxLife())), 7, lifeBarSize - 5 - (engine.getPlayerLife() * (lifeBarSize / engine.getPlayerMaxLife())));
                g.setColor(0x00FFFFFF);
                g.drawLine(lifeBarX + 2, lifeBarY + 2, lifeBarX + 2, lifeBarY + lifeBarSize - 4);
                g.drawLine(lifeBarX + 2, lifeBarY + 2, lifeBarX + 10, lifeBarY + 2);
                
                score.setRefPixelPosition(Stage.MAX_XPOS + timeImage.getHeight() - 3, Stage.MAX_YPOS - timeImage.getWidth() - 45);
                score.setTransform(DISPLAY_ROTATION);
                score.paint(g);
                
                {
                int currentStageNumber = engine.currentStageNumber();
                if(currentStageNumber <= 4)
                {
                	updateStageImage(g, Stage.MAX_XPOS + timeImage.getHeight(), Stage.MAX_YPOS - timeImage.getWidth() - 100);
                	stage.setRefPixelPosition(Stage.MAX_XPOS + timeImage.getHeight() - 3, Stage.MAX_YPOS - timeImage.getWidth() - 145);
                    stage.setTransform(DISPLAY_ROTATION);
                    stage.paint(g);                	
                }
                else
                {
                	secretStage.setRefPixelPosition(Stage.MAX_XPOS + timeImage.getHeight() - 3, Stage.MAX_YPOS - timeImage.getWidth() - 180);
                	secretStage.setTransform(DISPLAY_ROTATION);
                	secretStage.paint(g);
                }
                }
                
                energy.setRefPixelPosition(lifeBarX + 5 + energy.getWidth() + 5, lifeBarY);
                energy.setTransform(DISPLAY_ROTATION);
                energy.paint(g);
                
                power.setRefPixelPosition(dashBarX + 5 + power.getWidth() + 5, dashBarY);
                power.setTransform(DISPLAY_ROTATION);
                power.paint(g);
                break;
            case Sprite.TRANS_ROT270:
                g.setColor(0x00000000);
                g.fillRect(0, 0, Stage.MIN_XPOS, this.getHeight());
                g.fillRect(Stage.MAX_XPOS, 0, this.getWidth() - Stage.MAX_XPOS, this.getHeight());
                
                updateTimeImage(g, 3, timeImage.getWidth());
                
                dashBarSprite.setRefPixelPosition(dashBarX + 5, dashBarY);
                dashBarSprite.paint(g);
                g.setColor(0x00000000);
                g.fillRect(dashBarX - 2, dashBarY + 5, 7, (int) (dashBarSize - (dashBarSize * (engine.dashPercentage / 100))));
                g.setColor(0x00FFFFFF);
                g.drawLine(dashBarX + 4, dashBarY + 2, dashBarX + 4, dashBarY + dashBarSize - 3);
                g.drawLine(dashBarX - 3, dashBarY + dashBarSize - 3, dashBarX + 5, dashBarY + dashBarSize - 3);
                
                lifeBarSprite.setRefPixelPosition(lifeBarX + 5, lifeBarY);
                lifeBarSprite.paint(g);
                g.setColor(0x00000000);
                g.fillRect(lifeBarX - 4, lifeBarY + 5, 7, lifeBarSize - (engine.getPlayerLife() * (lifeBarSize / engine.getPlayerMaxLife())));
                g.setColor(0x00FFFFFF);
                g.drawLine(lifeBarX + 2, lifeBarY + 5, lifeBarX + 2, lifeBarY + lifeBarSize - 3);
                g.drawLine(lifeBarX - 5, lifeBarY + lifeBarSize - 3, lifeBarX + 2, lifeBarY + lifeBarSize - 3);
                
                score.setRefPixelPosition(6, timeImage.getWidth() + 45);
                score.setTransform(DISPLAY_ROTATION);
                score.paint(g);
                
                {
                int currentStageNumber = engine.currentStageNumber();
                if(currentStageNumber <= 4)
                {
                	updateStageImage(g, 3, timeImage.getWidth() + 100);
                	stage.setRefPixelPosition(6, timeImage.getWidth() + 145);
                    stage.setTransform(DISPLAY_ROTATION);
                    stage.paint(g);                    
                }
                else
                {
                	secretStage.setRefPixelPosition(6, timeImage.getWidth() + 180);
                	secretStage.setTransform(DISPLAY_ROTATION);
                	secretStage.paint(g);
                }
                }
                
                energy.setRefPixelPosition(lifeBarX - energy.getWidth() - 5, lifeBarY + lifeBarSize);
                energy.setTransform(DISPLAY_ROTATION);
                energy.paint(g);
                
                power.setRefPixelPosition(dashBarX - power.getWidth() - 5, dashBarY + dashBarSize);
                power.setTransform(DISPLAY_ROTATION);
                power.paint(g);
                break;
        }
    }
    
    private void updateStageImage(Graphics g, int xPos, int yPos)
    {    	        
        int currentStageNumber = engine.currentStageNumber();
        if(currentStageNumber > 4)
        	return;
        
        stageImgGraphics.setColor(0x00000000);
        stageImgGraphics.fillRect(0, 0, stageImg.getWidth(), stageImg.getHeight());
        stageImgGraphics.setColor(0x00FFFFFF);
        
        String stageStr = String.valueOf(currentStageNumber);
        stageImgGraphics.drawString(stageStr, 0, 0, 0);
        
    	stageSprite.setImage(stageImg, stageImg.getWidth(), stageImg.getHeight());
    	stageSprite.setTransform(DISPLAY_ROTATION);
        stageSprite.setRefPixelPosition(xPos, yPos);
        stageSprite.paint(g);
    }
    
    private void updateTimeImage(Graphics g, int xPos, int yPos)
    {
        timeImageGraphics.setColor(0x00000000);
        timeImageGraphics.fillRect(0, 0, timeImage.getWidth(), timeImage.getHeight());

        timeImageGraphics.setColor(0x00FFFFFF);
        timeImageGraphics.drawString(this.time, 0, 0, 0);

        timeSprite.setImage(timeImage, timeImage.getWidth(), timeImage.getHeight());
        
        timeSprite.setTransform(DISPLAY_ROTATION);
        
        timeSprite.setRefPixelPosition(xPos, yPos);
        timeSprite.paint(g);
    }
    
    protected void keyPressed(int keyCode)
    {
        this.engine.keyEvent(Engine.KEY_PRESSED, keyCode);
    }
    
    protected void keyReleased(int keyCode)
    {
        this.engine.keyEvent(Engine.KEY_RELEASED, keyCode);
    }
    
    private String time = "";
    
    public void setTime(String time)
    {
        this.time = time;
    }
    
    public void resetScreens()
    {
        this.gameOverScreen = null;
        this.interludeScreen = null;
        this.creditsScreen = null;
        this.highScoreScreen = null;
        this.instructionsScreen = null;                
    }
    
    public void setRotation(int rotation)
    {
        GameDisplay.DISPLAY_ROTATION = rotation;
        
        switch (rotation)
        {
            case Sprite.TRANS_ROT90:
                lifeBarX = DEFAULT_LIFE_BAR_X_ROT90;
                lifeBarY = DEFAULT_LIFE_BAR_Y_ROT90;
    
                dashBarX = DEFAULT_DASH_BAR_X_ROT90;
                dashBarY = DEFAULT_DASH_BAR_Y_ROT90;
                
                dashBarSprite.setTransform(Sprite.TRANS_ROT270);
                lifeBarSprite.setTransform(Sprite.TRANS_ROT270);
                break;
            case Sprite.TRANS_ROT270:
                lifeBarX = DEFAULT_LIFE_BAR_X_ROT270;
                lifeBarY = DEFAULT_LIFE_BAR_Y_ROT270;
    
                dashBarX = DEFAULT_DASH_BAR_X_ROT270;
                dashBarY = DEFAULT_DASH_BAR_Y_ROT270;
                
                dashBarSprite.setTransform(Sprite.TRANS_ROT90);
                lifeBarSprite.setTransform(Sprite.TRANS_ROT90);
                break;
        }
    }
    
    public Sounds getSounds()
    {
        return sounds;
    }
}
