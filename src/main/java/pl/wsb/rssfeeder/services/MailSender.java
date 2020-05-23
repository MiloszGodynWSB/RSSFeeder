package pl.wsb.rssfeeder.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailSender {

    @Value("${spring.sendgrid.api-key}")
    private String SENDGRID_API_KEY;
    private static final String EMAIL_FROM = "no-reply@rssfeeder.com";
    private static final String EMAIL_SUBJECT = "Your RSS Feeder for today!";
    private static final String EMAIL_CONTENT_TYPE = "text/html";
    private static final String EMAIL_REQUEST_ENDPOINT = "mail/send";

    public void sendFeeds(String emailAddress, String mailContent) {

        Email sender = new Email(EMAIL_FROM);
        Email receiver = new Email(emailAddress);
        Content content = new Content(EMAIL_CONTENT_TYPE, mailContent);
        Mail mail = new Mail(sender, EMAIL_SUBJECT, receiver, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint(EMAIL_REQUEST_ENDPOINT);
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(String.format("%s %s", response.getStatusCode(), emailAddress));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
