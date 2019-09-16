package com.lambdaschool.authenticatedusers.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "quotes")
public class Quote extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long quotesid;

    @Column(nullable = false)
    private String quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid",
                nullable = false)
    @JsonIgnoreProperties({"quotes", "hibernateLazyInitializer"})
    private User user;

    public Quote()
    {
    }

    public Quote(String quote, User user)
    {
        this.quote = quote;
        this.user = user;
    }

    public long getQuotesid()
    {
        return quotesid;
    }

    public void setQuotesid(long quotesid)
    {
        this.quotesid = quotesid;
    }

    public String getQuote()
    {
        return quote;
    }

    public void setQuote(String quote)
    {
        this.quote = quote;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    //These are getters from the fields in Auditable. They will display the audible fields to the user. But you have to put them in the class that you want them to be shown in.
//    public String getCreatedBy()
//    {
//        return createdBy;
//    }
//
//    public Date getCreatedDate()
//    {
//        return createdDate;
//    }
//
//    public String getLastModifiedBy()
//    {
//        return lastModifiedBy;
//    }
//
//    public Date getLastModifiedDate()
//    {
//        return lastModifiedDate;
//    }
}