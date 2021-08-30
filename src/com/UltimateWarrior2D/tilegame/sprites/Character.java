package com.UltimateWarrior2D.tilegame.sprites;

import java.lang.reflect.Constructor;
import com.UltimateWarrior2D.graphics.*;

/**
    A Character is a Sprite that is affected by gravity and can
    die. It has four Animations: moving left, moving right,
    dying on the left, and dying on the right.
*/
public abstract class Character extends Sprite {

    /**
        Amount of time to go from STATE_DYING to STATE_DEAD.
    */
    public static int LEFT=0;
    public static int RIGHT=1;
    protected int health;
    protected int prevHealth;
    public boolean dead;
    //private int direction; deleted from model


    /**
        Creates a new Character with the specified Animations.
    */

    public Character(){
        this.dead = false;
    }
    public Character(Mode mode)
    {
        super(mode);
        this.dead = false;
    }

    
    public abstract Object clone();
    /* {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(new Object[] {
                (Animation[])idle.clone(),
                (Animation[])walk.clone(),
                (Animation[])run.clone(),
                (Animation[])jump.clone(),
                (Animation[])attack.clone(),
                (Animation[])die.clone(),
                (Animation[])hurt.clone()
            });
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        } 
    }*/
    


    /**
        Gets the maximum speed of this Character.
    */
    /*
    public abstract float getWalkSpeed();

    public abstract float getRunSpeed();
    */


    /**
        Wakes up the Character when the Character first appears
        on screen. Normally, the Character starts moving left.
    */
    
    public abstract void wakeUp(); /*{
        
        if (getState() == STATE_NORMAL && getVelocityX() == 0) {
            setVelocityX(-getMaxSpeed());
        }
        
    } */
    
    
 

    /**
        Gets the state of this Character. The state is either
        STATE_NORMAL, STATE_DYING, or STATE_DEAD.
    */
  


    /**
        Sets the state of this Character to STATE_NORMAL,
        STATE_DYING, or STATE_DEAD.
    */

    /*
    public void setMode(int state){ 
        
        if (this.state != state) {
            this.state = state;
            stateTime = 0;
            if (state == STATE_DYING) {
                setVelocityX(0);
                setVelocityY(0);
            }
        }
        
    }
    */

    /**
        Checks if this Character is dying.
    */
    public boolean isDying() {
        return (health<=0);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDying(){
        health=0;
        setVelocityX(0);
        setVelocityY(0);
    }

    /**
        Called before update() if the Character collided with a
        tile horizontally.
    */
    public void collideHorizontal() {
        setVelocityX(-getVelocityX());
    }


    /**
        Called before update() if the Character collided with a
        tile vertically.
    */
    public void collideVertical() {
        setVelocityY(0);
    }

    
    

    /**
        Updates the animation for this Character.
    */
    // need to override this method from in Sprite
    public abstract void update(long elapsedTime); /* {
        
        // select the correct Animation
        Animation newAnim = anim;
        if (getVelocityX() < 0) {
            newAnim = idle[0];
        }
        else if (getVelocityX() > 0) {
            newAnim = idle[1];
        }
        if (state == STATE_DYING && newAnim == idle[0]) { //set to left left  {the second and}
            newAnim = idle[0];
        }
        else if (state == STATE_DYING && newAnim == idle[1]) {//set to right   {the second and}
            newAnim = idle[1];
        }

        // update the Animation
        if (anim != newAnim) {
            anim = newAnim;
            anim.start();
        }
        else {
            anim.update(elapsedTime);
        }

        // update to "dead" state
        stateTime += elapsedTime;
        if (state == STATE_DYING && stateTime >= DIE_TIME) {
            setState(STATE_DEAD);
        }
    }
    */

}
