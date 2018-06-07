package com.higginss.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * RESTful layer error handling handling application specific and system errors etc.
 * 
 * @author higginss
 */
@ControllerAdvice
public class RestErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleArticleNotFoundExceptions() {
        System.out.println("------------ Caught Article Not Found Exception-------------");
        //TODO - add in more details and handling here...
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleGlobalExceptions() {
        System.out.println("----------Caught System Exception-----------");
        LOGGER.debug("Handling an error (global)...");
        //TODO - add in more details and handling here...
    }

    //TODO - add in specific application exception handling cases ...
}
