/******************************************************************************
* File: World3D.java
* Author: Nelly Liu Peng, Christine Nguyen, Cesar Gonzalez
* Class: CS 445 – Computer Graphics
*
* Assignment: Final Project
* Date last modified: 12/1/2015
*
* Purpose: Create an original scene, which is similar to Minecraft.
*          It will show different aspects like texture mapping,
*          lighting, visible surface detection, and surface rendering.
*          Extra Features: sound (in World3D class)
*                          brick block type
*                          fullscreen (in Camera class)
*                          fps counter (in Camera class)
******************************************************************************/

import java.nio.FloatBuffer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

/**
 * This class is the main class that starts and initializes the 3d world.
 * 
 * @author Nelly Liu Peng
 * @author Christine Nguyen
 * @author Cesar Gonzalez
 */
public class World3D {
    
    // Window dimensions
    public static final int WIDTH = 640;                
    public static final int HEIGHT = 480;
   
    // Global variables
    private DisplayMode displayMode;
    private FloatBuffer lightPos;
    private FloatBuffer light;

    /**
     * This method creates the window shown to the user and starts the game.
     */
    public void create() {
        try {
            // Print in-game functions
            System.out.println("Keys:");
            System.out.println("w         - Move Foward");
            System.out.println("a         - Move Left");
            System.out.println("s         - Move BackWards");
            System.out.println("d         - Rotate right");
            System.out.println("LShift    - Move Down");
            System.out.println("spacebar  - Move Up");
            System.out.println("f         - Fullscreen");
            System.out.println("esc       - Exit");
            
            displayWindow();
            initGL();            
            playSound();
            
            // start screen
//            startScreen();
            
            // Parameters specify that camera starts at origin
            Camera cam = new Camera(0f,0f,0f);
            cam.control();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
  
    
    /**
     * This method sets the dimensions of the display window, display mode,
     * title, and creates the display.
     * @throws Exception 
     */
    private void displayWindow() throws Exception {
        Display.setFullscreen(false);
        
        DisplayMode dm[] = Display.getAvailableDisplayModes();
        
        for (DisplayMode dm1 : dm) 
            if (dm1.getWidth() == WIDTH && dm1.getHeight() == HEIGHT) {
                displayMode = dm1;
                break;
            }

        Display.setDisplayMode(displayMode);
        Display.setTitle("3D Cube");
        Display.create();
    }
    
    /**
     * This method adds sound to the world.
     * @throws Exception 
     */
    public static void playSound() {
        try {
              Clip clip = AudioSystem.getClip();
              AudioInputStream inputStream = 
                      AudioSystem.getAudioInputStream(
                        World3D.class.getResourceAsStream("GameMusic.wav"));
              clip.open(inputStream);
              clip.start(); 
              clip.loop(-1);
                      
            } catch (Exception e) {
                e.printStackTrace();
        }
    }
    
    /**
     * This method sets initial display values to show the game exactly
     * like it would be expected.
     */
    private void initGL() {
        glClearColor(0f, 0f, 0f, 0f);                   // background: black
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(80f, (float)displayMode.getWidth()/(float)displayMode.getHeight(), .1f, 200f);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnableClientState(GL_VERTEX_ARRAY);           // for chunk
        glEnableClientState(GL_COLOR_ARRAY);            // for chunk
        glEnable(GL_TEXTURE_2D);                        // for texture mapping
        glEnableClientState (GL_TEXTURE_COORD_ARRAY);   // for texture mapping
        
        initLight();
        glLight(GL_LIGHT0, GL_POSITION, lightPos);     
        glLight(GL_LIGHT0, GL_SPECULAR, light);         
        glLight(GL_LIGHT0, GL_DIFFUSE, light);         
        glLight(GL_LIGHT0, GL_AMBIENT, light);         
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
    }
    
    /**
     * This method adds a light source to the world
     */
    private void initLight() {
        lightPos = BufferUtils.createFloatBuffer(4);
        lightPos.put(0f).put(0f).put(0f).put(0f).flip();   // lightPosition
        light = BufferUtils.createFloatBuffer(4);
        light.put(1f).put(1f).put(1f).put(0f).flip();
    }

    
    /**
     * This is the main method. It starts up the whole game.
     * @param args 
     */
    public static void main(String[] args) {
        World3D myWorld = new World3D();
        myWorld.create();
    }    
}