package com.avparamonov.checkers.controllers;

import com.avparamonov.checkers.exceptions.CheckerNotFoundException;
import com.avparamonov.checkers.exceptions.GameNotFoundException;
import com.avparamonov.checkers.exceptions.MoveNotAllowedException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ValidationException;

@ControllerAdvice
@Log4j
public class GlobalExceptionController {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception(final Throwable throwable, final ModelAndView modelAndView) {
        log.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }

//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public void handleException(ValidationException exception) {
//        log.warn("Validation failed: " + exception.getMessage());
//    }
//
//    @ExceptionHandler(MoveNotAllowedException.class)
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public void handleException(MoveNotAllowedException exception) {
//        log.warn(exception.getMessage());
//    }
//
//    @ExceptionHandler(CheckerNotFoundException.class)
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public void handleException(CheckerNotFoundException exception) {
//        log.warn(exception.getMessage());
//    }
//
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleException(PlayerNotFoundException exception) {
        log.warn(exception.getMessage());
    }
//
//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public void handleException(GameNotFoundException exception) {
//        log.warn(exception.getMessage());
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
//    public void handleException(MethodArgumentNotValidException exception) {
//        log.warn("Not valid incoming data: " + exception.getMessage());
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public void handleException(HttpMessageNotReadableException exception) {
//        log.warn("Wrong request data", exception);
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public void handleException(HttpMediaTypeNotSupportedException exception) {
//        log.warn("Wrong request content-type", exception);
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public void handleException(Exception exception) {
//        log.error("Unknown error", exception);
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.FORBIDDEN)
//    public void handleException(SecurityException exception) {
//        log.warn("Security error", exception);
//    }

//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
//    public void handleException(UsernameNotFoundException exception) {
//        log.warn("Unauthorized", exception);
//    }
}
