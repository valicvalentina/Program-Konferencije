package koke.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import koke.domain.SpecialEvent;

public interface SpecialEventRepository extends JpaRepository<SpecialEvent, Long>{

}
