package org.txazo.wx.chat.record.format;

import org.apache.commons.io.IOUtils;
import org.txazo.wx.chat.record.bean.Record;
import org.txazo.wx.chat.record.util.PathUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RecordFormatter {

    private static String CHAT_LEFT_TEMPLATE;
    private static String CHAT_RIGHT_TEMPLATE;

    static {
        try {
            CHAT_LEFT_TEMPLATE = IOUtils.toString(RecordFormatter.class.getResourceAsStream("/chat_left.html"));
            CHAT_RIGHT_TEMPLATE = IOUtils.toString(RecordFormatter.class.getResourceAsStream("/chat_right.html"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void format(String date, List<Record> records) throws IOException {
        File file = new File(PathUtil.getFormatPath() + "/" + date + ".md");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write("---");
        bw.newLine();
        bw.write("layout: default");
        bw.newLine();
        bw.write("title:  " + date);
        bw.newLine();
        bw.write("---");
        bw.newLine();
        bw.newLine();
        bw.write("<div class=\"chat\">");
        bw.write("<ul>");
        for (Record c : records) {
            bw.write("<li>");
            bw.write("<div class=\"chat_time\"><span>" + c.getTime() + "</span></div>");
            bw.write((c.getType() == 2 ? CHAT_LEFT_TEMPLATE : CHAT_RIGHT_TEMPLATE)
                    .replace("${nickName}", c.getSender()).replace("${message}", c.getMessage()));
            bw.write("<div style=\"clear: both\"/>");
            bw.write("</li>");
        }
        bw.newLine();
        bw.write("</ul>");
        bw.newLine();
        bw.write("</div>");
        bw.newLine();
        bw.close();
    }

}
