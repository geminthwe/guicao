package cn.edu.fzu.sosd.guicao.user.service;

public interface ValidCodeService {
    boolean check(String email, String code);

    /**
     * Store the email and code
     * @param email the email
     */
    void send(String email);
}
