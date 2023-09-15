package com.review.resultModel;

import lombok.Data;

@Data
public class ResultError implements Result{

    private String errorMessage;
    private int statusCode;

    public ResultError(String errorMessage, int statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }




}
