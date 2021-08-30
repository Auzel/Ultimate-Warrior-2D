package com.UltimateWarrior2D.tilegame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;

import com.UltimateWarrior2D.graphics.*;
import com.UltimateWarrior2D.input.*;
import com.UltimateWarrior2D.tilegame.GameCore;
import com.UltimateWarrior2D.tilegame.sprites.*;
import com.UltimateWarrior2D.tilegame.sprites.Character;


/**
    GameManager manages all parts of the game.
*/
public class GameManager extends GameCore {

    public static void main(String[] args) {
        new GameManager().run();
    }

    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    private static final AudioFormat PLAYBACK_FORMAT =
        new AudioFormat(44100, 16, 1, true, false);

    private static final int DRUM_TRACK = 1;

    public static final float GRAVITY = 0.002f;

    private Point pointCache = new Point();
    private TileMap map;
    private SoundManager soundManager;
    private ResourceManager resourceManager;
    private InputManager inputManager;
    private TileMapRenderer renderer;

    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction run;
    private GameAction attack;
    private GameAction jump;
    private GameAction pause;
    private GameAction restart;
    private GameAction nextTrack;
    private GameAction mute;
    private GameAction exit;
    private InfoRenderer buttonRenderer;
    private Player player;


    public static final int HURT_DURATION = 5000;
    private long lastBite; 
    private long lastHitByBaton; 

    private Random random;

    private boolean muteBackgroundSound;
    private boolean gameOver;

    public void init() {
        super.init();


        // set up input manager
        initInput();

        // start resource manager
        resourceManager = ResourceManager.getInstance(screen.getFullScreenWindow().getGraphicsConfiguration());
        resourceManager.initGame();

        // load resources
        renderer = new TileMapRenderer();
        Image background = resourceManager.loadImage("Backgrounds/Level1/Background.png");
        float scaleX = displayMode.getWidth() / (background.getWidth(null) * 1.0f);
        float scaleY = displayMode.getHeight() / (background.getHeight(null) * 1.0f);
        Image scaledBackground = resourceManager.getScaledImage(background,scaleX,scaleY);

        renderer.setBackground(scaledBackground);

        // load first map
        map = resourceManager.loadNextMap();

        // load sounds and play background Sound
        soundManager = SoundManager.getInstance();
        soundManager.playTrack();;
        muteBackgroundSound = false;
        
        //buttonRenderer
        buttonRenderer = new InfoRenderer();


        //Initial player object
        player = new Player();

        random = new Random();

        gameOver = false;

    }

