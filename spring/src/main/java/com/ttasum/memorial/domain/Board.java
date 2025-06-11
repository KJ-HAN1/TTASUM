package com.ttasum.memorial.domain;

import lombok.Getter;

@Getter
public enum Board {
    DONATION("donation"),
    HEAVEN("heaven"),
    RECIPIENT("recipient"),
    NOTICE("notice"),
    REMEMBRANCE("remember"),;

    private final String type;

    Board(String type) {
        this.type = type;
    }
}
