package com.rrpserivce.demo.controller;

import com.rrpserivce.demo.entity.Bench;
import com.common.resformat.CommonResult;
import com.rrpserivce.demo.service.BenchDataService;
import com.rrpserivce.demo.service.BenchService;
import com.rrpserivce.demo.service.ProcessDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin()
@Api(value = "模台")
public class BenchController {
    @Autowired
    private BenchService benchService;
    @Autowired
    private BenchDataService benchDataService;
    @Autowired
    private ProcessDataService processDataService;

    @PostMapping(value = "/bench/QueryBench")
    @ApiOperation(value = "动态查询模台")
    public CommonResult QueryLease(@RequestBody Map<String, Object> jsonData){
        CommonResult result = new CommonResult();
        try {
            result.setData(benchService.query(jsonData));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/bench/getBench")
    @ApiOperation("获取全部模台列表")

    public CommonResult getBench() {
        CommonResult result = new CommonResult();
        try {
            result.setData(benchService.findAll());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/bench/getBenchByRobot")
    @ApiOperation("根据设备获取全部模台列表")

    public CommonResult getBenchByRobot(@RequestParam String robot_id) {
        CommonResult result = new CommonResult();
        try {
            result.setData(benchService.findAllByRobot(robot_id));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/bench/getByCompany")
    @ApiOperation("根据设备获取全部模台列表")

    public CommonResult getByCompany(@RequestParam int company_id) {
        CommonResult result = new CommonResult();
        try {
            result.setData(benchService.getByCompany(company_id));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/bench/getBenchById")
    @ApiOperation("根据id查找模台")
    public CommonResult getBenchById(int id) {
        CommonResult result = new CommonResult();
        try {
            result.setData(benchService.findById(id));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @PutMapping(value = "/bench/updateBench")
    @ApiOperation("修改模台")
    public CommonResult updateBench(@RequestBody Bench bench) {
        CommonResult result = new CommonResult();
        try {
            benchService.update(bench);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("失败");
            return result;
        }
    }


    @PostMapping(value = "/bench/deleteById")
    @ApiOperation("删除")
    public CommonResult deleteById(@RequestBody int id) {
        CommonResult result = new CommonResult();
        try {
            processDataService.deleteByBench(id);
            benchDataService.deleteByBench(id);
            benchService.deleteById(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("删除失败");
            return result;
        }
    }

    @PostMapping(value = "/bench/addBench")
    @ApiOperation("增加")
    public CommonResult add(@RequestBody Bench bench) {
        CommonResult result = new CommonResult();
        try {
            benchService.add(bench);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("新增失败");
            return result;
        }
    }

}
