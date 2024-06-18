package com.verygoodbank.tes.web.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class DateUtilTest {
    @Test
    void whenDateValidThenReturnTrue() {
        Assertions.assertTrue(DateUtil.isValidDate("20160101"));
    }

    @Test
    void whenDateInvalidThenReturnFalse() {
        Assertions.assertFalse(DateUtil.isValidDate("20169901"));
    }
}