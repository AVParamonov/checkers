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
//        String PLAYER = VERSION + "/player";
        String REGISTRATION = VERSION + "/registration";
        String ADMIN = VERSION + "/admin";
        String LOGIN = VERSION + "/login";
        String LOGOUT = VERSION + "/logout";
        String ERROR = VERSION + "/error";
        String HOME = VERSION + "/home";
        String GAME = VERSION + "/game";
        String MAKE_MOVE = VERSION + "/game/{gameId}/player/{playerId}/move";
    }
}
