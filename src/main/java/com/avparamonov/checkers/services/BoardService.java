package com.avparamonov.checkers.services;

import com.avparamonov.checkers.exceptions.CheckerNotFoundException;
import com.avparamonov.checkers.model.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CheckerService checkerService;

    public void init(Checker[][] board) {
        int whitesBorderRow = 3;
        int blacksBorderRow = board.length - 4;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if ( row % 2 == col % 2 ) { // black cells only
                    if (row < whitesBorderRow) {
                        board[row][col] = checkerService.createWithSideAndCoordinates(WHITE, row, col);
                    }
                    else if (row > blacksBorderRow) {
                        board[row][col] = checkerService.createWithSideAndCoordinates(BLACK, row, col);
                    }
                    else {
                        board[row][col] = null;
                    }
                }
                else {
                    board[row][col] = null;
                }
            }
        }
    }

    public void apply(Checker[][] board, Move move) throws CheckerNotFoundException {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();
        Checker checker = board[fromRow][fromCol];
        Checker enemyChecker = move.getEnemyToRemove();

        if (checker == null) {
            throw new CheckerNotFoundException("Checker not found at position with row='" +
                    fromRow + "' and column='" + fromCol + "'");
        }

        boolean becameWhiteKing = checker.getSide() == WHITE && toRow == board.length - 1;
        boolean becameBlackKing = checker.getSide() == BLACK && toRow == 0;

        if (checker.getType() == REGULAR && (becameWhiteKing || becameBlackKing)) {
            checker.setType(CheckerType.KING);
        }

        checker.setRow(toRow);
        checker.setCol(toCol);

        board[toRow][toCol] = checker;
        board[fromRow][fromCol] = null;

        if (enemyChecker != null) {
            board[enemyChecker.getRow()][enemyChecker.getCol()] = null;
        }
    }

    public List<Move> getMoves(Checker[][] board, int row, int col, Directions direction) {
        List<Move> moves = new ArrayList<>();
        Checker checker = board[row][col];
        int toRow;
        int toCol;
        int rowSign = direction.getSigns()[0];
        int colSign = direction.getSigns()[1];
        int moveRange = (checker.getType() == REGULAR) ? 2 : board.length;

        for (int i = 1; i < moveRange; i++) {
            toRow = row + i * rowSign;
            toCol = col + i * colSign;
            if (isAtBoard(board.length, toRow, toCol) && board[toRow][toCol] == null) {
                if (checker.getType() == KING) {
                    moves.add(new Move(row, col, toRow, toCol));
                } else if (isForward(checker.getSide(), row, toRow)) {
                    moves.add(new Move(row, col, toRow, toCol));
                }
            }
        }
        return moves;
    }

    public List<Move> getJumps(Checker[][] board, int row, int col, CheckerType checkerType, Directions direction) {
        List<Move> jumps = new ArrayList<>();
        Move jump;
        int enemyRow;
        int enemyCol;
        int jumpRow;
        int jumpCol;
        int moveRange = (checkerType == REGULAR) ? 2 : board.length;
        int rowSign = direction.getSigns()[0];
        int colSign = direction.getSigns()[1];
        Directions oppositeDirection = direction.getOpposite();

        for (int i = 1; i < moveRange; i++) {
            enemyRow = row + i * rowSign;
            enemyCol = col + i * colSign;
            if (isAtBoard(board.length, enemyRow, enemyCol) && isEnemy(board, row, col, enemyRow, enemyCol)) {
                for (int j = i; j < moveRange; j++) {
                    jumpRow = row + (j + 1) * rowSign;
                    jumpCol = col + (j + 1) * colSign;
                    if (isAtBoard(board.length, jumpRow, jumpCol) && board[jumpRow][jumpCol] == null) {
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
        return board[enemyRow][enemyCol] != null && board[row][col] != null
                && board[row][col].getSide() != board[enemyRow][enemyCol].getSide();
    }

    private boolean isAtBoard(int size, int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private boolean isForward(Side checkerSide, int fromRow, int toRow) {
        if (checkerSide == WHITE) {
            return toRow > fromRow;
        } else {
            return toRow < fromRow;
        }
    }
}