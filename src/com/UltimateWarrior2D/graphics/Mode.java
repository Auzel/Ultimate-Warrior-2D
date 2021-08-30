package com.UltimateWarrior2D.graphics;
import com.UltimateWarrior2D.tilegame.sprites.Character;
import java.awt.Image;

public class Mode {
    private long duration;
    private long startTime;
    private Animation leftAnimation;
    private Animation rightAnimation;
    protected Animation animation;

    public Mode(Animation anim){
        this.animation = anim;
        duration = -1; //indicate not valid. goes indefinitely
    }

    public Mode(Animation leftAnimation, Animation rightAnimation){
        this.animation = rightAnimation;
        this.leftAnimation = leftAnimation;
        this.rightAnimation = rightAnimation;
        this.duration = -1;
        this.startTime = System.currentTimeMillis();
    }

    public Mode(long duration, Animation leftAnimation, Animation rightAnimation){
        this.animation = rightAnimation;
        this.leftAnimation = leftAnimation;
        this.rightAnimation = rightAnimation;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
    }

    public Object clone() {
        return new Mode(duration, leftAnimation, rightAnimation);
    }
    public Object cloneSimple() {
        return new Mode(animation);
    }

    public void start(int direction){
        if(direction==Character.LEFT){
            this.animation = leftAnimation;
        } else{
            this.animation = rightAnimation;
        }
        animation.start();
    }

    public void update(long elapsedTime){
        animation.update(elapsedTime);
    }
    
    public void update(long elapsedTime, int direction){
        if(direction==Character.LEFT)
            animation = leftAnimation;
        else //we going right
            animation = rightAnimation;
        
        animation.update(elapsedTime);
    }

    public long getDuration(){
        return duration;
    }

    public long getStartTime(){
        return startTime;
    }

    /**
        Gets this Mode's width, based on the size of the
        current image.
    */
    public int getWidth() {
        return animation.getImage().getWidth(null);
    }

    /**
        Gets this Mode's height, based on the size of the
        current image.
    */
    public int getHeight() {
        return animation.getImage().getHeight(null);
    }

    /**
        Gets this Mode's max width, based on the size of the
        current image.
    */
    public int getMaxWidth() {
        return animation.getMaxWidth();
    }

    /**
        Gets this Mode's max height, based on the size of the
        current image.
    */
    public int getMaxHeight() {
        return animation.getMaxHeight();
    }


    
    /**
        Gets this Mode's current image.
    */
    public Image getImage() {
        return animation.getImage();
    }

 
    public int getDirection(){
        if(animation == leftAnimation)
            return Character.LEFT;
        return Character.RIGHT;
    }

    
}
