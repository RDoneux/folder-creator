package fc.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.github.cdimascio.dotenv.Dotenv;

public class SettingsPageController {

    private final Dotenv dotenv = Dotenv.load();

    @FXML
    public CheckBox issuesArrisingForm;

    @FXML
    public CheckBox attendanceSheet;

    @FXML
    public CheckBox evaluationCoelationForm;

    @FXML
    public CheckBox presentationsFolder;

    @FXML
    public CheckBox candidateRecordSheet;

    @FXML
    public CheckBox abiThreeRandom;

    @FXML
    public CheckBox abiProactiveWorkingPractices;

    @FXML
    public CheckBox abiKeepingSafe;

    @FXML
    public CheckBox abiPersonSpecific;

    @FXML
    public CheckBox abiRestrictivePersonSpecific;

    @FXML
    public CheckBox presentationFeedback;

    @FXML
    public CheckBox firstCourseProgrammeFeedback;

    @FXML
    public ComboBox<String> courseType;
    private ObservableList<String> courses;

    @FXML
    public void initialize() {
        courses = FXCollections.observableArrayList(dotenv.get("COURSES").split(","));
        courseType.setItems(courses);
    }

    @FXML
    public void handleSaveButtonPress() {

    }

    @FXML
    public void handleReturnButtonPress() {
        Client.changeScene("main-page");
    }

    @FXML
    public void handleCourseTypeChange() {
        JSONObject jsonFile;
        try {
            jsonFile = (JSONObject) new JSONParser()
                    .parse(new FileReader(System.getProperty("user.dir") + File.separator + "settings.json"));
            JSONObject courseTypeJSON = (JSONObject) jsonFile.get(courseType.getValue());

            issuesArrisingForm.setSelected(Boolean.parseBoolean(courseTypeJSON.get("issues arrising form").toString()));
            attendanceSheet.setSelected(Boolean.parseBoolean(courseTypeJSON.get("attendance sheet").toString()));
            evaluationCoelationForm.setSelected(Boolean.parseBoolean(courseTypeJSON.get("evaluation form").toString()));
            presentationsFolder.setSelected(Boolean.parseBoolean(courseTypeJSON.get("presentation folder").toString()));
            candidateRecordSheet
                    .setSelected(Boolean.parseBoolean(courseTypeJSON.get("candidate record sheet").toString()));
            abiThreeRandom.setSelected(Boolean.parseBoolean(courseTypeJSON.get("abi three random").toString()));
            abiProactiveWorkingPractices.setSelected(
                    Boolean.parseBoolean(courseTypeJSON.get("abi proactive working practices").toString()));
            abiKeepingSafe.setSelected(Boolean.parseBoolean(courseTypeJSON.get("abi keeping safe").toString()));
            abiPersonSpecific.setSelected(Boolean.parseBoolean(courseTypeJSON.get("abi person specific").toString()));
            abiRestrictivePersonSpecific.setSelected(
                    Boolean.parseBoolean(courseTypeJSON.get("abi restrictive person specific").toString()));
            presentationFeedback
                    .setSelected(Boolean.parseBoolean(courseTypeJSON.get("presentation feedback").toString()));
            firstCourseProgrammeFeedback
                    .setSelected(Boolean.parseBoolean(courseTypeJSON.get("first course programme").toString()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void handleStateChange() {
        System.out.println("called");
        JSONObject jsonFile;
        try {
            jsonFile = (JSONObject) new JSONParser()
                    .parse(new FileReader(System.getProperty("user.dir") + File.separator + "settings.json"));
            JSONObject courseTypeJSON = (JSONObject) jsonFile.get(courseType.getValue());

            courseTypeJSON.put("issues arrising form", issuesArrisingForm.isSelected());
            courseTypeJSON.put("attendance sheet", attendanceSheet.isSelected());
            courseTypeJSON.put("evaluation form", evaluationCoelationForm.isSelected());
            courseTypeJSON.put("presentation folder", presentationsFolder.isSelected());
            courseTypeJSON.put("candidate record sheet", candidateRecordSheet.isSelected());
            courseTypeJSON.put("abi three random", abiThreeRandom.isSelected());
            courseTypeJSON.put("abi proactive working practices",
                    abiProactiveWorkingPractices.isSelected());
            courseTypeJSON.put("abi keeping safe", abiKeepingSafe.isSelected());
            courseTypeJSON.put("abi person specific", abiPersonSpecific.isSelected());
            courseTypeJSON.put("abi restrictive person specific",
                    abiRestrictivePersonSpecific.isSelected());
            courseTypeJSON.put("presentation feedback", presentationFeedback.isSelected());
            courseTypeJSON.put("first course programme", firstCourseProgrammeFeedback.isSelected());

            FileWriter writer = new FileWriter(System.getProperty("user.dir") + File.separator + "settings.json");
            writer.write(jsonFile.toJSONString());
            writer.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleLogoClick() {
        Client.changeScene("main-page");
    }

}
