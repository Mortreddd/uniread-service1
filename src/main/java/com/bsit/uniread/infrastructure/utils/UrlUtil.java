package com.bsit.uniread.infrastructure.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class UrlUtil {


    private static UrlUtil instance;

    private UrlUtil() {}

    public static synchronized UrlUtil getInstance() {
        if(instance == null) {
            instance = new UrlUtil();
        }

        return instance;
    }

    public String getBaseUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();
    }

}
