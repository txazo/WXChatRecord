package org.txazo.wx.chat.record.util;

import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.OutputStream;

public abstract class FileUtil {

    public static void writeFile(String text, String path) throws Exception {
        OutputStream output = new FileOutputStream(path);
        IOUtils.write(text, output);
        IOUtils.closeQuietly(output);
    }

}
