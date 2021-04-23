package com.softeng.pms.controller;

import com.softeng.pms.component.DingTalkUtils;
import com.softeng.pms.component.Utils;
import com.softeng.pms.entity.DcRecord;
import com.softeng.pms.service.AuditService;
import com.softeng.pms.service.NotifyService;
import com.softeng.pms.service.UserService;
import com.softeng.pms.vo.CheckedVO;
import com.softeng.pms.vo.CheckVO;
import com.softeng.pms.vo.ToCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 10:50 AM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AuditController {
    @Autowired
    AuditService auditService;
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    UserService userService;
    @Autowired
    Utils utils;
    @Autowired
    NotifyService notifyService;

    /**
     * 审核员提交审核结果
     * @param  checkVO 审核结果信息
     * @return void
     * @date 9:35 AM 12/27/2019
     **/
    @PostMapping("/audit")
    public void submitAuditResult(@Valid @RequestBody CheckVO checkVO) {

        // 持久化审核结果
        DcRecord dc = auditService.submitAudit(checkVO);
        // 更新dcsummary
        auditService.updateDcSummary(dc.getApplicant().getId(), dc.getYearmonth(), dc.getWeek());
        // 发送消息
        notifyService.updateDcMessage(dc);
    }


    /**
     * 未审核申请数量
     * @param uid
     * @return
     */
    @GetMapping("/audit/uncheckcnt")
    public int  getUnCheckCnt(@RequestAttribute int uid) {
        return auditService.getUnCheckCnt(uid);
    }


    /**
     * 审核人获取待审核的申请
     * @param uid
     * @return
     */
    @GetMapping("/audit/pending")
    public List<ToCheckVO> getAuditApplication(@RequestAttribute int uid) {
        return auditService.getPendingApplication(uid);
    }


    /**
     * 审核人获取已经审核的申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.vo.CheckedVO>
     * @Date 8:41 PM 1/29/2020
     **/
    @GetMapping("/audit/checked/page/{page}")
    public Map listChecked(@RequestAttribute int uid, @PathVariable int page) {
        return auditService.listCheckedVO(uid, page, 10);
    }


    /**
     * 审核人根据时间筛选已经审核的申请
     * @param uid
     * @param map
     * @return
     */
    @PostMapping("/audit/checked/date")
    public List<CheckedVO> listCheckedByDate(@RequestAttribute int uid, @RequestBody Map<String, LocalDate> map) {
        int[] date = utils.getTimeFlag(map.get("date"));
        int yearmonth = date[0];
        int week = date[1];
        return auditService.listCheckedByDate(uid, yearmonth, week);
    }


    /**
     * 根据uid和时间获取周报
     * @param uid, date
     * @return java.util.Map
     * @Date 9:12 PM 2/4/2020
     **/
    @PostMapping("/audit/report/{uid}")
    public Map getReport(@PathVariable int uid, @RequestBody Map<String, LocalDate> map) {
        String userid = userService.getUserid(uid);
        LocalDateTime startTime = LocalDateTime.of(map.get("date"), LocalTime.of(8,0));
        return dingTalkUtils.getReport(userid, startTime, startTime.plusDays(5));
    }

}