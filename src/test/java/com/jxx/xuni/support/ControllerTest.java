package com.jxx.xuni.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxx.xuni.group.presentation.GroupCreateController;
import com.jxx.xuni.group.presentation.GroupManagingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({GroupCreateController.class, GroupManagingController.class})
public class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
}
