package com.github.commandercool.cloudsurfer.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T unmarshall(String json, Class<T> cls) throws Exception {
        return mapper.readValue(json, cls);
    }

    public static <T> String marshall(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

}
