package org.txazo.wx.chat.record.format;

import org.apache.commons.io.IOUtils;
import org.txazo.wx.chat.record.bean.Record;
import org.txazo.wx.chat.record.util.PathUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

    public static void format(String date, List<Record> records) throws Exception {
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

        int lastHour = -1;
        int currentLoopMinute = -1;
        for (Record c : records) {
            boolean showTime = false;
            int hour = getHour(c.getTime());
            int minute = getMinute(c.getTime());

            if (lastHour == -1) {
                showTime = true;
                lastHour = hour;
                currentLoopMinute = minute;
            }

            if (hour != lastHour) {
                showTime = true;
                currentLoopMinute = -1;
            } else if (minute - currentLoopMinute >= 5) {
                showTime = true;
                currentLoopMinute = minute;
            }

            bw.write("<li>");
            if (showTime) {
                bw.write("<div class=\"chat_time\"><span>" + c.getTime() + "</span></div>");
            }
            bw.write((c.getType() == 2 ? CHAT_LEFT_TEMPLATE : CHAT_RIGHT_TEMPLATE)
                    .replace("${nickName}", c.getSender()).replace("${message}", c.getMessage()));
            bw.write("</li>");

            lastHour = hour;
        }
        bw.newLine();
        bw.write("</ul>");
        bw.newLine();
        bw.write("</div>");
        bw.newLine();
        bw.close();
    }

    private static int getHour(String time) {
        String hourStr = time.substring(time.length() - 5, time.length() - 3);
        return hourStr.startsWith("0") ? Integer.parseInt(hourStr.substring(1)) : Integer.parseInt(hourStr);
    }

    private static int getMinute(String time) {
        String minuteStr = time.substring(time.length() - 2, time.length());
        return minuteStr.startsWith("0") ? Integer.parseInt(minuteStr.substring(1)) : Integer.parseInt(minuteStr);
    }

}
