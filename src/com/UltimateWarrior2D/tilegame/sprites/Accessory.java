package com.UltimateWarrior2D.tilegame.sprites;

import java.lang.reflect.Constructor;
import com.UltimateWarrior2D.graphics.*;

/**
    A PowerUp class is a Sprite that the player can pick up.
*/
public abstract class Accessory extends Sprite {

    public Accessory(Mode mode) {
        super(mode);
    }


    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(
                new Object[] {(Mode)mode.cloneSimple()});
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }




     /**
       Additional health
    */
    public static class Health extends Accessory {
        public Health(Mode mode) {
            super(mode);
        }
    }

    
     /**
        Extra Life Power Up
    */
    public static class Life extends Accessory {
        public Life(Mode mode) {
            super(mode);
        }
    }

    /**
        A XP PowerUp. Gives the player points.
    */
    public static class XP extends Accessory {
        public XP(Mode mode) {
            super(mode);
        }
    }



    /**
        A Door object.
    */
    public static class Door extends Accessory {
        public Door(Mode mode) {
            super(mode);
        }

        public static class Lock extends Door {
            public Lock(Mode mode) {
                super(mode);
            }
        }

    }

}