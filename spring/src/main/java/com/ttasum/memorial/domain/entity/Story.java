package com.ttasum.memorial.domain.entity;

import com.ttasum.memorial.domain.Board;
import lombok.AccessLevel;
import lombok.Getter;


@Getter
public abstract class Story {
    public abstract Board getBoardType();
    public abstract String getContents();
}
