package com.sicredi.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.app.config.Translator;
import com.sicredi.app.entity.Associate;
import com.sicredi.app.entity.Schedule;
import com.sicredi.app.entity.Vote;
import com.sicredi.app.enumerable.ExternalApiEnum;
import com.sicredi.app.exception.BusinessException;
import com.sicredi.app.repository.AssociateRepository;
import com.sicredi.app.repository.ScheduleRepository;
import com.sicredi.app.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Clase Responsavel  pelas regras de negocio dos votos da Pauta
 * @author  Augusto Berwaldt de Oliveira
 */
@Service
public class VoteService {

    private final int HTTP_CONNECT_TIMEOUT = 15000;

    private final int HTTP_READ_TIMEOUT = 10000;

    private final VoteRepository voteRepository;

    private final ScheduleRepository  scheduleRepository;

    private final AssociateRepository  associateRepository;

    public VoteService(VoteRepository voteRepository, ScheduleRepository  scheduleRepository, AssociateRepository  associateRepository) {
        this.voteRepository = voteRepository;
        this.scheduleRepository = scheduleRepository;
        this.associateRepository = associateRepository;

    }


    /**
     * save
     *
     * @param vote vote
     */
    public void save(Vote vote) {

        this.voteRepository.save(vote);
    }

    /**
     * Valida dados de entrada vindo requisicao
     *
     * @param data
     * @throws IllegalArgumentException
     */
    public void validateData(Map<String, Object> data) throws IllegalArgumentException, BusinessException {

        //TODO

        String cpf =  data.get("associate").toString();
        Long scheduleId =  Long.parseLong(data.get("schedule").toString());
        String vote =  data.get("vote").toString();

        if (cpf.isEmpty()) {
            throw new IllegalArgumentException(Translator.toLocale(Translator.toLocale("cpf_empty")));
        }

        Associate associate  = this.associateRepository.findByCpf(cpf);

        if (associate == null) {
            throw new IllegalArgumentException(Translator.toLocale(Translator.toLocale("cpf_associate")));
        }

        this.validateCpf(cpf);

        Optional<Schedule> scheduleOptional  = this.scheduleRepository.findById(scheduleId);

        if (!scheduleOptional.isPresent()) {
            throw new IllegalArgumentException(Translator.toLocale(Translator.toLocale("cpf_empty")));
        }

        Schedule schedule = scheduleOptional.get();

        Date now =  new Date();

        if (!(now.after(schedule.getDtStarted()) && now.before(schedule.getDtFinish()))) {
            throw new BusinessException(Translator.toLocale(Translator.toLocale("periodo_invalid")));
        }

    }

    /**
     * Converte dados de entrada para objeto Vote
     * @param data
     * @return
     * @throws IllegalArgumentException
     * @throws BusinessException
     */
    public Vote parser(Map<String, Object> data) throws IllegalArgumentException, BusinessException  {

        String cpf =  data.get("associate").toString();
        Long scheduleId =  Long.parseLong(data.get("schedule").toString());
        int vote =  Integer.parseInt(data.get("vote").toString());

        Vote oVote  = this.getVoteByCpfAndScheduleId(cpf, scheduleId);

        if (oVote !=null) {
            throw new IllegalArgumentException(Translator.toLocale(Translator.toLocale("cpf_vote")));
        }

        Optional<Schedule> scheduleOptional  = this.scheduleRepository.findById(scheduleId);

        oVote  = new  Vote();

        oVote.setAssociate(this.associateRepository.findByCpf(cpf));
        oVote.setSchedule(scheduleOptional.get());
        oVote.setVote(vote);

        return oVote;
    }

    /**
     * Busca na base de dados pelo cpf do associado e pelo Id, da pauta
     *
     * @param cpf
     * @param id
     * @return
     */
    public Vote getVoteByCpfAndScheduleId(String cpf, Long id) {
        return this.voteRepository.findByAssociate_CpfAndSchedule_Id(cpf, id);
    }


    /**
     * Verifica na API externa se cpf pode votar
     *
     * @param cpf
     * @throws IllegalArgumentException
     */
    public void  validateCpf(String cpf) throws IllegalArgumentException {

        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

        try {

            ResponseEntity<String> response = restTemplate.getForEntity(ExternalApiEnum.VALIDATE_CPF_URI.getValue() + "/" + cpf, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String status = jsonNode.get("status").asText();

            if (response.getStatusCodeValue() != HttpServletResponse.SC_OK) {
                throw new IllegalArgumentException(Translator.toLocale("cpf"));
            }

            if (status.equals("UNABLE_TO_VOTE")) {
                throw new IllegalArgumentException(Translator.toLocale("cpf_validateApiUnable"));
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(Translator.toLocale("cpf_validateApi"));

        }

    }

    /**
     * Nesse metodo aumentamos timeout, pois requisicao heroku pode demorar ate subir container
     *
     * @return
     */
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
        clientHttpRequestFactory.setReadTimeout(HTTP_READ_TIMEOUT);
        return clientHttpRequestFactory;
    }
}
