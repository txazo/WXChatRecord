package org.txazo.wx.chat.record.parse;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.txazo.wx.chat.record.bean.MailContent;
import org.txazo.wx.chat.record.bean.Record;
import org.txazo.wx.chat.record.bean.RecordPack;
import org.txazo.wx.chat.record.util.PathUtil;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordParser {

    private static final Pattern PATTERN_RECORD = Pattern.compile("^<span.*<b>([^<>]+)<br>.*</span>([^<>]+)\\n<br>\\n<br>$");
    private static final Pattern PATTERN_IMAGE = Pattern.compile("^图片(\\d+)（可在附件中查看）$");

    public static void execute() throws Exception {
        parse();
    }

    private static void parse() throws Exception {
        String text = FileUtils.readFileToString(new File(PathUtil.getDBRecordMetaPath()), "UTF-8");
        if (StringUtils.isBlank(text)) {
            return;
        }

        Map<String, List<Record>> recordMap = new HashMap<>();
        List<MailContent> contents = JSON.parseArray(text, MailContent.class);
        for (MailContent c : contents) {
            parseRecords(c).forEach(pack -> {
                if (recordMap.containsKey(pack.getDate())) {
                    recordMap.get(pack.getDate()).addAll(pack.getRecords());
                } else {
                    recordMap.put(pack.getDate(), pack.getRecords());
                }
            });
        }

        for (Map.Entry<String, List<Record>> entry : recordMap.entrySet()) {
            List<Record> records = entry.getValue();
            Collections.sort(records);
            FileUtils.writeLines(new File(PathUtil.getDBRecordChatPath() + entry.getKey()), records);
        }
    }

    private static List<RecordPack> parseRecords(MailContent content) throws Exception {
        String html = FileUtils.readFileToString(new File(PathUtil.getDBRecordHtmlPath() + content.getId()), "UTF-8");
        Document doc = Jsoup.parse(html);

        RecordPack recordPack = null;
        List<RecordPack> recordPacks = new ArrayList<>();

        int order = 1;
        String date = null;
        Elements elements = doc.select("div > div");
        for (Element e : elements) {
            Element span = e.selectFirst("p > span");
            if (span != null) {
                date = e.selectFirst("span").text().replaceAll("—", "").replaceAll(" ", "");
                recordPack = new RecordPack(date);
                recordPacks.add(recordPack);
            } else {
                Matcher matcher = PATTERN_RECORD.matcher(e.html());
                if (matcher.find()) {
                    String text = matcher.group(1);
                    String[] texts = text.split("\\s");
                    String message = matcher.group(2);

                    Record record = new Record();
                    record.setSender(texts[0]);
                    record.setTime(date + " " + texts[1]);
                    record.setMessage(message);
                    record.setType(texts[0].equals("小洲") ? 1 : 2);
                    record.setOrder(order++);

                    matcher = PATTERN_IMAGE.matcher(record.getMessage());
                    if (matcher.find()) {
                        record.setMessage("/images/" + content.getImages().get("__" + matcher.group(1) + ".png") + ".png");
                    }

                    recordPack.addRecord(record);
                }
            }
        }
        return recordPacks;
    }

}
