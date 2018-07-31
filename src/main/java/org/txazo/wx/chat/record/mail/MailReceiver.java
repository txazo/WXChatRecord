package org.txazo.wx.chat.record.mail;

import org.apache.commons.io.FileUtils;

import javax.mail.*;
import java.io.File;
import java.util.Properties;

public class MailReceiver {

    public static void main(String[] args) throws Exception {
        String protocol = "pop3";

        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.pop3.port", "110");
        prop.setProperty("mail.pop3.host", "pop3.163.com");

        Session session = Session.getDefaultInstance(prop);
        session.setDebug(false);

        Store store = session.getStore(protocol);
        store.connect("txazo1218@163.com", "xxxx");

        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        Message[] messages = folder.getMessages(folder.getMessageCount(), folder.getMessageCount());
        for (Message message : messages) {
            if (message.getSubject().equals("\"小Q\"和\"小洲\"的聊天记录")) {
                StringBuilder content = new StringBuilder();
                parseMessageContent(message, content);
                System.out.println(content.toString());
            }
        }

        folder.close(true);
        store.close();
    }

    private static void parseMessageContent(Part part, StringBuilder content) throws Exception {
        if (part.isMimeType("text/*")) {
            content.append(part.getContent());
        } else if (part.isMimeType("message/rfc822")) {
            parseMessageContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                parseMessageContent(multipart.getBodyPart(i), content);
            }
        } else if (part.isMimeType("application/pdf")) {
            FileUtils.copyInputStreamToFile(part.getInputStream(), new File("/Users/txazo/TxazoProject/WXChatRecord/" + part.getFileName()));
        }
    }

}
