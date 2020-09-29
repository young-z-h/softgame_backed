package com.rrpserivce.demo.controller;

import com.rrpserivce.demo.entity.Warning;
import com.common.resformat.CommonResult;
import com.rrpserivce.demo.service.WarningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin()
@Api(value = "警告")
public class WarningController {
    @Autowired
    private WarningService warningService;

    @PostMapping(value = "/warning/QueryWarning")
    @ApiOperation(value = "动态查询模台参数")
    public CommonResult QueryLease(@RequestBody Map<String, Object> jsonData){
        CommonResult result = new CommonResult();
        try {
            result.setData(warningService.query(jsonData));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/warning/getWarning")
    @ApiOperation("获取全部警告列表")

    public CommonResult getWarning() {
        CommonResult result = new CommonResult();
        try {
            result.setData(warningService.findAll());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/warning/getWarningByRobot")
    @ApiOperation("根据设备获取全部列表")

    public CommonResult getWarningByRobot(@RequestParam String robot_id) {
        CommonResult result = new CommonResult();
        try {
            result.setData(warningService.findAllByRobot(robot_id));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/warning/getWarningById")
    @ApiOperation("根据id查找警告")
    public CommonResult getWarningById(@RequestBody int id) {
        CommonResult result = new CommonResult();
        try {
            result.setData(warningService.findById(id));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @PutMapping(value = "/warning/updateWarning")
    @ApiOperation("修改警告")
    public CommonResult updateWarning(@RequestBody Warning warning) {
        CommonResult result = new CommonResult();
        try {
            warningService.update(warning);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("失败");
            return result;
        }
    }


    @PostMapping(value = "/warning/deleteById")
    @ApiOperation("删除")
    public CommonResult deleteById(@RequestBody int id) {
        CommonResult result = new CommonResult();
        try {
            warningService.deleteById(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("删除失败");
            return result;
        }
    }

    @PostMapping(value = "/warning/addWarning")
    @ApiOperation("增加")
    public CommonResult add(@RequestBody Warning warning) {
        CommonResult result = new CommonResult();
        try {
            warningService.add(warning);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("新增失败");
            return result;
        }
    }

}
