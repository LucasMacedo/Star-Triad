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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import com.aiyra.game.display.GameDisplay;
import com.aiyra.game.properties.ScenarioProperties;
import com.aiyra.game.properties.Sounds;

/**
 * 
 * @author bltorres
 */
public class Engine {
	// Key event type IDs
	public static final int KEY_REPEATED = 0;
	public static final int KEY_PRESSED = 1;
	public static final int KEY_RELEASED = 2;

	public static final int MAX_STAGES = 4;
	public static final int MINIMUM_POINTS_TO_LAST_STAGE = 200;

	public static final Random rnd = new Random();

	private final GameDisplay display;

	public float dashPercentage;
	private Timer dashRecoverTimer;
	private TimerTask dashRecoverTask;

	private Vector stages = new Vector();
	private Stage currentStage;

	private Player player;
	private boolean hyperSpeed;

	private GameClock clock;

	public Database bd = new Database();

	public int state;

	public static final int MENU = 0;
	public static final int PLAYING = 1;
	public static final int DEAD = 2;
	public static final int INTERLUDE = 3;
	public static final int HIGHSCORE = 4;
	public static final int CREDITS = 5;
	public static final int INSTRUCTIONS = 6;
	public static final int WIN = 7;
	public static final int HALF_WIN = 8;

	public static final boolean DEBUG_MODE = false;

	public int score;
	private int colisions;

	private Timer refreshTimer = new Timer();
	private TimerTask refreshTask = new TimerTask() {
		public void run() {
			// Memory management
			long totalMem = Runtime.getRuntime().totalMemory();
			long freeMem = Runtime.getRuntime().freeMemory();
			float ratio = (float) freeMem / totalMem;
			if (DEBUG_MODE) {
				System.out.println("Memory status (free/total): " + freeMem
						+ "/" + totalMem);
				System.out.println("Free mem: " + ratio * 100 + "%");
			}
			if (ratio < 0.10) {
				if (DEBUG_MODE)
					System.out.println(">>> Calls garbage collection.");
				Runtime.getRuntime().gc();
			}

			// timer tick
			timerTick();
		}
	};
	
	public int currentStageNumber()
	{
		return stages.size();
	}

	public void timerTick() {
		if (state == PLAYING) {
			player.update();
			testCollision();
			currentStage.update();
			testCollision();
		}

		if (display != null)
			display.repaint();
	}

	/** Creates a new instance of Engine */
	public Engine(GameDisplay display) {
		this.display = display;
		this.display.setEngine(this);
		this.refreshTimer.schedule(refreshTask, 0, 50);
	}

	public void initGame() {
		destroyStages();
		newStage();
		reset();
	}

	public void testCollision() {
		if (!this.hyperSpeed) {
			int i = player.collidesWith(currentStage.getVisibleObstacles());

			if (i >= 0) {
				++colisions;

				Obstacle o = currentStage.getObstacleAt(i);
				currentStage.destroyElement(i);
				boolean playerDied = player.processHit(o);

				if (playerDied)
					gameOver(DEAD);
			}
		}
	}

	public void drawElements(Graphics g) {
		currentStage.draw(g);
		player.draw(g);
	}

