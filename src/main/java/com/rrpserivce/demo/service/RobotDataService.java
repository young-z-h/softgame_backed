package com.rrpserivce.demo.service;

import com.rrpserivce.demo.entity.RobotData;
import com.rrpserivce.demo.repository.RobotDataRepository;
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
public class RobotDataService {
    @Autowired
    private RobotDataRepository robotDataRepository;

    //查询全部
    public List<RobotData> findAll() {
        return robotDataRepository.findAll();
    }

    //根据id查找
    public RobotData findById(int id) {
        return robotDataRepository.findById(id).get();
    }

    //增加
    public void add(RobotData robotData) {
        robotDataRepository.save(robotData);
    }

    //修改
    public void update(RobotData robotData) {
        robotDataRepository.save(robotData);
    }

    //根据id删除
    public void deleteById(int id) {
        robotDataRepository.deleteById(id);
    }

    public List<RobotData> findAllByRobot(String robot_id) {
        return robotDataRepository.getByRobot(robot_id);
    }

    //动态查询
    public List<RobotData> query(Map<String, Object> jsonData) {

        Specification<RobotData> mpsQuery = new Specification<RobotData>() {
            @Override
            public Predicate toPredicate(Root<RobotData> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonData.get("province"))) {
                    //equal为相等  root.get("") 即为 bench.get()                          jsonData.get()即为前端传参 jsondata
                    predicates.add(criteriaBuilder.equal(root.get("robot").get("company").get("province"), jsonData.get("province").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("city"))) {
                    predicates.add(criteriaBuilder.equal(root.get("robot").get("company").get("city"), jsonData.get("city").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("robotid"))) {
                    predicates.add(criteriaBuilder.equal(root.get("robot").get("id"), jsonData.get("robotid").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("companytypeid"))) {
                    //出租和制造企业
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 1 || Integer.parseInt(jsonData.get("companytypeid").toString()) == 2) {

                        if (!jsonData.get("owncompanyid").toString().equals("1")) {
                            predicates.add(criteriaBuilder.equal(root.get("robot").get("company").get("id"), jsonData.get("owncompanyid").toString()));
                        }
                        if (!StringUtils.isEmpty(jsonData.get("companyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("robot").get("company").get("id"), jsonData.get("companyid").toString()));
                        }
                    }
                    //租用企业 只能看自己数据
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 4 || Integer.parseInt(jsonData.get("companytypeid").toString()) == 3) {
                        if (!StringUtils.isEmpty(jsonData.get("owncompanyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("robot").get("company").get("id"), jsonData.get("owncompanyid").toString()));
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<RobotData> mpsPage = robotDataRepository.findAll(mpsQuery);
        return mpsPage;
    }
}
