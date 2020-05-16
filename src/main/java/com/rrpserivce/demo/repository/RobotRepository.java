package com.rrpserivce.demo.repository;
import com.rrpserivce.demo.entity.Company;
import com.rrpserivce.demo.entity.Robot;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RobotRepository extends JpaRepository<Robot, String> {
    public List<Robot> findAllByBelongingCompany_Id(int BelongingCompanyId);
    public List<Robot> findAll(Specification<Robot> spec);

    //出租企业查找 租用企业下的全部机器人
    @Query(value = "select distinct ro.* from robot ro inner join lease le on ro.id = le.robot_id  where le.company_id=?1 and ro.belonging_company =?2",nativeQuery = true)
    List<Robot> findByComapny(int id1,int id2);

    // 查询租用被该企业使用的机器人
    @Query(value = "select distinct ro.* from robot ro inner join lease le on ro.id = le.robot_id  where le.company_id=?1",nativeQuery = true)
    List<Robot> findByComapnyid(int id);
}
