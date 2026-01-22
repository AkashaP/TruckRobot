package io.github.seantu.truckrobot.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TruckRobotController.class)
public class TruckRobotWebTest {

    @Autowired MockMvc mvc;

    @MockitoBean TruckRobotService service;

    @Test
    void postCommandService() throws Exception {
        when(service.execute("REPORT")).thenReturn("ROBOT MISSING");

        mvc.perform(post("/api/v1/command")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("REPORT"))
                .andExpect(status().isOk());

        verify(service).execute("REPORT");
        verifyNoMoreInteractions(service);
    }

    @Test
    void postMultipleCommandsService() throws Exception {
        when(service.execute("PLACE 0 0 NORTH,REPORT")).thenReturn("0,0,NORTH");

        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("PLACE 0 0 NORTH,REPORT"))
                .andExpect(status().isOk());

        verify(service).executeAll("PLACE 0 0 NORTH,REPORT");
        verifyNoMoreInteractions(service);
    }

    @Test
    void postJSONCommandService() throws Exception {
        when(service.execute("REPORT")).thenReturn("{\"output\":\"ROBOT MISSING\"}");

        mvc.perform(post("/api/v1/command")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"command\": \"REPORT\"}"))
                .andExpect(status().isOk());

        verify(service).execute("REPORT");
        verifyNoMoreInteractions(service);
    }

    @Test
    void postJSONMultipleCommandsService() throws Exception {
        when(service.executeAll(List.of("PLACE 0 0 NORTH", "REPORT")))
                .thenReturn(List.of("0,0,NORTH").toString());

        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"commands\": [\"PLACE 0 0 NORTH\", \"REPORT\"] }"))
                .andExpect(status().isOk());

        verify(service).executeAll(List.of("PLACE 0 0 NORTH", "REPORT"));
        verifyNoMoreInteractions(service);
    }

    @Test
    void postInvalidJSONService() throws Exception {
        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"commands\": [ \"REPORT\" ")) // malformed JSON, no ending brackets
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }


}
