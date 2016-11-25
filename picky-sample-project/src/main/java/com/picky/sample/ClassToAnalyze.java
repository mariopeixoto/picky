package com.picky.sample;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

public class ClassToAnalyze {

    private MockClazz.MockEnum mockEnum = MockClazz.MockEnum.ONE;
    private List<String> list = Arrays.asList("1", "2");
    private List<MockClazz> list2 = new ArrayList<MockClazz>();
    private MockClazz mockClas = new MockClazz();

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
