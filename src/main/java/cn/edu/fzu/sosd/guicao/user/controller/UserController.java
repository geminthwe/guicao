package cn.edu.fzu.sosd.guicao.user.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.edu.fzu.sosd.guicao.common.exception.BadRequestException;
import cn.edu.fzu.sosd.guicao.common.util.MinioUtils;
import cn.edu.fzu.sosd.guicao.user.convert.UserConvert;
import cn.edu.fzu.sosd.guicao.user.dto.*;
import cn.edu.fzu.sosd.guicao.user.entity.User;
import cn.edu.fzu.sosd.guicao.user.service.PhoneValidCodeService;
import cn.edu.fzu.sosd.guicao.user.service.UserService;
import cn.edu.fzu.sosd.guicao.user.service.ValidCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValidCodeService validCodeService;

    @Autowired
    private PhoneValidCodeService phoneValidCodeService;

    @Autowired
    private MinioUtils minioUtils;

    @PostMapping("/register")
    public void register(@RequestBody CreateUserReq req) {
        // 处理注册逻辑
        if (StringUtils.isBlank(req.getPhone())) {
            throw new BadRequestException("手机号不能为空");
        }

        if (StringUtils.isBlank(req.getPassword())) {
            throw new BadRequestException("密码不能为空");
        }

        if (StringUtils.isBlank(req.getConfirmPassword())) {
            throw new BadRequestException("确认密码不能为空");
        }

        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BadRequestException("密码不一致");
        }

        if (StringUtils.isBlank(req.getCode())) {
            throw new BadRequestException("验证码不能为空");
        }

        if (!phoneValidCodeService.checkPhone(req.getPhone(), req.getCode())) {
            throw new BadRequestException("验证码错误");
        }

        userService.createUser(req);
    }

    @PostMapping("/login")
    public SaResult login(@RequestBody LoginReq req) {
        if (StringUtils.isBlank(req.getPhone())) {
            throw new BadRequestException("手机号不能为空");
        }
        if (StringUtils.isBlank(req.getPassword())) {
            throw new BadRequestException("密码不能为空");
        }

        SaTokenInfo tokenInfo = userService.login(req);
        return SaResult.data(tokenInfo);
    }

    @GetMapping("/info")
    public UserDto getUserInfo() {
        // 检查是否已登录
        if (!StpUtil.isLogin()) {
            throw new BadRequestException("用户未登录");
        }

        // 从Session中获取用户对象
        User user = (User) StpUtil.getSession().get("user");

        if (user == null) {
            throw new BadRequestException("用户信息不存在");
        }
        // 将User对象转换为UserDto
        UserDto userDto = UserConvert.toUserDto(user);

        return userDto;
    }

    @PutMapping("/update")
    public void updateUserInfo(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
    }

    @PutMapping("/updatePassword")
    public void updatePassword(@RequestBody UpdatePwReq req) {
        userService.updatePassword(req);
    }

    @PutMapping("/increasePoints")
    public void increasePoints(@RequestParam Integer points) {
        if (points == null || points <= 0) {
            throw new BadRequestException("积分必须大于0");
        }
        userService.increasePoints(points);
    }

    @GetMapping("/validCode/email")
    public void verifyCode(@RequestParam String email) {
        if (StringUtils.isBlank(email)) {
            throw new BadRequestException("email不能为空");
        }
        validCodeService.send(email);
    }

    @GetMapping("/validCode/phone")
    public void verifyPhoneCode(@RequestParam String phone) throws ExecutionException, InterruptedException {
        if (StringUtils.isBlank(phone)) {
            throw new BadRequestException("phone不能为空");
        }
        phoneValidCodeService.send(phone);
    }

    @PostMapping("/validCode/email/resetPassword")
    public void checkEmailCode(@RequestBody CheckEmailCodeReq req) {
        if (StringUtils.isBlank(req.getEmail())) {
            throw new BadRequestException("email不能为空");
        }
        if (StringUtils.isBlank(req.getCode())) {
            throw new BadRequestException("验证码不能为空");
        }
        if (!validCodeService.check(req.getEmail(), req.getCode())) {
            throw new BadRequestException("验证码错误");
        }
        if (req.getPassword() == null) {
            throw new BadRequestException("密码不能为空");
        }
        if (req.getConfirmPassword() == null) {
            throw new BadRequestException("确认密码不能为空");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BadRequestException("密码不一致");
        }
        userService.resetPassword(req);
    }

    @PostMapping("/validCode/phone/resetPassword")
    public void checkPhoneCode(@RequestBody CheckPhoneCodeReq req) throws ExecutionException, InterruptedException {
        if (StringUtils.isBlank(req.getPhone())) {
            throw new BadRequestException("phone不能为空");
        }
        if (StringUtils.isBlank(req.getCode())) {
            throw new BadRequestException("验证码不能为空");
        }
        if (!phoneValidCodeService.checkPhone(req.getPhone(), req.getCode())) {
            throw new BadRequestException("验证码错误");
        }
        if (req.getPassword() == null) {
            throw new BadRequestException("密码不能为空");
        }
        if (req.getConfirmPassword() == null) {
            throw new BadRequestException("确认密码不能为空");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BadRequestException("密码不一致");
        }
        userService.resetPasswordByPhone(req);
    }

    @GetMapping("/uploadAvatar")
    public void uploadAvatar(@RequestParam("file") MultipartFile file,String fileName) {
        minioUtils.upload(file, fileName);
    }

    @GetMapping("/getAvatarUrl")
    public String getAvatarUrl(@RequestParam String fileName) {
        return minioUtils.getFileUrl(fileName);
    }

    @PostMapping("/sign/in")
    public String signIn(@RequestParam Long userId) {
        return userService.signIn(userId);
    }

    @GetMapping("/sign/days")
    public Integer getContinuousDays(@RequestParam Long userId) {
        return userService.getContinuousDays(userId);
    }
}
