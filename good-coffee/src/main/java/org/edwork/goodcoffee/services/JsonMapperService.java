package org.edwork.goodcoffee.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;

public class JsonMapperService implements MapperService {

    private final ObjectMapper objectMapper;

    @Inject
    public JsonMapperService(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public String toString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T fromString(Class<T> clazz, String object) throws JsonProcessingException {
        return objectMapper.readValue(object, clazz);
    }
}
