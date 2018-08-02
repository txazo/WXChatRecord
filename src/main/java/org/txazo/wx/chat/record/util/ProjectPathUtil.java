package org.txazo.wx.chat.record.util;

import java.io.InputStream;

public class ProjectPathUtil {

    public static InputStream getResourceAsStream(String name) {
        return ProjectPathUtil.class.getResourceAsStream(name);
    }

    public static String getClassPath() {
        return ProjectPathUtil.class.getResource("/").getPath();
    }

    public static String getProjectPath() {
        return getClassPath().replace("/target/classes/", "");
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

}
