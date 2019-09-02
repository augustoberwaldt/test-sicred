package com.sicredi.app;
import com.sicredi.app.entity.Schedule;
import com.sicredi.app.enumerable.ExternalApiEnum;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class AppApplicationTests  extends AbstractTest {

    @LocalServerPort
    private String port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * Testa se api esta respondendo
     *
     * @throws Exception
     */
    @Test
    public void testConnectionServerApi() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/ping",
                String.class)).contains("pong");
    }

    /**
     * Testa se api no Heroku ta respondendo
     *
     * @throws Exception
     */
    @Test
    public void testConnectionUserInfo() {
        String cpf = "01610383010";
        String uri = ExternalApiEnum.VALIDATE_CPF_URI.getValue();
        boolean ret = (boolean) this.restTemplate.getForObject( uri + "/" + cpf, String.class).contains( "status");
        assertTrue(ret==true);
    }

    /**
     *  Testa criacao  de pauta
     *
     * @throws Exception
     */
    @Test
    public void createSchedule() throws Exception {
        String uri = "/v1/schedule/createSchedule";
        Schedule schedule = new Schedule();
        schedule.setTitle("Teste criação  de pauto");
        schedule.setDescription("Essa pauta tem por objtivo testar a inclusao");
        String inputJson = super.mapToJson(schedule);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "{\"status\":\"success\"}");
    }

    /**
     * Busca lista de pautas
     *
     * @throws Exception
     */
    @Test
    public void getSchedulesList() throws Exception {
        String uri = "/v1/schedule/getSchedules";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Schedule[] scheduleList = super.mapFromJson(content, Schedule[].class);
        assertTrue(scheduleList.length > 0);
    }

}
