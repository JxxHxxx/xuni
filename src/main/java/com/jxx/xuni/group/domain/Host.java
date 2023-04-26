package com.jxx.xuni.group.domain;

import com.jxx.xuni.common.exception.NotPermissionException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.NOT_PERMISSION;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Host {
    private Long hostId;
    private String hostName;

    public Host(Long hostId, String hostName) {
        this.hostId = hostId;
        this.hostName = hostName;
    }

    protected boolean isNotHost(Long memberId) {
        return !this.hostId.equals(memberId);
    }

    protected boolean isHost(Long memberId) {
        return this.hostId.equals(memberId);
    }
}
