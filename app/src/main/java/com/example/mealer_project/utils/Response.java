package com.example.mealer_project.utils;

public class Response {

    boolean isError;
    boolean isSuccess;
    String errorMessage;
    String successMessage;

    public Response(boolean isSuccess, boolean isError) {
        this.isSuccess = isSuccess;
        this.isError = isError;
    }

    public Response(boolean isSuccess, String message) {
        if (isSuccess) {
            this.isSuccess = isSuccess;
            this.successMessage = message;
        } else {
            this.isError = !this.isSuccess;
            this.errorMessage = message;
        }
    }

    public boolean isError() {
        return isError;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

}
