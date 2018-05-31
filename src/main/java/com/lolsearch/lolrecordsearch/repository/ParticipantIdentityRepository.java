package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.ParticipantIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantIdentityRepository extends JpaRepository<ParticipantIdentity, Long> {

    public List<ParticipantIdentity> findParticipantIdentitiesByGameIdOrderByParticipantId(Long gameId);

}
