package fc.logic;

import java.io.File;

public class Utils {
    
    public static String parseFilePath(String rawFilePath) {
        return rawFilePath.replaceAll("[/\\\\]", File.separator);
    }

}
