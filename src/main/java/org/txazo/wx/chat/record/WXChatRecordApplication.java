package org.txazo.wx.chat.record;

import org.txazo.wx.chat.record.core.WXChatRecordExecutor;
import org.txazo.wx.chat.record.util.ProjectPathUtil;

public class WXChatRecordApplication {

    public static void main(String[] args) throws Exception {
//        WXChatRecordExecutor.execute();
        System.out.println(ProjectPathUtil.getClassPath());
        System.out.println(ProjectPathUtil.getProjectPath());
    }

}
