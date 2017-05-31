package com.avparamonov.checkers.db.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * Cell entity.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "cells")
public class Cell {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "cell_id")
    long id;

    int row;

    int col;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "checker_id")
    Checker checker;

}
