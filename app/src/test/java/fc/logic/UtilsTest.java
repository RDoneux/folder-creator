package fc.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

public class UtilsTest {

    public static final String unparsedFilePath = "test/file\\path/to/be\\parsed";
    public static final String unparsedDateValue = "1991/07/17";

    @Test
    void shouldParseFilePath() {
        assertEquals(Utils.parseFilePath(unparsedFilePath), "test" + File.separator + "file" + File.separator + "path"
                + File.separator + "to" + File.separator + "be" + File.separator + "parsed");
    }

    @Test
    void shouldParseDateValue() {
        assertEquals(Utils.parseDateValue(unparsedDateValue), "17-07-1991");
    }

    @Test
    void shouldReturnNumberBetweenNegativeAndPositiveRange() {
        final Integer number = Utils.getRandom(-5, 5);
        assertTrue(number >= -5 && number <= 5);
    }

}
