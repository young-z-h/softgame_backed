package com.rrpserivce.demo.service;

import com.rrpserivce.demo.entity.BenchData;
import com.rrpserivce.demo.repository.BenchDataRepository;
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
public class BenchDataService {
    @Autowired
    private BenchDataRepository benchDataRepository;

    //查询全部
    public List<BenchData> findAll() {
        return benchDataRepository.findAll();
    }

    //根据id查找
    public BenchData findById(int id) {
        return benchDataRepository.findById(id).get();
    }

    //根据id查找
    public BenchData getByBenchMax(int id) {
        return benchDataRepository.getByBenchMax(id);
    }

    //增加
    public void add(BenchData benchManage) {
        benchDataRepository.save(benchManage);
    }

    //修改
    public void update(BenchData benchManage) {
        benchDataRepository.save(benchManage);
    }

    //根据id删除
    public void deleteById(int id) {
        benchDataRepository.deleteById(id);
    }

    //根据bench_id删除
    public void deleteByBench(int bench_id) {
        benchDataRepository.deleteByBench(bench_id);
    }




    //动态查询
    public List<BenchData> query(Map<String, Object> jsonData) {

        Specification<BenchData> mpsQuery = new Specification<BenchData>() {
            @Override
            public Predicate toPredicate(Root<BenchData> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonData.get("province"))) {
                                                //equal为相等  root.get("") 即为 benchData.get()                          jsonData.get()即为前端传参 jsondata
                    predicates.add(criteriaBuilder.equal(root.get("bench").get("robot").get("company").get("province"), jsonData.get("province").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("city"))) {
                    predicates.add(criteriaBuilder.equal(root.get("bench").get("robot").get("company").get("city"), jsonData.get("city").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("robotid"))) {
                    predicates.add(criteriaBuilder.equal(root.get("bench").get("robot").get("id"), jsonData.get("robotid").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("companytypeid"))) {
                    //出租和制造企业
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 1 ||Integer.parseInt(jsonData.get("companytypeid").toString()) == 2){

                        if(!jsonData.get("owncompanyid").toString().equals("1")){
                            predicates.add(criteriaBuilder.equal(root.get("bench").get("robot").get("company").get("id"), jsonData.get("owncompanyid").toString()));
                        }
                        if (!StringUtils.isEmpty(jsonData.get("companyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("bench").get("robot").get("company").get("id"),jsonData.get("companyid").toString()));
                        }
                    }
                    //租用企业 只能看自己数据
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 4 || Integer.parseInt(jsonData.get("companytypeid").toString()) == 3){
                        if (!StringUtils.isEmpty(jsonData.get("owncompanyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("bench").get("robot").get("company").get("id"),jsonData.get("owncompanyid").toString()));
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<BenchData> mpsPage = benchDataRepository.findAll(mpsQuery);
        return mpsPage;
    }
}
