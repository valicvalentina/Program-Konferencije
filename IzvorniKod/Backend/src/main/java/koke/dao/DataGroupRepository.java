package koke.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import koke.domain.DataGroup;

public interface DataGroupRepository extends JpaRepository<DataGroup, Long> {

}
