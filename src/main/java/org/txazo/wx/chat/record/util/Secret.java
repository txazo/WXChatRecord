package org.txazo.wx.chat.record.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 密钥
 */
public abstract class Secret {

    private static String SECRET_FORMAT = "md5([5][2][4])";

    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("xxx"));
    }

}
