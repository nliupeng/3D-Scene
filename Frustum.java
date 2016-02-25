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

import org.lwjgl.BufferUtils;
import java.nio.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * This class represents frustum culling in order to determine if a chunk
 * should be rendered. A chunk is only rendered if it is in the frustum.
 * Reference: http://www.lighthouse3d.com/tutorials/view-frustum-culling/
 * 
 * @author Nelly Liu Peng
 * @author Christine Nguyen
 * @author Cesar Gonzalez
 */
public class Frustum {
    private final int RIGHT = 0;
    private final int LEFT = 1;
    private final int BOTTOM = 2;
    private final int TOP = 3;
    private final int BACK = 4;
    private final int FRONT = 5;
    private final int A = 0;
    private final int B = 1;
    private final int C = 2;
    private final int D = 3;

    private float[][] frustum;
    private FloatBuffer modelBuffer;
    private FloatBuffer projectionBuffer;

    public Frustum() {
        frustum = new float[6][4];
        modelBuffer = BufferUtils.createFloatBuffer(16);
        projectionBuffer = BufferUtils.createFloatBuffer(16);
    }

    public void transformNormal(float[][] frustum, int side) {
        float factor = (float)Math.sqrt(frustum[side][A] * frustum[side][A]
                + frustum[side][B] * frustum[side][B]
                + frustum[side][C] * frustum[side][C]);

        frustum[side][A] /= factor;
        frustum[side][B] /= factor;
        frustum[side][C] /= factor;
        frustum[side][D] /= factor;
    }
    
    /**
     * This method calculates the frustum.
     * Part of method taken from: https://www.youtube.com/watch?v=MIOr8PFuGYo
     */
    public void calculateFrustum() {
        float[] newModel = new float[16];
        float[] model = new float[16];
        float[] clip = new float[16];

        // Taken from reference
        projectionBuffer.rewind();
        glGetFloat(GL_PROJECTION_MATRIX, projectionBuffer);
        projectionBuffer.rewind();
        projectionBuffer.get(newModel);

        modelBuffer.rewind();
        glGetFloat(GL_MODELVIEW_MATRIX, modelBuffer);
        modelBuffer.rewind();
        modelBuffer.get(model);
        // End reference code

        clip[0] = model[0] * newModel[0] + model[1] * newModel[4]
                + model[2] * newModel[8] + model[3] * newModel[12];
        clip[1] = model[0] * newModel[1] + model[1] * newModel[5]
                + model[2] * newModel[9] + model[3] * newModel[13];
        clip[2] = model[0] * newModel[2] + model[1] * newModel[6]
                + model[2] * newModel[10] + model[3] * newModel[14];
        clip[3] = model[0] * newModel[3] + model[1] * newModel[7]
                + model[2] * newModel[11] + model[3] * newModel[15];

        clip[4] = model[4] * newModel[0] + model[5] * newModel[4]
                + model[6] * newModel[8] + model[7] * newModel[12];
        clip[5] = model[4] * newModel[1] + model[5] * newModel[5]
                + model[6] * newModel[9] + model[7] * newModel[13];
        clip[6] = model[4] * newModel[2] + model[5] * newModel[6]
                + model[6] * newModel[10] + model[7] * newModel[14];
        clip[7] = model[4] * newModel[3] + model[5] * newModel[7]
                + model[6] * newModel[11] + model[7] * newModel[15];

        clip[8] = model[8] * newModel[0] + model[9] * newModel[4]
                + model[10] * newModel[8] + model[11] * newModel[12];
        clip[9] = model[8] * newModel[1] + model[9] * newModel[5]
                + model[10] * newModel[9] + model[11] * newModel[13];
        clip[10] = model[8] * newModel[2] + model[9] * newModel[6]
                + model[10] * newModel[10] + model[11] * newModel[14];
        clip[11] = model[8] * newModel[3] + model[9] * newModel[7]
                + model[10] * newModel[11] + model[11] * newModel[15];

        clip[12] = model[12] * newModel[0] + model[13] * newModel[4]
                + model[14] * newModel[8] + model[15] * newModel[12];
        clip[13] = model[12] * newModel[1] + model[13] * newModel[5]
                + model[14] * newModel[9] + model[15] * newModel[13];
        clip[14] = model[12] * newModel[2] + model[13] * newModel[6]
                + model[14] * newModel[10] + model[15] * newModel[14];
        clip[15] = model[12] * newModel[3] + model[13] * newModel[7]
                + model[14] * newModel[11] + model[15] * newModel[15];

        frustum[LEFT][A] = clip[3] + clip[0];
        frustum[LEFT][B] = clip[7] + clip[4];
        frustum[LEFT][C] = clip[11] + clip[8];
        frustum[LEFT][D] = clip[15] + clip[12];
        transformNormal(frustum, LEFT);

        frustum[RIGHT][A] = clip[3] - clip[0];
        frustum[RIGHT][B] = clip[7] - clip[4];
        frustum[RIGHT][C] = clip[11] - clip[8];
        frustum[RIGHT][D] = clip[15] - clip[12];
        transformNormal(frustum, RIGHT);

        frustum[BOTTOM][A] = clip[3] + clip[1];
        frustum[BOTTOM][B] = clip[7] + clip[5];
        frustum[BOTTOM][C] = clip[11] + clip[9];
        frustum[BOTTOM][D] = clip[15] + clip[13];
        transformNormal(frustum, BOTTOM);

        frustum[TOP][A] = clip[3] - clip[1];
        frustum[TOP][B] = clip[7] - clip[5];
        frustum[TOP][C] = clip[11] - clip[9];
        frustum[TOP][D] = clip[15] - clip[13];
        transformNormal(frustum, TOP);

        frustum[FRONT][A] = clip[3] + clip[2];
        frustum[FRONT][B] = clip[7] + clip[6];
        frustum[FRONT][C] = clip[11] + clip[10];
        frustum[FRONT][D] = clip[15] + clip[14];
        transformNormal(frustum, FRONT);

        frustum[BACK][A] = clip[3] - clip[2];
        frustum[BACK][B] = clip[7] - clip[6];
        frustum[BACK][C] = clip[11] - clip[10];
        frustum[BACK][D] = clip[15] - clip[14];
        transformNormal(frustum, BACK);
    }

    /**
     * This method determines if the chunk is in the frustum or not.
     * @param x starting x position
     * @param y starting y position
     * @param z starting z position
     * @param size size of chunk
     * @return boolean if chunk is visible or not
     */
    public boolean determineVisibility(float x, float y, float z, float size) {
        float lessX = x - size;
        float moreX = x + size;
        float lessY = y - size;
        float moreY = y + size;
        float lessZ = z - size;
        float moreZ = z + size;
        
        for (int i = 0; i < 6; i++ ) {
            float ia = frustum[i][A];
            float ib = frustum[i][B];
            float ic = frustum[i][C];
            float id = frustum[i][D];
            if (ia * lessX + ib * lessY + ic * lessZ + id > 0)
                continue;
            if (ia * moreX + ib * lessY + ic * lessZ + id > 0)
                continue;
            if (ia * lessX + ib * moreY + ic * lessZ + id > 0)
                continue;
            if (ia * moreX + ib * moreY + ic * lessZ + id > 0)
                continue;
            if (ia * lessX + ib * lessY + ic * moreZ + id > 0)
                continue;
            if (ia * moreX + ib * lessY + ic * moreZ + id > 0)
                continue;
            if (ia * lessX + ib * moreY + ic * moreZ + id > 0)
                continue;
            if (ia * moreX + ib * moreY + ic * moreZ + id > 0)
                continue;
            return false;
        }
        return true;
    }
}