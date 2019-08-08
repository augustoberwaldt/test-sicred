package com.sicredi.app.service;
import com.sicredi.app.entity.Associate;
import com.sicredi.app.repository.AssociateRepository;
import org.springframework.stereotype.Service;

/**
 * Clase Responsavel  pelas regras de negocio dos Associados
 * @author  Augusto Berwaldt de Oliveira
 */
@Service
public class AssociateService {

    private final AssociateRepository associateRepository;

    public AssociateService(AssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }

    /**
     * save
     * @param Associate  associate
     */
    public void save(Associate associate) {
        this.associateRepository.save(associate);
    }


    /**
     * Busca associado pelo cpf na base de dados
     *
     * @param cpf
     * @return
     */
    public Associate getByCpf(String cpf) {
        return this.associateRepository.findByCpf(cpf);
    }

}
