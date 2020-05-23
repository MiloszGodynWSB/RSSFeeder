package pl.wsb.rssfeeder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String emailAddress;
    private String feedUrl;

    public Feed() {
    }

    public Feed(String emailAddress, String feedUrl) {
        this.emailAddress = emailAddress;
        this.feedUrl = feedUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedURL) {
        this.feedUrl = feedURL;
    }
}
