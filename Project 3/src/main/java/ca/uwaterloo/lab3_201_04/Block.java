package ca.uwaterloo.lab3_201_04;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An individual block with specified attributes.
 */
class Block {

    private static final int BASE_PIXEL = -80;

    private final Lab3_201_04 instance;
    private final RelativeLayout layout;
    private final ImageView block;
    private final int sizeOfBlock;
    private final int value;
    private final Block[][] logicalGrid;
    private int xOld;
    private int yOld;
    private boolean isInMotion;

    Block(Lab3_201_04 instance, RelativeLayout layout, int blocksPerScreen, int gameBoardDimension,
          Block[][] logicalGrid, int value, int xIndex, int yIndex) {
        this.instance = instance;
        this.layout = layout;
        this.value = value;
        this.logicalGrid = logicalGrid;
        xOld = xIndex;
        yOld = yIndex;
        logicalGrid[yIndex][xIndex] = this;
        final int BLOCK_PIXEL_SIZE = 130;
        sizeOfBlock = gameBoardDimension / blocksPerScreen;
        final float ratio = (float) sizeOfBlock / (blocksPerScreen * BLOCK_PIXEL_SIZE);
        block = createImageViewProperties();
        block.setImageResource(R.drawable.gameblock);
        block.setScaleX(ratio);
        block.setScaleY(ratio);
        setToIndex(xIndex, yIndex);
    }

    /**
     * Creates an image handle and adds it to the view.
     *
     * @return image view handle
     */
    private ImageView createImageViewProperties() {
        final ImageView handle = new ImageView(instance.getApplicationContext());
        layout.addView(handle);
        return handle;
    }

    /**
     * Sets the block to the specified grid indices.
     *
     * @param xIndex x index
     * @param yIndex y index
     */
    private void setToIndex(int xIndex, int yIndex) {
        final int xPixel = BASE_PIXEL + sizeOfBlock * xIndex;
        final int yPixel = BASE_PIXEL + sizeOfBlock * yIndex;
        block.setX(xPixel);
        block.setY(yPixel);
        setLogicalGrid(xIndex, yIndex);
    }

    /**
     * Moves the block to the specified grid indices.
     *
     * @param xIndex x index
     * @param yIndex y index
     */
    void moveToIndex(int xIndex, int yIndex) {
        // TODO: these two checks should be gone in lab 4
        if (xIndex == -1) {
            xIndex = xOld;
        }
        if (yIndex == -1) {
            yIndex = yOld;
        }
        animateBlockMove(xIndex, yIndex);
        setLogicalGrid(xIndex, yIndex);
    }

    /**
     * Sets the logical grid at the specified index to the current block.
     *
     * @param xIndex x index
     * @param yIndex y index
     */
    private void setLogicalGrid(int xIndex, int yIndex) {
        logicalGrid[yOld][xOld] = null;
        xOld = xIndex;
        yOld = yIndex;
        logicalGrid[yIndex][xIndex] = this;
    }

    /**
     * Animates the motion of the block.
     *
     * @param xIndex x index
     * @param yIndex y index
     */
    private void animateBlockMove(final int xIndex, final int yIndex) {
        final int EXECUTE_PERIOD_IN_MILLI_SECONDS = 1;
        final int PIXEL_CHANGE_ON_ITERATION = 5;
        final boolean isPositiveX = xIndex > xOld;
        final boolean isPositiveY = yIndex > yOld;

        isInMotion = true;

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int xCurrent = BASE_PIXEL + sizeOfBlock * xOld;
            final int xEnd = BASE_PIXEL + sizeOfBlock * xIndex;
            int yCurrent = BASE_PIXEL + sizeOfBlock * yOld;
            final int yEnd = BASE_PIXEL + sizeOfBlock * yIndex;

            @Override
            public void run() {
                if (xCurrent == xEnd && yCurrent == yEnd) {
                    isInMotion = false;
                    timer.cancel();
                    return;
                }
                if (xCurrent != xEnd) {
                    xCurrent += (isPositiveX ? 1 : -1) * PIXEL_CHANGE_ON_ITERATION;
                    block.setX(xCurrent);
                }
                if (yCurrent != yEnd) {
                    yCurrent += (isPositiveY ? 1 : -1) * PIXEL_CHANGE_ON_ITERATION;
                    block.setY(yCurrent);
                }
            }
        }, EXECUTE_PERIOD_IN_MILLI_SECONDS, EXECUTE_PERIOD_IN_MILLI_SECONDS);
    }

    boolean isInMotion() {
        return isInMotion;
    }

    int getValue() {
        // TODO: this is for lab 4
        return value;
    }
}
