package cn.edu.fzu.sosd.guicao.email.builder;

public class ValidateCodeEmailBuilder extends AbstractEmailBuilder{
    private final String code;
    private final long validTime;

    public ValidateCodeEmailBuilder(String code, long validTime) {
        this.code = code;
        this.validTime = validTime;
    }

    @Override
    protected String subject() {
        return "规巢 验证码";
    }

    @Override
    protected String emailContent() {
        int minute = (int) (validTime / 1000 / 60);
        return "[规巢] 您的验证码是：" + code + "，请在" + minute + "分钟内完成验证。";
    }
}
