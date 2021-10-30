package fc.logic;

import java.io.File;
import java.text.DateFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.ObservableList;

public class FolderGenerator {

    private final Logger log = Logger.getLogger(FolderGenerator.class.getName());
    private final Dotenv dotenv = Dotenv.load();

    public boolean create(String courseType, String courseDate, ObservableList<String> candidates) {

        if (!errorCheckInputs(courseType, courseDate, candidates)) {
            return false;
        }

        if (!createFileStructure(courseType, courseDate, candidates)) {
            return false;
        }

        return true;
    }

    private boolean errorCheckInputs(String courseType, String courseDate, ObservableList<String> candidates) {
        if (courseType == null
                || (!courseType.equals("Introductory & Foundation") && !courseType.equals("Instructor Course")
                        && !courseType.equals("Re-certification") && !courseType.equals("Assessment Day"))) {
            log.log(Level.SEVERE, "Course type not recognised: " + courseType);
            return false;
        }

        if (candidates == null || candidates.size() <= 0) {
            log.log(Level.SEVERE, "No candidates have been identified");
            return false;
        }

        if (courseDate == null || courseDate.isEmpty()) {
            log.log(Level.SEVERE, "Course Date cannot be empty");
            return false;
        }
        return true;
    }

    private boolean createFileStructure(String courseType, String courseDate, ObservableList<String> candidates) {
        String parsedCourseDate = parseDateValue(courseDate);
        File source = new File(parseFilePath(dotenv.get("ROOT_PATH")) + getDateValue(parsedCourseDate, "year")
                + File.separator + getDateValue(parsedCourseDate, "month"));

        if (!source.exists()) {
            log.log(Level.INFO, "No Source Directory. Creating new Source Directory...");
            if (source.mkdirs()) {
                log.log(Level.INFO, "Source Directory successfully created");
            } else {
                log.log(Level.SEVERE, "Error creating Source Directory");
                return false;
            }
        }

        File course = new File(source.getAbsolutePath() + File.separator + parsedCourseDate + "~" + courseType);

        if (!course.exists()) {
            log.log(Level.INFO, "No pre-existing course detected. Creating new course...");
            if (course.mkdirs()) {
                log.log(Level.INFO, "Course created");
            } else {
                log.log(Level.SEVERE, "Error creating course");
                return false;
            }
        }

        log.log(Level.INFO, "creating " + candidates.size() + " candidate folder" + (candidates.size() > 1 ? "s" : ""));
        for (String candidate : candidates) {
            File candidateFile = new File(course.getAbsolutePath() + File.separator + candidate);
            if (!candidateFile.exists()) {
                if (candidateFile.mkdirs()) {
                    log.log(Level.INFO, "Candidate folder: '" + candidate + "' created");
                } else {
                    log.log(Level.SEVERE, "Error creating candiate " + candidate);
                    return false;
                }
            }
            if (!copyRequiredFiles(candidateFile, courseType)) {
                return false;
            }
        }
        return true;
    }

    private boolean copyRequiredFiles(File candidateFile, String courseType) {
        // create appropriate file structures based upon course type
        return true;
    }

    private String getDateValue(String date, String value) {
        System.out.println(date);
        switch (value) {
        case "year":
            return date.split("-")[0];
        case "month":
            return new DateFormatSymbols().getMonths()[Integer.valueOf(date.split("-")[1]) - 1];
        case "day":
            return date.split("-")[2];
        default:
            throw new IllegalArgumentException(
                    "date value not recognised. Must be 'year', 'month', 'day'. Value: " + value);
        }
    }

    private String parseDateValue(String dateValue) {
        return dateValue.replaceAll("/", "-");
    }

    private String parseFilePath(String rawFilePath) {
        return rawFilePath.replaceAll("[/.\\\\]", File.separator);
    }

}
