package com.xuni.core.group.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return Objects.equals(hostId, host.hostId) && Objects.equals(hostName, host.hostName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostId, hostName);
    }
}
