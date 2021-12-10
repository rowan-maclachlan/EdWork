package org.edwork.goodcoffee.services;

public interface MapperService {

    String toString(Object object) throws Exception;

    <T> T fromString(Class<T> clazz, String object) throws Exception;
}
