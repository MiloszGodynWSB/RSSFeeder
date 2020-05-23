package pl.wsb.rssfeeder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.wsb.rssfeeder.repository.FeedRepository;

@Controller
public class DebugFrameController {

    private FeedRepository feedRepository;
    private static String content;

    @Autowired
    public DebugFrameController(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @RequestMapping("/debug")
    @ResponseBody
    String debug() {
        return content;
    }

    public static void setContent(String result) {
        content = result;
    }
}
