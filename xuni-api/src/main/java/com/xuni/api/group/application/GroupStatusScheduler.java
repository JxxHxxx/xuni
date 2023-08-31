package com.xuni.api.group.application;

import com.xuni.group.domain.GroupStatusHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** 2023-05-11 BREAKING CHANGE : 그룹 스케줄러 책임 변경
 *  그룹 상태에 스케줄러 관리는 이제 xuni-lambda 프로젝트에서 책임집니다.
 *  https://github.com/JxxHxxx/xuni-lambda
 */

@Deprecated
@Component
@RequiredArgsConstructor
public class GroupStatusScheduler {

    private final GroupStatusHandler groupStatusHandler;

//    @Scheduled(cron = "0 0 0 * * *")
    public void doChangeToStart() {
        groupStatusHandler.changeToStart();
    }

//    @Scheduled(cron = "5 0 0 * * *")
    public void doChangeToEnd() {
        groupStatusHandler.changeToEnd();
    }
}
