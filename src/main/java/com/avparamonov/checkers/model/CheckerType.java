package com.avparamonov.checkers.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Checker type enum.
 *
 * Created by AVParamonov on 25.05.17.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CheckerType {

    REGULAR(1, "regular"),
    KING(2, "king");

    private int id;
    private String label;

    CheckerType(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

}
