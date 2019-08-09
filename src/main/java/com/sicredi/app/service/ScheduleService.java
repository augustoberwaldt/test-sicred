package com.sicredi.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.app.config.QueueSender;
import com.sicredi.app.entity.Schedule;
import com.sicredi.app.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Clase Responsavel  pelas regras de negocio das  Pauta
 * @author  Augusto Berwaldt de Oliveira
 */
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private  final QueueSender queueSender;

    public ScheduleService(ScheduleRepository scheduleRepository, QueueSender queueSender) {
        this.scheduleRepository = scheduleRepository;
        this.queueSender =  queueSender;
    }


    /**
     * save
     * @param Schedule  schedule
     */
    public void save(Schedule schedule) {

        this.scheduleRepository.save(schedule);
    }

    public List<Schedule> getAll()   {
        return this.scheduleRepository.findAll();
    }

    /**
     * Busca uma pauta pelo id
     *
     * @param id
     * @return
     */
    public Schedule get(Long id) {
        return this.scheduleRepository.findById(id).get();
    }

    /**
     * Calcula diferenca entre duas  datas
     *
     * @param Date a
     * @param Date b
     */
    public int daysBetween(Date a, Date b) {
        Calendar dInicial = Calendar.getInstance();
        dInicial.setTime(a); Calendar dFinal = Calendar.getInstance(); dFinal.setTime(b);
        int count = 0;
        while (dInicial.get(Calendar.DAY_OF_MONTH) != dFinal.get(Calendar.DAY_OF_MONTH)){
            dInicial.add(Calendar.DAY_OF_MONTH, 1);
            count ++;
        }

        return count;
    }

    /**
     * Metodo responsavel por
     *
     * @param data
     * @return
     */
    public Schedule parser(Map<String, Object> data) {

        Long scheduleId =  Long.parseLong(data.get("id").toString());
        Long timestart =  Long.parseLong(data.get("timestart").toString());
        Long timeend =  Long.parseLong(data.get("timeend").toString());

        boolean dtFinish = false;

        if (data.get("dtFinish") != null) {
            dtFinish = Boolean.parseBoolean(data.get("dtFinish").toString());
        }

        int time =  Integer.parseInt(data.get("time").toString());

        Schedule schedule = this.get(scheduleId);

        if (dtFinish) {
            Date dtEnd  = this.addMinutesToDate(timeend, new Date());
            schedule.setDtFinish(dtEnd);
        } else {
            Date dtStarted = this.addMinutesToDate(timestart, new Date());
            schedule.setDtStarted(dtStarted);
            schedule.setTime(time);

        }

        return  schedule;
    }

    /**
     * Adiciona minutos a uma data (Date)
     *
     * @param minutes
     * @param beforeTime
     * @return
     */
    private  Date addMinutesToDate(Long  minutes, Date beforeTime) {

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + minutes);
        return afterAddingMins;
    }

    /**
     * Update pauta na base, adiciona na fila  quando adicionar datafinish
     *
     * @param schedule
     */
    public void update(Schedule schedule) {

        if (schedule.getDtFinish() !=  null) {
            String send = this.mapToJson(schedule);
            if (!send.isEmpty()) {
                this.queueSender.send(this.mapToJson(schedule));
            }
        }

        this.save(schedule);

    }

    /**
     * Converte u objeto em json literal (string)
     *
     *
     * @param obj
     * @return
     */
    public String mapToJson(Object obj)  {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
