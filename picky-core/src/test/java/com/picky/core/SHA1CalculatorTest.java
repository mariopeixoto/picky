package com.picky.core;

import com.picky.core.api.HashCalculator;
import com.picky.core.impl.SHA1Calculator;
import com.picky.util.IOUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SHA1CalculatorTest {

    private HashCalculator SHA1Calculator = new SHA1Calculator();

    @Test
    public void test() throws IOException {
        //TODO Instrument: started testCase
        InputStream file = IOUtil.getClassFile(this.getClass());
        String hash = SHA1Calculator.calculate(file);

        assertNotNull(hash);
        //TODO Instrument: ended testCase?
    }

}
