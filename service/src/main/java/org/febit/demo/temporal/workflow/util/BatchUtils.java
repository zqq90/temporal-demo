package org.febit.demo.temporal.workflow.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class BatchUtils {

    public static final DateTimeFormatter FMT_MIN = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

    public static String batchIdInMin() {
        var now = LocalDateTime.now();
        return FMT_MIN.format(now);
    }

    public static long batchNumberInMin() {
        var ms = System.currentTimeMillis();
        return ms / 1000 / 60;
    }
}
