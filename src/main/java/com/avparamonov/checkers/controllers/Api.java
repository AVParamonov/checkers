package com.avparamonov.checkers.controllers;

/**
 * Public REST API for work with application.
 *
 * Created by AVParamonov on 09.06.17.
 */
public interface Api {

    String ROOT_PATH = "/checkers";

    interface V1 {
        String VERSION = "/v1";
        String CREATE_PLAYER = VERSION + "/player";
        String CREATE_GAME = VERSION + "/currentGame";
        String MAKE_MOVE = VERSION + "/currentGame/{gameId}/player/{playerId}/move";
    }
}
