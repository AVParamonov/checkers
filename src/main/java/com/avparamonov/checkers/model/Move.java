package com.avparamonov.checkers.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;

/**
 * Checker's move.
 *
 * Created by AVParamonov on 08.06.17.
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Move {

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    int fromRow;
    int fromCol;
    int toRow;
    int toCol;
    Checker enemyToRemove;

    public boolean isJump() {
        return getEnemyToRemove() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (fromRow != move.fromRow) return false;
        if (fromCol != move.fromCol) return false;
        if (toRow != move.toRow) return false;
        return toCol == move.toCol;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + fromRow;
        result = 31 * result + fromCol;
        result = 31 * result + toRow;
        result = 31 * result + toCol;
        return result;
    }
}
