package chess;

import static chess.PieceType.*;
public class Knight implements Piece {

    /* A constructor for a piece on the board with color COLOR,
       game GAME, and location (X, Y). */
    public Knight(PieceColor color, Game game, int x, int y) {
        _color = color;
        _game = game;
        _x = x;
        _y = y;
    }

    @Override
    // return the abbreviation of the piece
    public String imageString() {
        return _color.abbrev() + KNIGHT.abbrev();
    }

    @Override
    // Return the color of this piece
    public PieceColor color() {
        return _color;
    }

    @Override
    // return knight type
    public PieceType type() {
        return KNIGHT;
    }

    @Override
    // Returns false if the move is not valid. Otherwise, make the move.
    public boolean makeValidMove(int a, int b) {
        if (_game.get(a, b) != null
            && _game.get(a, b).color() == _color) {
            return false;
        } else if ((Math.abs(a - _x) == 2 && Math.abs(b - _y) == 1)
            || (Math.abs(b - _y) == 2 && Math.abs(a - _x) == 1)) {
            SingleMove move = new SingleMove(this, _x, _y,
                _game.get(a, b), a, b);
            return makeMoveCareful(move);
        } else {
            return false;
        }
    }

    @Override
    // setlocation (X, Y) and update the board.
    public void setLocation(int x, int y) {
        _x = x;
        _y = y;
    }

    @Override
    // hasMove() returns true if the knight can move to (A, B)
    public boolean hasMove() {
        if ((_x - 2 >= 0 && _y + 1 <= 7 && makeValidMove(_x - 2, _y + 1))
            || (_x - 2 >= 0 && _y - 1 >= 0 && makeValidMove(_x - 2, _y - 1))
            || (_x - 1 >= 0 && _y + 2 <= 7 && makeValidMove(_x - 1, _y + 2))
            || (_x - 1 >= 0 && _y - 2 >= 0 && makeValidMove(_x - 1, _y - 2))
            || (_x + 1 <= 7 && _y + 2 <= 7 && makeValidMove(_x + 1, _y + 2))
            || (_x + 1 <= 7 && _y - 2 >= 0 && makeValidMove(_x + 1, _y - 2))
            || (_x + 2 <= 7 && _y + 1 <= 7 && makeValidMove(_x + 2, _y + 1))
            || (_x + 2 <= 7 && _y - 1 >= 0 && makeValidMove(_x + 2, _y - 1))) {
            _game.undoMove();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canCapture(int a, int b) {
        return ((Math.abs(a - _x) == 2 && Math.abs(b - _y) == 1)
            || (Math.abs(b - _y) == 2 && Math.abs(a - _x) == 1));
    }

    /* Makes the move MOVE, and returns false if the move doesn't leave the
       king in check, in which case the move is undone, and true otherwise. */
    private boolean makeMoveCareful(Move move) {
        _game.makeMove(move);
        if (_game.inCheck(_game.turn().opposite())) {
            _game.undoMove();
            return false;
        } else {
            return true;
        }
    }

    // game is the game the piece is in
    private Game _game;
    // The color of the piece.
    private PieceColor _color;
    // The x-coordinate of the piece.
    private int _x;
    // The y-coordinate of the piece.
    private int _y;

}
