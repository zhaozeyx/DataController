/*******************************************************************************
 * Copyright (C) 2009-2010 eoeMobile.
 * All rights reserved.
 * http://www.eoeMobile.com/
 *
 * CHANGE LOG:
 *  DATE			AUTHOR			COMMENTS
 * =============================================================================
 *  2010MAY11		Waznheng Ma		Refine for Constructor and error handler.
 *
 *******************************************************************************/

package com.demon.library.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5 加密
 * @version 1.0
 */
public final class MD5 {
	private static final String LOG_TAG = "MD5";
	private static final String ALGORITHM = "MD5";

	private static char sHexDigits[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
};
	private static MessageDigest sDigest;

	static {
		try {
			sDigest = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			Log.e(LOG_TAG, "Get MD5 Digest failed.");
//			throw new UnsupportedDigestAlgorithmException(ALGORITHM, e);
		}
	}

	private MD5() {
	}


	final public static String encode(String source) {
		byte[] btyes = source.getBytes();
		byte[] encodedBytes = sDigest.digest(btyes);

		return hexString(encodedBytes);
	}
    /**
     * 对一个文件获取md5值
     * @return md5串
     */
    public static String getFileMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                sDigest.update(buffer, 0, length);
            }


            return hexString(sDigest.digest());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String hexString(byte[] source) {
        if (source == null || source.length <= 0) {
            return "";
        }

        final int size = source.length;
        final char str[] = new char[size * 2];
        int index = 0;
        byte b;
        for (int i = 0; i < size; i++) {
            b = source[i];
            str[index++] = sHexDigits[b >>> 4 & 0xf];
            str[index++] = sHexDigits[b & 0xf];
        }
        return new String(str);
    }
}
