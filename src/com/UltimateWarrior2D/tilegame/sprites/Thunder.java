package com.UltimateWarrior2D.tilegame.sprites;
import com.UltimateWarrior2D.graphics.Mode;
import com.UltimateWarrior2D.graphics.Animation;
import com.UltimateWarrior2D.tilegame.Weapons.Spear;

/**
    Thunder Warrior
*/
public class Thunder extends Warrior {

    public static int initial_width = 48 ;
    public static int initial_length = 70 ;
    public static float initial_walk_speed = 0.100f;
    public static float initial_run_speed = 0.45f ;
    public static float initial_jump_speed = 0.975f;
    public static int initial_health = 60;

    public Thunder(Mode idle, Mode walk,Mode run, Mode jump, 
    Mode attack, Mode die, Mode hurt)
    {
        super(idle,walk,run,jump,attack, die,hurt);
        prevHealth= initial_health;
        health = initial_health;
        walkSpeed=initial_walk_speed;
        runSpeed = initial_run_speed;
        jumpSpeed = initial_jump_speed;
        weapon = new Spear();

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
