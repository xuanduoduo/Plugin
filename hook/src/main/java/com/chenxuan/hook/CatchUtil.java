package com.chenxuan.hook;

import java.util.Date;

public class CatchUtil {
    private void handle() {
        try {
            Date date = new Date();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
