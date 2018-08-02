package org.txazo.wx.chat.record.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RecordPack implements Serializable {

    private static final long serialVersionUID = 7535352296783944814L;

    private String date;

    private List<Record> records = new ArrayList<>();

    public RecordPack(String date) {
        this.date = date;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

}
