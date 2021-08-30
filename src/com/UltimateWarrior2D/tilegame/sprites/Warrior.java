package com.UltimateWarrior2D.tilegame.sprites;

import com.UltimateWarrior2D.graphics.Animation;
import com.UltimateWarrior2D.graphics.Mode;
import com.UltimateWarrior2D.tilegame.Weapons.Weapon;
import java.lang.reflect.Constructor;

import java.util.HashMap;

import javax.lang.model.util.ElementScanner14;

/**
    Warrior
*/
public abstract class Warrior extends Character {

    private HashMap<String, Mode> modes;
    private long duration;
    protected float walkSpeed;
    protected float runSpeed;
    protected float jumpSpeed;
    protected Weapon weapon;
    protected float hit;
  
    

    protected boolean onGround;

    public Warrior(Mode idle, Mode walk,Mode run, Mode jump, 
                    Mode attack, Mode die, Mode hurt)
    {
        super(idle);

        modes = new HashMap<>();
        modes.put("IDLE", idle);
        modes.put("WALK", walk);
        modes.put("RUN", run);
        modes.put("JUMP", jump);
        modes.put("ATTACK", attack);
        modes.put("DIE", die);
        modes.put("HURT", hurt);

        duration=0;
    }

    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            Object warriorAsObject =  constructor.newInstance(new Object[] {
                (Mode)(modes.get("IDLE")).clone(),
                (Mode)(modes.get("WALK")).clone(),
                (Mode)(modes.get("RUN")).clone(),
                (Mode)(modes.get("JUMP")).clone(),
                (Mode)(modes.get("ATTACK")).clone(),
                (Mode)(modes.get("DIE")).clone(),
                (Mode)(modes.get("HURT")).clone()
            });
            Warrior warrior = (Warrior) warriorAsObject;
            warrior.health=health;
            warrior.walkSpeed=walkSpeed;
            warrior.runSpeed=runSpeed;
            warrior.jumpSpeed=jumpSpeed;
            warrior.weapon=weapon;
            return warrior;
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        } 
    }

   

    public void update(long elapsedTime){
         // select the correct Animation
        duration+=elapsedTime;
         // don't change mode til after duration needed
         Mode newMode=mode;

         if(duration > mode.getDuration()){
             
             //if current mode is die (after exceeding duration) then update to dead
             if(mode==modes.get("DIE")){
                 dead=true;
             }

             //now update mode for further iterations
            if(isDying()){
                newMode = modes.get("DIE");
             } 
             else if(prevHealth!=health){
                newMode = modes.get("HURT");  //when u go in a mode for a duration the gme need to not allow u to get in another mode in game mgr
             }
             else if(!onGround){
                newMode = modes.get("JUMP");
             }
             else if(isAttacking()){
                newMode = modes.get("ATTACK");
             }
             
             else if(Math.abs(getVelocityX())==runSpeed){
                newMode = modes.get("RUN");
             }
             else if(Math.abs(getVelocityX())==walkSpeed){
                newMode = modes.get("WALK");
             }
             else{
                newMode = modes.get("IDLE");
             }
            

         }
         
         
        int direction=Character.RIGHT;

         if (getVelocityX() < 0) {
            direction = Character.LEFT;
         }
         else if (getVelocityX() > 0) {
            direction = Character.RIGHT;
         }
         else{ // ==0
            direction = mode.getDirection(); // get direction of currently standing
         }
 
         // update the Animation

         if(mode!=newMode){
            mode=newMode;
            mode.start(direction);
            duration=0;
         } else{
             mode.update(elapsedTime, direction);
         }
 
         prevHealth = health; 
         hit = 0;  //reset hit
         
    }

   public void updateInMenu(long elapsedTime){

        mode.update(elapsedTime,Character.RIGHT);
        
    }

    public void setMode(String newModeStr){
        Mode newMode = modes.get(newModeStr);
        if(newMode!=null){
            mode = newMode;
        }
    }
        
   


    public void wakeUp(){
        //do nothing

    }

    @Override
    public void collideHorizontal() {
        setVelocityX(0);
    }


    @Override
    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        super.collideVertical();
    }


    @Override
    public void setY(float y) {
        // check if falling
        if (Math.round(y) > Math.round(getY())) {
            onGround = false;
        }
        super.setY(y);
    }

  
    public boolean isAttacking(){
        return hit!=0;
    }
    /**
        Makes the warrior jump if the player is on the ground or
        if forceJump is true.
    */
    public abstract void jump();/* {
        
        if (onGround) {
            onGround = false;
            setVelocityY(JUMP_SPEED);
        }
        
    }
    */

 
    
    public float getPower(){
        return weapon.getPower();
    }

    public void reduceHealth(float hit){
        float reduction;
        reduction = hit*getInitialHealth();
        if(health-reduction<=0){
            health=0;
        } else{
            health-= reduction;
        }
        
    }

    
    public abstract void run(int direction);
    public abstract void walk(int direction);
    public abstract void attack();
    public abstract int getInitialHealth();

    public abstract float getHealthPercent();
    public abstract void increaseHealth();
    public abstract void newLife();

}
