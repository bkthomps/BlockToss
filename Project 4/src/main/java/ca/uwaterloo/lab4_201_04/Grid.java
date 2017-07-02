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

    }

    /**
     * Move and merge the blocks down.
     */
    void moveDown() {

    }

    /**
     * Move and merge the blocks left.
     */
    void moveLeft() {
        for (int vertical = 0; vertical < logicalGrid.length; vertical++) {
            for (int current = 1; current < logicalGrid[0].length; current++) {
                final Block me = logicalGrid[vertical][current];
                if (me != null) {
                    for (int next = 0; next < current; next++) {
                        if (logicalGrid[vertical][next] == null) {
                            me.moveToIndex(next, vertical);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Move and merge the blocks right.
     */
    void moveRight() {

    }
}
