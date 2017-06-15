package com.avparamonov.checkers.services;

import com.avparamonov.checkers.model.*;
import com.avparamonov.checkers.model.db.entity.Player;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.avparamonov.checkers.model.CheckerType.*;
import static com.avparamonov.checkers.model.Side.*;

/**
 * Board service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class BoardService {

    private enum Directions {
        UP_LEFT(new int[] {1, -1}, "DOWN_RIGHT"),
        UP_RIGHT(new int[] {1, 1}, "DOWN_LEFT"),
        DOWN_RIGHT(new int[] {-1, 1}, "UP_LEFT"),
        DOWN_LEFT(new int[] {-1, -1}, "UP_RIGHT");

        private final int[] signs;
        private final String oppositeDirection;

        Directions(int[] signs, String oppositeDirection) {
            this.signs = signs;
            this.oppositeDirection = oppositeDirection;
        }

        public int[] getSigns() { return signs; }

        public String getOpposite() { return this.oppositeDirection; }
    }

    public void makeMove(Checker[][] board, Move move) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = null;

        Checker enemyToRemove = move.getEnemyToRemove();
        if (enemyToRemove != null) {
            board[enemyToRemove.getRow()][enemyToRemove.getCol()] = null;
        }

        if (toRow == 0 || toRow == 7) {
            board[toRow][toCol].setType(CheckerType.KING);
        }
    }

    public List<Move> getAvailableMoves(Player player, Checker[][] board) {
        List<Move> jumps = new ArrayList<>();
        List<Move> moves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Checker checker = board[row][col];
                if (checker == null || checker.getSide() != player.getSide()) {
                    continue;
                }
                for (Directions direction: Directions.values()) {
                    jumps.addAll(getJumps(board, row, col, checker.getType(), direction));
                    moves.addAll(getMoves(board, row, col, direction));
                }
            }
        }
        if (jumps.isEmpty()) {
            return moves;
        }
        return jumps;
    }

    private List<Move> getMoves(Checker[][] board, int row, int col, Directions direction) {
        List<Move> moves = new ArrayList<>();
        Checker checker = board[row][col];
        int toRow;
        int toCol;
        int rowSign = direction.getSigns()[0];
        int colSign = direction.getSigns()[1];
        int depth = (checker.getType() == REGULAR) ? 2 : 8;

        for (int i = 1; i < depth; i++) {
            toRow = row + i * rowSign;
            toCol = col + i * colSign;
            if (isAtBoard(toRow, toCol) && board[toRow][toCol] == null) {
                if (checker.getType() == KING) {
                    moves.add(new Move(row, col, toRow, toCol));
                } else if (isForward(checker.getSide(), row, toRow)) {
                    moves.add(new Move(row, col, toRow, toCol));
                }
            }
        }
        return moves;
    }

    private List<Move> getJumps(Checker[][] board, int row, int col, CheckerType checkerType, Directions direction) {
        List<Move> jumps = new ArrayList<>();
        Move jump;
        int enemyRow;
        int enemyCol;
        int jumpRow;
        int jumpCol;
        int depth = (checkerType == REGULAR) ? 2 : 8;
        int rowSign = direction.getSigns()[0];
        int colSign = direction.getSigns()[1];
        Directions oppositeDirection = Directions.valueOf(direction.getOpposite());

        for (int i = 1; i < depth; i++) {
            enemyRow = row + i * rowSign;
            enemyCol = col + i * colSign;
            if (isAtBoard(enemyRow, enemyCol) && isEnemy(board, row, col, enemyRow, enemyCol)) {
                for (int j = i; j < depth; j++) {
                    jumpRow = row + (j + 1) * rowSign;
                    jumpCol = col + (j + 1) * colSign;
                    if (isAtBoard(jumpRow, jumpCol) && board[jumpRow][jumpCol] == null) {
                        jump = new Move(row, col, jumpRow, jumpCol);
                        jump.setEnemyToRemove(board[enemyRow][enemyCol]);
                        jumps.add(jump);
                        for (Directions childDirection: Directions.values()) {
                            if (childDirection.equals(oppositeDirection)) {
                                continue;
                            }
                            jumps.addAll(getJumps(board, jumpRow, jumpCol, checkerType, childDirection));
                        }
                    }
                }
            }
        }
        return jumps;
    }

    private boolean isEnemy(Checker[][] board, int row, int col, int enemyRow, int enemyCol) {
        return board[row][col] != null && board[enemyRow][enemyCol] != null
                && board[row][col].getSide() != board[enemyRow][enemyCol].getSide();
    }

    private boolean isAtBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private boolean isForward(Side checkerSide, int fromRow, int toRow) {
        if (checkerSide == WHITE) {
            return toRow > fromRow;
        } else {
            return toRow < fromRow;
        }
    }
}