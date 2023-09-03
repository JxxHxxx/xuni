package com.xuni.api.group.presentation;

import com.xuni.api.group.application.GroupCreateService;
import com.xuni.api.group.application.GroupJoinFacade;
import com.xuni.api.group.application.GroupManagingService;
import com.xuni.api.group.application.GroupReadService;
import com.xuni.api.group.infra.GroupRepository;
import com.xuni.api.group.application.SimpleHostCreator;
import com.xuni.core.common.query.PageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@Slf4j
@TestConfiguration
public class GroupControllerTestConfig {
    @MockBean
    GroupRepository groupRepository;
    @MockBean
    SimpleHostCreator simpleHostCreator;
    @MockBean
    GroupCreateService groupCreateService;
    @MockBean
    GroupManagingService groupManagingService;
    @MockBean
    GroupReadService groupReadService;
    @MockBean
    GroupJoinFacade groupJoinFacade;
    @MockBean
    PageConverter pageConverter;

}
