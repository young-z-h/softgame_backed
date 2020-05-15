package com.rrpserivce.demo.repository;
import com.rrpserivce.demo.entity.Approval;
import com.rrpserivce.demo.entity.Lease;
import com.rrpserivce.demo.entity.Pay;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
    public List<Approval> findAll(Specification<Approval> spec);
    @Modifying
    @Query(value = "update  approval set state = '1' where id=?1",nativeQuery = true)
    public void changeState(int id);


    @Query(value = "select * from approval  where lease_id=?1 and state='0'",nativeQuery = true)
    public List<Approval> isApproval(int id);
}
