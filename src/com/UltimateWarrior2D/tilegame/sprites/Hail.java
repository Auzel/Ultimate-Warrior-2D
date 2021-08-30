package com.UltimateWarrior2D.tilegame.sprites;
import com.UltimateWarrior2D.graphics.Mode;
import com.UltimateWarrior2D.graphics.Animation;
import com.UltimateWarrior2D.tilegame.Weapons.Axe;

/**
    Hail Warrior
*/
public class Hail extends Warrior {

    public static int initial_width = 65;
    public static int initial_length = 80;
    public static float initial_walk_speed = 0.075f ;
    public static float initial_run_speed = 0.400f ;
    public static  float initial_jump_speed = 0.950f;
    public static int initial_health = 50;

    public Hail(Mode idle, Mode walk,Mode run, Mode jump, 
    Mode attack, Mode die, Mode hurt)
    {
        super(idle,walk,run,jump,attack, die,hurt);
        prevHealth= initial_health;
        health = initial_health;
        walkSpeed=initial_walk_speed;
        runSpeed = initial_run_speed;
        jumpSpeed = initial_jump_speed;
        weapon = new Axe();
    }

    public void jump(){
        if (onGround) {
            onGround = false;
            setVelocityY(-initial_jump_speed);
        }
    }

    public void run(int direction){

        if(direction==Character.LEFT){
            setVelocityX(-runSpeed);
        } else{
            setVelocityX(runSpeed);
        }
        
    }

    public void walk(int direction){

        if(direction==Character.LEFT){
            setVelocityX(-runSpeed);
        } else{
            setVelocityX(runSpeed);
        }
        
    }
    public void attack(){

        hit = weapon.getPower();
        
    }

    public int getInitialHealth(){
        return initial_health;
    }


    public float getHealthPercent(){
        return (health/(initial_health*1.0f))*100;
    }

    public void increaseHealth(){
        float increment = 0.25f*initial_health;
        if(health+increment>=initial_health){
            health=initial_health;
        }
        else{
            health+= increment;
        }
    }
    
    public void newLife(){
        health = initial_health;
    }
}
