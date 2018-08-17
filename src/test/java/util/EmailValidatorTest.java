package util;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

public class EmailValidatorTest {
    @Test
    public void testIsValid_valid() {
        final String email = "owej_-f1289874@ioij3l.32io.cn";
        assertTrue(EmailValidator.isVaild(email));
    }

    @Test
    public void testIsValid_invalid_no_AT() {
        final String email = "thelo-_ve1289874_ioij3l.32io.cn.co";
        assertFalse(EmailValidator.isVaild(email));
    }

    @Test
    public void testIsValid_invalid_hasSpace() {
        final String email = "ow128_874@io i32io.cn";
        assertFalse(EmailValidator.isVaild(email));
    }
}
