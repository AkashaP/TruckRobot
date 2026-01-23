package io.github.seantu.truckrobot.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TruckRobotController.class)
class TruckRobotWebTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    TruckRobotService service;

    /* -----------------------------
     * /api/v1/command — TEXT
     * ----------------------------- */

    @Test
    void postTextCommand_returnsPlainTextOutput() throws Exception {
        when(service.execute("REPORT")).thenReturn("ROBOT MISSING");

        mvc.perform(post("/api/v1/command")
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.TEXT_PLAIN)
                        .content("REPORT"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("ROBOT MISSING"));

        verify(service).execute("REPORT");
        verifyNoMoreInteractions(service);
    }

    /* -----------------------------
     * /api/v1/command — JSON
     * ----------------------------- */

    @Test
    void postJsonCommand_returnsWrappedJsonOutput() throws Exception {
        when(service.execute("REPORT")).thenReturn("ROBOT MISSING");

        mvc.perform(post("/api/v1/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "command": "REPORT" }
                                 """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.output").value("ROBOT MISSING"));

        verify(service).execute("REPORT");
        verifyNoMoreInteractions(service);
    }

    @Test
    void postJsonCommand_withMalformedJson_returns400_andDoesNotInvokeService() throws Exception {
        mvc.perform(post("/api/v1/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"command\": REPORT }")) // invalid JSON
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void postJsonCommand_withInvalidCommandFormat_returns400() throws Exception {
        mvc.perform(post("/api/v1/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "command": "PLACE A,3,WEST" }
                                 """))
                .andExpect(status().isBadRequest());
    }

    /* -----------------------------
     * /api/v1/commands — TEXT
     * ----------------------------- */

    @Test
    void postTextBatchCommands_returnsFinalReportOutput() throws Exception {
        when(service.executeAll("PLACE 0,0,NORTH;MOVE;REPORT"))
                .thenReturn("0,1,NORTH");

        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.TEXT_PLAIN)
                        .content("PLACE 0,0,NORTH;MOVE;REPORT"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("0,1,NORTH"));

        verify(service).executeAll("PLACE 0,0,NORTH;MOVE;REPORT");
        verifyNoMoreInteractions(service);
    }

    /* -----------------------------
     * /api/v1/commands — JSON
     * ----------------------------- */

    @Test
    void postJsonBatchCommands_returnsWrappedJsonOutput() throws Exception {
        when(service.executeAll(List.of("PLACE 0,0,NORTH", "REPORT")))
                .thenReturn("0,0,NORTH");

        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                   "commands": [
                                     "PLACE 0,0,NORTH",
                                     "REPORT"
                                   ]
                                 }
                                 """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.output").value("0,0,NORTH"));

        verify(service).executeAll(List.of("PLACE 0,0,NORTH", "REPORT"));
        verifyNoMoreInteractions(service);
    }

    @Test
    void postJsonBatchCommands_withMalformedJson_returns400() throws Exception {
        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "commands": [ "REPORT" }
                                 """)) // missing closing ]
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void postJsonBatchCommands_withInvalidCommand_returns400() throws Exception {
        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                   "commands": [
                                     "PLACE 4,3,3,WEST",
                                     "REPORT"
                                   ]
                                 }
                                 """))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                   "commands": [
                                     "PLACE 4,3,",
                                     "REPORT"
                                   ]
                                 }
                                 """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postJsonBatchCommands_withUnknownCommands_returns400() throws Exception {
        mvc.perform(post("/api/v1/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                   "commands": [
                                     "HELLO",
                                     "WORLD"
                                   ]
                                 }
                                 """))
                .andExpect(status().isBadRequest());
    }
}
