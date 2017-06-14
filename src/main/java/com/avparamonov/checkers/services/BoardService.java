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
        UP_LEFT,
        UP_RIGHT,
        DOWN_RIGHT,
        DOWN_LEFT
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
                        Move move = getRegularMoveInDirection(direction, board, row, col);
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
                        jumps.addAll(getKingJumpsInDirection(direction, board, row, col));
                        moves.addAll(getKingMovesInDirection(direction, board, row, col));
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
            if (checker != null && isAtBoard(jumpRow, jumpCol)) {
                if (checker.getSide() != board[row][col].getSide() && board[jumpRow][jumpCol] == null) {
                    Move jump = new Move(row, col, jumpRow, jumpCol);
                    jump.setEnemyToRemove(checker);
                    return jump;
                }
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
        int jumpRow;
        int jumpCol;
        int i = 1;
        while (isAtBoard(row + i * rowSign, col + i * colSign)) {
            Checker checker = board[row + i * rowSign][col + i * colSign];
            if (checker != null && checker.getSide() != board[row][col].getSide()) {
                int j = i;
//                jumpRow = row + (j + 1) * rowSign;
//                jumpCol = col + (j + 1) * colSign;
                while (isAtBoard(row + (j + 1) * rowSign, col + (j + 1) * colSign) && board[row + (j + 1) * rowSign][col + (j + 1) * colSign] == null) {
                    jump = new Move(row, col, row + (j + 1) * rowSign, col + (j + 1) * colSign);
                    jump.setEnemyToRemove(checker);
                    jumps.add(jump);
                    j++;
                    for (Directions direction: Directions.values()) {
                        if (direction.equals(oppositeDirection)) {
                            continue;
                        }
                        jumps.addAll(getKingJumpsInDirection(direction, board, row + (j + 1) * rowSign, col + (j + 1) * colSign));
                    }
                }
            }
            i++;
        }
        return jumps;
    }

    private Move getRegularMoveInDirection(Directions direction, Checker[][] board, int row, int col) {
        switch (direction) {
            case UP_LEFT:
                return getRegularMove(board, row, col, 1, -1);
            case UP_RIGHT:
                return getRegularMove(board, row, col, 1, 1);
            case DOWN_RIGHT:
                return getRegularMove(board, row, col, -1, 1);
            case DOWN_LEFT:
                return getRegularMove(board, row, col, -1, -1);
            default:
                return null;
        }
    }

    private List<Move> getKingMovesInDirection(Directions direction, Checker[][] board, int row, int col) {
        switch (direction) {
            case UP_LEFT:
                return getKingMoves(board, row, col, 1, -1);
            case UP_RIGHT:
                return getKingMoves(board, row, col, 1, 1);
            case DOWN_RIGHT:
                return getKingMoves(board, row, col, -1, 1);
            case DOWN_LEFT:
                return getKingMoves(board, row, col, -1, -1);
            default:
                return Collections.emptyList();
        }
    }

    private List<Move> getKingJumpsInDirection(Directions direction, Checker[][] board, int row, int col) {
        switch (direction) {
            case UP_LEFT:
                return getKingJumps(board, row, col, 1, -1, Directions.DOWN_RIGHT);
            case UP_RIGHT:
                return getKingJumps(board, row, col, 1, 1, Directions.DOWN_LEFT);
            case DOWN_RIGHT:
                return getKingJumps(board, row, col, -1, 1, Directions.UP_LEFT);
            case DOWN_LEFT:
                return getKingJumps(board, row, col, -1, -1, Directions.UP_RIGHT);
            default:
                return Collections.emptyList();
        }
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
