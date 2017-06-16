package com.avparamonov.checkers.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Checker's move.
 *
 * Created by AVParamonov on 08.06.17.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude={"enemyToRemove"})
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

}
