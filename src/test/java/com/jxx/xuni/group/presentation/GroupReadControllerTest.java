package com.jxx.xuni.group.presentation;

import com.jxx.xuni.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_ALL_READ;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(GroupControllerTestConfig.class)
class GroupReadControllerTest extends ControllerTest {

    @DisplayName("스터디 그룹 조회 성공 요청")
    @Test
    void read_all_group() throws Exception {
        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_ALL_READ));
    }
}