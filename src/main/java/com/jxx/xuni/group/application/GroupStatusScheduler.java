package com.jxx.xuni.group.application;

import com.jxx.xuni.group.domain.GroupStatusHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupStatusScheduler {

    private final GroupStatusHandler groupStatusHandler;

    @Scheduled(cron = "0 0 0 * * *")
    public void doChangeToStart() {
        groupStatusHandler.changeToStart();
    }

    @Scheduled(cron = "5 0 0 * * *")
    public void doChangeToEnd() {
        groupStatusHandler.changeToEnd();
    }
}
