package org.txazo.wx.chat.record.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RecordPack implements Serializable {

    private static final long serialVersionUID = 7535352296783944814L;

    private String date;

    private List<Record> records;

}
