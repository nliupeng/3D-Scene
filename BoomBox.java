/******************************************************************************
* File: World3D.java
* Author: Nelly Liu Peng, Christine Nguyen, Cesar Gonzalez
* Class: CS 445 – Computer Graphics
*
* Assignment: Final Project
* Date last modified: 11/13/2015
*
* Purpose: Create an original scene, which is similar to Minecraft.
*          It will show different aspects like texture mapping,
*          lighting, visible surface detection, and surface rendering.
******************************************************************************/

import java.io.*;
import javax.sound.sampled.*;

/**
 * This class manages all of the sound in the game.
 * 
 * @author Nelly Liu Peng
 * @author Christine Nguyen
 * @author Cesar Gonzalez
 */
public class BoomBox {
    File gameMusic;
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;
    
    public BoomBox() {
        try {
            gameMusic = new File("GameMusic.wav");
            stream = AudioSystem.getAudioInputStream(gameMusic);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stopMusic() {
        clip.stop();
    }
}
