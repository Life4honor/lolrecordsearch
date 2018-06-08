package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.ParticipantIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantIdentityRepository extends JpaRepository<ParticipantIdentity, Long> {

    List<ParticipantIdentity> findParticipantIdentitiesByGameIdOrderByParticipantId(Long gameId);

}
