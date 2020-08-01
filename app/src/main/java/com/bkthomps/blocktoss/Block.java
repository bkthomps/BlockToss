package com.bkthomps.blocktoss;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An individual block with specified attributes.
 */
class Block {

    private static final int BASE_PIXEL = -80;
    private static final int Y_TEXT_BASE = 160;
    private static final int X_TEXT_BASE = 120;

    private final BlockToss instance;
    private final RelativeLayout layout;
    private final int sizeOfBlock;
    private final Block[][] logicalGrid;
    private ImageView block;

    private int value;
    private TextView valueText;

    private int xOld;
    private int yOld;

    private boolean isMoving;

    Block(BlockToss instance, RelativeLayout layout, int blocksPerScreen, int gameBoardDimension,
          Block[][] logicalGrid, int value, int xIndex, int yIndex) {
        this.value = value;
        this.instance = instance;
        this.layout = layout;
        this.logicalGrid = logicalGrid;
        logicalGrid[yIndex][xIndex] = this;
        xOld = xIndex;
        yOld = yIndex;
        initializeValueText();
        final int BLOCK_PIXEL_SIZE = 130;
        sizeOfBlock = gameBoardDimension / blocksPerScreen;
        final float ratio = (float) sizeOfBlock / (blocksPerScreen * BLOCK_PIXEL_SIZE);
        block = createImageViewProperties();
        block.setImageResource(R.drawable.gameblock);
        block.setScaleX(ratio);
        block.setScaleY(ratio);
        setToIndex(xIndex, yIndex);
        valueText.bringToFront();
    }

    /**
     * Initializes the value text of the block.
     */
    private void initializeValueText() {
        final String valueString = createValueString();
        valueText = new TextView(instance.getApplicationContext());
        valueText.setText(valueString);
        layout.addView(valueText);
        final int textSize = 30;
        valueText.setTextSize(textSize);
        valueText.setTextColor(Color.BLACK);
    }

    /**
     * Sets the value of the block.
     *
     * @param value the value of the block to set
     */
    private void setValue(int value) {
        this.value = value;
        final String valueString = createValueString();
        valueText.setText(valueString);
        if (value == BlockToss.WIN_VALUE) {
            instance.gameWin();
        }
    }

    /**
     * Create string based on the value of the block.
     *
     * @return string based on value of the block
     */
    private String createValueString() {
        final String formatType;
        if (value < 10) {
            formatType = "    %1d";
        } else if (value < 100) {
            formatType = "  %2d";
        } else if (value < 1000) {
            formatType = " %3d";
        } else {
            formatType = "%4d";
        }
        return String.format(Locale.US, formatType, value);
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
        valueText.setX(xPixel + X_TEXT_BASE);
        valueText.setY(yPixel + Y_TEXT_BASE);
        setLogicalGrid(xIndex, yIndex);
    }

    /**
     * Moves the block to the specified grid indices.
     *
     * @param xIndex x index
     * @param yIndex y index
     */
    void moveToIndex(int xIndex, int yIndex) {
        if (xOld == xIndex && yOld == yIndex) {
            return;
        }
        Grid.didMovementOccur = true;
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
        final Block atPosition = logicalGrid[yIndex][xIndex];
        if (atPosition == null) {
            logicalGrid[yIndex][xIndex] = this;
        } else if (atPosition.getValue() == this.getValue()) {
            atPosition.kill(this);
            logicalGrid[yIndex][xIndex] = this;
        } else {
            throw new IllegalStateException("Can only move onto null or block of equal value.");
        }
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

        isMoving = true;

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int xCurrent = BASE_PIXEL + sizeOfBlock * xOld;
            final int xEnd = BASE_PIXEL + sizeOfBlock * xIndex;
            int yCurrent = BASE_PIXEL + sizeOfBlock * yOld;
            final int yEnd = BASE_PIXEL + sizeOfBlock * yIndex;

            @Override
            public void run() {
                instance.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        if (xCurrent == xEnd && yCurrent == yEnd) {
                            isMoving = false;
                            timer.cancel();
                            return;
                        }
                        if (xCurrent != xEnd) {
                            xCurrent += (isPositiveX ? 1 : -1) * PIXEL_CHANGE_ON_ITERATION;
                            block.setX(xCurrent);
                            valueText.setX(xCurrent + X_TEXT_BASE);
                        }
                        if (yCurrent != yEnd) {
                            yCurrent += (isPositiveY ? 1 : -1) * PIXEL_CHANGE_ON_ITERATION;
                            block.setY(yCurrent);
                            valueText.setY(yCurrent + Y_TEXT_BASE);
                        }
                    }
                });
            }
        }, EXECUTE_PERIOD_IN_MILLI_SECONDS, EXECUTE_PERIOD_IN_MILLI_SECONDS);
    }

    /**
     * Kills the block, making its views transparent, and nulling them.
     *
     * @param killer which block is killing this block
     */
    private void kill(final Block killer) {
        final int CHECK_RATE = 100;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                instance.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        if (!killer.isMoving) {
                            block.setImageResource(android.R.color.transparent);
                            block = null;
                            valueText.setText("");
                            valueText = null;
                            killer.setValue(value * 2);
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, CHECK_RATE);
    }

    int getValue() {
        return value;
    }

    boolean isMoving() {
        return isMoving;
    }
}
