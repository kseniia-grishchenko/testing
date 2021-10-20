import org.testng.annotations.*;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

import static org.testng.Assert.*;

public class MainTest {
    private Path workingDir;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeSuite(groups = { "test-get-scanner", "test-main" })
    void initDir() {
        this.workingDir = Path.of("", "src/test/resources");
    }

    @BeforeMethod(groups = { "test-get-scanner", "test-main" })
    public void setStreams() {
        outContent.reset();
        System.setOut(new PrintStream(outContent));
    }

    @AfterMethod(groups = { "test-get-scanner", "test-main" })
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test(groups = { "test-get-scanner" })
    public void getScannerWrongPathname() {
        String filename = String.valueOf(this.workingDir.resolve("in_1.txt"));

        assertThrows(FileNotFoundException.class, () -> Main.getScanner(filename));
    }

    @Test(groups = { "test-get-scanner" })
    public void getScannerCorrectPathname() throws FileNotFoundException {
        String filename = String.valueOf(this.workingDir.resolve("in.txt"));
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        assertNotNull(Main.getScanner(filename));
        assertEquals(scanner, Main.getScanner(filename));
    }

    @Test(groups = { "test-main" })
    public void mainWrongPathname() {
        String input = "wrong.txt";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Main.main(null);

        assertTrue(outContent.toString().contains("Invalid file pathname"));
    }

    @Test(groups = { "test-main" })
    public void mainCorrectPathnameResponseYes() {
        String filename = String.valueOf(this.workingDir.resolve("in.txt"));
        String input = String.format("%1$s 1", filename);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Main.main(null);

        assertTrue(outContent.toString().contains("YES") );
    }

    @Test(groups = { "test-main" })
    public void mainCorrectPathnameResponseNo() {
        String filename = String.valueOf(this.workingDir.resolve("in.txt"));
        String input = String.format("%1$s 2", filename);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Main.main(null);

        assertTrue(outContent.toString().contains("NO") );
    }

    @DataProvider
    public Object[][] resolveFileContents() {
        return new Object[][]{
                {"src/test/resources/in.txt 2", "NO"},
                {"src/test/resources/in.txt 1", "YES"},
                {"src/test/resources/in.txt 4", "NO"},
        };
    }

    @Test(dataProvider = "resolveFileContents", groups = { "test-get-scanner" })
    public void testAutomataSteps(String input, String expectedResponse) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.main(null);

        assertTrue(outContent.toString().contains(expectedResponse));
    }
}