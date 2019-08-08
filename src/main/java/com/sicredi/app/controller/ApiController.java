package com.sicredi.app.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sicredi.app.service.AssociateService;
import com.sicredi.app.service.VoteService;
import com.sicredi.app.config.Translator;
import com.sicredi.app.entity.Associate;
import com.sicredi.app.entity.Schedule;
import com.sicredi.app.entity.Vote;
import com.sicredi.app.service.ScheduleService;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ApiController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    private final ScheduleService scheduleService;

    private final AssociateService associateService;

    private final VoteService voteService;

    public ApiController(ScheduleService scheduleService, AssociateService  associateService,  VoteService voteService) {
        this.scheduleService = scheduleService;
        this.associateService = associateService;
        this.voteService = voteService;
    }


    @PostMapping(path = "/schedule/createSchedule",  produces = "application/json",  consumes = "application/json")
    public  ResponseEntity<JsonNode>   createSchedule(@RequestBody Schedule schedule) {


        if (logger.isDebugEnabled()) {
            logger.info("Requisição de entrada createSchedule :" + schedule.toString());
        }

        JsonNode reponse = JsonNodeFactory.instance.objectNode();

        try {

            this.scheduleService.save(schedule);
            ((ObjectNode) reponse).put("status", Translator.toLocale("ok"));

        } catch (Exception e) {
            ((ObjectNode) reponse).put("status", Translator.toLocale("error"));
            ((ObjectNode) reponse).put("msg", e.getMessage());
            return ResponseEntity.badRequest().body(reponse);
        }

        return ResponseEntity.ok(reponse);
    }

    @GetMapping(path = "/schedule/getSchedules",  produces = "application/json")
    public List<Schedule> getSchedules() {
        return  this.scheduleService.getAll();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "snapshot",
                    dataType = "DesktopSnapshot",
                    value = "{'snapshot'：{'type': 'AAA'}}",
                    examples = @io.swagger.annotations.Example(
                            value = {
                                    @ExampleProperty(value = "{'snapshot'：{'type': 'AAA'}}", mediaType = "application/json")
                            }))
    })

    @PutMapping(path = "/schedule/updateSchedule",  produces = "application/json",  consumes = "application/json")
    public  ResponseEntity<JsonNode>   updateSchedule(@RequestBody Map<String, Object> payload) {

        if (logger.isDebugEnabled()) {
            logger.info("Requisição de entrada updateSchedule :" + payload.toString());
        }

        JsonNode reponse = JsonNodeFactory.instance.objectNode();

        try {

            Schedule schedule = this.scheduleService.parser(payload);
            this.scheduleService.update(schedule);
            ((ObjectNode) reponse).put("status", Translator.toLocale("ok"));
        } catch (Exception e) {
            ((ObjectNode) reponse).put("status", Translator.toLocale("error"));
            ((ObjectNode) reponse).put("msg", e.getMessage());
            return ResponseEntity.badRequest().body(reponse);
        }

        return ResponseEntity.ok(reponse);
    }

    @PostMapping( path = "/schedule/addVote",  produces = "application/json",  consumes = "application/json")
    public  ResponseEntity<JsonNode>   createSchedule(@RequestBody Map<String, Object> payload) {

        JsonNode reponse = JsonNodeFactory.instance.objectNode();

        try {

          this.voteService.validateData(payload);
          Vote vote = this.voteService.parser(payload);
          this.voteService.save(vote);
          ((ObjectNode) reponse).put("status", Translator.toLocale("ok"));
        } catch (Exception e) {
            ((ObjectNode) reponse).put("status", Translator.toLocale("error"));
            ((ObjectNode) reponse).put("msg", e.getMessage());
            return ResponseEntity.badRequest().body(reponse);
        }

        return ResponseEntity.ok(reponse);
    }


    @PostMapping(path = "/associate/createAssociate",  produces = "application/json",  consumes = "application/json")
    public  ResponseEntity<JsonNode>   createAssociate(@RequestBody Associate associate) {

        if (logger.isDebugEnabled()) {
            logger.info("Requisição de entrada  createAssociate :" + associate.toString());
        }

        JsonNode reponse = JsonNodeFactory.instance.objectNode();

        try {

            if (this.associateService.getByCpf(associate.getCpf()) != null) {
                throw new IllegalArgumentException(Translator.toLocale(Translator.toLocale("cpf_exist")));
            }

            this.associateService.save(associate);
            ((ObjectNode) reponse).put("status", Translator.toLocale("ok"));
        } catch (Exception e) {
            ((ObjectNode) reponse).put("status", Translator.toLocale("error"));
            ((ObjectNode) reponse).put("msg", e.getMessage());

            return ResponseEntity.badRequest().body(reponse);
        }

        return ResponseEntity.ok(reponse);
    }

}
