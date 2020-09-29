package com.rrpserivce.demo.repository;
import com.rrpserivce.demo.entity.BenchData;
import com.rrpserivce.demo.entity.Company;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    public List<Company> findAllByNameLike(String key);
    @Modifying
    @Query("select c from Company c where c.companyType.type in (?1,?2)")
    List<Company> findAllBy2Keys(String key1, String key2);
    public List<Company> findAll(Specification<Company> spec);

    // 查询租用自己企业设备的 所有租用企业
    @Query(value = "select distinct co.* from company co inner join lease le on le.company_id =?1 where co.type ='租用企业'",nativeQuery = true)
    List<Company> findByRobot(int id);
}
