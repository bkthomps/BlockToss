package ca.uwaterloo.lab4_201_04;

import android.graphics.Point;
import android.view.Display;
import android.widget.RelativeLayout;

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
                addBlock();
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
     * Moves the single block instance up.
     * TODO: should be implemented differently for lab 4
     */
    void moveUp() {
        final Block block = getSingleInstance();
        final boolean isMoving = block.isInMotion();
        if (isMoving) {
            return;
        }
        block.moveToIndex(-1, 0);
    }

    /**
     * Moves the single block instance down.
     * TODO: should be implemented differently for lab 4
     */
    void moveDown() {
        final Block block = getSingleInstance();
        final boolean isMoving = block.isInMotion();
        if (isMoving) {
            return;
        }
        block.moveToIndex(-1, blocksPerScreen - 1);
    }

    /**
     * Moves the single block instance left.
     * TODO: should be implemented differently for lab 4
     */
    void moveLeft() {
        final Block block = getSingleInstance();
        final boolean isMoving = block.isInMotion();
        if (isMoving) {
            return;
        }
        block.moveToIndex(0, -1);
    }

    /**
     * Moves the single block instance right.
     * TODO: should be implemented differently for lab 4
     */
    void moveRight() {
        final Block block = getSingleInstance();
        final boolean isMoving = block.isInMotion();
        if (isMoving) {
            return;
        }
        block.moveToIndex(blocksPerScreen - 1, -1);
    }

    /**
     * Retrieves the single block instance.
     * TODO: should be gone by lab 4
     *
     * @return the single block instance
     * @throws NullPointerException if forgot to add an instance of a block
     */
    private Block getSingleInstance() {
        for (int vertical = 0; vertical < blocksPerScreen; vertical++) {
            for (int horizontal = 0; horizontal < blocksPerScreen; horizontal++) {
                final Block instance = logicalGrid[vertical][horizontal];
                if (instance != null) {
                    return instance;
                }
            }
        }
        throw new NullPointerException();
    }
}
