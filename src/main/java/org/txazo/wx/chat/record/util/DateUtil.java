package org.txazo.wx.chat.record.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtil {

    public static Date parseYMDHM(String dateTime) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm");
        return dateFormat.parse(dateTime);
    }

}
