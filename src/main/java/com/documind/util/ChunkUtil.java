package com.documind.util;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtil {

    public static List<String> split(String text, int size) {
        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < text.length(); i += size) {
            chunks.add(text.substring(i, Math.min(text.length(), i + size)));
        }

        return chunks;
    }
}