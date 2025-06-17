package com.ttasum.memorial.domain.entity;

import lombok.Getter;

// 댓글 공통 조상
@Getter
public abstract class Comment extends Contents {
    public abstract Integer getCommentSeq();
    public abstract String getContents();
}
