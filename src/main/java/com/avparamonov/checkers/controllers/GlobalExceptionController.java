package com.avparamonov.checkers.controllers;

import com.avparamonov.checkers.exceptions.CheckerNotFoundException;
import com.avparamonov.checkers.exceptions.GameNotFoundException;
import com.avparamonov.checkers.exceptions.MoveNotAllowedException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ValidationException;

@ControllerAdvice
@Log4j
public class GlobalExceptionController {

    @MessageExceptionHandler(MoveNotAllowedException.class)
    @SendToUser("/queue/error")
    public String handleException(MoveNotAllowedException exception) {
        log.warn(exception.getMessage());
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(ValidationException exception) {
        log.warn("Validation failed: " + exception.getMessage());
        return createErrorModelAndView(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(CheckerNotFoundException exception) {
        log.warn(exception.getMessage());
        return createErrorModelAndView(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(PlayerNotFoundException exception) {
        log.warn(exception.getMessage());
        return createErrorModelAndView(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(GameNotFoundException exception) {
        log.warn(exception.getMessage());
        return createErrorModelAndView(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleException(HttpMessageNotReadableException exception) {
        log.warn("Wrong request data", exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleException(HttpMediaTypeNotSupportedException exception) {
        log.warn("Wrong request content-type", exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleException(MethodArgumentNotValidException exception) {
        log.warn("Not valid incoming data: " + exception.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception exception) {
        log.error("Unknown error", exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public void handleException(SecurityException exception) {
        log.warn("Security error", exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public void handleException(UsernameNotFoundException exception) {
        log.warn("Unauthorized", exception);
    }

    private ModelAndView createErrorModelAndView(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getLocalizedMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
