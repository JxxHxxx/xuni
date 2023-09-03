package com.xuni.core.common.domain;

public enum Category {
    JAVA, JAVASCRIPT, REACT, JPA, SPRING_FRAMEWORK, TEST, NETWORK, AWS, MYSQL, REST, ETC, CSS, STATE_MANAGEMENT;

    public boolean isNotEmpty() {
        return !this.equals("");
    }
}