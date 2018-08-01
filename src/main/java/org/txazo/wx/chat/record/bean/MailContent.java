package org.txazo.wx.chat.record.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class MailContent implements Serializable {

    private static final long serialVersionUID = -8901086543957631989L;

    private String id;

    private String title;

    private transient String text;

    private transient String html;

    private Map<String, String> images = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailContent that = (MailContent) o;
        if (!id.equals(that.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
