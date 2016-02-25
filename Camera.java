/******************************************************************************
* File: Camera.java
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
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;

/**
 * This class specifies what the keyboard and mouse does in the program.
 * It represents what the user can do to view and move around in the
 * simulated world.
 * 
 * @author Nelly Liu Peng
 * @author Christine Nguyen
 * @author Cesar Gonzalez
 */
public class Camera {
    
    // Global variables
    private Vector3f position;          // Camera position
    private float pitch = 0f;           // X axis rotation
    private float yaw = 0f;             // Y axis rotation
    long lastFrame;
    int fps;
    long lastFPS = getTime();
//    private ChunkManager cm;
    private Chunk chunk1 = new Chunk(0, 30, 0);
    private Chunk chunk2 = new Chunk(30, 30, 0);
    private Chunk chunk3= new Chunk(0, 30, 30);
    private Chunk chunk4 = new Chunk(30, 30, 30);
    private Frustum frustum;
    
    /**
     * This constructor will instantiate position Vector3f to the x, y, and z
     * parameters.
     * @param x x position
     * @param y y position
     * @param z z position
     */
    public Camera(float x, float y, float z) {
        // Initialize position
        position = new Vector3f(x, y, z);
        
        // Initialize the chunk manager
//        cm = new ChunkManager();
        
        // Initialize frustum culling object
        frustum = new Frustum();
    }
    
    /**
     * This method will increment the camera's current yaw rotation by the
     * amount indicated.
     * @param num amount to increment yaw
     */
    private void yaw(float num) {
        yaw += num;
    }
    
    /**
     * This method will decrement the pitch by the amount indicated.
     * @param num
     */
    private void pitch(float num) { 
        pitch -= num;
    }
    
    /**
     * This method moves the camera forward relative to its current rotation
     * (yaw).
     * @param distance distance to move forward
     */
    private void moveForward(float distance) {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw));
    }
    
    /**
     * This method moves the camera backwards relative to its current rotation
     * (yaw).
     * @param distance distance to move backwards
     */
    private void moveBackwards(float distance) {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
    }
    
    /**
     * This method strafes the camera left relative to its current rotation
     * (yaw).
     * @param distance distance to move left
     */
    private void moveLeft(float distance) {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw+90));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw+90));
    }
    
    /**
     * This method strafes the camera right relative to its current rotation
     * (yaw).
     * @param distance distance to move right
     */
    private void moveRight(float distance) {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw-90));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw-90));
    }
    
    /**
     * This method moves the camera up relative to its current rotation (yaw).
     * @param distance distance to move up
     */
    private void moveUp(float distance) {
        position.y -= distance;
    }

    /**
     * This method moves the camera down.
     * @param distance distance to move down
     */
    private void moveDown(float distance) {
        position.y += distance;
    }
    
    /**
     * This method translates and rotate the matrix so that it looks through
     * the camera. This does basically what gluLookAt() does.
     */
    private void lookAt() {
        glRotatef(pitch, 1f, 0f, 0f);
        glRotatef(yaw, 0f, 1f, 0f);
        glTranslatef(position.x, position.y, position.z);
        
        // light reacting to camera view
        FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
        lightPos.put(position.x).put(position.y).put(position.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPos);
    }
    
    private long getTime() {	    
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * This method calculates the FPS and sets it in the title bar.
     */
    private void updateFPS() {
	if (getTime() - lastFPS > 1000) {
                Display.setTitle("3D Cube FPS: " + fps);
		fps = 0;
		lastFPS += 1000;
	}
        fps++;
    }
    
    /**
     * This method switches the display window from fullscreen mode to regular
     * mode or vice versa.
     * @param width width of display window
     * @param height height of display window
     * @param fullscreen is display window in fullscreen or not
     */
    private void toFullScreen(int width, int height, boolean fullscreen) {
 
        // Return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width) && 
            (Display.getDisplayMode().getHeight() == height) && 
            (Display.isFullscreen() == fullscreen)) {
            return;
        }
         
        try {
            DisplayMode targetDisplayMode = null;
             
            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                for (int i=0;i<modes.length;i++) {
                    DisplayMode current = modes[i];
                     
                    if ((current.getWidth() == width) && (current.getHeight() == height)) 
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) 
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                }
            } 
            else 
                targetDisplayMode = new DisplayMode(width,height);
            
            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
             
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
    
    /**
     * This method moves the camera around based on the user's request.
     * It will keep looping until the escape key is pressed.  Then the display
     * will close.
     */
    public void control() {
        Mouse.setGrabbed(true);
        float mouseSpeed = .1f;
        float keysSpeed = .5f;
//        float mouseSpeed = .3f;
//        float keysSpeed = .8f;
        float dx, dy;
 
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            updateFPS();
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();

            yaw(mouseSpeed*dx);
            pitch(mouseSpeed*dy);
            
            // Move forward
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                moveForward(keysSpeed);
            }
            
            // Move backwards
            if (Keyboard.isKeyDown(Keyboard.KEY_S))
                moveBackwards(keysSpeed);
            
            // Move left
            if (Keyboard.isKeyDown(Keyboard.KEY_A))
                moveLeft(keysSpeed);

            // Move right
            if (Keyboard.isKeyDown(Keyboard.KEY_D))
                moveRight(keysSpeed);
            
            // Move up
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                moveUp(keysSpeed);
            
            // Move down
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) 
                moveDown(keysSpeed);
            
             while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	toFullScreen(640, 480, !Display.isFullscreen());
		        }
		    }
		}
            
            glLoadIdentity();

            lookAt();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
//            cm.render();
            frustum.calculateFrustum();
            if (frustum.determineVisibility(0, 30, 0, 35))
                chunk1.render();
//            if (frustum.determineVisibility(30, 30, 0, 35))
//                chunk2.render();
//            if (frustum.determineVisibility(0, 30, 30, 35))
//                chunk3.render();
//            if (frustum.determineVisibility(30, 30, 30, 35))
//                chunk4.render();
            
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
    
}