package org.txazo.wx.chat.record.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Record implements Serializable, Comparable<Record> {

    private static final long serialVersionUID = -1526110624579960300L;

    private int type;

    private String sender;

    // yyyy-MM-dd HH:mm
    private String time;

    private String message;

    private int order;

    @Override
    public int compareTo(Record o) {
        return this.time.compareTo(o.time);
    }

}
