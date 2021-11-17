package fc.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FolderGeneratorTest {

    private static FolderGenerator folderGenerator;

    private static final String UNRECOGNISED_COURSE_TITLE = "unrecognised-course-title";
    private static final String UNRECOGNISED_COURSE_DATE = "";
    private static final ObservableList<String> UNRECOGNISED_CANDIDATE_LIST = FXCollections.observableArrayList();

    private static final String RECOGNISED_COURSE_TITLE = "test-course-title";
    private static final String RECOGNISED_COURSE_DATE = "1991/01/01";
    private static final ObservableList<String> RECOGNISED_CANDIDATE_LIST = FXCollections.observableArrayList();

    private static final String ROOT_LOCATION = System.getProperty("user.dir") + File.separator + "dump/";

    @BeforeAll
    static void test() {
        folderGenerator = new FolderGenerator();

        RECOGNISED_CANDIDATE_LIST.add("recognised-candidate-name-one");
        RECOGNISED_CANDIDATE_LIST.add("recognised-candidate-name-two");

    }

    @Test
    void shouldReturnFalseIfCourseTypeNotRecognised() {
        assertFalse(
                folderGenerator.create(UNRECOGNISED_COURSE_TITLE, RECOGNISED_COURSE_DATE, RECOGNISED_CANDIDATE_LIST));
    }

    @Test
    void shouldReturnFalseIfCourseTypeIsNull() {
        assertFalse(folderGenerator.create(null, RECOGNISED_COURSE_DATE, RECOGNISED_CANDIDATE_LIST));
    }

    @Test
    void shouldReturnFalseIfCourseDateIsEmpty() {
        assertFalse(
                folderGenerator.create(RECOGNISED_COURSE_TITLE, UNRECOGNISED_COURSE_DATE, RECOGNISED_CANDIDATE_LIST));
    }

    @Test
    void shouldReturnFalseIfCourseDateIsNull() {
        assertFalse(folderGenerator.create(RECOGNISED_COURSE_TITLE, null, RECOGNISED_CANDIDATE_LIST));
    }

    @Test
    void shouldReturnFalseIfCandidateSizeIs0() {
        assertFalse(
                folderGenerator.create(RECOGNISED_COURSE_TITLE, RECOGNISED_COURSE_DATE, UNRECOGNISED_CANDIDATE_LIST));
    }

    @Test
    void shouldReturnFalseIfCandidateIsNull() {
        assertFalse(folderGenerator.create(RECOGNISED_COURSE_TITLE, RECOGNISED_COURSE_DATE, null));
    }

    @Test
    void shouldReturnTrueIfNoErrors() {
        assertTrue(folderGenerator.create(RECOGNISED_COURSE_TITLE, RECOGNISED_COURSE_DATE, RECOGNISED_CANDIDATE_LIST));
    }

    @Test
    void shouldCreateAppropriateFileStructure() {
        folderGenerator.overrideRootLocation(ROOT_LOCATION);
        folderGenerator.create(RECOGNISED_COURSE_TITLE, RECOGNISED_COURSE_DATE, RECOGNISED_CANDIDATE_LIST);

        String parsedDateValue = Utils.parseDateValue(RECOGNISED_COURSE_DATE);

        assertTrue(new File(ROOT_LOCATION).exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2]).exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January")
                .exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January"
                + File.separator + parsedDateValue + "~" + RECOGNISED_COURSE_TITLE).exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January"
                + File.separator + parsedDateValue + "~" + RECOGNISED_COURSE_TITLE + File.separator
                + "Attendance Sheet.docx").exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January"
                + File.separator + parsedDateValue + "~" + RECOGNISED_COURSE_TITLE + File.separator
                + "Evaluation Collation Form.docx").exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January"
                + File.separator + parsedDateValue + "~" + RECOGNISED_COURSE_TITLE + File.separator
                + "Issues Arising Form.docx").exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January"
                + File.separator + parsedDateValue + "~" + RECOGNISED_COURSE_TITLE + File.separator
                + RECOGNISED_CANDIDATE_LIST.get(0)).exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January"
                + File.separator + parsedDateValue + "~" + RECOGNISED_COURSE_TITLE + File.separator
                + RECOGNISED_CANDIDATE_LIST.get(0)).listFiles().length == 8);
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January"
                + File.separator + parsedDateValue + "~" + RECOGNISED_COURSE_TITLE + File.separator
                + RECOGNISED_CANDIDATE_LIST.get(1)).exists());
        assertTrue(new File(ROOT_LOCATION + File.separator + parsedDateValue.split("-")[2] + File.separator + "January"
                + File.separator + parsedDateValue + "~" + RECOGNISED_COURSE_TITLE + File.separator
                + RECOGNISED_CANDIDATE_LIST.get(1)).listFiles().length == 8);

        folderGenerator.deleteOnExit();
    }

}
