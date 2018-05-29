package com.higginss.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(String exception) {
        super(exception);
    }
}
