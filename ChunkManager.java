
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;

/******************************************************************************
* File: ChunkManager.java
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
 * This class represents a manager for all the chunks in the world.
 * 
 * @author Nelly Liu Peng
 * @author Christine Nguyen
 * @author Cesar Gonzalez
 */
public class ChunkManager {
    private Frustum frustum;
    private HashMap<Vector3f, Chunk> chunks;
    private int minX;
    private int minZ;
    private int maxX;
    private int maxZ;
    
    public ChunkManager() {
        frustum = new Frustum();
        chunks = new HashMap<>();
        chunks.put(new Vector3f(-30, 30, 0), new Chunk(-30, 30, 0));
        chunks.put(new Vector3f(0, 30, -30), new Chunk(0, 30, -30));
        chunks.put(new Vector3f(-30, 30, -30), new Chunk(-30, 30, -30));
        chunks.put(new Vector3f(0, 30, 0), new Chunk(0, 30, 0));
        chunks.put(new Vector3f(30, 30, 0), new Chunk(30, 30, 0));
        chunks.put(new Vector3f(0, 30, 30), new Chunk(0, 30, 30));
        chunks.put(new Vector3f(30, 30, 30), new Chunk(30, 30, 30));
        minX = -30;
        minZ = -30;
        maxX = 30;
        maxZ = 30;
    }
    
    private void addChunk(boolean isMax) {
        if (isMax)
            chunks.put(new Vector3f(maxX, 30, maxZ), new Chunk(maxX, 30, maxZ));
        else
            chunks.put(new Vector3f(minX, 30, minZ), new Chunk(minX, 30, minZ));
    }
    
    private void checkNewChunk() {
        frustum.calculateFrustum();
        if (maxX > maxZ) {
            boolean isVisible = frustum.determineVisibility(maxZ, 30, maxX, 30);
            if (isVisible) {
                int temp = maxX;
                maxX = maxZ;
                maxZ = maxX;
                addChunk(true);
            }
        } else if (maxZ > maxX) {
            boolean isVisible = frustum.determineVisibility(maxZ, 30, maxZ, 30);
            if (isVisible) {
                maxX = maxZ;
                addChunk(true);
            }
        } else {
            boolean isVisible = frustum.determineVisibility(maxX + 30, 30, maxZ, 30);
            if (isVisible) {
                maxX += 30;
                addChunk(true);
            }
        }
        
        if (minX < minZ) {
            boolean isVisible = frustum.determineVisibility(minZ, 30, minX, 30);
            if (isVisible) {
                int temp = minX;
                minX = minZ;
                minZ = minX;
                addChunk(false);
            }
        } else if (minZ < minX) {
            boolean isVisible = frustum.determineVisibility(minZ, 30, minZ, 30);
            if (isVisible) {
                minX = minZ;
                addChunk(false);
            }
        } else {
            boolean isVisible = frustum.determineVisibility(minX - 30, 30, minZ, 30);
            if (isVisible) {
                minX -= 30;
                addChunk(false);
            }
        }
    }
    
    public void render() {
        checkNewChunk();
        for (Map.Entry<Vector3f, Chunk> entry : chunks.entrySet()) {
            boolean isVisible = frustum.determineVisibility(entry.getKey().x, entry.getKey().y, entry.getKey().z, 30);
            if (isVisible)
                entry.getValue().render();
        }
    }
}
