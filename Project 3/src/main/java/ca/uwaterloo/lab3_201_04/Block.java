package ca.uwaterloo.lab3_201_04;

import android.widget.ImageView;
import android.widget.RelativeLayout;

class Block {

    private final Lab3_201_04 instance;
    private final RelativeLayout layout;
    private final ImageView block;
    private final int sizeOfBlock;
    private final int value;
    private final Block[][] logicalGrid;
    private int xOld;
    private int yOld;

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
        moveToIndex(xIndex, yIndex);
    }

    private ImageView createImageViewProperties() {
        final ImageView handle = new ImageView(instance.getApplicationContext());
        layout.addView(handle);
        return handle;
    }

    void moveToIndex(int xIndex, int yIndex) {
        // TODO: these two checks should be gone in lab 5
        if (xIndex == -1) {
            xIndex = xOld;
        }
        if (yIndex == -1) {
            yIndex = yOld;
        }
        final int BASE_PIXEL = -80;
        block.setX(BASE_PIXEL + sizeOfBlock * xIndex);
        block.setY(BASE_PIXEL + sizeOfBlock * yIndex);
        logicalGrid[yOld][xOld] = null;
        xOld = xIndex;
        yOld = yIndex;
        logicalGrid[yIndex][xIndex] = this;
    }

    int getValue() {
        return value;
    }
}
