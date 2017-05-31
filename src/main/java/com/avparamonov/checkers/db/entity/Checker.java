package com.avparamonov.checkers.db.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * Checker entity.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "checkers")
public class Checker {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "checker_id")
    long id;

    @Enumerated
    CheckerType type;

    @Enumerated
    Side side;

    @Column(name = "is_current")
    boolean isCurrent;

}
