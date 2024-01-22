package com.example.graduation.utils;

import java.util.Optional;

public class result<T> {
    public T data;
    public Integer statusCode;

    // 构造器Success，使用可选参数并设置默认状态码
    public result(T data) {
        this.data = data;
        this.statusCode = status.success; // 假设Status类有一个静态变量SUCCESS
    }

    // 构造器Error，带有必填参数
    public result(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    // 为了模拟Dart中的null安全类型，可以使用Optional包装data和statusCode
//    public Optional<T> getData() {
//        return Optional.ofNullable(data);
//    }
//
//    public Optional<Integer> getStatusCode() {
//        return Optional.ofNullable(statusCode);
//    }
}

