package com.infamous.framework.converter;

public interface ObjectMapper {

    <T> T readValue(String value, Class<T> valueType) throws RuntimeException;

    String writeValue(Object value) throws RuntimeException;
}
