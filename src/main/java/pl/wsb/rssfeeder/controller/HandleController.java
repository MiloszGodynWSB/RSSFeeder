package pl.wsb.rssfeeder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wsb.rssfeeder.model.Feed;
import pl.wsb.rssfeeder.repository.FeedRepository;
import pl.wsb.rssfeeder.services.FeedReader;
import pl.wsb.rssfeeder.services.MailSender;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HandleController {

    private FeedRepository feedRepository;
    private FeedReader feedReader;
    private MailSender mailSender;
    private static final String ACTION_SAVE = "save";
    private static final String ACTION_SEND = "send";
    private static final String EMAIL_PARAM_NAME = "emailAddress";
    private static final String FEEDURL_PARAM_NAME = "feedUrl";

    @Autowired
    public HandleController(FeedRepository feedRepository, FeedReader feedReader, MailSender mailSender) {
        this.feedRepository = feedRepository;
        this.feedReader = feedReader;
        this.mailSender = mailSender;
    }

    @PostMapping("/handle")
    String handle(@RequestParam String action,
                  @RequestParam String emailAddress,
                  @RequestParam String feedUrl) {

        if (ACTION_SAVE.equals(action) && !emailAddress.isEmpty() && !feedUrl.isEmpty()) {
            String forward = String.format("forward:/save?%s=%s&%s=%s", EMAIL_PARAM_NAME, emailAddress,
                    FEEDURL_PARAM_NAME, feedUrl);
            return forward;
        } else if (ACTION_SEND.equals(action) && !emailAddress.isEmpty()) {
            String forward = String.format("forward:/send?%s=%s", EMAIL_PARAM_NAME, emailAddress);
            return forward;
        } else {
            return "redirect:/";
        }

    }

    @RequestMapping("/save")
    String saveNewFeed(HttpServletRequest request) {

        String emailAddress = request.getParameter(EMAIL_PARAM_NAME);
        String feedUrl = request.getParameter(FEEDURL_PARAM_NAME);
        Feed feed = new Feed(emailAddress, feedUrl);
        feedRepository.save(feed);
        return "redirect:/";

    }

    @RequestMapping("/send")
    String sendFeedsToEmail(HttpServletRequest request) {

        String emailAddress = request.getParameter(EMAIL_PARAM_NAME);
        String content = feedReader.createHTMLResponse(emailAddress);
        mailSender.sendFeeds(emailAddress, content);
        DebugFrameController.setContent(content);

        return "redirect:/";

    }

}
