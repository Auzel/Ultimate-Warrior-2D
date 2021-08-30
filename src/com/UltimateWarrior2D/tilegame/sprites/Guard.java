package com.UltimateWarrior2D.tilegame.sprites;

import com.UltimateWarrior2D.graphics.Animation;
import com.UltimateWarrior2D.graphics.Mode;
import java.lang.reflect.Constructor;


/**
    Police
*/
public class Guard extends Character {

    public static float INITIAL_SPEED = 0.2f;

    private Mode alive;
    private Mode die;  //just change it with color
    

    public Guard(Mode alive, Mode die)
    {
        super();
        this.mode = alive;
        this.alive = alive;
        this.die = die;
        this.health = 1;
    
    }

    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(new Object[] {
                (Mode)alive.clone(),
                (Mode)die.clone(),
            });
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }


 
    /**
        Gets the initial speed of this Creature.
    */
    public float getInitialSpeed() {
        return INITIAL_SPEED;
    }


    /**
        Wakes up the creature when the Creature first appears
        on screen. Normally, the creature starts moving left.
    */
    public void wakeUp() {
        if (!isDying() && getVelocityX() == 0) {
            setVelocityX(getInitialSpeed());
        }
    }

    /**
        Updates the animaton for this creature.
    */
    public void update(long elapsedTime) {
        Mode newMode = mode;
        int direction=Character.RIGHT;

        if (getVelocityX() < 0) {
            direction = Character.LEFT;
        }
        else if (getVelocityX() > 0) {
            direction = Character.RIGHT;
        }

        if(mode==die){
            dead=true;
        }

        if (isDying()) {
            newMode = die;
        } else{
            newMode = alive;
        }

        if (mode != newMode) {
            mode = newMode;
            mode.start(direction);
        }
        else {
            mode.update(elapsedTime,direction);
        }
    }


}
