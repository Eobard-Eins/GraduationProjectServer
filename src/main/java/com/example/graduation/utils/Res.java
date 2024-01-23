package com.example.graduation.utils;

public class Res<T> {
    public T data;
    public Integer statusCode;


    /**
     * @desc 构造器Success
     * @param data
     */
    public Res(T data) {
        this.data = data;
        this.statusCode = status.success;
    }

    /**
     * @desc 构造器Error，带有必填参数
     * @param statusCode
     */
    public Res(int statusCode) {
        this.statusCode = statusCode;
        this.data = null;
    }


    // 为了模拟Dart中的null安全类型，可以使用Optional包装data和statusCode
//    public Optional<T> getData() {
//        return Optional.ofNullable(data);
//    }
//
//    public Optional<Integer> getStatusCode() {
//        return Optional.ofNullable(statusCode);
//    }
    public static <T> Res<T> Sucess(T data) {
        return new Res<>(data);
    }
    public static <T> Res<T> Error(int statusCode) {
        return new Res<>(statusCode);
    }
}

