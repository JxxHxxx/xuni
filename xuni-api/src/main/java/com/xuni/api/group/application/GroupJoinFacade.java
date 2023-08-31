package com.xuni.api.group.application;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.group.domain.exception.GroupJoinException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupJoinFacade {

    private final GroupManagingService groupManagingService;

    public void join(MemberDetails memberDetails, Long groupId) {
        while (true) {
            try {
                groupManagingService.join(memberDetails, groupId);
                break;
            }
            catch (GroupJoinException exception) {
                log.info("join fail because = {}", exception.getMessage());
                throw exception;
            }

            catch (ObjectOptimisticLockingFailureException exception) {
                try {
                    log.info("race condition!!!! retry");
                    Thread.sleep(50);
                }

                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
