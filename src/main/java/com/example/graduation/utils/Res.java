package com.example.graduation.utils;

import lombok.Data;

@Data
public class Res<T> {
    public T data;
    public Integer statusCode;
    public String message;
    public Boolean isSuccess;

    /**
     * @desc 构造器Success
     * @param data
     */
    public Res(T data) {
        this.data = data;
        this.statusCode = status.success;
        this.isSuccess = true;
    }

    /**
     * @desc 构造器Error，带有必填参数
     */
    public Res(String message,Boolean isSuccess) {
        this.statusCode = status.error;
        this.data = null;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public Res(Integer statusCode,Boolean isSuccess) {
        this.statusCode = statusCode;
        this.isSuccess = isSuccess;
    }


    public static <T> Res<T> Success(T data) {
        return new Res<>(data);
    }
    public static <T> Res<T> SuccessBut(Integer statusCode) {
        return new Res<>(statusCode,true);
    }
    public static <T> Res<T> Error(String message) {
        return new Res<>(message,false);
    }

    public boolean isError(){return !this.isSuccess;}
    public boolean isSuccess(){return this.isSuccess;}
}

