package com.rrpserivce.demo.controller;

import com.rrpserivce.demo.entity.Trouble;
import com.common.resformat.CommonResult;
import com.rrpserivce.demo.service.TroubleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin()
@Api(value = "故障")
public class TroubleController {
    @Autowired
    private TroubleService troubleService;

    @PostMapping(value = "/trouble/QueryTrouble")
    @ApiOperation(value = "动态查询模台参数")
    public CommonResult QueryLease(@RequestBody Map<String, Object> jsonData){
        CommonResult result = new CommonResult();
        try {
            result.setData(troubleService.query(jsonData));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/trouble/getTrouble")
    @ApiOperation("获取全部故障列表")

    public CommonResult getTrouble() {
        CommonResult result = new CommonResult();
        try {
            result.setData(troubleService.findAll());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/trouble/getTroubleByRobot")
    @ApiOperation("根据设备获取全部列表")

    public CommonResult getTroubleByRobot(@RequestParam String robot_id) {
        CommonResult result = new CommonResult();
        try {
            result.setData(troubleService.findAllByRobot(robot_id));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/trouble/getTroubleById")
    @ApiOperation("根据id查找故障")
    public CommonResult getTroubleById(@RequestBody int id) {
        CommonResult result = new CommonResult();
        try {
            result.setData(troubleService.findById(id));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @PutMapping(value = "/trouble/updateTrouble")
    @ApiOperation("修改故障")
    public CommonResult updateTrouble(@RequestBody Trouble trouble) {
        CommonResult result = new CommonResult();
        try {
            troubleService.update(trouble);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("失败");
            return result;
        }
    }


    @PostMapping(value = "/trouble/deleteById")
    @ApiOperation("删除")
    public CommonResult deleteById(@RequestBody int id) {
        CommonResult result = new CommonResult();
        try {
            troubleService.deleteById(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("删除失败");
            return result;
        }
    }

    @PostMapping(value = "/trouble/addTrouble")
    @ApiOperation("增加")
    public CommonResult add(@RequestBody Trouble trouble) {
        CommonResult result = new CommonResult();
        try {
            troubleService.add(trouble);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("新增失败");
            return result;
        }
    }

}
