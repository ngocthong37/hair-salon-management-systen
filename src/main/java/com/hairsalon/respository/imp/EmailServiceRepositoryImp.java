package com.hairsalon.respository.imp;

import com.hairsalon.respository.IEmailServiceRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@Transactional
@Repository
public class EmailServiceRepositoryImp implements IEmailServiceRepository {

    private String fromEmail = "ngocthong2k2@gmail.com";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration config;

    @Override
    public String sendMail(String to, String[] cc, String subject, Map<String, Object> model) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

//            mimeMessageHelper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            Template t;
            if (subject.equals("Tài khoản truy cập Hair Salon của bạn đã được tạo")) {
                t = config.getTemplate("register.ftl");
            }
            else if (subject.equals("Thông báo sản phẩm mới")) {
                t = config.getTemplate("email-template.ftl");
            }
            else if(subject.equals("Thông báo dịch vụ mới")){
                t = config.getTemplate("notify-service.ftl");
            }
            else if(subject.equals("Thông báo đặt lịch thành công")) {
                t = config.getTemplate("notify-appointment.ftl");
            }
            else {
                t = config.getTemplate("notify-order.ftl");
            }
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            mimeMessageHelper.setText(html, true);
            javaMailSender.send(mimeMessage);

            return "mail send";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
