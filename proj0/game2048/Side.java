package game2048;

/** Symbolic names for the four sides of a board.
 *  @author P. N. Hilfinger */
public enum Side {
    /** The parameters (COL0, ROW0, DCOL, and DROW) for each of the
     *  symbolic directions, D, below are to be interpreted as follows:
     *     The board's standard orientation has the top of the board
     *     as NORTH, and rows and columns (see Model) are numbered
     *     from its lower-left corner. Consider the board oriented
     *     so that side D of the board is farthest from you. Then
     *        * (COL0*s, ROW0*s) are the standard coordinates of the
     *          lower-left corner of the reoriented board (where s is the
     *          board size), and
     *        * If (c, r) are the standard coordinates of a certain
     *          square on the reoriented board, then (c+DCOL, r+DROW)
     *          are the standard coordinates of the squares immediately
     *          above it on the reoriented board.
     *  The idea behind going to this trouble is that by using the
     *  col() and row() methods below to translate from reoriented to
     *  standard coordinates, one can arrange to use exactly the same code
     *  to compute the result of tilting the board in any particular
     *  direction. */
    /** 对于下面每个符号化方向 D 的参数 (COL0, ROW0, DCOL 和 DROW)，应按如下方式解释：
     *  棋盘的标准方向是以北方（NORTH）为棋盘顶部，行和列（参见 Model）从棋盘的左下角开始编号。
     *  考虑棋盘按某个方向 D 摆放，这时棋盘的 D 方向处于离你最远的位置。然后：
     *    * (COL0 * s, ROW0 * s) 是重新定向棋盘的左下角的标准坐标（其中 s 是棋盘的大小），并且
     *    * 如果 (c, r) 是重新定向棋盘上某个方块的标准坐标，则 (c + DCOL, r + DROW)
     *      是重新定向棋盘上该方块正上方方块的标准坐标。
     *  进行这种处理的目的是，通过使用下面的 col() 和 row() 方法，将重新定向的坐标转换为标准坐标，
     *  从而可以使用完全相同的代码来计算将棋盘向任意方向倾斜的结果。 */

    NORTH(0, 0, 0, 1),
    EAST(0, 1, 1, 0),
    SOUTH(1, 1, 0, -1),
    WEST(1, 0, -1, 0);

    /** The side that is in the direction (DCOL, DROW) from any square
     *  of the board.  Here, "direction (DCOL, DROW) means that to
     *  move one space in the direction of this Side increases the row
     *  by DROW and the colunn by DCOL.  (COL0, ROW0) are the row and
     *  column of the lower-left square when sitting at the board facing
     *  towards this Side. */
    /** 棋盘上任何方块在 (DCOL, DROW) 方向上的一侧。
     * 在这里，“方向 (DCOL, DROW)”意味着沿着该侧移动一格，行号增加 DROW，列号增加 DCOL。
     *  (COL0, ROW0) 是当面对该侧坐在棋盘前时，棋盘左下角方块的行和列。 */
    Side(int col0, int row0, int dcol, int drow) {
        this.row0 = row0;
        this.col0 = col0;
        this.drow = drow;
        this.dcol = dcol;
    }

    /** Returns the side opposite of side S. */
    static Side opposite(Side s) {
        if (s == NORTH) {
            return SOUTH;
        } else if (s == SOUTH) {
            return NORTH;
        } else if (s == EAST) {
            return WEST;
        } else {
            return EAST;
        }
    }

    /** Return the standard column number for square (C, R) on a board
     *  of size SIZE oriented with this Side on top. */
    /** 返回位于 (C, R) 位置的方块在以此侧为顶部且大小为 SIZE 的棋盘上的标准列号。 */
    public int col(int c, int r, int size) {
        return col0 * (size - 1) + c * drow + r * dcol;
    }

    /** Return the standard row number for square (C, R) on a board
     *  of size SIZE oriented with this Side on top. */
    public int row(int c, int r, int size) {
        return row0 * (size - 1) - c * dcol + r * drow;
    }

    /** Parameters describing this Side, as documented in the comment at the
     *  start of this class. */
    private int row0, col0, drow, dcol;

};
