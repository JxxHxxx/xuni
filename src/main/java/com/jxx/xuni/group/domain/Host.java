package com.jxx.xuni.group.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Host {
    private Long hostId;
    private String hostName;

    protected Host(Long hostId, String hostName) {
        this.hostId = hostId;
        this.hostName = hostName;
    }
}
