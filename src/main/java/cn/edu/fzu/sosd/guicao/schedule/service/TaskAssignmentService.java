package cn.edu.fzu.sosd.guicao.schedule.service;

import cn.edu.fzu.sosd.guicao.schedule.entity.Schedule;

import java.util.Date;
import java.util.List;

public interface TaskAssignmentService {
    public List<Schedule> assignTasksToDormitoryMembers(String dormitory, Date date);
}
