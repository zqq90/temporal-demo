package org.febit.demo.temporal.api;

import org.febit.demo.temporal.BaseMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonApiTest extends BaseMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void basic() throws Exception {
        mockMvc.perform(get("/api/v1/common/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("pong"))
                .andDo(print());
    }
}
