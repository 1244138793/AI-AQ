package com.aiaq.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/10/12 14:49
 * @Description json工具类
 */
public class JsonUtil {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static <T> T toObj(String str, Class<T> clz) {
        try {
            return jsonMapper.readValue(str, clz);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> String toStr(T t) {
        try {
            return jsonMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static SimpleModule bigIntToStrSimpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, newSerializer(String::valueOf));
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(Long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(BigDecimal.class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigDecimal[].class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger[].class, newSerializer((Function<BigInteger, String>) BigInteger::toString));
        return simpleModule;
    }

    public static <T, K> JsonSerializer<T> newSerializer(Function<K, String> func) {
        return new JsonSerializer<T>() {
            @Override
            public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                if (t == null) {
                    jsonGenerator.writeNull();
                    return;
                }

                if (t.getClass().isArray()) {
                    jsonGenerator.writeStartArray();
                    Stream.of(t).forEach(s -> {
                        try {
                            jsonGenerator.writeString(func.apply((K) s));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    jsonGenerator.writeEndArray();
                } else {
                    jsonGenerator.writeString(func.apply((K) t));
                }
            }
        };
    }
}
