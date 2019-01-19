package com.onkar.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

public class NumberFormatter {

    public static String formateNumberAndAppendRupee(final BigDecimal amount) {
        DecimalFormat decimalFormat= new DecimalFormat("#,##0");
        return "Rs. "+decimalFormat.format(amount);
    }
}
