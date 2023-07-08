package com.viable.tasklist.domain.mapper;

public interface AbstractMapper<FROM, TO> {
    TO map(FROM var1);
}
