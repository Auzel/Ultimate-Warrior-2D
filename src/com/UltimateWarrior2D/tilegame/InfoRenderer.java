package com.UltimateWarrior2D.tilegame;

import com.UltimateWarrior2D.graphics.ScreenManager;
import com.UltimateWarrior2D.tilegame.sprites.*;
import java.awt.*;


public class InfoRenderer{

    int pWidth, pHeight;

    private ScreenManager screenManager;
    

    



    public InfoRenderer(){
        screenManager = ScreenManager.getInstance();
        pWidth = screenManager.getWidth();
        pHeight = screenManager.getHeight();

    }
    


    public void drawButtons (Graphics g, TileMap map, Player player, boolean paused, boolean mute, boolean gameOver, ResourceManager resourceMgr) {

        Warrior playerWarrior = (Warrior) map.getPlayer();

        Font oldFont, newFont;

        oldFont = g.getFont();		// save current font to restore when finished

        newFont = new Font ("TimesRoman", Font.ITALIC + Font.BOLD, 18);
        g.setFont(newFont);		// set this as font for text on buttons


        g.setColor(Color.RED);
        g.drawString("Level",pWidth-350, 50);
        g.setColor(Color.WHITE);
        String lvl = String.valueOf(player.getLevel());
        g.drawString(lvl, pWidth-350, 80);

        g.setColor(Color.RED);
        g.drawString("HEALTH",pWidth-275, 50);
        g.setColor(Color.WHITE);
        String health = String.valueOf((int)(playerWarrior.getHealthPercent()));
        g.drawString(health+"%", pWidth-275, 80);


        g.setColor(Color.RED);
        g.drawString("XP",pWidth-150, 50);
        g.setColor(Color.WHITE);
        String xp= String.valueOf(player.getXP());
        g.drawString(xp, pWidth-150, 80);

        g.setColor(Color.RED);
        g.drawString("Music",pWidth-75, 50);

        
        if(!mute){
            g.drawImage(resourceMgr.getMusicImage(),pWidth-75,60,null);
        } else{
            g.drawImage(resourceMgr.getMuteImage(),pWidth-75,60,null);
        }
        
    
        if(paused){
            g.drawImage(resourceMgr.getPauseImage(),pWidth / 3,pHeight / 3-50,null);
        } 
        

        if(gameOver){
            gameOverMessage(g);
        }
    
        
        
    }

    private void gameOverMessage(Graphics g) {
		
		Font font = new Font("SansSerif", Font.BOLD, 36);

		String msg = "Game Over!";

		int x = pWidth / 2 - 100; 
		int y = pHeight / 2 ;

		g.setColor(Color.RED);
		g.setFont(font);
		g.drawString(msg, x, y);

	}



}
    