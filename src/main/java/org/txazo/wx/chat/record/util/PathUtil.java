package org.txazo.wx.chat.record.util;

import java.io.InputStream;

public class PathUtil {

    public static InputStream getResourceAsStream(String name) {
        return PathUtil.class.getResourceAsStream(name);
    }

    public static String getClassPath() {
        return PathUtil.class.getResource("/").getPath();
    }

    public static String getProjectPath() {
        return getClassPath().replace("/target/classes/", "").replace("/target/classes", "");
    }

    public static String getDBImagePath() {
        return getProjectPath() + "/db/images/";
    }

    public static String getDBRecordPath() {
        return getProjectPath() + "/db/record/";
    }

    public static String getDBRecordTextPath() {
        return getDBRecordPath() + "/text/";
    }

    public static String getDBRecordHtmlPath() {
        return getDBRecordPath() + "/html/";
    }

    public static String getDBRecordMetaPath() {
        return getDBRecordPath() + "/meta.data";
    }

    public static String getDBRecordChatPath() {
        return getDBRecordPath() + "/chat/";
    }

    public static String getFormatPath() {
        return "/Users/txazo/Txazoc/txazoc.github.io/chat";
    }

}
