package org.txazo.wx.chat.record.core;

import org.txazo.wx.chat.record.mail.MailReceiver;
import org.txazo.wx.chat.record.parse.RecordParser;
import org.txazo.wx.chat.record.util.PathUtil;

import java.io.File;

public class WXChatRecordExecutor {

    public static void execute() throws Exception {
        init();
//        MailReceiver.execute();
        RecordParser.execute();
    }

    private static void init() throws Exception {
        new File(PathUtil.getDBImagePath()).mkdirs();
        new File(PathUtil.getDBRecordTextPath()).mkdirs();
        new File(PathUtil.getDBRecordHtmlPath()).mkdirs();
        new File(PathUtil.getDBRecordChatPath()).mkdirs();
        File metaFile = new File(PathUtil.getDBRecordMetaPath());
        if (!metaFile.exists()) {
            metaFile.createNewFile();
        }
    }

}
