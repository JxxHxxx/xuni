package com.jxx.xuni.group.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 2023-05-11 BREAKING CHANGE : 그룹 스케줄러 책임 변경
 *  그룹 상태에 스케줄러 관리는 이제 xuni-lambda 프로젝트에서 책임집니다.
 *  https://github.com/JxxHxxx/xuni-lambda
 */

@Deprecated
@Service
@RequiredArgsConstructor
public class GroupStatusHandler {

    private final GroupRepository groupRepository;

    @Transactional
    public void changeToStart() {
        groupRepository.updateGroupStatusToStart();
    }

    @Transactional
    public void changeToEnd() {
        groupRepository.updateGroupStatusToEnd();
    }
}
