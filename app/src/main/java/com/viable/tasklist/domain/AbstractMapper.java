package com.viable.tasklist.domain;

public interface AbstractMapper<FROM, TO> {
    TO map(FROM var1);
}
