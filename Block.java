/******************************************************************************
* File: Block.java
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
 * This class represents a block with different textures used in the chunk
 * class.
 * 
 * @author Nelly Liu Peng
 * @author Christine Nguyen
 * @author Cesar Gonzalez
 */
public class Block {
    
    private boolean isActive;
    private BlockType type;
    private float x,y,z;
    
    public Block(BlockType type){
        this.type = type;
    }
    
    public void setCoordinates(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active){
        isActive = active;
    }
    
    public int getID(){
        return type.getID();
    }
    
    // Defining types of blocks allowed
    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5),
        BlockType_Brick(6);
        
        private int blockID; 
       
        BlockType(int i) {
            blockID = i;
        }

        public int getID(){
            return blockID;
        }

        public void setID(int i){
            blockID = i;
        }
    } // end of enum
    
}