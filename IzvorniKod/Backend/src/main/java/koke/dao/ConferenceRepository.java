package koke.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import koke.domain.Conference;

public interface ConferenceRepository extends JpaRepository<Conference, Long>{

}