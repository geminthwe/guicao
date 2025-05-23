package cn.edu.fzu.sosd.guicao.user.convert;


import cn.edu.fzu.sosd.guicao.common.util.PasswdUtil;
import cn.edu.fzu.sosd.guicao.user.dto.CreateUserReq;
import cn.edu.fzu.sosd.guicao.user.dto.UserDto;
import cn.edu.fzu.sosd.guicao.user.entity.User;

public class UserConvert {

    /**
     * 转换 CreateUserReq 到 User
     *
     * @param req
     * @return
     */
    public static User toUser(CreateUserReq req) {
        User user = new User();
        String encodedPassword = PasswdUtil.encode(req.getPassword());
        user.setPassword(encodedPassword);
        user.setPhone(req.getPhone());
        return user;
    }

    /**
     * 转换 User 到 UserDto
     *
     * @param user
     * @return
     */
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAvatar(user.getAvatar());
        userDto.setStudentId(user.getStudentId());
        userDto.setDormitory(user.getDormitory());
        userDto.setCollege(user.getCollege());
        userDto.setMajor(user.getMajor());
        userDto.setPoints(user.getPoints());
        if (user.getGender() == 0) {
            userDto.setGender("未知");
        } else if (user.getGender() == 1) {
            userDto.setGender("男");
        } else if (user.getGender() == 2) {
            userDto.setGender("女");
        }
        return userDto;
    }

    /**
     * 转换 UserDto 到 User
     *
     * @param userDto
     * @return
     */
    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAvatar(userDto.getAvatar());
        user.setStudentId(userDto.getStudentId());
        user.setDormitory(userDto.getDormitory());
        user.setCollege(userDto.getCollege());
        user.setMajor(userDto.getMajor());
        user.setPoints(userDto.getPoints());
        if ("未知".equals(userDto.getGender())) {
            user.setGender(0);
        } else if ("男".equals(userDto.getGender())) {
            user.setGender(1);
        } else if ("女".equals(userDto.getGender())) {
            user.setGender(2);
        }
        return user;
    }
}
