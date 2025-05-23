package cn.edu.fzu.sosd.guicao.schedule.controller;

import cn.edu.fzu.sosd.guicao.schedule.dto.AssignReq;
import cn.edu.fzu.sosd.guicao.schedule.entity.Schedule;
import cn.edu.fzu.sosd.guicao.schedule.service.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @GetMapping("/assign")
    public List<Schedule> assignTasksToDormitoryMembers(@RequestBody AssignReq assignReq) {

        return taskAssignmentService.assignTasksToDormitoryMembers(assignReq.getDormitory(), assignReq.getDate());
    }
}
