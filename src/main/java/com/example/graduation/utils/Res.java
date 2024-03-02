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
    public Res(Integer statusCode) {
        this.statusCode = statusCode;
        this.data = null;
    }

    public Res(Integer statusCode, T data) {
        this.data = data;
        this.statusCode = statusCode;
    }


    // 为了模拟Dart中的null安全类型，可以使用Optional包装data和statusCode
//    public Optional<T> getData() {
//        return Optional.ofNullable(data);
//    }
//
//    public Optional<Integer> getStatusCode() {
//        return Optional.ofNullable(statusCode);
//    }
    public static <T> Res<T> Success(T data) {
        return new Res<>(data);
    }
    public static <T> Res<T> Success(Integer statusCode, T data) {
        return new Res<>(statusCode, data);
    }
    public static <T> Res<T> Error(Integer statusCode) {
        return new Res<>(statusCode);
    }

    public boolean isError(){return statusCode != status.success;}
    public boolean isSuccess(){return statusCode == status.success;}
}

