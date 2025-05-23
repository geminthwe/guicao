package cn.edu.fzu.sosd.guicao.user.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.edu.fzu.sosd.guicao.common.exception.BadRequestException;
import cn.edu.fzu.sosd.guicao.common.util.PasswdUtil;
import cn.edu.fzu.sosd.guicao.user.convert.UserConvert;
import cn.edu.fzu.sosd.guicao.user.dto.*;
import cn.edu.fzu.sosd.guicao.user.entity.User;
import cn.edu.fzu.sosd.guicao.user.entity.UserSignIn;
import cn.edu.fzu.sosd.guicao.user.mapper.UserMapper;
import cn.edu.fzu.sosd.guicao.user.mapper.UserSignInMapper;
import cn.edu.fzu.sosd.guicao.user.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserSignInMapper userSignInMapper;

    @Override
    public void createUser(CreateUserReq req) {
        try {
            User user = UserConvert.toUser(req);
            // 保存用户到数据库
            userMapper.insert(user);
        } catch (DuplicateKeyException ex) {
            String errorMessage = ex.getCause().getMessage();
            // System.out.println("errorMessage: " + errorMessage);
            if (errorMessage.contains("sys_user.uk_phone")) {
                throw new BadRequestException("手机号已存在，请选择其他手机号");
            } else if (errorMessage.contains("sys_user.uk_email")) {
                throw new BadRequestException("邮箱已存在，请选择其他邮箱");
            } else {
                // 其他类型的唯一约束冲突
                throw new RuntimeException("数据插入失败: " + errorMessage);
            }
        }
    }

    @Override
    public SaTokenInfo login(LoginReq req) {
        User user = null;

        // 处理用户名登录逻辑
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", req.getPhone());
        user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BadRequestException("用户名不存在");
        }
        // 验证密码
        String encodedPassword = user.getPassword();
        String decodedPassword = PasswdUtil.decode(encodedPassword);
        if (!req.getPassword().equals(decodedPassword)) {
            throw new BadRequestException("密码错误");
        }
        StpUtil.login(user.getId());
        StpUtil.getSession().set("user", user);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // System.out.println("tokenInfo: " + tokenInfo);
        return tokenInfo;
    }

    @Override
    public void updateUser(UserDto userDto) {
        // 检查是否已登录
        if (!StpUtil.isLogin()) {
            throw new BadRequestException("用户未登录");
        }

        // 从Session中获取用户对象
        User user = (User) StpUtil.getSession().get("user");
        if (user == null) {
            throw new BadRequestException("用户信息不存在");
        }

        // 更新用户信息
        User newUser = UserConvert.toUser(userDto);
        newUser.setId(user.getId());
        userMapper.updateById(newUser);
        // 更新Session中的用户信息
        User updatedUser = userMapper.selectById(newUser.getId());
        StpUtil.getSession().set("user", updatedUser);
    }

    @Override
    public void updatePassword(UpdatePwReq req) {
        // 检查是否已登录
        if (!StpUtil.isLogin()) {
            throw new BadRequestException("用户未登录");
        }

        // 从Session中获取用户对象
        User user = (User) StpUtil.getSession().get("user");
        if (user == null) {
            throw new BadRequestException("用户信息不存在");
        }
        // 验证旧密码
        String encodedPassword = user.getPassword();
        String decodedPassword = PasswdUtil.decode(encodedPassword);
        if (!req.getOldPassword().equals(decodedPassword)) {
            throw new BadRequestException("旧密码错误");
        }
        // 验证新密码是否与旧密码相同
        if (req.getNewPassword().equals(req.getOldPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        // 验证新密码是否与确认密码相同
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new BadRequestException("新密码与确认密码不匹配");
        }
        // 加密新密码
        String encodedNewPassword = PasswdUtil.encode(req.getNewPassword());
        user.setPassword(encodedNewPassword);
        userMapper.updateById(user);
    }

    @Override
    public void resetPassword(CheckEmailCodeReq req) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", req.getEmail());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BadRequestException("邮箱不存在");
        }
        String encodedPassword = PasswdUtil.encode(req.getPassword());
        user.setPassword(encodedPassword);
        userMapper.updateById(user);
    }

    @Override
    public void increasePoints(Integer points) {
        // 检查是否已登录
        if (!StpUtil.isLogin()) {
            throw new BadRequestException("用户未登录");
        }
        User user = (User) StpUtil.getSession().get("user");
        if (user == null) {
            throw new BadRequestException("用户信息不存在");
        }
        user.setPoints(user.getPoints() + points);
        userMapper.updateById(user);
    }

    @Override
    public void resetPasswordByPhone(CheckPhoneCodeReq req) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", req.getPhone());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BadRequestException("手机号不存在");
        }
        String encodedPassword = PasswdUtil.encode(req.getPassword());
        user.setPassword(encodedPassword);
        userMapper.updateById(user);
    }

    @Override
    public String signIn(Long userId) {
        LocalDate today = LocalDate.now();

        // 查询最近一次签到记录
        QueryWrapper<UserSignIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserSignIn lastRecord = userSignInMapper.selectOne(queryWrapper);

        if (lastRecord != null && lastRecord.getSignDate().equals(today)) {
            return "您今天已经签到过了";
        }

        int continuousDays = 1;
        if (lastRecord != null && lastRecord.getSignDate().plusDays(1).equals(today)) {
            continuousDays = lastRecord.getContinuousDays() + 1;
            lastRecord.setContinuousDays(continuousDays);
            userSignInMapper.updateById(lastRecord);
        }

        if (lastRecord == null ) {
            UserSignIn newRecord = new UserSignIn();
            newRecord.setSignDate(today);
            newRecord.setUserId(userId);
            newRecord.setContinuousDays(continuousDays);
            userSignInMapper.insert(newRecord);
        }

        return "签到成功，已连续签到：" + continuousDays + "天";
    }

    @Override
    public Integer getContinuousDays(Long userId) {
        QueryWrapper<UserSignIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserSignIn lastRecord = userSignInMapper.selectOne(queryWrapper);

        if (lastRecord != null) {
            return lastRecord.getContinuousDays();
        }
        return 0;
    }
}
