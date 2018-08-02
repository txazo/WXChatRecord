package org.txazo.wx.chat.record.mail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Splitter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.txazo.wx.chat.record.bean.MailContent;
import org.txazo.wx.chat.record.util.FileUtil;
import org.txazo.wx.chat.record.util.PathUtil;

import javax.mail.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MailReceiver {

    private static Properties emailProps = new Properties();
    private static Properties accountProps = new Properties();

    static {
        init();
    }

    private static void init() {
        try {
            emailProps.load(new InputStreamReader(PathUtil.getResourceAsStream("/mail.properties"), "UTF-8"));
            accountProps.load(new InputStreamReader(PathUtil.getResourceAsStream("/account.properties"), "UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void execute() throws Exception {
        String protocol = emailProps.getProperty("mail.transport.protocol");

        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.pop3.port", emailProps.getProperty("mail.pop3.port"));
        prop.setProperty("mail.pop3.host", emailProps.getProperty("mail.pop3.host"));

        Session session = Session.getDefaultInstance(prop);
        session.setDebug(false);

        Store store = session.getStore(protocol);
        store.connect(accountProps.getProperty("mail.receiver.username"), accountProps.getProperty("mail.receiver.password"));

        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        Date acceptStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(emailProps.getProperty("mail.accept.startDate"));
        Set<String> acceptTitles = new HashSet<>(Splitter.on(",").splitToList(accountProps.getProperty("mail.accept.titles")));

        boolean end = false;
        int pageSize = 20;
        int endIndex = folder.getMessageCount();
        while (!end) {
            Message[] messages = folder.getMessages(endIndex - pageSize + 1, endIndex);
            for (int i = messages.length - 1; i >= 0; i--) {
                Message message = messages[i];
                if (message.getSentDate().before(acceptStartDate)) {
                    end = true;
                    break;
                }

                if (acceptTitles.contains(message.getSubject())) {
                    MailContent content = new MailContent();
                    content.setId(DateFormatUtils.format(message.getSentDate(), "yyyyMMddHHmmss"));
                    content.setTitle(message.getSubject());
                    parseMessageContent(message, content);
                    storage(content);
                }
            }
            endIndex -= pageSize;
        }

        folder.close(true);
        store.close();
    }

    private static void parseMessageContent(Part part, MailContent content) throws Exception {
        if (part.isMimeType("text/plain")) {
            content.setText(part.getContent().toString());
        } else if (part.isMimeType("text/html")) {
            content.setHtml(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            parseMessageContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                parseMessageContent(multipart.getBodyPart(i), content);
            }
        } else if (part.isMimeType("application/pdf")) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(part.getInputStream(), baos);
            byte[] byteArray = baos.toByteArray();
            String md5 = DigestUtils.md5Hex(byteArray);
            File imageFile = new File(PathUtil.getDBImagePath() + md5 + ".png");
            if (!imageFile.exists()) {
                FileUtils.copyInputStreamToFile(new ByteArrayInputStream(byteArray), imageFile);
            }
            content.getImages().put(part.getFileName(), md5);
        }
    }

    private static void storage(MailContent content) throws Exception {
        FileUtil.writeFile(content.getText(), PathUtil.getDBRecordTextPath() + content.getId());
        FileUtil.writeFile(content.getHtml(), PathUtil.getDBRecordHtmlPath() + content.getId());
        String text = FileUtils.readFileToString(new File(PathUtil.getDBRecordMetaPath()), "UTF-8");
        Set<MailContent> contents = new HashSet<>();
        if (StringUtils.isNotBlank(text)) {
            contents.addAll(JSON.parseArray(text, MailContent.class));
        }
        if (!contents.contains(content)) {
            contents.add(content);
            FileUtil.writeFile(JSON.toJSONString(contents, SerializerFeature.PrettyFormat), PathUtil.getDBRecordMetaPath());
        }
    }

}
