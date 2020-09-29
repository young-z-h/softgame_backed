package com.rrpserivce.demo.controller;
import com.common.resformat.CommonResult;
import com.rrpserivce.demo.entity.Robot;
import com.rrpserivce.demo.service.RobotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@CrossOrigin
@Api(value = "设备")
public class RobotController {
    @Autowired
    private RobotService robotService;
    @PostMapping(value = "/robot/addRobot")
    @ApiOperation(value = "增加设备")
    public CommonResult add(@RequestBody Robot robot){
        CommonResult result = new CommonResult();
        try {
            robotService.add(robot);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("新增失败");
            return result;
        }
    }


    @DeleteMapping(value = "/robot/deleteRobot")
    @ApiOperation(value = "删除设备")
    public CommonResult deleteById(@RequestParam(value = "id") String id){
        CommonResult result = new CommonResult();
        try {
            robotService.delete(id);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("删除失败");
            return result;
        }
    }


    @PutMapping(value = "/robot/updateRobot")
    @ApiOperation("修改设备")
    public CommonResult updateDepartment(@RequestBody Robot robot){
        CommonResult result = new CommonResult();
        try {
            robotService.update(robot);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("失败");
            return result;
        }
    }


    @GetMapping(value = "/robot/findAllRobot")
    @ApiOperation(value = "查找所有设备")
    public CommonResult findAll(){
        CommonResult result = new CommonResult();
        try {
            result.setData(robotService.findAll());
            System.out.println(robotService.findAll().get(1).toString());
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }


    @GetMapping(value = "/robot/findById")
    @ApiOperation(value = "查找一个设备")
    public CommonResult findById(@RequestParam(value = "id")String id){
        CommonResult result = new CommonResult();
        try {
            result.setData(robotService.find(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/robot/getByCompany")
    @ApiOperation(value = "查找设备")
    public CommonResult getByCompany(@RequestParam int company_id) {
        CommonResult result = new CommonResult();
        try {
            result.setData(robotService.getByCompany(company_id));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }


    @GetMapping(value = "/robot/findAllByCompany")
    @ApiOperation(value = "按企业id查找设备")
    public CommonResult findAllByCompany(@RequestParam(value = "id")int id){
        CommonResult result = new CommonResult();
        try {
            result.setData(robotService.findAllByBelongingCompany(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }
    @GetMapping(value = "/robot/findAllByBelongCompanyAndCanBeLeased")
    @ApiOperation(value = "按企业id查找设备")
    public CommonResult findAllByBelongCompany(@RequestParam(value = "id")int id){
        CommonResult result = new CommonResult();
        try {
            result.setData(robotService.findByBelongingCompanyAndCanBeLease(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/robot/findAllByCompanyid")
    @ApiOperation(value = "按企业id查找被该企业使用的设备")
    public CommonResult findAllByCompanyid(int id){
        CommonResult result = new CommonResult();
        try {
            result.setData(robotService.findByCompanyId(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    //出租企业查找 租用企业下的全部设备
    @PostMapping(value = "/robot/findByComapny")
    @ApiOperation(value = "出租企业查找 租用企业下的全部设备")
    public CommonResult findByComapny(@RequestBody  Map<String,Object> jsondata){
        CommonResult result = new CommonResult();
        try {
            result.setData(robotService.findByComapny(jsondata));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }


    @PostMapping(value = "/robot/QueryRobot")
    @ApiOperation(value = "动态查询设备")
    public CommonResult QueryLease(@RequestBody Map<String, Object> jsonData){
        CommonResult result = new CommonResult();
        try {
            result.setData(robotService.query(jsonData));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }
}
