package cn.edu.fzu.sosd.guicao.schedule.service.impl;

import cn.edu.fzu.sosd.guicao.schedule.entity.DormitoryTask;
import cn.edu.fzu.sosd.guicao.schedule.entity.Schedule;
import cn.edu.fzu.sosd.guicao.schedule.entity.UserSchedule;
import cn.edu.fzu.sosd.guicao.schedule.mapper.DormitoryTaskMapper;
import cn.edu.fzu.sosd.guicao.schedule.mapper.ScheduleMapper;
import cn.edu.fzu.sosd.guicao.schedule.mapper.TaskMapper;
import cn.edu.fzu.sosd.guicao.schedule.mapper.UserScheduleMapper;
import cn.edu.fzu.sosd.guicao.schedule.service.TaskAssignmentService;
import cn.edu.fzu.sosd.guicao.user.entity.User;
import cn.edu.fzu.sosd.guicao.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DormitoryTaskMapper dormitoryTaskMapper;

    @Autowired
    private UserScheduleMapper userScheduleMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Schedule> assignTasksToDormitoryMembers(String dormitory, Date date) {
        // 1. 获取宿舍成员
        List<User> users = userMapper.selectList(new QueryWrapper<User>().eq("dormitory", dormitory));
        if (users.isEmpty()) throw new RuntimeException("宿舍无成员");

        // 2. 获取该宿舍绑定的所有任务及其每日时间安排
        List<DormitoryTask> dormitoryTasks = dormitoryTaskMapper.selectList(new QueryWrapper<DormitoryTask>().eq("dormitory", dormitory));
        if (dormitoryTasks.isEmpty()) throw new RuntimeException("宿舍未绑定任何任务");

        // 3. 获取每个人当天的日程安排
        Map<Long, List<UserSchedule>> userSchedulesMap = new HashMap<>();
        for (User user : users) {
            QueryWrapper<UserSchedule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user.getId())
                    .eq("date", date);
            List<UserSchedule> schedules = userScheduleMapper.selectList(queryWrapper);
            userSchedulesMap.put(user.getId(), schedules);
        }

        // 4. 分配任务（轮询 + 平均分配）
        List<Schedule> assignedSchedules = new ArrayList<>();
        int userIndex = 0;

        for (DormitoryTask dt : dormitoryTasks) {
            boolean assigned = false;

            while (!assigned && userIndex < users.size()) {
                User user = users.get(userIndex++);
                Long userId = user.getId();
                List<UserSchedule> schedules = userSchedulesMap.getOrDefault(userId, Collections.emptyList());

                // 构造当天的任务起止时间
                LocalDate localDate = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                LocalDateTime taskStart = LocalDateTime.of(localDate, dt.getStartTime());
                LocalDateTime taskEnd = LocalDateTime.of(localDate, dt.getEndTime());

                // 检查是否与日程冲突
                boolean conflict = schedules.stream()
                        .anyMatch(s -> !taskEnd.isBefore(ChronoLocalDateTime.from(s.getStartTime())) && !taskStart.isAfter(ChronoLocalDateTime.from(s.getEndTime())));

                if (!conflict) {
                    // 创建排班记录
                    Schedule schedule = new Schedule();
                    schedule.setUserId(userId);
                    schedule.setTaskId(dt.getTaskId());
                    schedule.setStartTime(LocalTime.from(taskStart));
                    schedule.setEndTime(LocalTime.from(taskEnd));
                    schedule.setDate(date);
                    schedule.setStatus(0); // 未完成
                    schedule.setPoints(10); // 示例积分
                    scheduleMapper.insert(schedule);

                    assignedSchedules.add(schedule);
                    assigned = true;
                }
            }

            if (!assigned) {
                throw new RuntimeException("无法为任务 ID=" + dt.getTaskId() + " 找到合适成员");
            }
        }

        return assignedSchedules;
    }
}
