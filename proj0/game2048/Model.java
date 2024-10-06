package game2048;

import java.util.*;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff（if and only if)™ the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        //should consider the side to transfer the cordination
        board.setViewingPerspective(side);

        for (int c = 0; c < board.size(); c++) {
            //on every rows iteration , take a map to record fixed col's row tile had been merged
            Boolean[] mergedRowRecorder = new Boolean[board.size()];
            Arrays.fill(mergedRowRecorder, false);
            for (int r = board.size() -1 ; r >=0 ; r--) {
                Tile t = board.tile(c,r);
                if (t != null){
//                    we can use move function's return boolean to judge whether the move to null or merged
                    // so most impotant this is to get what is the true input of move function
                    // because we only think in the NORTH's side ,so on the every move ,
                    // the tile's next only to the upper rows or static
                    // because the iteration from one col to every row form bottom to top
                    //e.g.  now c = 0 ,and we will iterate 0,0 0,1 0,2 0,3
                    // is iterating from bottom to top  a good thing for tricky merge or triple merge
                    // anyway we should let change happen first
                    // think from noMerge and BasicMerge
                    // let's watch for col side : 0 0 2 0
                    // when we go to the '2', we will watch the upper,if it's null ,goto it,and clean the board value of the prev

                    //record the final null, or find the merging object
                    int destinationOnRow = r;
                    for (int nextRow = r + 1; nextRow < board.size(); nextRow++) {
                        if (board.tile(c,nextRow) != null || nextRow == board.size() - 1){
                            destinationOnRow = nextRow;
                            break;
                        }
                    }
                    if (destinationOnRow != r){
                        if(board.tile(c,destinationOnRow) == null){
                            board.move(c, destinationOnRow, t);
                        } else if (board.tile(c,destinationOnRow).value() == t.value() && ! mergedRowRecorder[destinationOnRow]){

                            //merge part : we should think about whether [board.tile(c,destinationOnRow)] merged
                            //can we use a Map to record the recordings
                            board.move(c, destinationOnRow, t);
                            score += t.value() * 2;
                            mergedRowRecorder[destinationOnRow] = true;
                        }else{
                            //cannot merge
                            board.move(c, destinationOnRow - 1, t);
                        }
                        changed = true;

                    }

                }
            }
        }

        board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
                if (b.tile(col,row) == null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
                if (b.tile(col,row) != null && b.tile(col,row).value() == 2048 ){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if(Model.emptySpaceExists(b)){
            return true;
        }
        if(Model.maxTileExists(b)){
            return true;
        }

        // judge if there have two adjacent tiles with the same value.
        // think about iterate 沿着每一行或者每一列做两两比较，如果存在两两相等就可以

        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
//                左右移动，比相邻列
                int adcol = col + 1;
                if(adcol < b.size() && adcol >= 0){
                    if (b.tile(adcol,row).value() == b.tile(col,row).value()){
                        return true;
                    }
                }
                adcol = col - 1;
                if(adcol < b.size() && adcol >= 0){
                    if (b.tile(adcol,row).value() == b.tile(col,row).value()){
                        return true;
                    }
                }

//                上下移动，比相邻行
                int adrow = row + 1;
                if(adrow < b.size() && adrow >= 0){
                    if (b.tile(col,adrow).value() == b.tile(col,row).value()){
                        return true;
                    }
                }
                adrow = row - 1;
                if(adrow < b.size() && adrow >= 0){
                    if (b.tile(col,adrow).value() == b.tile(col,row).value()){
                        return true;
                    }
                }


            }
        }

        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
