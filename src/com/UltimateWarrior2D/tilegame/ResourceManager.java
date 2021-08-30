package com.UltimateWarrior2D.tilegame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;

import com.UltimateWarrior2D.graphics.*;
import com.UltimateWarrior2D.tilegame.sprites.*;


/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class ResourceManager {

    private static ResourceManager resourceManager=null;

    private final int NUM_SPRITE_DIRECTIONS=2;
    private final int NUM_SPRITE_FRAMES=5;
    private final int NUM_SPRITE_STATES = 7;


    private ArrayList tiles;
    private int currentMap;
    private int numMapsUsed;
    private GraphicsConfiguration gc;

    // host sprites used for cloning
    //private Sprite warriorSprite;


    private HashMap<String,Sprite> warriorSprites;
    private Sprite opponentWarriorSprite;
    private Sprite dogSprite;
    private Sprite guardSprite;

    private Sprite healthSprite;
    private Sprite lifeSprite;
    private Sprite xpSprite;
    private Sprite doorSprite;

    private Image muteImage;
    private Image musicImage;
    private Image pauseImage;
    

    private ResourceManager(){

    }
    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */

    public static ResourceManager getInstance(GraphicsConfiguration gc){
        if(resourceManager!=null){
            return resourceManager;
        }
        resourceManager  = new ResourceManager();
        resourceManager.gc=gc;
        resourceManager.warriorSprites = new HashMap<>();
        return resourceManager;
        
    }

    public void initGame(){
        this.numMapsUsed=1;
        loadTileImages();
        loadCharacterSprites();
        loadAccessorySprites();
        loadIcons();
    }

    /**
        Gets an image from the images/ directory.
    */
    public Image loadImage(String name) {
        String filename = "images/" + name;
        return new ImageIcon(filename).getImage();
    }


    public Image getMirrorImage(Image image) {
        return getScaledTransformedImage(image, -1, 1);
    }


    public Image getFlippedImage(Image image) {
        return getScaledTransformedImage(image, 1, -1);
    }


    private Image getScaledTransformedImage(Image image, float x, float y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }

    public Image getScaledImage(Image image, float x, float y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            (int) (image.getWidth(null)*x),
            (int) (image.getHeight(null)*y),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }


    public TileMap loadNextMap() {
        TileMap map = null;
        while (map == null) {
            currentMap++;
            try {
                map = loadMap(
                    "maps/map" + currentMap + ".txt");
                    
            }
            catch (IOException ex) {
                if (currentMap == 1) {
                    // no maps to load!
                    return null;
                }
                currentMap = 0;
                map = null;
            }
            if(currentMap>numMapsUsed){
                numMapsUsed=currentMap;
                
            }
        }

        return map;
    }


    public TileMap reloadMap() {
        try {
            return loadMap(
                "maps/map" + 1 + ".txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    private TileMap loadMap(String filename)
        throws IOException
    {
        loadOpponentWarrior();

        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }

                // check if the char represents a sprite
                
                
                else if (ch == '0') {
                    addSprite(newMap, opponentWarriorSprite, x, y);
                }

                else if (ch == '1') {
                    addSprite(newMap, dogSprite, x, y);
                }
            
                else if (ch == '2') {
                    addSprite(newMap, guardSprite, x, y);
                }
                
                else if (ch == '3') {
                    addSprite(newMap, healthSprite, x, y);
                }
                
                else if (ch == '4') {
                    addSprite(newMap, lifeSprite, x, y);
                }
                else if (ch == '5') {
                    addSprite(newMap, xpSprite, x, y);
                }
                else if (ch == '6') {
                    addSprite(newMap, doorSprite, x, y);
                }
            }
        }

        
        // add the player to the map
        Sprite playerWarriorSprite = warriorSprites.get("Hunter");
        Sprite playerWarrior = (Sprite)playerWarriorSprite.clone();
        playerWarrior.setX(TileMapRenderer.tilesToPixels(3));
        playerWarrior.setY(0);
        newMap.setPlayer(playerWarrior);

        return newMap;
    }


    private void addSprite(TileMap map,
        Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            Sprite sprite = (Sprite)hostSprite.clone();

            // center the sprite
            sprite.setX(
                TileMapRenderer.tilesToPixels(tileX) +
                (TileMapRenderer.tilesToPixels(1) -
                sprite.getWidth()) / 2);

            // bottom-justify the sprite
            sprite.setY(
                TileMapRenderer.tilesToPixels(tileY + 1) -
                sprite.getHeight());

            // add it to the map
            map.addSprite(sprite);
        }
    }


    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------


    public void loadTileImages() {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        tiles = new ArrayList();
        char ch = 'A';
        while (true) {
            String name = "tiles/tile_" + ch + ".png";
            File file = new File("images/" + name);
            if (!file.exists()) {
                break;
            }
            tiles.add(loadImage(name));
            ch++;
            
        }
    }


    public void loadOpponentWarrior(){
        Random random = new Random();
        int rand;
        rand = random.nextInt(3);

    
        if(rand==0){
            opponentWarriorSprite = warriorSprites.get("Hunter");
        } 
        else if(rand==1){
            opponentWarriorSprite = warriorSprites.get("Hail");
        }
        else if(rand==2){
            opponentWarriorSprite = warriorSprites.get("Thunder");
        }    
        
    }

    public void loadCharacterSprites() {

        loadWarriorSprites();
        loadDogSprite();
        loadGuardSprite();

    }

    private void loadWarriorSprites(){
        warriorSprites.put("Hunter",loadWarriorSprite("Hunter"));
        warriorSprites.put("Hail",loadWarriorSprite("Hail"));
        warriorSprites.put("Thunder",loadWarriorSprite("Thunder"));
    }

    private Sprite loadWarriorSprite(String warrior){
        Sprite warriorSprite;
        LinkedHashMap<String,Animation[]> stateAnimations;
        Animation[][] stateAnimationsArr;

        stateAnimations = createWarriorAnimations(warrior);
        stateAnimationsArr = new Animation[NUM_SPRITE_STATES][];
        stateAnimations.values().toArray(stateAnimationsArr);
        //load the correct warrior state

        Mode idle, walk, run, jump, attack, die, hurt;
        int modeDuration= 500 ;
        int dieDuration = 1000;
        int attackDuration = 500;
        
        idle = new Mode(modeDuration,stateAnimationsArr[0][0],stateAnimationsArr[0][1]);
        walk = new Mode(modeDuration,stateAnimationsArr[1][0],stateAnimationsArr[1][1]);
        run = new Mode(modeDuration,stateAnimationsArr[2][0],stateAnimationsArr[2][1]);
        jump = new Mode(modeDuration,stateAnimationsArr[3][0],stateAnimationsArr[3][1]);
        attack = new Mode(attackDuration,stateAnimationsArr[4][0],stateAnimationsArr[4][1]);
        die = new Mode(dieDuration,stateAnimationsArr[5][0],stateAnimationsArr[5][1]);
        hurt = new Mode(modeDuration,stateAnimationsArr[6][0],stateAnimationsArr[6][1]);
        
        warriorSprite = WarriorFactory.createWarrior(warrior, idle,walk,run,jump,attack,die,hurt);

        return warriorSprite;
    }


    private LinkedHashMap<String,Animation[]> createWarriorAnimations(String warrior){

        LinkedHashMap<String,Image[][]> warriorImages = loadWarriorImages(warrior);

        LinkedHashMap<String,Animation[]> warriorAnimations = new LinkedHashMap<>();


        
        for(Map.Entry<String,Image[][]> entry : warriorImages.entrySet()){
            Animation[] directionAnim = new Animation[NUM_SPRITE_DIRECTIONS];
            Image[][] images  = new Image[NUM_SPRITE_DIRECTIONS][];
            Image[] directionImage = new Image[NUM_SPRITE_FRAMES];

            for(int i=0;i<NUM_SPRITE_DIRECTIONS;i++){
                images = entry.getValue();
                directionImage = images[i];
                directionAnim[i] = createAnimation(directionImage);
                
            }
            
            warriorAnimations.put(entry.getKey(),directionAnim);

        }
    
        return warriorAnimations;
    }

    private LinkedHashMap<String,Image[][]> loadWarriorImages(String warrior){
        LinkedHashMap<String,Image[][]> stateSprites = new LinkedHashMap<>();
  
        //final int NUMSTATES  = 7;
       // Image[][] images = new Image[NUMSTATES][];
        String[] states = {"IDLE","WALK","RUN","JUMP","ATTACK","DIE","HURT"};
        // load left-facing images

     
        for(int i=0; i<states.length; i++){
            Image[] rightStateImage = new Image[] {
                loadImage("Warrior/"+warrior+"/"+(i+1)+" "+states[i]+"_000.png"),
                loadImage("Warrior/"+warrior+"/"+(i+1)+" "+states[i]+"_001.png"),
                loadImage("Warrior/"+warrior+"/"+(i+1)+" "+states[i]+"_002.png"),
                loadImage("Warrior/"+warrior+"/"+(i+1)+" "+states[i]+"_003.png"),
                loadImage("Warrior/"+warrior+"/"+(i+1)+" "+states[i]+"_004.png")
            };


            Image[] leftStateImage = new Image[rightStateImage.length];
            for (int j=0; j<rightStateImage.length; j++) {
                // left-facing images
                leftStateImage[j] = getMirrorImage(rightStateImage[j]);
             
            }
            

            Image[][] stateImages = new Image[][]{leftStateImage,rightStateImage};

            stateSprites.put(states[i],stateImages);
        }
        
        return stateSprites;

    }

    private void loadGuardSprite(){
        //come back and add more states

        LinkedHashMap<String,Image[][]> stateSprites = new LinkedHashMap<>();
  
        Image[] rightAttackImage;

        rightAttackImage = new Image[] {
            loadImage("Guard/3_guard_attack_Attack1_000.png"),
            loadImage("Guard/3_guard_attack_Attack1_001.png"),
            loadImage("Guard/3_guard_attack_Attack1_002.png"),
            loadImage("Guard/3_guard_attack_Attack1_003.png"),
            loadImage("Guard/3_guard_attack_Attack1_004.png"),
            loadImage("Guard/3_guard_attack_Attack1_005.png"),
            loadImage("Guard/3_guard_attack_Attack1_006.png"),
            loadImage("Guard/3_guard_attack_Attack1_007.png"),
            loadImage("Guard/3_guard_attack_Attack1_008.png"),
            loadImage("Guard/3_guard_attack_Attack1_009.png"),
        };
        
        Image[] leftAttackImage = new Image[rightAttackImage.length];
        for (int j=0; j<rightAttackImage.length; j++) {
            // left-facing images
            leftAttackImage[j] = getMirrorImage(rightAttackImage[j]);
            
        }
            

        Animation leftAnimation = createAnimation(leftAttackImage);
        Animation rightAnimation = createAnimation (rightAttackImage);
        Animation deadRightAnimation = rightAnimation;
        Animation deadLeftAnimation = leftAnimation;   //come back and update with new dead image; as well as new states
        
        Mode aliveMode  = new Mode(leftAnimation,rightAnimation);
        Mode deadMode  = new Mode(deadLeftAnimation,deadRightAnimation);
        guardSprite = new Guard(aliveMode, deadMode);
        

    }

    private Animation createAnimation(Image[] images){
        Animation anim = new Animation();
        for(Image image: images){
            anim.addFrame(image, 100);
        }
        return anim;
    }

    private void loadDogSprite(){
    
        Image rightStripImage1 = loadImage("dog1.png"); //Part 1
        Image rightStripImage2 = loadImage("dog2.png"); //Part 2

        Image leftStripImage1 = getMirrorImage(rightStripImage1);
        Image leftStripImage2 = getMirrorImage(rightStripImage2);


        Animation leftAnimation = getAnimation (leftStripImage1, leftStripImage2,true);
        Animation rightAnimation = getAnimation (rightStripImage1, rightStripImage2,false);
       
        Animation deadLeftAnimation = leftAnimation;
        Animation deadRightAnimation = rightAnimation;  // change these after with some color changes 

        Mode aliveMode  = new Mode(leftAnimation,rightAnimation);
        Mode deadMode  = new Mode(deadLeftAnimation,deadRightAnimation);
        dogSprite = new Dog(aliveMode, deadMode);
    }

    public Animation getAnimation(Image stripImage1, Image stripImage2, boolean reflected){

        Animation animation = new Animation();
    
            

        Image stripImage = stripImage1;
        for(int j=0;j<2;j++){
            int imageWidth = (int) (stripImage.getWidth(null) / 6);
            int imageHeight = stripImage.getHeight(null);
            //int transparency = stripImage.getColorModel().getTransparency();

            BufferedImage frameImage;
            int curr;
            for (int i=0; i<6; i++) {
                    frameImage = GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice().getDefaultConfiguration().
                    createCompatibleImage(imageWidth, imageHeight, Transparency.TRANSLUCENT);
                    Graphics2D g = (Graphics2D) frameImage.getGraphics();
                    
                    if(reflected)     // reflected image means that the order is reversed
                        curr=5-i;
                    else
                        curr=i;

                    g.drawImage(stripImage, 
                        0, 0, imageWidth, imageHeight,
                        curr*imageWidth, 0, (curr*imageWidth)+imageWidth, imageHeight,
                        null);

                animation.addFrame(frameImage, 80);
            }

            stripImage=stripImage2;  // update strip image to second image;
        }
        return animation;
    }
               

    private void loadAccessorySprites() {
        Animation anim;
        Mode mode;
        // create health "+" sprite
        anim = new Animation();
        for(int i=1;i<=10;i++){
            anim.addFrame(loadImage("Accessories/hit_point"+i+".png"), 100);
        }
        
        mode = new Mode(anim);
        healthSprite = new Accessory.Health(mode);
       
        // create life "heart" sprite
        anim = new Animation();
        for(int i=1;i<=10;i++){
            anim.addFrame(loadImage("Accessories/heart"+i+".png"), 100);
        }
        mode = new Mode(anim);
        lifeSprite = new Accessory.Life(mode);

        // create xp "star" sprite
        anim = new Animation();
        for(int i=1;i<=10;i++){
            anim.addFrame(loadImage("Accessories/star"+i+".png"), 100);
        }
        mode = new Mode(anim);
        xpSprite = new Accessory.XP(mode);


        // create door sprite
        anim = new Animation();
        anim.addFrame(loadImage("Accessories/door1.png"), 150);
        anim.addFrame(loadImage("Accessories/door2.png"), 150);
        anim.addFrame(loadImage("Accessories/door3.png"), 150);
        anim.addFrame(loadImage("Accessories/door4.png"), 150);
        anim.addFrame(loadImage("Accessories/door5.png"), 150);

        mode= new Mode(anim);
        doorSprite = new Accessory.Door(mode);


    }

    public int getGameLevel(){
        return (numMapsUsed+1)/2;
    }
    
    public void loadIcons(){
        pauseImage = loadImage("pause.png");
        musicImage = loadImage("music.png");
        muteImage = loadImage("mute.png");
    }

    public Image getPauseImage(){
        return pauseImage;
    }
    public Image getMusicImage(){
        return musicImage;
    }
   
    public Image getMuteImage(){
        return muteImage;
    }

    public HashMap<String,Sprite> getWarriorSprites(){
        if(warriorSprites==null)
            loadWarriorSprites();
        return warriorSprites;
    }
}
