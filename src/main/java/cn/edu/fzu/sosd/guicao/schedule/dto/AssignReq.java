package cn.edu.fzu.sosd.guicao.schedule.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AssignReq {
    private String dormitory;
    private Date date;
}
