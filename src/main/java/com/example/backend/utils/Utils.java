package com.example.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String encodeBase64Str(String json) {
        return Base64.getEncoder().encodeToString(json.getBytes());
    }

    public static <T> T map(String encodedStr, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        String decodedStr = decodeBase64Str(encodedStr);
        try {
            return mapper.readValue(decodedStr, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeBase64Str(String base64) {
        return new String(Base64.getDecoder().decode(base64));
    }

    public static String stringifyJson(Map json) {
        return new JSONObject(json).toString();
    }

    public static String stringifyJson(Object object) {
        return new JSONObject(object).toString();
    }

    public static String createBase64Image(byte[] bytes) {
        return "data:image/jpeg;base64," + toBase64(bytes);
    }

    public static String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static void createDirectory(String path) {
        Path directory = Path.of(path);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectory(directory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T map(Class<T> clazz, String object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(object, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T map(Class<T> clazz, Object object) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(object, clazz);
    }

    public static String buildUriWithParams(String uri, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder(uri);
        if (params != null && !params.isEmpty()) {
            builder.append("?");
            params.forEach((key, value) -> builder.append("&").append(key).append("=").append(value));
            return builder.toString();
        }
        return null;
    }

    public static <T> T getObjectFromEncodedStr(String encodedStr, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        String decodedStr = decodeBase64Str(encodedStr);
        try {
            return mapper.readValue(decodedStr, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getObjectFromStr(String str, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> getObjectListFromStr(String str, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(str, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
