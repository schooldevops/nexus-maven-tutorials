package com.schooldevops.libdemo.services;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
public class StringUtil {

    public static String toUpper(String inputValue) {

        String convertedString = inputValue.toUpperCase(Locale.ROOT);

        log.info("toUpperCase: from {} to {}", inputValue, convertedString );
        return convertedString;
    }

    public static String toLower(String inputValue) {

        String convertedString = inputValue.toLowerCase(Locale.ROOT);
        log.info("toLowerCase: from {} to {}", inputValue, convertedString );
        return convertedString;
    }

}
