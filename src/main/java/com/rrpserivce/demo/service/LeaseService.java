package com.rrpserivce.demo.service;

import com.rrpserivce.demo.entity.*;
import com.rrpserivce.demo.repository.ApprovalRepository;
import com.rrpserivce.demo.repository.LeaseRepository;
import com.rrpserivce.demo.repository.RobotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class LeaseService {
    @Autowired
    private LeaseRepository leaseRepository;
    @Autowired
    private RobotRepository robotRepository;
    @Autowired
    private ApprovalRepository approvalRepository;
    @Autowired
    private PayService payService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private RobotService robotService;
    private static  final Logger logger = LoggerFactory.getLogger(LeaseService.class);
    public List<Lease> query(Map<String, Object> jsonData) {

        Specification<Lease> mpsQuery = new Specification<Lease>() {
            @Override
            public Predicate toPredicate(Root<Lease> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonData.get("province"))) {
                    predicates.add(criteriaBuilder.equal(root.get("companyId").get("province"), jsonData.get("province").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("city"))) {
                    predicates.add(criteriaBuilder.equal(root.get("companyId").get("city"), jsonData.get("city").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("robotid"))) {
                    predicates.add(criteriaBuilder.equal(root.get("robot").get("id"), jsonData.get("robotid").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("companytypeid"))) {
                    //出租和制造企业
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 1 ||Integer.parseInt(jsonData.get("companytypeid").toString()) == 2){

                        if(!jsonData.get("owncompanyid").toString().equals("1")){
                            predicates.add(criteriaBuilder.equal(root.get("robot").get("belongingCompany").get("id"), jsonData.get("owncompanyid").toString()));
                        }
                        if (!StringUtils.isEmpty(jsonData.get("companyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("companyId").get("id"),jsonData.get("companyid").toString()));
                        }
                    }
                    //租用企业 只能看自己数据
                    if (Integer.parseInt(jsonData.get("companytypeid").toString()) == 4 || Integer.parseInt(jsonData.get("companytypeid").toString()) == 3){
                        if (!StringUtils.isEmpty(jsonData.get("owncompanyid"))) {
                            predicates.add(criteriaBuilder.equal(root.get("companyId").get("id"),jsonData.get("owncompanyid").toString()));
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<Lease> mpsPage = leaseRepository.findAll(mpsQuery);
        return mpsPage;
    }
    //增加
    public void add(Lease lease)  {
        leaseRepository.save(lease);
        //更新robot表 company
        Company company = lease.getCompanyId();
        Robot robot = lease.getRobot();
        robot.setCompany(company);
        this.robotService.update(robot);
    }
    //删除文件
    public void deleteFile(int id) throws URISyntaxException {
        String uploadurl = this.find(id).getUploadurl();
        int i = uploadurl.indexOf("u");
        String filePath = uploadurl.substring(i,uploadurl.length());
        String fileUtl = this.getClass().getResource("/static/"+filePath).toURI().getPath();
        logger.info("文件url:{}"+ fileUtl);
        File file = new File(fileUtl);
        if(file.isFile()&&file.exists()){
            file.delete();
            logger.info("---------------已删除文件!");
        } else {
            logger.error("--------------删除失败！未找到文件-------------");
        }
    }
    //删除
    public void delete(int id) throws URISyntaxException {
        this.deleteFile(id);
        leaseRepository.deleteById(id);
    }
    //修改
    public void update(Lease lease){leaseRepository.save(lease);}
    //查找全部
    public List<Lease> findAll(){return leaseRepository.findAll();}
    //按id查找
    public Lease find(int id){return leaseRepository.findById(id).get();}
    //根据设备id寻找
    public List<Lease> findByRobot(int id){return leaseRepository.findAllByRobot_Id(id);}
    //根据租用企业寻找
    public List<Lease> findByCompany(int id){
        return leaseRepository.findAllByCompanyId_Id(id);
    }

    public void setRemind(int id){
        leaseRepository.setRemind(id);
    }
    public void cancleRemind(int id){
        leaseRepository.cancleRemind(id);
    }

    //登陆时查询是否被提醒
    public Map<String,Object> findRemind(int companyid) throws ParseException {
        List<Lease> leases = leaseRepository.findRemind(companyid);
        Map map =new HashMap();
        if (leases.size()>0){
            for (Lease lease: leases){
                //0缴费 1欠费
                if (lease.getPaymentSituation()=='1'){
                    map.put("msg",lease.getRobot().getName()+"已欠费，请及时缴费！");
                } else {
//                    Pay pay = payService.findByLeaseId(lease.getId());
//                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//                    String date2 = pay.getPaymentDeadline();
                    long daysBetween = (lease.getPaymentdeadline().getTime()-new Date().getTime())/(60*60*24*1000);
                    map.put("msg",lease.getRobot().getName()+"还有"+daysBetween+"天到期，请及时缴费！");
                }
            }
            return map;
        } else {
            return null;
        }
    }
    public Map start(Lease lease){
        Map map =new HashMap();
        List<Approval> approvals = approvalRepository.isApproval(lease.getId());
        if (approvals.size()!=0){
            String request = approvals.get(0).getRequest();
            String id = approvals.get(0).getRobot().getId();
            map.put("msg",id+"正在"+request+" 请求中，请耐心等待客服经理审批！");
            return map;
        } else {
            Approval approval =new Approval();
            approval.setRobot(lease.getRobot());
            approval.setLease(lease);
            approval.setState('0');
            approval.setRequest("启用");
            approvalService.add(approval);
            map.put("msg","已发起启用审核，等待客服经理审批！");
            return map;
        }
    }

    public Map stop(Lease lease){
        Map map =new HashMap();
        List<Approval> approvals = approvalRepository.isApproval(lease.getId());
        if (approvals.size()!=0){
            String request = approvals.get(0).getRequest();
            String id = approvals.get(0).getRobot().getId();
            map.put("msg",id+"正在"+request+" 请求中，请耐心等待客服经理审批！");
            return map;
        } else {
            Approval approval =new Approval();
            approval.setRobot(lease.getRobot());
            approval.setLease(lease);
            approval.setState('0');
            approval.setRequest("停用");
            approvalService.add(approval);
            map.put("msg","已发起停用审核，等待客服经理审批！");
            return map;
        }
    }
    //续费
    public void pay(Pay pay) throws ParseException {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        pay.setPaymentDeadline(sdf.format(sdf.parse(pay.getPaymentDeadline())));
        Approval approval =new Approval();
        approval.setLease(pay.getLease());
        approval.setRobot(pay.getRobot());
        approval.setState('0');
        approval.setRequest("续费审核");
        payService.add(pay);
        approvalService.add(approval);
        leaseRepository.changePaymentSituation('2',pay.getLease().getId());
    }
    //缴费审核通过
    public void ChangePaymentSituation(int id){
        leaseRepository.changePaymentSituation('0',id);
    }
    //启用,停用 审核通过
    public void ChangeState(String state,int id){
        leaseRepository.changeState(state,id);
    }

    //上传合同 并返回url
    public String upload(MultipartFile file, HttpServletRequest request,int id) throws IOException, URISyntaxException {
        if(!StringUtils.isEmpty(id)){
            // 删除文件
            this.deleteFile(id);
        }
        String UPLOAD_PATH_PREFIX = "static/uploadFile/";
        String realPath = new String("src/main/resources/" + UPLOAD_PATH_PREFIX);
        String oldName =file.getOriginalFilename();
        logger.info("-----------文件原始的名字【"+ oldName +"】-----------");
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."),oldName.length());
        logger.info("-----------文件要保存后的新名字【"+ newName +"】-----------");
        logger.info("-----------上传文件保存的路径【"+ realPath +"】-----------");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String format = sdf.format(new Date());
        //存放上传文件的文件夹
        File file2 = new File(realPath + format);
        logger.info("-----------存放上传文件的文件夹【"+ file2 +"】-----------");
        logger.info("-----------输出文件夹绝对路径 -- 这里的绝对路径是相当于当前项目的路径而不是“容器”路径【"+ file2.getAbsolutePath() +"】-----------");
        if(!file2.isDirectory()){
            //递归生成文件夹
            file2.mkdirs();
        }
        try {
            //构建真实的文件路径
            File newFile = new File(file2.getAbsolutePath() + File.separator + newName);
            //转存文件到指定路径，如果文件名重复的话，将会覆盖掉之前的文件,这里是把文件上传到 “绝对路径”
            file.transferTo(newFile);
            String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploadFile/" + format + newName;
            logger.info("-----------【"+ filePath +"】-----------");
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
}

    public List<Lease> findLeaseByRobotAndCompany(Map<String, Object> jsonData){

        Specification<Lease> mpsQuery = new Specification<Lease>() {
            @Override
            public Predicate toPredicate(Root<Lease> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonData.get("robotid"))) {
                    predicates.add(criteriaBuilder.equal(root.get("robot").get("id"), jsonData.get("robotid").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("companyid"))) {
                    predicates.add(criteriaBuilder.equal(root.get("companyId").get("id"), jsonData.get("companyid").toString()));
                }
                if (!StringUtils.isEmpty(jsonData.get("belongcompanyid"))) {
                    predicates.add(criteriaBuilder.equal(root.get("robot").get("belongingCompany").get("id"), jsonData.get("belongcompanyid").toString()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<Lease> mpsPage = leaseRepository.findAll(mpsQuery);
        return mpsPage;
    }
    public List<Lease> findRobotByCity(Map<String, Object> jsonData){

        Specification<Lease> mpsQuery = new Specification<Lease>() {
            @Override
            public Predicate toPredicate(Root<Lease> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonData.get("city"))) {
                    predicates.add(criteriaBuilder.equal(root.get("companyId").get("city"), jsonData.get("city").toString()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<Lease> mpsPage = leaseRepository.findAll(mpsQuery);
        return mpsPage;
    }
    public void uploadInstruction(MultipartFile file, HttpServletRequest request) throws IOException, URISyntaxException {
        String realPath = new String("src/main/resources/static");
        String fileName = "instruction.pdf";
        logger.info("-----------文件要保存后的名字【"+ fileName +"】-----------");
        //存放上传文件的文件夹
        File file2 = new File(realPath);
        if(!file2.isDirectory()){
            //递归生成文件夹
            file2.mkdirs();
        }
        try {
            //构建真实的文件路径
            File newFile = new File(file2.getAbsolutePath() + File.separator + fileName);
            //转存文件到指定路径，如果文件名重复的话，将会覆盖掉之前的文件,这里是把文件上传到 “绝对路径”
            file.transferTo(newFile);
            String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/"+fileName;
            logger.info("------------------------上传成功！--------------------------");
            logger.info("-----------【"+ filePath +"】-----------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
