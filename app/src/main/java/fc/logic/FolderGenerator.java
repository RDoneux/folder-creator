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
    private String rootLocation;
    private JSONObject courseSettings;
    private JSONObject settings;

    public boolean create(String courseType, String courseDate, ObservableList<String> candidates) {
        try {
            FileReader reader = new FileReader(
                    System.getProperty("user.dir") + Utils.parseFilePath(dotenv.get("SETTINGS_PATH")));
            settings = (JSONObject) new JSONParser().parse(reader);
            reader.close();
            courseSettings = (JSONObject) settings.get(courseType);
            if (rootLocation == null)
                rootLocation = Utils.parseFilePath(settings.get("ROOT_PATH").toString());
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

    public FolderGenerator overrideRootLocation(String overriddenPath) {
        rootLocation = Utils.parseFilePath(overriddenPath);
        log.log(Level.INFO, "Root file location has been overridden to: " + rootLocation
                + ". This may make the application unstable.");
        MainPageController.log("Root file location has been overridden to: " + rootLocation
                + ". This may make the application unstable.");

        return this;
    }

    public void deleteOnExit() {
        deleteStructureOnExit(new File(rootLocation));
    }

    private void deleteStructureOnExit(File files) {
        for (File file : files.listFiles()) {
            if (file.isDirectory())
                deleteStructureOnExit(file);
            file.delete();
        }
    }

    private boolean errorCheckInputs(String courseType, String courseDate, ObservableList<String> candidates) {
        if (courseType == null || (!courseType.equals("Introductory & Foundation")
                && !courseType.equals("Instructor Course") && !courseType.equals("Re-certification")
                && !courseType.equals("Assessment Day") && !courseType.equals("test-course-title"))) {
            log.log(Level.SEVERE, "Course type not recognised: " + courseType);
            MainPageController.log("Course type not recognised: " + courseType, Color.RED);
            return false;
        }

        if (candidates == null || candidates.size() <= 0) {
            log.log(Level.SEVERE, "No candidates have been identified");
            MainPageController.log("No candidates have been identified", Color.RED);
            return false;
        }

        if (courseDate == null || courseDate.isEmpty()) {
            log.log(Level.SEVERE, "Course Date cannot be empty");
            MainPageController.log("Course Date cannot be empty", Color.RED);
            return false;
        }
        return true;
    }

    private boolean createFileStructure(String courseType, String courseDate, ObservableList<String> candidates) {
        String parsedCourseDate = Utils.parseDateValue(courseDate);
        File source = new File(rootLocation + getDateValue(parsedCourseDate, "year") + File.separator
                + getDateValue(parsedCourseDate, "month"));

        if (!source.exists()) {
            log.log(Level.INFO, "No Source Directory. Creating new Source Directory...");
            MainPageController.log("No Source Directory. Creating new Source Directory...", Color.BLACK);
            if (source.mkdirs()) {
                log.log(Level.INFO, "Source Directory successfully created");
                MainPageController.log("Source Directory successfully created", Color.BLACK);
                MainPageController.logLineBreak();
            } else {
                log.log(Level.SEVERE, "Error creating Source Directory");
                MainPageController.log("Error creating Source Directory", Color.RED);
                return false;
            }
        }

        File course = new File(source.getAbsolutePath() + File.separator + parsedCourseDate + "~" + courseType);

        if (!course.exists()) {
            log.log(Level.INFO, "No pre-existing course detected. Creating new course...");
            MainPageController.log("No pre-existing course detected. Creating new course...", Color.BLACK);
            if (course.mkdirs()) {
                log.log(Level.INFO, "Course created ('" + parsedCourseDate + "')");
                MainPageController.log("Course created ('" + parsedCourseDate + "')", Color.BLACK);
                MainPageController.logLineBreak();
            } else {
                log.log(Level.SEVERE, "Error creating course");
                MainPageController.log("Error creating course", Color.RED);
                return false;
            }
        }

        if (!copyGeneralFiles(course, courseType)) {
            return false;
        }

        log.log(Level.INFO,
                "creating " + candidates.size() + " candidate folder" + (candidates.size() > 1 ? "s" : "") + ":");
        MainPageController.log(
                "Creating " + candidates.size() + " candidate folder" + (candidates.size() > 1 ? "s" : "") + ":",
                Color.BLACK);
        Integer skipped = 0;
        for (String candidate : candidates) {
            File candidateFile = new File(course.getAbsolutePath() + File.separator + candidate);
            if (!candidateFile.exists()) {
                if (candidateFile.mkdirs()) {
                    log.log(Level.INFO, "Candidate folder: '" + candidate + "' created");
                    MainPageController.log("Candidate folder: '" + candidate + "' created", Color.BLACK);
                } else {
                    log.log(Level.SEVERE, "Error creating candiate " + candidate);
                    MainPageController.log("Error creating candiate " + candidate, Color.RED);
                    return false;
                }
            } else {
                skipped++;
                log.log(Level.INFO, "Candidate folder " + candidate + " already exists. Skipping...");
                MainPageController.log("Candidate folder " + candidate + " already exists. Skipping...", Color.ORANGE);
            }
            if (!copyCandidateFiles(candidateFile, courseType)) {
                return false;
            }
        }
        final Integer candidateSize = candidates.size() - skipped;
        log.log(Level.INFO, candidateSize + " new folder" + (candidateSize != 1 ? "s" : "") + " created successfully.");
        log.log(Level.INFO, skipped + " folder" + (skipped != 1 ? "s" : "") + " skipped.");
        MainPageController.logLineBreak();
        MainPageController.log(
                candidateSize + " new folder" + (candidateSize != 1 ? "s" : "") + " created successfully", Color.GREEN);
        MainPageController.log(skipped + " folder" + (skipped != 1 ? "s" : "") + " skipped.", Color.ORANGE);
        return true;
    }

    private boolean copyCandidateFiles(File candidateFile, String courseType) {
        try {
            if (getSetting("candidate record sheet")) {
                copyFile("forms/record/General - Record Sheet", candidateFile.getAbsolutePath(),
                        candidateFile.getName() + " ~ Record Sheet");
            }
            if (getSetting("presentation feedback")) {
                copyFile("forms/record/" + courseType + " - Presentation Feedback", candidateFile.getAbsolutePath(),
                        candidateFile.getName() + " ~ Presentation Feedback");
                if (courseType.equals("Instructor Course")) {
                    // add in presentation feedback day 4
                }
            }
            if (getSetting("first course programme")) {
                copyFile("forms/record/First Course Feedback", candidateFile.getAbsolutePath(),
                        candidateFile.getName() + " ~ First Course Feedback");
            }
            if (getSetting("abi three random")) {
                copyFile("forms/record/abi" + Utils.getRandom(0, 5), candidateFile.getAbsolutePath(),
                        "Audit-based intervention Assessment Form");
            }
            if (getSetting("abi proactive working practices")) {
                copyFile("forms/record/General - Physical Intervention Records PWP", candidateFile.getAbsolutePath(),
                        "Audit-based Interventions PWP");
            }
            if (getSetting("abi proactive working practices")) {
                copyFile("forms/record/General - Physical Intervention Records KS", candidateFile.getAbsolutePath(),
                        "Audit-based Interventions KS");
            }
            if (getSetting("abi proactive working practices")) {
                copyFile("forms/record/General - Physical Intervention Records PS", candidateFile.getAbsolutePath(),
                        "Audit-based Interventions PS");
            }
            if (getSetting("abi proactive working practices")) {
                copyFile("forms/record/General - Physical Intervention Records RPS", candidateFile.getAbsolutePath(),
                        "Audit-based Interventions RPS");
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage() + "' because: " + e.getMessage());
            MainPageController.log(e.getMessage() + "' because: " + e.getMessage(), Color.RED);
            return false;
        }
        return true;
    }

    private boolean copyGeneralFiles(File courseFile, String courseType) {
        log.log(Level.INFO, "Copying general documents");
        MainPageController.log("Copying general documents", Color.BLACK);
        MainPageController.logLineBreak();

        try {
            if (getSetting("issues arrising form")) {
                copyFile("forms/general/Issues Arising Form", courseFile.getAbsolutePath(), "Issues Arising Form");
            }
            if (getSetting("attendance sheet")) {
                copyFile("forms/general/Attendance Sheet", courseFile.getAbsolutePath(), "Attendance Sheet");
            }
            if (getSetting("evaluation form")) {
                copyFile("forms/general/Evaluation Form", courseFile.getAbsolutePath(), "Evaluation Collation Form");
            }
            if (getSetting("presentation folder")) {
                new File(courseFile.getAbsolutePath() + File.separator + "0. presentations").mkdirs();
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, "Error creating general documents caused by " + e.getLocalizedMessage());
            MainPageController.log("Error creating general documents caused by " + e.getLocalizedMessage(), Color.RED);
            return false;
        }
        return true;
    }

    private Boolean getSetting(String setting) {
        return Boolean.parseBoolean(courseSettings.get(setting).toString());
    }

    private void copyFile(String source, String destination, String fileName) throws IOException {
        FileUtils.copyURLToFile(this.getClass().getClassLoader().getResource(source + ".docx"),
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

}
