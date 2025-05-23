package cn.edu.fzu.sosd.guicao.user.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.edu.fzu.sosd.guicao.user.dto.*;

public interface UserService {
    void createUser(CreateUserReq req);

    SaTokenInfo login(LoginReq req);

    void updateUser(UserDto userDto);

    void updatePassword(UpdatePwReq req);

    void resetPassword(CheckEmailCodeReq req);

    void increasePoints(Integer points);

    void resetPasswordByPhone(CheckPhoneCodeReq req);

    String signIn(Long userId);

    Integer getContinuousDays(Long userId);
}
