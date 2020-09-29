package com.rrpserivce.demo.controller;
import com.common.resformat.CommonResult;
import com.rrpserivce.demo.entity.Company;
import com.rrpserivce.demo.entity.SCZT;
import com.rrpserivce.demo.service.CompanyService;
import com.rrpserivce.demo.service.ScztService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@CrossOrigin
@Api(value = "生产状态")
public class ScztController {
    @Autowired
    private ScztService scztService;
    @PostMapping(value = "/sczt/addSczt")
    @ApiOperation(value = "增加生产状态")
    public CommonResult add(@RequestBody SCZT sczt){
        CommonResult result = new CommonResult();
        try {
            scztService.add(sczt);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("新增失败");
            return result;
        }
    }


    @DeleteMapping(value = "/sczt/deleteSczt")
    @ApiOperation(value = "删除生产状态")
    public CommonResult deleteById(@RequestParam(value = "id") int id){
        CommonResult result = new CommonResult();
        try {
            scztService.deleteById(id);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("删除失败");
            return result;
        }
    }



    @GetMapping(value = "/sczt/findAllSczt")
    @ApiOperation(value = "查找所有生产状态")
    public CommonResult findAll(){
        CommonResult result = new CommonResult();
        try {
            result.setData(scztService.findAll());
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/sczt/findById")
    @ApiOperation(value = "按id查找生产状态")
    public CommonResult findById(@RequestParam(value = "id")int id){
        CommonResult result = new CommonResult();
        try {
            result.setData(scztService.find(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }


    @GetMapping(value = "/sczt/findScztByRobot")
    @ApiOperation(value = "查找设备的生产状态")
    public CommonResult findByRobot(String id){
        CommonResult result = new CommonResult();
        try {
            result.setData(scztService.findByRobot(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

}