	public void keyEvent(int type, int keyCode) {
		if (state == PLAYING && !hyperSpeed) {
			switch (type) {
			case KEY_REPEATED:
				break;
			case KEY_PRESSED:
				keyPressedEvent(keyCode, GameDisplay.DISPLAY_ROTATION);
				break;
			case KEY_RELEASED:
				keyReleasedEvent(keyCode, GameDisplay.DISPLAY_ROTATION);
				break;
			default:
				throw new IllegalArgumentException();
			}
		} else if (state == MENU) {
			switch (type) {
			case KEY_PRESSED:
				if (GameDisplay.getInstance().menuScreen.available) {
					if (GameDisplay.FIRE == display.getGameAction(keyCode))
						GameDisplay.getInstance().menuScreen.chooseOption();
					else if (GameDisplay.LEFT == display.getGameAction(keyCode)
							|| GameDisplay.RIGHT == display
									.getGameAction(keyCode))
						;
					GameDisplay.getInstance().menuScreen.changeOption(display
							.getGameAction(keyCode));
				} else {
					if (!GameDisplay.getInstance().menuScreen.splash) {
						display.getSounds().playSound(Sounds.CHOOSE_OPTION);
						GameDisplay.getInstance().menuScreen.available = true;
					}
				}

				break;
			}
		} else if (state == INTERLUDE) {
			switch (type) {
			case KEY_RELEASED:
				if (GameDisplay.FIRE == display.getGameAction(keyCode)) {
					display.getSounds().playSound(Sounds.CHOOSE_OPTION);
					newStage();
					resetTime();
					player.init();
					hyperSpeed = false;
					state = PLAYING;
					GameDisplay.getInstance().interludeScreen = null;
				}
				break;
			}
		} else if (state == CREDITS) {
			switch (type) {
			case KEY_PRESSED:
				display.getSounds().playSound(Sounds.CHOOSE_OPTION);
				state = MENU;
				break;
			}
		} else if (state == HIGHSCORE) {
			switch (type) {
			case KEY_PRESSED:
				if (GameDisplay.FIRE == display.getGameAction(keyCode)) {
					display.getSounds().playSound(Sounds.CHOOSE_OPTION);
					state = MENU;
				}
				break;
			}
		} else if (state == DEAD || state == HALF_WIN || state == WIN) {
			switch (type) {
			case KEY_PRESSED:
				if (GameDisplay.FIRE == display.getGameAction(keyCode)) {
					display.getSounds().playSound(Sounds.CHOOSE_OPTION);
					state = HIGHSCORE;
				}
				break;
			}
		}else if (state == INSTRUCTIONS) {
			switch (type) {
			case KEY_PRESSED:
				if (GameDisplay.FIRE == display.getGameAction(keyCode)) {
					display.getSounds().playSound(Sounds.CHOOSE_OPTION);
					GameDisplay.getInstance().instructionsScreen.changeScreen();
				}
				break;
			}
		}
	}

	public void resetTime() {
		this.clock = new GameClock();
		this.clock.setTime(0);
	}

	public int getTime() {
		return this.clock.getTime();
	}

	private void keyPressedEvent(int keyCode, int rotationDirection) {
		switch (display.getGameAction(keyCode)) {
		case GameDisplay.UP:
			player.yAcc += -Player.ACCEL_HORIZONTAL;
			break;
		case GameDisplay.DOWN:
			player.yAcc += Player.ACCEL_HORIZONTAL;
			break;
		case GameDisplay.LEFT:
			player.xAcc += -Player.ACCEL_VERTICAL;
			break;
		case GameDisplay.RIGHT:
			player.xAcc += Player.ACCEL_VERTICAL;
			break;
		default:
			if (GameDisplay.KEY_NUM0 == keyCode)
				endStage();
			else if (GameDisplay.KEY_NUM5 == keyCode)
				dash();
			break;
		}

		setPlayerDirectionFrame(rotationDirection);
	}

	private void keyReleasedEvent(int keyCode, int rotationDirection) {
		switch (display.getGameAction(keyCode)) {
		case GameDisplay.UP:
			player.yAcc -= -Player.ACCEL_HORIZONTAL;
			break;
		case GameDisplay.DOWN:
			player.yAcc -= Player.ACCEL_HORIZONTAL;
			break;
		case GameDisplay.LEFT:
			player.xAcc -= -Player.ACCEL_VERTICAL;
			break;
		case GameDisplay.RIGHT:
			player.xAcc -= Player.ACCEL_VERTICAL;
			break;
		}

		setPlayerDirectionFrame(rotationDirection);
	}

	private void setPlayerDirectionFrame(int rotationDirection) {
		if (rotationDirection == Sprite.TRANS_ROT90) {
			if (player.xAcc > 0)
				player.setDirection(Player.UP);
			else if (player.xAcc < 0)
				player.setDirection(Player.DOWN);
			else
				player.setDirection(Player.NORMAL);
		} else if (rotationDirection == Sprite.TRANS_ROT270) {
			if (player.xAcc > 0)
				player.setDirection(Player.DOWN);
			else if (player.xAcc < 0)
				player.setDirection(Player.UP);
			else
				player.setDirection(Player.NORMAL);
		}
	}

	public void gameOver(int gameOverType) {
		
		updateScore();
		state = gameOverType;
		currentStage.destroy();
		stages = new Vector();
		addScoreToHighScore(score);
	}

	private void addScoreToHighScore(int score) {
		int[] scores = bd.getHighScores();

		if (scores.length > 1 && scores[scores.length - 1] < score)
			bd.saveScore(score);
	}

	public int getPlayerLife() {
		return this.player.life;
	}

	public int getPlayerMaxLife() {
		return Player.MAX_LIFE;
	}

	public void destroy() {
		destroyTime();
		destroyStages();

		if (dashRecoverTimer != null) {
			this.dashRecoverTimer.cancel();
			this.dashRecoverTimer = null;
			this.dashRecoverTask = null;
		}

		this.display.setEngine(null);
	}

