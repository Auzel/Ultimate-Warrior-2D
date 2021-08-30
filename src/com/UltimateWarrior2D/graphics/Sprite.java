package com.UltimateWarrior2D.graphics;

import java.awt.Image;

public class Sprite {
 
    protected Mode mode;
 
    // position (pixels)
    private float x;
    private float y;
    // velocity (pixels per millisecond)
    private float dx;
    private float dy;

    /**
        Creates a new Sprite object with the specified Animation.
    */

    public Sprite(){
        
    }
    public Sprite(Mode mode) {
        this.mode = mode;
    }

    /**
        Updates this Sprite's Animation and its position based
        on the velocity.
    */
    public void update(long elapsedTime) {
        x += dx * elapsedTime;
        y += dy * elapsedTime;
        mode.update(elapsedTime);
    }

    /**
        Gets this Sprite's current x position.
    */
    public float getX() {
        return x;
    }

    /**
        Gets this Sprite's current y position.
    */
    public float getY() {
        return y;
    }

    /**
        Sets this Sprite's current x position.
    */
    public void setX(float x) {
        this.x = x;
    }

    /**
        Sets this Sprite's current y position.
    */
    public void setY(float y) {
        this.y = y;
    }

    
    /**
        Gets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityX() {
        return dx;
    }

    /**
        Gets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityY() {
        return dy;
    }

    /**
        Sets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityX(float dx) {
        this.dx = dx;
    }

    /**
        Sets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityY(float dy) {
        this.dy = dy;
    }

    /**
        Clones this Sprite. Does not clone position or velocity
        info.
    */
    public Object clone() {
        return new Sprite(mode);
    }

    public Mode getMode(){
        return mode;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }


    /**
        Gets this Sprite's width, based on the size of the
        current image.
    */
    public int getWidth() {
        return mode.getWidth();
    }

    /**
        Gets this Mode's height, based on the size of the
        current image.
    */
    public int getHeight() {
        return mode.getHeight();
    }

     /**
        Gets this Sprite's max width, based on the size of the
        current image.
    */
    public int getMaxWidth() {
        return mode.getMaxWidth();
    }

    /**
        Gets this Mode's max height, based on the size of the
        current image.
    */
    public int getMaxHeight() {
        return mode.getMaxHeight();
    }

   



    
    /**
        Gets this Mode's current image.
    */
    public Image getImage() {
        return mode.getImage();
    }


}
