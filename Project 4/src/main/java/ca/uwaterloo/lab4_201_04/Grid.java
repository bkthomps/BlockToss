package ca.uwaterloo.lab4_201_04;

import android.graphics.Point;
import android.view.Display;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
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

    static boolean didMovementOccur;

    Grid(Lab4_201_04 instance, RelativeLayout layout, int blocksPerScreen) {
        this.instance = instance;
        this.layout = layout;
        this.blocksPerScreen = blocksPerScreen;
        this.gameBoardDimension = gameBoardDimension();
        logicalGrid = new Block[blocksPerScreen][blocksPerScreen];
        setGameBoard();
        addBlock();
    }

    /**
     * Set the size of the game board and the game board itself.
     */
    private void setGameBoard() {
        layout.getLayoutParams().width = gameBoardDimension;
        layout.getLayoutParams().height = gameBoardDimension;
        layout.setBackgroundResource(R.drawable.gameboard);
    }

    /**
     * Schedules a block to be added once all the moving blocks settle.
     */
    private void scheduleBlockAdd() {
        final int CHECK_RATE = 100;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                instance.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        if (didMovementOccur && isMovePossible()) {
                            addBlock();
                            didMovementOccur = false;
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, CHECK_RATE);
    }

    /**
     * Add block to the game board. If there are no free spaces left, the game is lost. Otherwise, a space is randomly
     * selected, and if it is free, a block will be placed there.
     */
    private void addBlock() {
        int xIndex, yIndex;
        do {
            xIndex = (int) (Math.random() * logicalGrid[0].length);
            yIndex = (int) (Math.random() * logicalGrid.length);
        } while (logicalGrid[yIndex][xIndex] != null);
        final int amountOfSpawnBlocks = Lab4_201_04.BLOCKS_THAT_CAN_SPAWN.length;
        final int index = (int) (Math.random() * amountOfSpawnBlocks);
        final int value = Lab4_201_04.BLOCKS_THAT_CAN_SPAWN[index];
        if (value % 2 != 0 && value != 1) {
            throw new IllegalStateException("Spawn block value must be 1 or a multiple of 2.");
        }
        new Block(instance, layout, blocksPerScreen, gameBoardDimension, logicalGrid, value, xIndex, yIndex);
        if (!isAnyCoordinateFree() && !isCombinePossible()) {
            instance.gameLose();
        }
    }

    /**
     * Checks if any coordinate is free.
     *
     * @return if a coordinate is free
     */
    private boolean isAnyCoordinateFree() {
        for (Block[] slit : logicalGrid) {
            for (Block block : slit) {
                if (block == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if blocks can combine.
     *
     * @return if blocks can combine
     */
    private boolean isCombinePossible() {
        for (int i = 0; i < logicalGrid.length; i++) {
            for (int j = 0; j < logicalGrid[0].length - 1; j++) {
                if (logicalGrid[i][j].getValue() == logicalGrid[i][j + 1].getValue()) {
                    return true;
                }
            }
        }
        for (int i = 0; i < logicalGrid.length - 1; i++) {
            for (int j = 0; j < logicalGrid[0].length; j++) {
                if (logicalGrid[i][j].getValue() == logicalGrid[i + 1][j].getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Specifies what size the game board should be, based on user phone specifications.
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
        if (isMoveProhibited()) {
            return;
        }
        for (int horizontal = 0; horizontal < logicalGrid[0].length; horizontal++) {
            moveSlitUp(horizontal);
        }
        scheduleBlockAdd();
    }

    /**
     * Move and merge the blocks down.
     */
    void moveDown() {
        if (isMoveProhibited()) {
            return;
        }
        for (int horizontal = 0; horizontal < logicalGrid[0].length; horizontal++) {
            moveSlitDown(horizontal);
        }
        scheduleBlockAdd();
    }

    /**
     * Move and merge the blocks left.
     */
    void moveLeft() {
        if (isMoveProhibited()) {
            return;
        }
        for (int vertical = 0; vertical < logicalGrid.length; vertical++) {
            moveSlitLeft(vertical);
        }
        scheduleBlockAdd();
    }

    /**
     * Move and merge the blocks right.
     */
    void moveRight() {
        if (isMoveProhibited()) {
            return;
        }
        for (int vertical = 0; vertical < logicalGrid.length; vertical++) {
            moveSlitRight(vertical);
        }
        scheduleBlockAdd();
    }

    /**
     * Checks to see if move is prohibited.
     *
     * @return if move is prohibited
     */
    private boolean isMoveProhibited() {
        return !isMovePossible() || Lab4_201_04.isGameOfficiallyDone;
    }

    /**
     * Moves a slit up.
     *
     * @param slitSize the size of the slit to move
     */
    private void moveSlitUp(int slitSize) {
        // Adding blocks to the list
        final List<Block> blocks = new ArrayList<>();
        for (int i = 0; i < logicalGrid.length; i++) {
            final Block block = logicalGrid[i][slitSize];
            if (block != null) {
                blocks.add(block);
            }
        }
        if (blocks.isEmpty()) {
            return;
        }
        // Computing the position
        final int size = blocks.size();
        final int[] position = new int[size];
        computePosition(blocks, position);
        // Converting position to block position
        for (int i = 0; i < size; i++) {
            blocks.get(i).moveToIndex(slitSize, position[i]);
        }
    }

    /**
     * Moves a slit down.
     *
     * @param slitSize the size of the slit to move
     */
    private void moveSlitDown(int slitSize) {
        // Adding blocks to the list
        final List<Block> blocks = new ArrayList<>();
        for (int i = 0; i < logicalGrid.length; i++) {
            final Block block = logicalGrid[i][slitSize];
            if (block != null) {
                blocks.add(block);
            }
        }
        if (blocks.isEmpty()) {
            return;
        }
        Collections.reverse(blocks);
        // Computing the position
        final int size = blocks.size();
        final int[] position = new int[size];
        computePosition(blocks, position);
        // Converting position to block position
        for (int i = 0; i < size; i++) {
            blocks.get(i).moveToIndex(slitSize, logicalGrid.length - 1 - position[i]);
        }
    }

    /**
     * Moves a slit left.
     *
     * @param slitSize the size of the slit to move
     */
    private void moveSlitLeft(int slitSize) {
        // Adding blocks to the list
        final List<Block> blocks = new ArrayList<>();
        for (Block block : logicalGrid[slitSize]) {
            if (block != null) {
                blocks.add(block);
            }
        }
        if (blocks.isEmpty()) {
            return;
        }
        // Computing the position
        final int size = blocks.size();
        final int[] position = new int[size];
        computePosition(blocks, position);
        // Converting position to block position
        for (int i = 0; i < size; i++) {
            blocks.get(i).moveToIndex(position[i], slitSize);
        }
    }

    /**
     * Moves a slit right.
     *
     * @param slitSize the size of the slit to move
     */
    private void moveSlitRight(int slitSize) {
        // Adding blocks to the list
        final List<Block> blocks = new ArrayList<>();
        for (Block block : logicalGrid[slitSize]) {
            if (block != null) {
                blocks.add(block);
            }
        }
        if (blocks.isEmpty()) {
            return;
        }
        Collections.reverse(blocks);
        // Computing the position
        final int size = blocks.size();
        final int[] position = new int[size];
        computePosition(blocks, position);
        // Converting position to block position
        for (int i = 0; i < size; i++) {
            blocks.get(i).moveToIndex(logicalGrid.length - 1 - position[i], slitSize);
        }
    }

    /**
     * Computes the position.
     *
     * @param blocks   the list of blocks.
     * @param position the position in which the blocks should be displayed
     */
    private void computePosition(List<Block> blocks, int[] position) {
        final int size = blocks.size();
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

    /**
     * Checks to see if it is possible to move.
     *
     * @return if it is possible to move
     */
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
