package com.avparamonov.checkers.db.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

/**
 * Board entity.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "board_id")
    long id;

    @OneToMany(fetch = FetchType.EAGER)
    List<Cell> cells;

}
