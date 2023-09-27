package com.jiedui.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyp 2023/09/27
 */
public class FileUtils {
    /**
     * 读取文件中的内容
     * @param fileName 文件名
     * @return {@link List}<{@link String}> 文件内容
     * @throws IOException 文件打开异常
     */
    public static List<String> readFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }
}
