package com.UltimateWarrior2D.tilegame;

public class Player {
    private String name;
    private int XP;
    private int level;

    public Player(){
        this.name="";
        XP=0;
        level=1;
    }
    public Player(String name){
        this.name = name;
    }

    public void increaseXP(){
        XP++;
    }

    public void increaseXP(int XP){
        this.XP+=XP;
    }

    public int getXP(){
        return XP;
    }

    public String getName(){
        return name;
    }
    
    public void setLevel(int lvl){
        this.level=lvl;
    }

    public int getLevel(){
        return this.level;
    }
}
