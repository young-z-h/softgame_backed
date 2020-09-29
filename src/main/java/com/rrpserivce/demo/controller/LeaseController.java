package com.rrpserivce.demo.controller;
import com.common.resformat.CommonResult;
import com.rrpserivce.demo.entity.Lease;
import com.rrpserivce.demo.entity.Pay;
import com.rrpserivce.demo.service.LeaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;


@RestController
@CrossOrigin
@Api(value = "租赁")
public class LeaseController {
    @Autowired
    private LeaseService leaseService;



    @PostMapping(value = "/lease/QueryLease")
    @ApiOperation(value = "动态查询租赁")
    public CommonResult QueryLease(@RequestBody Map<String, Object> jsonData){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.query(jsonData));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }
//    @GetMapping(value = "/lease/findNewestByRobot")
//    @ApiOperation(value = "根据设备查找最新的租赁")
//    public CommonResult findNewestByRobot(@RequestParam(value = "id")String id){
//        CommonResult result = new CommonResult();
//        try {
//            result.setData(leaseService.findNewestByRobot(id));
//            return result;
//        } catch (Exception e){
//            e.printStackTrace();
//            result.setState(500);
//            result.setMsg("获取失败");
//            return result;
//        }
//    }
    @PostMapping(value = "/lease/addLease")
    @ApiOperation(value = "增加租赁")
    public CommonResult add(@RequestBody Lease lease){
        CommonResult result = new CommonResult();
        try {
            leaseService.add(lease);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("新增失败");
            return result;
        }
    }


    @DeleteMapping(value = "/lease/deleteLease")
    @ApiOperation(value = "删除租赁")
    public CommonResult deleteById(@RequestParam(value = "id") int id){
        CommonResult result = new CommonResult();
        try {
            leaseService.delete(id);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("删除失败");
            return result;
        }
    }


    @PutMapping(value = "/lease/updateLease")
    @ApiOperation("修改租赁")
    public CommonResult updateDepartment(@RequestBody Lease lease){
        CommonResult result = new CommonResult();
        try {
            leaseService.update(lease);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(500);
            result.setMsg("失败");
            return result;
        }
    }



    @GetMapping(value = "/lease/findAllLease")
    @ApiOperation(value = "查找所有租赁")
    public CommonResult findAll(){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.findAll());
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }


    @GetMapping(value = "/lease/findAllByRobot")
    @ApiOperation(value = "根据设备查找所有租赁")
    public CommonResult findAllByRobot(@RequestParam(value = "id")int id){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.findByRobot(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }


    @GetMapping(value = "/lease/findAllByCompany")
    @ApiOperation(value = "根据企业查找所有租赁")
    public CommonResult findAllByCompany(@RequestParam(value = "id")int id){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.findByCompany(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }


    @GetMapping(value = "/lease/findById")
    @ApiOperation(value = "查找一个租赁")
    public CommonResult findById(@RequestParam(value = "id") int id){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.find(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }
    @GetMapping(value = "/lease/cancleRemind")
    @ApiOperation(value = "取消提醒")
    public CommonResult cancleRemind(int id){
        CommonResult result = new CommonResult();
        try {
            leaseService.cancleRemind(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }
    @GetMapping(value = "/lease/setRemind")
    @ApiOperation(value = "设置提醒")
    public CommonResult setRemind(int id){
        CommonResult result = new CommonResult();
        try {
            leaseService.setRemind(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @GetMapping(value = "/lease/findRemind")
    @ApiOperation(value = "查询是否被提醒")
    public CommonResult findRemind(int id){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.findRemind(id));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }
    @PostMapping(value = "/lease/start")
    @ApiOperation(value = "启用")
    public CommonResult start(@RequestBody Lease lease){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.start(lease));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @PostMapping(value = "/lease/stop")
    @ApiOperation(value = "停用")
    public CommonResult stop(@RequestBody Lease lease){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.stop(lease));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @PostMapping(value = "/lease/pay")
    @ApiOperation(value = "缴费")
    public CommonResult pay(@RequestBody Pay pay){
        CommonResult result = new CommonResult();
        try {
            leaseService.pay(pay);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("获取失败");
            return result;
        }
    }

    @PostMapping(value = "/lease/upload")
    @ApiOperation(value = "文件上传并返回url")
    public CommonResult upload(@RequestBody MultipartFile file, HttpServletRequest request,int leaseid){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.upload(file, request,leaseid));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("上传失败");
            return result;
        }
    }

    @PostMapping(value = "/lease/findLeaseByRobotAndCompany")
    @ApiOperation(value = "根据设备和企业查询租赁信息")
    public CommonResult findLeaseByRobotAndCompany(@RequestBody Map<String, Object> jsonData){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.findLeaseByRobotAndCompany(jsonData));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("上传失败");
            return result;
        }
    }
    @PostMapping(value = "/lease/findRobotByCity")
    @ApiOperation(value = "根据设备和企业查询租赁信息")
    public CommonResult findRobotByCity(@RequestBody Map<String, Object> jsonData){
        CommonResult result = new CommonResult();
        try {
            result.setData(leaseService.findRobotByCity(jsonData));
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("上传失败");
            return result;
        }
    }

    @PostMapping(value = "/wxwh/upload")
    @ApiOperation(value = "操作指南上传")
    public CommonResult uploadInstruction(@RequestBody MultipartFile file, HttpServletRequest request){
        CommonResult result = new CommonResult();
        try {
            leaseService.uploadInstruction(file, request);
            result.setData("成功");
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("上传失败");
            return result;
        }
    }
}
