/******************************************************************************
* File: TextureCube.java
* Author: Nelly Liu Peng, Christine Nguyen, Cesar Gonzalez
* Class: CS 445 – Computer Graphics
*
* Assignment: Final Project
* Date last modified: 11/18/2015
*
* Purpose: Create an original scene, which is similar to Minecraft.
*          It will show different aspects like texture mapping,
*          lighting, visible surface detection, and surface rendering.
*          Extra Features: sound (in World3D class)
*                          brick block type
*                          fullscreen (in Camera class)
*                          fps counter (in Camera class)
******************************************************************************/

/**
 * This class contains the coordinates of the respective textures in 
 * a PNG file. This file is used to decorate the different types of cubes.
 * 
 * @author Nelly Liu Peng
 * @author Christine Nguyen
 * @author Cesar Gonzalez
 */
public class TextureCube {
    float offset;
    
    public TextureCube(float offset) {
        this.offset = offset;
    }
    
    float[] getGrassData() {
        return new float[] {
            // Top
            offset*3, offset*1,    offset*4, offset*1,
            offset*4, offset*2,    offset*3, offset*2,
            // Bottom
            offset*2, offset*0,    offset*3, offset*0,
            offset*3, offset*1,    offset*2, offset*1,
            // Front
            offset*1, offset*3,    offset*2, offset*3,
            offset*2, offset*4,    offset*1, offset*4,
            // Back
            offset*2, offset*4,    offset*1, offset*4,
            offset*1, offset*3,    offset*2, offset*3,
            // Left
            offset*1, offset*3,    offset*2, offset*3,
            offset*2, offset*4,    offset*1, offset*4,
            // Right
            offset*1, offset*3,    offset*2, offset*3,
            offset*2, offset*4,    offset*1, offset*4
        };
    }
    
    float[] getSandData() {
        return new float[] {
            // Top
            offset*0, offset*2,    offset*1, offset*2,
            offset*1, offset*3,    offset*0, offset*3,
            // Bottom
            offset*0, offset*2,    offset*1, offset*2,
            offset*1, offset*3,    offset*0, offset*3,
            // Front
            offset*0, offset*2,    offset*1, offset*2,
            offset*1, offset*3,    offset*0, offset*3,
            // Back
            offset*1, offset*3,    offset*0, offset*3,
            offset*0, offset*2,    offset*1, offset*2,
            // Left
            offset*0, offset*2,    offset*1, offset*2,
            offset*1, offset*3,    offset*0, offset*3,
            // Right
            offset*0, offset*2,    offset*1, offset*2,
            offset*1, offset*3,    offset*0, offset*3
        };
    }
    
    float[] getWaterData() {
        return new float[] {
            // Top
            offset*0, offset*0,    offset*1, offset*0,
            offset*1, offset*1,    offset*0, offset*1,
            // Bottom
            offset*0, offset*0,    offset*1, offset*0,
            offset*1, offset*1,    offset*0, offset*1,
            // Front
            offset*0, offset*0,    offset*1, offset*0,
            offset*1, offset*1,    offset*0, offset*1,
            // Back
            offset*1, offset*1,    offset*0, offset*1,
            offset*0, offset*0,    offset*1, offset*0,
            // Left
            offset*0, offset*0,    offset*1, offset*0,
            offset*1, offset*1,    offset*0, offset*1,
            // Right
            offset*0, offset*0,    offset*1, offset*0,
            offset*1, offset*1,    offset*0, offset*1
        };
    }
    
    float[] getDirtData() {
        return new float[] {
            // Top
            offset*2, offset*0,    offset*3, offset*0,
            offset*3, offset*1,    offset*2, offset*1,
            // Bottom
            offset*2, offset*0,    offset*3, offset*0,
            offset*3, offset*1,    offset*2, offset*1,
            // Front
            offset*2, offset*0,    offset*3, offset*0,
            offset*3, offset*1,    offset*2, offset*1,
            // Back
            offset*3, offset*1,    offset*2, offset*1,
            offset*2, offset*0,    offset*3, offset*0,
            // Left
            offset*2, offset*0,    offset*3, offset*0,
            offset*3, offset*1,    offset*2, offset*1,
            // Right
            offset*2, offset*0,    offset*3, offset*0,
            offset*3, offset*1,    offset*2, offset*1
        };
    }
    
    float[] getStoneData() {
        return new float[] {
            // Top
            offset*2, offset*2,    offset*3, offset*2,
            offset*3, offset*3,    offset*2, offset*3,
            // Bottom
            offset*2, offset*2,    offset*3, offset*2,
            offset*3, offset*3,    offset*2, offset*3,
            // Front
            offset*2, offset*2,    offset*3, offset*2,
            offset*3, offset*3,    offset*2, offset*3,
            // Back
            offset*3, offset*3,    offset*2, offset*3,
            offset*2, offset*2,    offset*3, offset*2,
            // Left
            offset*2, offset*2,    offset*3, offset*2,
            offset*3, offset*3,    offset*2, offset*3,
            // Right
            offset*2, offset*2,    offset*3, offset*2,
            offset*3, offset*3,    offset*2, offset*3
        };
    }
    
    float[] getBedrockData() {
        return new float[] {
            // Top
            offset*1, offset*1,    offset*2, offset*1,
            offset*2, offset*2,    offset*1, offset*2,
            // Bottom
            offset*1, offset*1,    offset*2, offset*1,
            offset*2, offset*2,    offset*1, offset*2,
            // Front
            offset*1, offset*1,    offset*2, offset*1,
            offset*2, offset*2,    offset*1, offset*2,
            // Back
            offset*2, offset*2,    offset*1, offset*2,
            offset*1, offset*1,    offset*2, offset*1,
            // Left
            offset*1, offset*1,    offset*2, offset*1,
            offset*2, offset*2,    offset*1, offset*2,
            // Right
            offset*1, offset*1,    offset*2, offset*1,
            offset*2, offset*2,    offset*1, offset*2
        };
    }
    
    float[] getBrickData() {
        return new float[] {
            // Top
            offset*3, offset*3,    offset*4, offset*3,
            offset*4, offset*4,    offset*3, offset*4,
            // Bottom
            offset*3, offset*3,    offset*4, offset*3,
            offset*4, offset*4,    offset*3, offset*4,
            // Front
            offset*3, offset*3,    offset*4, offset*3,
            offset*4, offset*4,    offset*3, offset*4,
            // Back
            offset*4, offset*4,    offset*3, offset*4,
            offset*3, offset*3,    offset*4, offset*3,
            // Left
            offset*3, offset*3,    offset*4, offset*3,
            offset*4, offset*4,    offset*3, offset*4,
            // Right
            offset*3, offset*3,    offset*4, offset*3,
            offset*4, offset*4,    offset*3, offset*4
        };
    }

}
