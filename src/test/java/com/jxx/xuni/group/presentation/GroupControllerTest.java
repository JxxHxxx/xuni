package com.jxx.xuni.group.presentation;

import com.jxx.xuni.ControllerTest;
import com.jxx.xuni.group.dto.request.GroupCreateForm;
import com.jxx.xuni.group.dto.response.GroupCreateMessage;
import com.jxx.xuni.member.domain.LoginInfo;
import com.jxx.xuni.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.jxx.xuni.subject.domain.Category.JAVA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GroupControllerTestConfig.class)
public class GroupControllerTest extends ControllerTest {

    Long memberId = null;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder()
                .id(1L)
                .loginInfo(LoginInfo.of("leesin5498@naver.com", "1234"))
                .name("자몽")
                .build();

        memberId = member.getId();
    }

    @Test
    void create_success() throws Exception {
        //given
        GroupCreateForm groupCreateForm = makeGroupCreateForm(5);
        //when
        mockMvc.perform(post("/groups", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupCreateForm)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(GroupCreateMessage.SUCCESS_MESSAGE));
    }

    @DisplayName("스터디 그룹의 인원의 범위는 1 이상 5 이하이다. 그 외의 값을 입력할 경우 " +
            "컨트롤러에서는 " +
            "400 상태 코드 " +
            "{FAIL_MESSAGE} 메시지를 반환한다.")
    @Test
    void create_fail1() throws Exception {
        //given
        GroupCreateForm groupCreateForm = makeGroupCreateForm(-1);
        //when - then
        String requestBody = objectMapper.writeValueAsString(groupCreateForm);
        mockMvc.perform(post("/groups", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.message").value(GroupCreateMessage.FAIL_MESSAGE));
    }

    private GroupCreateForm makeGroupCreateForm(Integer capacity) {
        return new GroupCreateForm(LocalDate.now(), LocalDate.of(2023, 7, 15),
                LocalTime.NOON, LocalTime.MIDNIGHT, capacity, "자바의 정석", JAVA);
    }
}
