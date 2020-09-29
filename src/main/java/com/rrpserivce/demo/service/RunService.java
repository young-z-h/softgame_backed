package com.rrpserivce.demo.service;

import com.rrpserivce.demo.entity.Run;
import com.rrpserivce.demo.repository.RunRepository;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RunService {
    @Autowired
    private RunRepository runRepository;

    //查询
    public List<Run> getRatio(String begin, String end) {
        return runRepository.getRatio(begin, end);
    }


    //查询
    public List<Run> getRatioByCompany(int company_id, String begin, String end) {
        return runRepository.getRatioByCompany(company_id, begin, end);
    }

    //根据id查询
    public List<Run> getRatioById(String time) {
        return runRepository.getRatioById(time);
    }

    //根据id查询
    public List<Run> getRatioLate(int company_id) {
        return runRepository.getRatioLate(company_id);
    }

    //动态查询
    public List<Run> query(Map<String, Object> jsonData) {
//        int page = Integer.parseInt(jsonData.get("page").toString());
//        int rows = Integer.parseInt(jsonData.get("rows").toString());
        Sort sort = Sort.by(Sort.Order.desc("time"));
        Pageable pageable = PageRequest.of(0, 20, sort);
        Specification<Run> mpsQuery = new Specification<Run>() {
            @Override
            public Predicate toPredicate(Root<Run> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonData.get("startdate")) || !StringUtils.isEmpty(jsonData.get("enddate"))) {
                    java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String s = jsonData.get("startdate").toString();
                    String s1 = jsonData.get("enddate").toString();
                    Date date = new Date();
                    Date date1 = new Date();
                    try {
                        date = formatter.parse(s);
                        date1 = formatter.parse(s1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //equal为相等  root.get("") 即为 bench.get()                          jsonData.get()即为前端传参 jsondata
                    predicates.add(criteriaBuilder.between(root.get("time"), date, date1));
                }
                if (!StringUtils.isEmpty(jsonData.get("province"))) {
                    //equal为相等  root.get("") 即为 run.get()                          jsonData.get()即为前端传参 jsondata
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
        List<Run> mpsPage = null;
        if (StringUtils.isEmpty(jsonData.get("check"))){
            mpsPage = runRepository.findAll(mpsQuery, pageable);
        }
        else {
            mpsPage = runRepository.findAll(mpsQuery);
        }

//        List<Run> mpsPage = runRepository.getPage(mpsQuery,pageable);
        return mpsPage;
    }

}
