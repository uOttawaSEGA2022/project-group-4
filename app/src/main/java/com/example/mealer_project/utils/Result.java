package com.example.mealer_project.utils;

import androidx.annotation.Nullable;

public class Result<S, E> {

    S onSuccessObject;
    E onErrorObject;

    public Result(@Nullable S onSuccessObject, @Nullable E onErrorObject) {
        this.onSuccessObject = onSuccessObject;
        this.onErrorObject = onErrorObject;
    }

    public S getSuccessObject() {
        return onSuccessObject;
    }

    public E getErrorObject() {
        return onErrorObject;
    }

    public boolean isError() {
        return this.onErrorObject != null;
    }

    public boolean isSuccess() {
        return this.onSuccessObject != null;
    }

}
