package fc.logic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fc.frontend.MainPageController;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class FolderGenerator {

    private final Logger log = Logger.getLogger(FolderGenerator.class.getName());
    private final Dotenv dotenv = Dotenv.load();
    private JSONObject settings;

    public boolean create(String courseType, String courseDate, ObservableList<String> candidates) {

        try {
            JSONObject object = (JSONObject) new JSONParser().parse(
                    new FileReader(System.getProperty("user.dir") + Utils.parseFilePath(dotenv.get("SETTINGS_PATH"))));
            settings = (JSONObject) object.get(courseType);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

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
            MainPageController.debugConsole.addText("Course type not recognised: " + courseType, Color.RED);
            return false;
        }

        if (candidates == null || candidates.size() <= 0) {
            log.log(Level.SEVERE, "No candidates have been identified");
            MainPageController.debugConsole.addText("No candidates have been identified", Color.RED);
            return false;
        }

        if (courseDate == null || courseDate.isEmpty()) {
            log.log(Level.SEVERE, "Course Date cannot be empty");
            MainPageController.debugConsole.addText("Course Date cannot be empty", Color.RED);
            return false;
        }
        return true;
    }

    private boolean createFileStructure(String courseType, String courseDate, ObservableList<String> candidates) {
        String parsedCourseDate = parseDateValue(courseDate);
        File source = new File(Utils.parseFilePath(dotenv.get("ROOT_PATH")) + getDateValue(parsedCourseDate, "year")
                + File.separator + getDateValue(parsedCourseDate, "month"));

        if (!source.exists()) {
            log.log(Level.INFO, "No Source Directory. Creating new Source Directory...");
            MainPageController.debugConsole.addText("No Source Directory. Creating new Source Directory...",
                    Color.BLACK);
            if (source.mkdirs()) {
                log.log(Level.INFO, "Source Directory successfully created");
                MainPageController.debugConsole.addText("Source Directory successfully created", Color.BLACK);
                MainPageController.debugConsole.lineBreak();
            } else {
                log.log(Level.SEVERE, "Error creating Source Directory");
                MainPageController.debugConsole.addText("Error creating Source Directory", Color.RED);
                return false;
            }
        }

        File course = new File(source.getAbsolutePath() + File.separator + parsedCourseDate + "~" + courseType);

        if (!course.exists()) {
            log.log(Level.INFO, "No pre-existing course detected. Creating new course...");
            MainPageController.debugConsole.addText("No pre-existing course detected. Creating new course...",
                    Color.BLACK);
            if (course.mkdirs()) {
                log.log(Level.INFO, "Course created ('" + parsedCourseDate + "')");
                MainPageController.debugConsole.addText("Course created ('" + parsedCourseDate + "')", Color.BLACK);
                MainPageController.debugConsole.lineBreak();
            } else {
                log.log(Level.SEVERE, "Error creating course");
                MainPageController.debugConsole.addText("Error creating course", Color.RED);
                return false;
            }
        }

        if (!copyGeneralFiles(course, courseType)) {
            return false;
        }

        log.log(Level.INFO,
                "creating " + candidates.size() + " candidate folder" + (candidates.size() > 1 ? "s" : "") + ":");
        MainPageController.debugConsole.addText(
                "Creating " + candidates.size() + " candidate folder" + (candidates.size() > 1 ? "s" : "") + ":",
                Color.BLACK);
        Integer skipped = 0;
        for (String candidate : candidates) {
            File candidateFile = new File(course.getAbsolutePath() + File.separator + candidate);
            if (!candidateFile.exists()) {
                if (candidateFile.mkdirs()) {
                    log.log(Level.INFO, "Candidate folder: '" + candidate + "' created");
                    MainPageController.debugConsole.addText("Candidate folder: '" + candidate + "' created",
                            Color.BLACK);
                } else {
                    log.log(Level.SEVERE, "Error creating candiate " + candidate);
                    MainPageController.debugConsole.addText("Error creating candiate " + candidate, Color.RED);
                    return false;
                }
            } else {
                skipped++;
                log.log(Level.INFO, "Candidate folder " + candidate + " already exists. Skipping...");
                MainPageController.debugConsole
                        .addText("Candidate folder " + candidate + " already exists. Skipping...", Color.ORANGE);
            }
            if (!copyCandidateFiles(candidateFile, courseType)) {
                return false;
            }
        }
        final Integer candidateSize = candidates.size() - skipped;
        log.log(Level.INFO, candidateSize + " new folder" + (candidateSize > 1 ? "s" : "") + " created successfully");
        log.log(Level.INFO, skipped + " folder " + (skipped > 1 ? "s" : "") + "skipped.");
        MainPageController.debugConsole.lineBreak();
        MainPageController.debugConsole.addText(
                candidateSize + " new folder" + (candidateSize > 1 ? "s" : "") + " created successfully", Color.GREEN);
        MainPageController.debugConsole.addText(skipped + " folder" + (skipped > 1 ? "s" : "") + " skipped.");
        return true;
    }

    private boolean copyCandidateFiles(File candidateFile, String courseType) {
        try {
            copyFile("forms/record/General - Record Sheet V1.docx", candidateFile.getAbsolutePath(),
                    candidateFile.getName() + "~Record Sheet" + ".docx");

        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage() + "' because: " + e.getMessage());
            MainPageController.debugConsole.addText(e.getMessage() + "' because: " + e.getMessage(), Color.RED);
            return false;
        }
        return true;
    }

    private boolean copyGeneralFiles(File courseFile, String courseType) {
        log.log(Level.INFO, "Copying general documents");
        MainPageController.debugConsole.addText("Copying general documents", Color.BLACK);
        MainPageController.debugConsole.lineBreak();

        try {
            if (getSetting("issues arrising form")) {
                copyFile("forms/general/Issues Arising Form.docx", courseFile.getAbsolutePath(),
                        "Issues Arrising.docx");
            }
            if (getSetting("attendance sheet")) {
                copyFile("forms/general/Attendance Sheet.docx", courseFile.getAbsolutePath(), "Attendance Sheet.docx");
            }
            if (getSetting("evaluation form")) {
                //copyFile("forms/general/Evaluation Form.docx", courseFile.getAbsolutePath(), "Evaluation Form.docx");
            }
            if (getSetting("presentation folder")) {
                new File(courseFile.getAbsolutePath() + File.separator + "presentations").mkdirs();
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, "Error creating general documents caused by " + e.getLocalizedMessage());
            MainPageController.debugConsole
                    .addText("Error creating general documents caused by " + e.getLocalizedMessage(), Color.RED);
            return false;
        }
        return true;
    }

    private Boolean getSetting(String setting) {
        System.out.println(setting);
        return Boolean.parseBoolean(settings.get(setting).toString());
    }

    private void copyFile(String source, String destination, String fileName) throws IOException {
        FileUtils.copyURLToFile(this.getClass().getClassLoader().getResource(source),
                new File(destination + File.separator + fileName + ".docx"));
    }

    private String getDateValue(String date, String value) {
        switch (value) {
        case "year":
            return date.split("-")[2];
        case "month":
            return new DateFormatSymbols().getMonths()[Integer.valueOf(date.split("-")[1]) - 1];
        case "day":
            return date.split("-")[0];
        default:
            throw new IllegalArgumentException(
                    "date value not recognised. Must be 'year', 'month', 'day'. Value: " + value);
        }
    }

    private String parseDateValue(String dateValue) {
        String normalisedDateValue[] = dateValue.replaceAll("/", "-").split("-");
        return normalisedDateValue[2] + "-" + normalisedDateValue[1] + "-" + normalisedDateValue[0];
    }

}
