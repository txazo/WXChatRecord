package org.txazo.wx.chat.record.core;

import org.txazo.wx.chat.record.mail.MailReceiver;

public class WXChatRecordExecutor {

    public static void execute() throws Exception {
        MailReceiver.execute();
    }

}
