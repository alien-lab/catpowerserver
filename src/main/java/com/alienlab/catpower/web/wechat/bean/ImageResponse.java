package com.alienlab.catpower.web.wechat.bean;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.NONE)
public class ImageResponse extends MessageResponse {
    public ImageResponse(){
        super();
    }
    @XmlElementWrapper(name = "Image")
    @XmlElement(name = "MediaId")
    private List<String> MediaId;

    public List<String> getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        this.MediaId = new ArrayList<>();
        this.MediaId.add(mediaId);
    }
}
