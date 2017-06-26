package ca.uwaterloo.lab3_201_04;

import android.graphics.Point;
import android.view.Display;
import android.widget.RelativeLayout;

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

    private void setGameBoard() {
        layout.getLayoutParams().width = gameBoardDimension;
        layout.getLayoutParams().height = gameBoardDimension;
        layout.setBackgroundResource(R.drawable.gameboard);
    }

    private void addBlock(int value, int xIndex, int yIndex) {
        new Block(instance, layout, blocksPerScreen, gameBoardDimension, logicalGrid, value, xIndex, yIndex);
    }

    private int gameBoardDimension() {
        final Display display = instance.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        final int BANNER_OFFSET = 300;
        final int width = size.x;
        final int height = size.y - BANNER_OFFSET;
        return (width < height) ? width : height;
    }

    void moveUp() {
        getSingleInstance().moveToIndex(-1, 0);
    }

    void moveDown() {
        getSingleInstance().moveToIndex(-1, blocksPerScreen - 1);
    }

    void moveLeft() {
        getSingleInstance().moveToIndex(0, -1);
    }

    void moveRight() {
        getSingleInstance().moveToIndex(blocksPerScreen - 1, -1);
    }

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
