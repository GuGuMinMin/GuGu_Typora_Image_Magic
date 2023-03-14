package com.gugumin.tim.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * @author minmin
 * @date 2023/03/14
 */
public class ValidUtil {
    private ValidUtil() {
    }

    public static List<Path> checkPath(String[] pathStrArray) {
        if (pathStrArray.length < 1) {
            throw new RuntimeException("Please provide image path");
        }
        List<Path> pathList = new LinkedList<>();
        for (String pathStr : pathStrArray) {
            Path path = Path.of(pathStr);
            if (Files.notExists(path)) {
                throw new RuntimeException(path + " does not exist");
            }
            pathList.add(path);
        }
        return pathList;
    }
}
