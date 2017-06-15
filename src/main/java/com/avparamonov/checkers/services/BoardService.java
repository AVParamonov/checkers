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
        board[move.getToRow()][move.getToCol()] = board[move.getFromRow()][move.getFromCol()];
        board[move.getFromRow()][move.getFromCol()] = null;

        Checker enemyToRemove = move.getEnemyToRemove();
        if (enemyToRemove != null) {
            board[enemyToRemove.getRow()][enemyToRemove.getCol()] = null;
        }

        if (move.getToRow() == 0 || move.getToRow() == 7) {
            board[move.getToRow()][move.getToCol()].setType(CheckerType.KING);
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
                if (checker.getType() == REGULAR) {
                    for (Directions direction: Directions.values()) {
                        Move move = getRegularMove(board, row, col, direction.getSigns()[0], direction.getSigns()[1]);
                        if (move != null) {
                            if (move.isJump()) {
                                jumps.add(move);
                            } else {
                                moves.add(move);
                            }
                        }
                    }
                } else {
                    for (Directions direction: Directions.values()) {
                        int rowSign = direction.getSigns()[0];
                        int colSign = direction.getSigns()[1];
                        Directions oppositeDirection = Directions.valueOf(direction.getOpposite());
                        jumps.addAll(getKingJumps(board, row, col, rowSign, colSign, oppositeDirection));
                        moves.addAll(getKingMoves(board, row, col, rowSign, colSign));
                    }
                }
            }
        }
        if (jumps.isEmpty()) {
            return moves;
        }
        return jumps;
    }

    private Move getRegularMove(Checker[][] board, int row, int col, int rowSign, int colSign) {
        int enemyRow = row + rowSign;
        int enemyCol = col + colSign;
        int jumpRow = row + 2 * rowSign;
        int jumpCol = col + 2 * colSign;

        if (isAtBoard(enemyRow, enemyCol)) {
            Checker checker = board[enemyRow][enemyCol];
            if (checker != null && isAtBoard(jumpRow, jumpCol) && checker.getSide() != board[row][col].getSide() && board[jumpRow][jumpCol] == null) {
                Move jump = new Move(row, col, jumpRow, jumpCol);
                jump.setEnemyToRemove(checker);
                return jump;
            } else if (isAllowedDirection(board, row, col, enemyRow, enemyCol)) {
                return new Move(row, col, enemyRow, enemyCol);
            }
        }
        return null;
    }

    private List<Move> getKingMoves(Checker[][] board, int row, int col, int rowSign, int colSign) {
        List<Move> moves = new ArrayList<>();
        int i = 1;
        while (isAtBoard(row + i * rowSign, col + i * colSign)) {
            if (board[row + i * rowSign][col + i * colSign] == null) {
                moves.add(new Move(row, col, row + i * rowSign, col + i * colSign));
            }
            i++;
        }
        return moves;
    }

    private List<Move> getKingJumps(Checker[][] board, int row, int col, int rowSign, int colSign, Directions oppositeDirection) {
        List<Move> jumps = new ArrayList<>();
        Move jump;
        int enemyRow;
        int enemyCol;
        int jumpRow;
        int jumpCol;

        for (int i = 1; i < 8; i++) {
            enemyRow = row + i * rowSign;
            enemyCol = col + i * colSign;
            if (isAtBoard(enemyRow, enemyCol) && isEnemy(board, row, col, enemyRow, enemyCol)) {
                Checker checker = board[enemyRow][enemyCol];
                for (int j = i; j < 8; j++) {
                    jumpRow = row + (j + 1) * rowSign;
                    jumpCol = col + (j + 1) * colSign;
                    if (isAtBoard(jumpRow, jumpCol) && board[jumpRow][jumpCol] == null) {
                        jump = new Move(row, col, jumpRow, jumpCol);
                        jump.setEnemyToRemove(checker);
                        jumps.add(jump);
                        for (Directions direction: Directions.values()) {
                            if (direction.equals(oppositeDirection)) {
                                continue;
                            }
                            int rSign = direction.getSigns()[0];
                            int cSign = direction.getSigns()[1];
                            Directions opposite = Directions.valueOf(direction.getOpposite());
                            jumps.addAll(getKingJumps(board, jumpRow, jumpCol, rSign, cSign, opposite));
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

    private boolean isAllowedDirection(Checker[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (board[fromRow][fromCol].getSide() == WHITE) {
            return toRow > fromRow;
        } else {
            return toRow < fromRow;
        }
    }
}