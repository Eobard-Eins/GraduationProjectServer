package com.example.graduation.utils;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomCodeUtils {

    /**
     * 随机验证码
     * @param length int
     * @return String
     */
    public static String createSmsCode(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, length).forEach(i -> builder.append(random.nextInt(10)));
        return builder.toString();
    }
}