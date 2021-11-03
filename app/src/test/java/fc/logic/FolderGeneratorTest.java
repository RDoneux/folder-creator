package fc.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FolderGeneratorTest {

    private static FolderGenerator folderGenerator;

    private static final String UNRECOGNISED_COURSE_TITLE = "unrecognised-course-title";
    private static final String UNRECOGNISED_COURSE_DATE = "";
    private static final ObservableList<String> UNRECOGNISED_CANDIDATE_LIST = FXCollections.observableArrayList();

    private static final String RECOGNISED_COURSE_TITLE = "Introductory & Foundation";
    private static final String RECOGNISED_COURSE_DATE = "1991/01/01";
    private static final ObservableList<String> RECOGNISED_CANDIDATE_LIST = FXCollections.observableArrayList();

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
    void shouldCreateAppropriateFileStructure() {
        assertTrue(folderGenerator.create(RECOGNISED_COURSE_TITLE, RECOGNISED_COURSE_DATE, RECOGNISED_CANDIDATE_LIST));
    }

}
