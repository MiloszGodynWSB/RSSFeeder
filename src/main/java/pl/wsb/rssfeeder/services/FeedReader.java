package pl.wsb.rssfeeder.services;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wsb.rssfeeder.model.Feed;
import pl.wsb.rssfeeder.repository.FeedRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedReader {

    private FeedRepository feedRepository;
    private static final String HTML_PREFIX =
            "        <!DOCTYPE html>\n" +
            "           <html lang=\"en\">\n" +
            "           <head>\n" +
            "               <meta charset=\"UTF-8\">\n" +
            "               <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css\"\n" +
            "                     integrity=\"sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh\" crossorigin=\"anonymous\">\n" +
            "           </head>\n" +
            "           <body>\n" +
            "\n" +
            "           <div class=\"mx-auto\" style=\"width: 50%;\"><br/>\n" +
            "               <h3 style=\"text-align: center\">Welcome to your RSS Feeder!</h3><br/>\n" +
            "               <hr>\n";
    private static final String HTML_ENTRY =
            "            <h5><a href=\"%s\" target=\"_blank\">%s</a></h5>\n" +
            "                        <p>%s</p>\n" +
            "                        <a href=\"%s\" target=\"_blank\"><p style=\"font-size: small\">%s</p></a>\n" +
            "                        <hr>\n";
    private static final String HTML_POSTFIX =
            "        </div>\n" +
            "\n" +
            "        </body>\n" +
            "        </html>\n";

    @Autowired
    public FeedReader(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public String createHTMLResponse(String emailAddress) {
        String htmlResponse = HTML_PREFIX;
        List<SyndFeed> feeds = getFeedsByEmailAddress(emailAddress);
        for (SyndFeed feed : feeds) {
            String feedTitle = feed.getTitle();
            String feedUrl = feed.getLink();
            List<SyndEntry> entries = feed.getEntries();
            htmlResponse += createFeedComponent(feedTitle, feedUrl, entries);
        }
        htmlResponse+= HTML_POSTFIX;
        return htmlResponse;
    }

    private String createFeedComponent(String feedTitle, String feedUrl, List<SyndEntry> entries) {
        String result = "";
        for (SyndEntry entry : entries) {
            result += String.format(HTML_ENTRY,
                    entry.getLink(),
                    entry.getTitle(),
                    entry.getDescription().getValue(),
                    feedUrl, feedTitle);
        }
        return result;
    }

    private List<SyndFeed> getFeedsByEmailAddress(String emailAddress) {
        List<Feed> feeds = feedRepository.findByEmailAddress(emailAddress);
        SyndFeedInput input = new SyndFeedInput();
        List<SyndFeed> result = new ArrayList<SyndFeed>();
        for (Feed feed : feeds) {
            try {
                URL feedSource = new URL(feed.getFeedUrl());
                result.add(input.build(new XmlReader(feedSource)));
            } catch (FeedException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
}
