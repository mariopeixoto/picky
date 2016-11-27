package com.picky.core.impl;

import com.picky.core.GeneralException;
import com.picky.core.api.HashCalculator;

import java.io.*;
import java.security.MessageDigest;
import java.util.Formatter;

public class SHA1Calculator implements HashCalculator {

    public String calculate (final InputStream file) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

            InputStream is = new BufferedInputStream(file);
            final byte[] buffer = new byte[1024];
            for (int read = 0; (read = is.read(buffer)) != -1; ) {
                messageDigest.update(buffer, 0, read);
            }
            Formatter formatter = new Formatter();
            for (final byte b : messageDigest.digest()) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (Exception e) {
            throw new GeneralException(e);
        }

    }

}
