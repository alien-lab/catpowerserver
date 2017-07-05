package com.alienlab.catpower.web.wechat.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 橘 on 2016/12/23.
 */
@XmlRootElement(name="item")
public class Article {
    private String Title;
    private String Description;
    private String PicUrl;
    private String Url;
    @XmlElement(name="Title")
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
    @XmlElement(name="Description")
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
    @XmlElement(name="PicUrl")
    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
    @XmlElement(name="Url")
    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}

