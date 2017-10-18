package com.avparamonov.checkers.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * Checker entity.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Checker {

    int row;
    int col;
    CheckerType type;
    Side side;
}
