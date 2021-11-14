package fc.logic;

import java.io.File;
import java.util.Random;

public class Utils {

    public static String parseFilePath(String rawFilePath) {
        return rawFilePath.replaceAll("[/\\\\]", File.separator);
    }

    public static String parseDateValue(String dateValue) {
        String normalisedDateValue[] = dateValue.replaceAll("/", "-").split("-");
        return normalisedDateValue[2] + "-" + normalisedDateValue[1] + "-" + normalisedDateValue[0];
    }

    public static Integer getRandom(Integer min, Integer max) {
        return new Random().nextInt(max - min) + min;
    }

}
