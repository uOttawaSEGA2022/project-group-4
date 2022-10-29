package com.example.mealer_project.utils;

import androidx.annotation.Nullable;

/**
 * Utility class for returning and handling result of an operation
 * @param <S> Success object: Object returned when the operation is success
 * @param <E> Error object: Object returned when the operation is failed
 */
public class Result<S, E> {

    S onSuccessObject;
    E onErrorObject;

    /**
     * Utility class for returning and handling result of an operation
     * @param onSuccessObject Object returned when the operation is success
     * @param onErrorObject Object returned when the operation is failed
     */
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

    static public final Result get(Object sObj, Object eObj){
        return new Result(sObj, eObj);
    }

}
