/******************************************************************************
* File: Chunk.java
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

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;            // texture
import org.newdawn.slick.opengl.TextureLoader;      // texture
import org.newdawn.slick.util.ResourceLoader;       // texture

/**
 * This class one chunk that is made up of multiple blocks in the world.
 * 
 * @author Nelly Liu Peng
 * @author Christine Nguyen
 * @author Cesar Gonzalez
 */
public class Chunk {
    
    static final int CHUNK_SIZE = 35;
    static final int CUBE_LENGTH = 2;
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;           // texture
    private Texture texture;                // texture
    private Random r;
    
    /**
     * This constructor method initializes the whole chunk with its variables.
     * @param startX starting x position
     * @param startY starting y position
     * @param startZ starting z position
     */
    public Chunk(int startX, int startY, int startZ) {
        try {
            texture = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("textures.png"));
        } catch(Exception e) {
            System.out.print("Texture Image Error");
        }
        
        r = new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        create();
        buffer();
        rebuildMesh(startX, startY, startZ);
    }
    
    /**
     * This method fills the block array with blocks.
     */
    private void create() {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (y > 32)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Brick);
                    else if (y > 28)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    else if (y > 26)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                    else if (y > 20)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    else if (y > 13)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    else if (y > 7)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                    else if (y > 2)
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
                    else
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);     // default
                }
            }
        }
    }
    
    private void buffer() {
        VBOVertexHandle  = glGenBuffers();
        VBOColorHandle   = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
    }
    
    private float[] getCubeColor(Block block) {
        return new float[] { 1, 1, 1 };
    }
    
    /**
     * This method uses simplex noise to display the chunk up to a certain
     * height at every x,z position.
     * @param startX starting x position
     * @param startY starting y position
     * @param startZ starting z position
     */
    private void rebuildMesh(float startX, float startY, float startZ) {
        int seed = r.nextInt(5000 - 300 + 1) + 300;
        SimplexNoise noise = new SimplexNoise(40, .03, seed);
        
        buffer();
        
        FloatBuffer VertexPositionData = 
                BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*72);
        FloatBuffer VertexColorData = 
                BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*72);
        FloatBuffer VertexTextureData = 
                BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*72);     // texture
        
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int i = (int)(startX + x * ((300 - startX) / 640));
                int j = (int)(startZ + z * ((300 - startZ) / 480));
                float height = (startY + (int)(100 * noise.getNoise(i,j)) * CUBE_LENGTH);
                for (int y = 0; y <= height; y++) {
                    VertexPositionData.put(createCube((startX + x*CUBE_LENGTH),
                            ((float)(CHUNK_SIZE*-2) + y*CUBE_LENGTH),
                            (startZ - z*CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[x][y][z])));
                    
                    // If a block of type grass is at a position not at height,
                    // replace that block with a dirt block
                    if ((y != height) && (Blocks[x][y][z].getID() == 0))
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    VertexTextureData.put(createCubeTexture(Blocks[x][y][z]));    // texture
                }
            }
        }
        
        VertexPositionData.flip();
        VertexColorData.flip();
        VertexTextureData.flip();      //texture
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);                    //texture
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);   //texture
        glBindBuffer(GL_ARRAY_BUFFER, 0);                                   //texture
    }
    
    private static float[] createCubeTexture(Block block) {
        float offset = 1/4f;                // PNG file divided into 4x4 sections
        TextureCube tc = new TextureCube(offset);
        switch (block.getID()) {
            case 0:                         // BlockType_Grass
                return tc.getGrassData();
            case 1:                         // BlockType_Sand
                return tc.getSandData();
            case 2:                         // BlockType_Water
                return tc.getWaterData();
            case 3:                         // BlockType_Dirt
                return tc.getDirtData();
            case 4:                         // BlockType_Stone
                return tc.getStoneData();
            case 5:                         // BlockType_Bedrock
                return tc.getBedrockData();
            case 6:                         // BlockType_Brick
                return tc.getBrickData();
            default: 
                return tc.getGrassData();   // default: BlockType_Grass
        }
    }
    
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length*24];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
    }
    
    private static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[] {
            // Top
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // Bottom
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // Front
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            // Back
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // Left
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // Right
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z 
        };
    }
    
    public void render(){
        glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER,VBOVertexHandle);
        glVertexPointer(3,GL_FLOAT,0,0L);
        glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
        glColorPointer(3,GL_FLOAT,0,0L);
        glBindBuffer(GL_ARRAY_BUFFER,VBOTextureHandle);    // texture
        glBindTexture(GL_TEXTURE_2D,1);                    // texture
        glTexCoordPointer(2,GL_FLOAT,0,0L);                // texture  
        glDrawArrays(GL_QUADS,0,CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*24);
        glPopMatrix();
    }
    
}
