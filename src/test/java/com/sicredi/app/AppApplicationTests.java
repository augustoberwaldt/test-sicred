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
        this.restTemplate.getForObject( uri + "/" + cpf, String.class).contains( "status");
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


    /**
     * Testa update com start (inicio da votacao) da pauta
     *
     * @throws Exception
     */
    @Test
    public void updateScheduleStartSchedule() throws Exception {

       /*
        curl -X PUT \
          http://localhost:8080/v1/schedule/updateSchedule \
          -H 'Content-Type: application/json' \
          -H 'Postman-Token: 5f43d233-9e57-45f8-8ec0-084aced23768' \
          -H 'cache-control: no-cache' \
          -d '{"id":1,"title":null,"description":null,"dtStarted":"2019-08-08 19:44","dtFinish":null,"time":60,"timeend":"60","timestart":"60"}'
        */

        String uri = "/v1/schedule/updateSchedule";
        Schedule schedule = new Schedule();
        schedule.setId((long) 1);
        schedule.setDtStarted(new Date());
        schedule.setTime(60);
        schedule.setTimeend("60");
        schedule.setTimestart("60");

        String inputJson = super.mapToJson(schedule);
        System.out.println(inputJson);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "{\"status\":\"success\"}");

    }

    /**
     * Testa update com finalizacao da pauta
     *
     * @throws Exception
     */
    @Test
    public void updateScheduleEndchedule() throws Exception {
        String uri = "/v1/schedule/updateSchedule";
        Schedule schedule = new Schedule();
        schedule.setId((long) 1);
        schedule.setDtFinish(new Date());
        schedule.setTime(60);
        schedule.setTimeend("60");
        schedule.setTimestart("60");

        String inputJson = super.mapToJson(schedule);
        System.out.println(inputJson);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "{\"status\":\"success\"}");

    }


}
