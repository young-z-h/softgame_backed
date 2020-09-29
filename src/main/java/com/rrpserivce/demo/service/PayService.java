package com.rrpserivce.demo.service;

import com.rrpserivce.demo.entity.Lease;
import com.rrpserivce.demo.entity.Pay;
import com.rrpserivce.demo.repository.PayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PayService {
    @Autowired
    private PayRepository payRepository;
    //增加
    public void add(Pay pay){payRepository.save(pay);}
    //删除
    public void delete(int id){payRepository.deleteById(id);};
    //修改
    public void update(Pay pay){payRepository.save(pay);}
    //查找全部
    public List<Pay> findAll(){return payRepository.findAll();}
    //按id查找
    public Pay findById(int id){return payRepository.findById(id).get();}
    public Pay findByLeaseId(int id){
        return payRepository.findByLeaseId(id);
    }
    //审核通过
    public void ChangeExamineSituation(int id){
         payRepository.changeExamineSituation(id);
    }
    //动态查询
    public List<Pay> query(Map<String, Object> jsonData) {
        Specification<Pay> mpsQuery = new Specification<Pay>() {
            @Override
            public Predicate toPredicate(Root<Pay> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonData.get("province"))) {
                    predicates.add(criteriaBuilder.equal(root.get("company").get("province"), jsonData.get("province").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("city"))) {
                    predicates.add(criteriaBuilder.equal(root.get("company").get("city"), jsonData.get("city").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("robotid"))) {
                    predicates.add(criteriaBuilder.equal(root.get("robot").get("id"), jsonData.get("robotid").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("companytypeid"))) {
                    //出租和制造企业
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 1 ||Integer.parseInt(jsonData.get("companytypeid").toString()) == 2){

                        if(!jsonData.get("owncompanyid").toString().equals("1")){
                            predicates.add(criteriaBuilder.equal(root.get("robot").get("belongingCompany").get("id"), jsonData.get("owncompanyid").toString()));
                        }
                        if (!StringUtils.isEmpty(jsonData.get("companyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("company").get("id"),jsonData.get("companyid").toString()));
                        }
                    }
                    //租用企业 只能看自己数据
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 4|| Integer.parseInt(jsonData.get("companytypeid").toString()) == 3){
                        if (!StringUtils.isEmpty(jsonData.get("owncompanyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("company").get("id"),jsonData.get("owncompanyid").toString()));
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<Pay> mpsPage = payRepository.findAll(mpsQuery);
        return mpsPage;
    }
    public Pay findByLeaseid(int id){
        return payRepository.findByLeaseIdAndState(id);
    }
}
