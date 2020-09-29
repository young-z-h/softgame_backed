package com.rrpserivce.demo.repository;

import com.rrpserivce.demo.entity.RobotData;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RobotDataRepository extends JpaRepository<RobotData, Integer> {
    @Transactional
    @Query(value = "select * from robot_data where robot_id = ?",nativeQuery = true)
    List<RobotData> getByRobot(String robot_id);
    List<RobotData> findAll(Specification<RobotData> spec);
}
