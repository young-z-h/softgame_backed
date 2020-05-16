package com.rrpserivce.demo.service;

import com.rrpserivce.demo.entity.BenchCount;
import com.rrpserivce.demo.entity.ConcreteCount;
import com.rrpserivce.demo.repository.ConcreteCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcreteCountService {
    @Autowired
    private ConcreteCountRepository concreteCountRepository;

    //查询
    public List<ConcreteCount> getCount(String begin, String end,String robot_id) {
        return concreteCountRepository.getCount(begin, end,robot_id);
    }

    public List<ConcreteCount> findAllByRobot(String robot_id) {
        return concreteCountRepository.getByRobot(robot_id);
    }

    //根据id查询
    public List<ConcreteCount> getCountById(String time) {
        return concreteCountRepository.getCountById(time);
    }

}
