package com.sicredi.app.repository;

import com.sicredi.app.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, Long> {
    Associate findByCpf(String cpf);
}