	public void destroyStages() {
		updateScore();
		destroyCurrentStage();
		this.stages.removeAllElements();

		// memory management
		System.gc();
	}

	private void destroyCurrentStage() {
		if (currentStage != null) {
			// updateScore();
			currentStage.destroy();
			currentStage = null;
		}

		// memory management
		System.gc();
	}

	private void destroyTime() {
		this.refreshTimer.cancel();
		this.refreshTimer = null;
		this.refreshTask = null;
	}

	public void newStage() {
		Integer idScenario = null;

		if (this.stages.size() == MAX_STAGES)
			idScenario = new Integer(ScenarioProperties.NUMBER_OF_SCENARIOS - 1);
		else {
			idScenario = new Integer(Math.abs(rnd.nextInt())
					% (ScenarioProperties.NUMBER_OF_SCENARIOS - 1));

			while (stages.contains(idScenario))
				idScenario = new Integer(Math.abs(rnd.nextInt())
						% (ScenarioProperties.NUMBER_OF_SCENARIOS - 1));
		}

		updateScore();
		destroyCurrentStage();
		this.currentStage = new Stage(idScenario.intValue());
		this.stages.addElement(idScenario);
	}

	public void reset() {
		this.player = new Player();
		this.clock = new GameClock();
		this.state = PLAYING;
		this.score = 0;
		this.colisions = 0;
		this.hyperSpeed = false;
		player.invincible = false;
		this.dashPercentage = 100;

		this.dashRecoverTimer = new Timer();
		this.dashRecoverTask = new TimerTask() {
			public void run() {
				if (GameDisplay.getInstance().getEngine().dashPercentage < 100)
					GameDisplay.getInstance().getEngine().dashPercentage++;
			}
		};

		this.dashRecoverTimer.schedule(dashRecoverTask, 0, 1000);
		this.display.resetScreens();
	}

	private void endStage() {
		this.player.invincible = true;
		this.hyperSpeed = true;
		this.currentStage.speedCounterLimit = 2;

		display.getSounds().playSound(Sounds.HYPERSPEED);

		Timer speedWaitTimer = new Timer();
		TimerTask speedWaitTask = new TimerTask() {
			public void run() {
				hyperSpeedAction();
			}
		};

		speedWaitTimer.schedule(speedWaitTask, 5000);

		this.player.xSpd = 0;
		this.player.ySpd = 0;
		this.player.xAcc = 0;
		this.player.yAcc = 0;
		setPlayerDirectionFrame(GameDisplay.DISPLAY_ROTATION);

		player.animateDash(20);
	}

	private void dash() {
		if (this.dashPercentage >= 100) {
			this.player.invincible = true;
			this.dashPercentage = 0;

			switch (GameDisplay.DISPLAY_ROTATION) {
			case Sprite.TRANS_ROT90:
				this.player.ySpd += 30;
				break;
			case Sprite.TRANS_ROT270:
				this.player.ySpd -= 30;
				break;
			}

			display.getSounds().playSound(Sounds.DASH);
			player.animateDash(50);

			Timer dashTimer = new Timer();
			TimerTask dashTask = new TimerTask() {
				public void run() {
					player.destroyDashAnimation();
					player.sprite.setFrame(Player.NORMAL);
					player.invincible = false;
				}
			};

			dashTimer.schedule(dashTask, 1000);
		}
	}

	private void hyperSpeedAction() {
		this.player.hyperSpeed = true;
		switch (GameDisplay.DISPLAY_ROTATION) {
		case Sprite.TRANS_ROT90:
			this.player.ySpd = 50;
			break;

		case Sprite.TRANS_ROT270:
			this.player.ySpd = -50;
			break;
		}

		Timer speedWaitTimer = new Timer();
		TimerTask speedWaitTask = new TimerTask() {
			public void run() {
				switchStage();
				player.destroyDashAnimation();
				setPlayerDirectionFrame(GameDisplay.DISPLAY_ROTATION);
			}
		};

		speedWaitTimer.schedule(speedWaitTask, 2000);
	}

	private void switchStage() {
		updateScore();
		if (this.stages.size() == MAX_STAGES && this.score < MINIMUM_POINTS_TO_LAST_STAGE)
		{
			gameOver(HALF_WIN);
		}
		else if(this.stages.size() == MAX_STAGES + 1)
		{
			gameOver(WIN);
		}
		else
		{
			destroyCurrentStage();
			this.state = INTERLUDE;
		}
	}

	private void updateScore() {
		if (clock != null) {
			score += clock.getTime();// - colisions;
			clock.stop();
			clock = null;
			colisions = 0;
		}

		if (score < 0)
			score = 0;
	}
}
