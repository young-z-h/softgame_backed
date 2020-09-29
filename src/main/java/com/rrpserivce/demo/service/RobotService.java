package com.rrpserivce.demo.service;

import com.rrpserivce.demo.entity.Lease;
import com.rrpserivce.demo.entity.Robot;
import com.rrpserivce.demo.repository.RobotRepository;
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
public class RobotService {
    @Autowired
    private RobotRepository robotRepository;
    //增加
    public void add(Robot robot){robotRepository.save(robot);}
    //删除
    public void delete(String id){robotRepository.deleteById(id);}
    //修改
    public void update(Robot robot){robotRepository.save(robot);}
    //查全部
    public List<Robot> findAll(){return robotRepository.findAll();}
    //查全部
    public List<Robot> getByCompany(int company_id){return robotRepository.getByCompany(company_id);}
    //按id查找
    public Robot find(String id){return robotRepository.findById(id).get();}
    //根据企业id号查找
    public List<Robot> findAllByBelongingCompany(int id){
        return robotRepository.findAllByBelongingCompany_Id(id);
    }
    // 根据所属企业查找未被租赁的设备
    public List<Robot> findByBelongingCompanyAndCanBeLease(int id){
        return robotRepository.findAllByBelongCompanyid(id);
    }
    //出租企业查找 租用企业下的全部设备
    public List<Robot> findByComapny(Map<String,Object> jsondata){
        String companyid = null == jsondata.get("companyid")? null:jsondata.get("companyid").toString();
        String owncompanyid = null == jsondata.get("owncompanyid")? null:jsondata.get("owncompanyid").toString();
        if(companyid!=null && owncompanyid!=null){
            return robotRepository.findByComapny(Integer.parseInt(companyid),Integer.parseInt(owncompanyid));
        } else {
            return null;
        }
    }

    public List<Robot> findByCompanyId(int id){
        return robotRepository.findByComapnyid(id);
    }
    public List<Robot> query(Map<String, Object> jsonData) {

        Specification<Robot> mpsQuery = new Specification<Robot>() {
            @Override
            public Predicate toPredicate(Root<Robot> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonData.get("province"))) {
                    predicates.add(criteriaBuilder.equal(root.get("belongingCompany").get("province"), jsonData.get("province").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("city"))) {
                    predicates.add(criteriaBuilder.equal(root.get("belongingCompany").get("city"), jsonData.get("city").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("robotid"))) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), jsonData.get("robotid").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("companytypeid"))) {
                    //出租和制造企业
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 1 ||Integer.parseInt(jsonData.get("companytypeid").toString()) == 2){

                        if(!jsonData.get("owncompanyid").toString().equals("1")){
                            predicates.add(criteriaBuilder.equal(root.get("belongingCompany").get("id"), jsonData.get("owncompanyid").toString()));
                        }
                        if (!StringUtils.isEmpty(jsonData.get("companyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("belongingCompany").get("id"),jsonData.get("companyid").toString()));
                        }
                    }
                    //租用企业 只能看自己数据
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 4 || Integer.parseInt(jsonData.get("companytypeid").toString()) == 3){
                        if (!StringUtils.isEmpty(jsonData.get("owncompanyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("belongingCompany").get("id"),jsonData.get("owncompanyid").toString()));
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<Robot> mpsPage = robotRepository.findAll(mpsQuery);
        return mpsPage;
    }
}
