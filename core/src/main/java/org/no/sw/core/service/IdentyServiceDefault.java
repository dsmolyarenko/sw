package org.no.sw.core.service;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class IdentyServiceDefault implements IdentyService {

    private final Random random = new Random(System.currentTimeMillis());
    
    @Override
    public String getRootId() {
        return digits(0, 12);
    }

    @Override
    public String getId() {
        return digits(random.nextLong(), 12);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
