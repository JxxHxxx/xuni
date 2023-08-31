package com.xuni.group.domain.exception;

public class GroupExceptionMessage {

    private GroupExceptionMessage() {

    }

    public static final String INCORRECT_PERIOD = "시작 일이 종료 일보다 늦을 수 없습니다";
    public static final String INCORRECT_TIME = "시작 시간이 종료 시간보다 늦을 수 없습니다";
}
