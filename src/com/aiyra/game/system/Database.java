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

import com.aiyra.game.display.HighScoreScreen;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

/**
 *
 * @author Musmanno
 */
public class Database
{
    private RecordStore recordStore;
    
    public Database()
    {
    }
    
    private void abrirBanco() throws Exception
    {
        recordStore = RecordStore.openRecordStore("aiyTr_Ranking", true);
    }
    
    private void fecharBanco() throws Exception
    {
        recordStore.closeRecordStore();
    }
    
    public void saveScore(int score)
    {
        byte[] dados = intToByteArray(score);
        try
        {
            abrirBanco();
            recordStore.addRecord(dados, 0, dados.length);
            
            //getHighScores() sanitizes highScores database.
            getHighScores();
            
            fecharBanco();
        }
        catch (Exception e) 
        {
             e.printStackTrace();
        }
    }
    
    public int[] getHighScores()
    {
        int total = 0;
        int[] output = new int[HighScoreScreen.NUMBER_OF_SCORES];
        int[] scores = null;
        
        try
        {
            abrirBanco();
            total = recordStore.getNumRecords();
            
            scores = new int[total];
            
            RecordEnumeration recEnum = recordStore.enumerateRecords(null, null, false);
            
            for (int i = 0; i < total; ++i)
                scores[i] = byteArrayToInt(recEnum.nextRecord());
            
            scores = sortScores(scores);
            
            for (int i = 0; i < HighScoreScreen.NUMBER_OF_SCORES && i < scores.length; i++)
                output[i] = scores[i];
            
            recEnum.reset();
            int previousId = 0; 
                    
            if (recEnum.hasNextElement())
                previousId = recEnum.nextRecordId();
                
            while (recEnum.hasNextElement())
            {
                 /*int id =*/ recEnum.nextRecordId();
                 int previousScore = byteArrayToInt(recEnum.previousRecord());
                 
                 if (previousScore < output[output.length - 1])
                     recordStore.deleteRecord(previousId);
                 
                 previousId = recEnum.nextRecordId();
            }
            
            fecharBanco();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return output;
    }
    
    public static int[] sortScores(int[] scores)
    {
        for (int i = 0; i < scores.length; i++)
        {
            int max = i;
            for (int j = i; j < scores.length; j++)
            {
                if (scores[max] < scores[j])
                    max = j;
            }

            int temp = scores[max];
            scores[max] = scores[i];
            scores[i] = temp;
        }
        
        return scores;
    }

    private static byte[] intToByteArray(int value)
    {
        return new byte[]
        {
            (byte)(value >>> 24),
            (byte)(value >>> 16),
            (byte)(value >>> 8),
            (byte)value
        };
    }
    
    private static int byteArrayToInt(byte[] b) 
    {
        return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + (b[3] & 0xFF);
    }
}
