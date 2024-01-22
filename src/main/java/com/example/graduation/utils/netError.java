package com.example.graduation.utils;

class netError<T> {
    public final int status;
    public final String message;
    public final T data;

    public netError(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

//    public int getStatus() {
//        return status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    // 使用Optional来模拟Dart中data可能为null的情况
//    public Optional<T> getData() {
//        return Optional.ofNullable(data);
//    }
}