    private void initInput() {
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        run = new GameAction("run");
        attack = new GameAction("attack",GameAction.DETECT_INITAL_PRESS_ONLY);
        jump = new GameAction("jump",GameAction.DETECT_INITAL_PRESS_ONLY);
        restart = new GameAction("restart",GameAction.DETECT_INITAL_PRESS_ONLY);
        pause = new GameAction("pause",GameAction.DETECT_INITAL_PRESS_ONLY);
        nextTrack = new GameAction("next",GameAction.DETECT_INITAL_PRESS_ONLY);
        mute = new GameAction("mute",GameAction.DETECT_INITAL_PRESS_ONLY);
        exit = new GameAction("exit",
            GameAction.DETECT_INITAL_PRESS_ONLY);

        inputManager = InputManager.getInstance(screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(attack, KeyEvent.VK_C);
        inputManager.mapToKey(run, KeyEvent.VK_SHIFT);
        inputManager.mapToKey(restart, KeyEvent.VK_R);
        inputManager.mapToKey(pause, KeyEvent.VK_P);
        inputManager.mapToKey(mute, KeyEvent.VK_M);
        inputManager.mapToKey(nextTrack, KeyEvent.VK_N);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
    }
    
    public void checkInputWhenPaused(){
        if(pause.isPressed()){
            pauseGame = !pauseGame;
        }
        if(mute.isPressed()){
            if(!muteBackgroundSound){
                soundManager.muteTrack();;
            }
            else{
                soundManager.playTrack();;
            }
            muteBackgroundSound = !muteBackgroundSound;
        }
        if(nextTrack.isPressed()){
            soundManager.playNextTrack();
        }

        if(restart.isPressed()){
            gameOver=false;
            pauseGame=false;
            map = resourceManager.reloadMap();
        }
        if (exit.isPressed()) {
            stop();
        } 

    }


    private void checkInput(long elapsedTime) {

    
        Warrior playerWarrior = (Warrior)map.getPlayer();
        if (!playerWarrior.isDead()) {

            
            if (moveLeft.isPressed() && run.isPressed()){
                playerWarrior.run(Character.LEFT);
               
            }
            else if(moveRight.isPressed() && run.isPressed()){
               playerWarrior.run(Character.RIGHT);
              
            }
            
            else if (moveLeft.isPressed()) {
                playerWarrior.walk(Character.LEFT);
                
            }
            else if(moveRight.isPressed()){
                playerWarrior.walk(Character.RIGHT);
            }
         
            if (jump.isPressed()) {
                playerWarrior.jump();
            }
            if(attack.isPressed()){
                playerWarrior.attack();
        
            }

            if(!moveLeft.isPressed() && !moveRight.isPressed()){
                playerWarrior.setVelocityX(0); //reset velocity to 0
            }

        }

    }


    public void draw(Graphics2D g) {
        renderer.draw(g, map,
            screen.getWidth(), screen.getHeight());
        
        buttonRenderer.drawButtons(g,map,player,pauseGame,muteBackgroundSound,gameOver,resourceManager);
    }


    /**
        Gets the current map.
    */
    public TileMap getMap() {
        return map;
    }


    /**
        Gets the tile that a Sprites collides with. Only the
        Sprite's X or Y should be changed, not both. Returns null
        if no collision is detected.
    */
    public Point getTileCollision(Sprite sprite,
        float newX, float newY)
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);

    
        // get the tile locations
        int fromTileX = TileMapRenderer.pixelsToTiles(fromX);
        int fromTileY = TileMapRenderer.pixelsToTiles(fromY);
        int toTileX = TileMapRenderer.pixelsToTiles(
            toX + sprite.getWidth() - 1);
        int toTileY = TileMapRenderer.pixelsToTiles(
            toY + sprite.getHeight() - 1);

        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                    map.getTile(x, y) != null)
                {
                    
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                    
                }
            }
        }

        // no collision found
        return null;
    }


    /**
        Checks if two Sprites collide with one another. Returns
        false if the two Sprites are the same. Returns false if
        one of the Sprites is a Character that is not alive.
    */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }

        // if one of the Sprites is a dead Character, return false
        if (s1 instanceof Character && ((Character)s1).isDead()) {
            return false;
        }
        if (s2 instanceof Character && ((Character)s2).isDead()) {
            return false;
        }

        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());

        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
            s2x < s1x + s1.getWidth() &&
            s1y < s2y + s2.getHeight() &&
            s2y < s1y + s1.getHeight());
    }


    /**
        Gets the Sprite that collides with the specified Sprite,
        or null if no Sprite collides with the specified Sprite.
    */
    public Sprite getSpriteCollision(Sprite sprite) {

        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }

        // no collision found
        return null;
    }


    /**
        Updates Animation, position, and velocity of all Sprites
        in the current map.
    */
    public void update(long elapsedTime) {
        Character playerWarrior = (Character) (map.getPlayer());


        // playerWarrior is dead! start map over
        if (playerWarrior.isDead()) {
            gameOver = true;
            return;
        }
        

        // get keyboard/mouse input
        checkInput(elapsedTime);

        // update playerWarrior
       updateCharacter(playerWarrior, elapsedTime);
        playerWarrior.update(elapsedTime);
        

        // update other sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Character) {
                Character character = (Character)sprite;
                if (character.isDead()) {
                    i.remove();
                }
                else {
                   updateCharacter(character, elapsedTime);
                }
            }
           
            // normal update
           sprite.update(elapsedTime);
           if(sprite instanceof Warrior){
            Warrior warr = (Warrior) sprite;

            updateOpponentAction(warr);
        }
        }
    }


    /**
        Updates the Character, applying gravity for Characters that
        aren't flying, and checks collisions.
    */
    private void updateCharacter(Character character,
        long elapsedTime)
    {
       
        // apply gravity
       
        character.setVelocityY(character.getVelocityY() + GRAVITY * elapsedTime);
        

        // change x
        float dx = character.getVelocityX();
       
        float oldX = character.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
            getTileCollision(character, newX, character.getY());
        if (tile == null) {
            character.setX(newX);
            
        }
        else {
           
           
            // line up with the tile boundary
            if (dx > 0) {
                
                character.setX(
                    TileMapRenderer.tilesToPixels(tile.x) -
                    character.getMaxWidth());   
  
            }
            else if (dx < 0) {
                character.setX(
                    TileMapRenderer.tilesToPixels(tile.x + 1));
            }
            character.collideHorizontal();
            
        }
       
        
        // change y
        float dy = character.getVelocityY();
        float oldY = character.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(character, character.getX(), newY);
        if (tile == null) {
            character.setY(newY);
        }
        else {
            
            // line up with the tile boundary
            if (dy > 0) {
                character.setY(
                    TileMapRenderer.tilesToPixels(tile.y) -
                    character.getMaxHeight());
            }
            
            else if (dy < 0) {
                character.setY(
                    TileMapRenderer.tilesToPixels(tile.y + 1));
            }
            character.collideVertical();
             
        }
        
        if (character==map.getPlayer()) {
            checkPlayerCollision((Warrior)character);
        }
       
        

    }


    /**
        Checks for Player collision with other Sprites. If
        canKill is true, collisions with characters will kill
        them.
    */
    public void checkPlayerCollision(Warrior warrior)
    {
        if (warrior.isDead()) {
            return;
        }

        // check for warrior collision with other sprites
        Sprite collisionSprite = getSpriteCollision(warrior);

        if (collisionSprite instanceof Accessory) {
            acquireAccessory((Accessory)collisionSprite);
            return;
        }

        else if (collisionSprite instanceof Guard) {
            Guard guard  = (Guard) collisionSprite; 
            if(warrior.isAttacking()){ // then warrior is in attack mode
                guard.setDying();
                soundManager.playEffects("guardHurt");
            } else{
                if(System.currentTimeMillis()-lastHitByBaton>HURT_DURATION){
                    warrior.reduceHealth((1/3));
                    guard.setVelocityX(-guard.getVelocityX());
                    lastHitByBaton=System.currentTimeMillis();
                    soundManager.playEffects("warriorHurt");
                } 
            }   
           
            
        }

        else if(collisionSprite instanceof Dog){
            Dog dog  = (Dog) collisionSprite; 
            if(warrior.isAttacking()){ // then warrior is in attack mode
                dog.setDying();
                soundManager.playEffects("dogHurt");
            } else{
                if(System.currentTimeMillis()-lastBite>HURT_DURATION){
                    warrior.reduceHealth(0.5f);
                    dog.setVelocityX(-dog.getVelocityX());
                    lastBite=System.currentTimeMillis();
                    soundManager.playEffects("warriorHurt");
                
                }
                
            }

        }
        else if(collisionSprite instanceof Warrior){
            Warrior opponentWarrior  = (Warrior) collisionSprite; 

            opponentWarrior.setVelocityX(0);
            warrior.setVelocityX(0);
         
            
            if(warrior.isAttacking()){ // then playerWarrior is in attack mode
                opponentWarrior.reduceHealth(warrior.getPower());
            } 
            
            if(opponentWarrior.isAttacking()){
                warrior.reduceHealth(opponentWarrior.getPower());
                
            }
            soundManager.playEffects("warriorHurt");
        }
        

    }


    /**
        Gives the playerWarrior the speicifed power up and removes it
        from the map.
    */
    public void acquireAccessory(Accessory powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);

        Warrior playerWarrior = (Warrior) map.getPlayer();

        if (powerUp instanceof Accessory.Health) {
            soundManager.playEffects("health");
            playerWarrior.increaseHealth();
        }
        else if (powerUp instanceof Accessory.Life) {
           soundManager.playEffects("life");
            playerWarrior.newLife();
        }
        else if (powerUp instanceof Accessory.XP) {
            player.increaseXP();
            soundManager.playEffects("XP");
        }
        else if (powerUp instanceof Accessory.Door) {
            player.setLevel(resourceManager.getGameLevel());
            map = resourceManager.loadNextMap();
            //soundManager.playSound("DoorSound",false);
        }
        
    }
    //give gameplay behavior in fight
    public void updateOpponentAction(Warrior warrior){
        int rand, direction;
        
        Warrior playerWarrior = (Warrior) map.getPlayer();

        if(playerWarrior.getX()<warrior.getX()){
            direction = Character.LEFT; 
        } else{
            direction = Character.RIGHT;
        }


        rand = random.nextInt(1000);

        if(rand>=0 && rand<=5){
            warrior.attack();
        }

        if(rand==6){
            warrior.jump();
        }

        if(rand>=7 && rand<=14){
            warrior.run(direction);
        }
        if(rand>15 && rand<=19){
            warrior.walk(direction);
        }
     
        

    }

}
