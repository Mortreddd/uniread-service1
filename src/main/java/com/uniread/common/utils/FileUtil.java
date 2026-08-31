package com.uniread.common.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String getExtension(MultipartFile file) {
        if(file == null) return "";

        String name = file.getOriginalFilename();
        if (name == null || !name.contains(".")) {
            return "";
        }
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
