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
import javax.microedition.lcdui.game.Sprite;

/**
 * 
 * @author Musmanno
 */
public class InterludeScreen implements Displayable {
	private Sprite backgroundSprite;
	private Sprite textSprite;
	private Sprite pressOKSprite;

	private int blinkCount = 10;

	public InterludeScreen() {
		Image background = null;
		Image pressOK = null;

		try {
			background = Image
					.createImage(GameOverScreen.class
							.getResourceAsStream("/com/aiyra/game/resources/fundo.png"));
			pressOK = Image
					.createImage(MenuScreen.class
							.getResourceAsStream("/com/aiyra/game/resources/fonts/press ok.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image image = Image.createImage(GameDisplay.getInstance().getHeight() + 10, GameDisplay.getInstance().getWidth() + 10);
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(0xFF000000);
		graphics.fillRect(0, 0, GameDisplay.getInstance().getHeight(),
				GameDisplay.getInstance().getWidth());
		graphics.setColor(0xFFFFFFFF);
		graphics.drawString("NEXT STAGE",
				GameDisplay.getInstance().getHeight() / 3 + 20, GameDisplay
						.getInstance().getWidth() / 2 - 50, Graphics.TOP
						| Graphics.LEFT);
		graphics.drawString("Get ready!",
				GameDisplay.getInstance().getHeight() / 3 + 20, GameDisplay
						.getInstance().getWidth() / 2 - 20, Graphics.TOP
						| Graphics.LEFT);
		graphics.drawString("Score = "
				+ GameDisplay.getInstance().getEngine().score, GameDisplay
				.getInstance().getHeight() / 3 + 20, GameDisplay.getInstance()
				.getWidth() / 2 + 10, Graphics.TOP | Graphics.LEFT);
		
		// convert image pixels data to int array
		int[] rgb = new int[image.getWidth() * image.getHeight()];

		image.getRGB(rgb, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

		// make alpha transparent on pixels that are not white		
		for (int i = 0; i < rgb.length; ++i) {
			if (rgb[i] != 0xFFFFFFFF) {
				rgb[i] = 0x00000000;
			}
		}
		// create a new image with the pixel data and set process alpha flag to
		// true
		image = Image.createRGBImage(rgb, image.getWidth(), image.getHeight(), true);

		backgroundSprite = new Sprite(background);

		textSprite = new Sprite(image);

		pressOKSprite = new Sprite(pressOK);
		pressOKSprite.defineReferencePixel(pressOKSprite.getWidth() / 2,
				pressOKSprite.getHeight() / 2);

		changeRotation(GameDisplay.DISPLAY_ROTATION);
	}

	public void changeRotation(int rotation) {
		switch (rotation) {
		case Sprite.TRANS_ROT90:
			backgroundSprite.setRefPixelPosition(GameDisplay.getInstance()
					.getWidth(), 0);
			textSprite.setRefPixelPosition(
					GameDisplay.getInstance().getWidth(), 0);
			pressOKSprite.setRefPixelPosition(GameDisplay.getInstance()
					.getWidth() / 5, GameDisplay.getInstance().getHeight() / 2);
			break;
		case Sprite.TRANS_ROT270:
			backgroundSprite.setRefPixelPosition(0, GameDisplay.getInstance()
					.getHeight());
			textSprite.setRefPixelPosition(0, GameDisplay.getInstance()
					.getHeight());
			pressOKSprite.setRefPixelPosition(GameDisplay.getInstance()
					.getWidth() - (GameDisplay.getInstance().getWidth() / 5),
					GameDisplay.getInstance().getHeight() / 2);
			break;
		}

		backgroundSprite.setTransform(rotation);
		textSprite.setTransform(rotation);
		pressOKSprite.setTransform(rotation);
	}

	public void draw(Graphics g) {
		this.backgroundSprite.paint(g);
		this.textSprite.paint(g);

		blinkCount++;

		if (blinkCount >= 10)
			this.pressOKSprite.paint(g);

		if (blinkCount == 20)
			blinkCount = 0;
	}
}