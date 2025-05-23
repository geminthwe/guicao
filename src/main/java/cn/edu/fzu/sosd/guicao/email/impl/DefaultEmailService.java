package cn.edu.fzu.sosd.guicao.email.impl;


import cn.edu.fzu.sosd.guicao.email.Email;
import cn.edu.fzu.sosd.guicao.email.EmailConfig;
import cn.edu.fzu.sosd.guicao.email.EmailService;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.annotation.Resource;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.eclipse.angus.mail.smtp.SMTPSSLTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DefaultEmailService implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(DefaultEmailService.class);
    private final ExecutorService sender = Executors.newScheduledThreadPool(1);

    @Resource
    private EmailConfig emailConfig;

    @Override
    public void send(Email email) {

        SMTPSSLTransport t = null;
        try {
            Properties prop = System.getProperties();
            Session session = Session.getInstance(prop, null);

            email.setSenderEmailAddress(emailConfig.getSender());

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(email.getSenderEmailAddress()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getRecipientsString(), false));
            msg.setSubject(email.getSubject());
            msg.setDataHandler(new DataHandler(new HTMLDataSource(email.getBody())));

            String host = emailConfig.getHost();
            String sender = emailConfig.getSender();
            String password = emailConfig.getPassword();

            t = (SMTPSSLTransport) session.getTransport(emailConfig.getProtocol());
            t.connect(host, sender, password);
            msg.saveChanges();
            t.sendMessage(msg, msg.getAllRecipients());
            logger.debug("email response: {}", t.getLastServerResponse());
        } catch (Exception e) {
            logger.error("send email failed.", e);
        } finally {
            if (t != null) {
                try {
                    t.close();
                } catch (Exception e) {
                    // nothing
                }
            }
        }
    }

    static class HTMLDataSource implements DataSource {

        private final String html;

        HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (html == null) {
                throw new IOException("html message is null!");
            }
            return new ByteArrayInputStream(html.getBytes());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        @Override
        public String getContentType() {
            return "text/html";
        }

        @Override
        public String getName() {
            return "HTMLDataSource";
        }
    }
}
