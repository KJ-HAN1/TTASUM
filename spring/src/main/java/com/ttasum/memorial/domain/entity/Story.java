package com.ttasum.memorial.domain.entity;

import com.ttasum.memorial.domain.Board;
import lombok.AccessLevel;
import lombok.Getter;

// 게시글 공통 조상
@Getter
public abstract class Story {
    public abstract Integer getId();
    public abstract Board getBoardType();
    public abstract String getContents();
}
