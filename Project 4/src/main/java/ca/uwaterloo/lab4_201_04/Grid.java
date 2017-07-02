package ca.uwaterloo.lab4_201_04;

import android.graphics.Point;
import android.view.Display;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The grid that the game 2048 is played on, which contains blocks.
 */
class Grid {

    private final Lab4_201_04 instance;
    private final RelativeLayout layout;
    private final int blocksPerScreen;
    private final int gameBoardDimension;
    private final Block[][] logicalGrid;
    private final Timer timer = new Timer();

    Grid(Lab4_201_04 instance, RelativeLayout layout, int blocksPerScreen) {
        this.instance = instance;
        this.layout = layout;
        this.blocksPerScreen = blocksPerScreen;
        this.gameBoardDimension = gameBoardDimension();
        logicalGrid = new Block[blocksPerScreen][blocksPerScreen];
        setGameBoard();
        scheduleTimer();
    }

    private void scheduleTimer() {
        final int BLOCK_APPEAR_RATE = 5000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                instance.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        addBlock();
                    }
                });
            }
        }, 0, BLOCK_APPEAR_RATE);
    }

    /**
     * Set the size of the game board and the game board itself.¬
     */
    private void setGameBoard() {
        layout.getLayoutParams().width = gameBoardDimension;
        layout.getLayoutParams().height = gameBoardDimension;
        layout.setBackgroundResource(R.drawable.gameboard);
    }

    /**
     * Add block to the game board. If there are no free spaces left, the game is lost. Otherwise, a space is randomly
     * selected, and if it is free, a block will be placed there.
     */
    private void addBlock() {
        if (!isAnyCoordinateFree()) {
            instance.gameLose();
            timer.cancel();
            return;
        }
        int xIndex, yIndex;
        do {
            xIndex = (int) (Math.random() * logicalGrid[0].length);
            yIndex = (int) (Math.random() * logicalGrid.length);
        } while (logicalGrid[yIndex][xIndex] != null);
        final int value = (int) (Math.random() * 2) + 1;
        new Block(instance, layout, blocksPerScreen, gameBoardDimension, logicalGrid, value, xIndex, yIndex);
    }

    private boolean isAnyCoordinateFree() {
        for (int vertical = 0; vertical < logicalGrid.length; vertical++) {
            for (int horizontal = 0; horizontal < logicalGrid[0].length; horizontal++) {
                if (logicalGrid[vertical][horizontal] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Specifies what size the game board should be based on user phone specifications.
     *
     * @return dimension of the game board
     */
    private int gameBoardDimension() {
        final Display display = instance.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        final int BANNER_OFFSET = 300;
        final int width = size.x;
        final int height = size.y - BANNER_OFFSET;
        return (width < height) ? width : height;
    }

    /**
     * Move and merge the blocks up.
     */
    void moveUp() {
        if (!isMovePossible()) {
            return;
        }
    }

    /**
     * Move and merge the blocks down.
     */
    void moveDown() {
        if (!isMovePossible()) {
            return;
        }
    }

    /**
     * Move and merge the blocks left.
     */
    void moveLeft() {
        if (!isMovePossible()) {
            return;
        }
        for (int vertical = 0; vertical < logicalGrid.length; vertical++) {
            moveSlitLeft(vertical);
        }
    }

    /**
     * Move and merge the blocks right.
     */
    void moveRight() {
        if (!isMovePossible()) {
            return;
        }
    }

    private void moveSlitLeft(int slitSize) {
        final List<Block> blocks = new ArrayList<>();
        for (Block block : logicalGrid[slitSize]) {
            if (block != null) {
                blocks.add(block);
            }
        }
        if (blocks.isEmpty()) {
            return;
        }
        final int size = blocks.size();
        final int[] position = new int[size];
        computePosition(blocks, position);
        for (int i = 0; i < size; i++) {
            blocks.get(i).moveToIndex(position[i], slitSize);
        }
    }

    private void computePosition(List<Block> blocks, int[] position) {
        final int size = position.length;
        for (int i = 0; i < size; i++) {
            position[i] = -1;
        }
        int positionCounter = 0;
        for (int i = 0; i < size - 1; i++) {
            if (position[i] == -1 && blocks.get(i).getValue() == blocks.get(i + 1).getValue()) {
                position[i] = positionCounter;
                position[i + 1] = positionCounter;
                positionCounter++;
            } else if (position[i] == -1) {
                position[i] = positionCounter;
                positionCounter++;
            }
        }
        if (position[size - 1] == -1) {
            position[size - 1] = positionCounter;
        }
    }

    private boolean isMovePossible() {
        for (Block[] slit : logicalGrid) {
            for (Block block : slit) {
                if (block != null && block.isMoving()) {
                    return false;
                }
            }
        }
        return true;
    }
}
