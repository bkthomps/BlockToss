package ca.uwaterloo.lab3_201_04;

import android.graphics.Point;
import android.view.Display;
import android.widget.RelativeLayout;

/**
 * The grid that the game 2048 is played on, which contains blocks.
 */
class Grid {

    private final Lab3_201_04 instance;
    private final RelativeLayout layout;
    private final int blocksPerScreen;
    private final int gameBoardDimension;
    private final Block[][] logicalGrid;

    Grid(Lab3_201_04 instance, RelativeLayout layout, int blocksPerScreen) {
        this.instance = instance;
        this.layout = layout;
        this.blocksPerScreen = blocksPerScreen;
        this.gameBoardDimension = gameBoardDimension();
        logicalGrid = new Block[blocksPerScreen][blocksPerScreen];
        setGameBoard();
        addBlock(1, 0, 0);
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
     * Add block to the game board with specified attributes.
     *
     * @param value  the value of the block in 2048
     * @param xIndex the x index
     * @param yIndex the y index
     */
    private void addBlock(int value, int xIndex, int yIndex) {
        new Block(instance, layout, blocksPerScreen, gameBoardDimension, logicalGrid, value, xIndex, yIndex);
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
     *
     * @return if the block can move, the direction
     */
    String moveUp() {
        final Block block = getSingleInstance();
        final boolean isMoving = block.isInMotion();
        if (isMoving) {
            return "MOVING";
        }
        block.moveToIndex(-1, 0);
        return "UP";
    }

    /**
     * Moves the single block instance down.
     * TODO: should be implemented differently for lab 4
     *
     * @return if the block can move, the direction
     */
    String moveDown() {
        final Block block = getSingleInstance();
        final boolean isMoving = block.isInMotion();
        if (isMoving) {
            return "MOVING";
        }
        block.moveToIndex(-1, blocksPerScreen - 1);
        return "DOWN";
    }

    /**
     * Moves the single block instance left.
     * TODO: should be implemented differently for lab 4
     *
     * @return if the block can move, the direction
     */
    String moveLeft() {
        final Block block = getSingleInstance();
        final boolean isMoving = block.isInMotion();
        if (isMoving) {
            return "MOVING";
        }
        block.moveToIndex(0, -1);
        return "LEFT";
    }

    /**
     * Moves the single block instance right.
     * TODO: should be implemented differently for lab 4
     *
     * @return if the block can move, the direction
     */
    String moveRight() {
        final Block block = getSingleInstance();
        final boolean isMoving = block.isInMotion();
        if (isMoving) {
            return "MOVING";
        }
        block.moveToIndex(blocksPerScreen - 1, -1);
        return "RIGHT";
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
