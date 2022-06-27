package com.chenxuan.hook.sample;

public class SampleHelper {
    public static void handle() {
        new SampleImpl().something();
    }
}