package nl.berwout.api;

import nl.berwout.api.exceptions.InvalidFileFormatException;
import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.services.TextFileParser;
import org.junit.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TextFileParserTest {

    private TextFileParser textFileParser = new TextFileParser();

    @Test
    public void TestGoedVoorbeeld1() throws IOException, URISyntaxException, InvalidFileFormatException{
        ClassLoader classLoader = getClass().getClassLoader();
        String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("GoedVoorbeeld.txt").toURI())));
        List<CourseInstance> result = textFileParser.parse(content);
        //later this value should be 3
        assertThat(result.size(), is(5));
    }

    @Test(expected = InvalidFileFormatException.class)
    public void TestFoutVoorbeeld1() throws IOException, URISyntaxException, InvalidFileFormatException{
        ClassLoader classLoader = getClass().getClassLoader();
        String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("FoutVoorbeeld1.txt").toURI())));
        List<CourseInstance> result = textFileParser.parse(content);
    }

    @Test(expected = InvalidFileFormatException.class)
    public void TestFoutVoorbeeld2() throws IOException, URISyntaxException, InvalidFileFormatException{
        ClassLoader classLoader = getClass().getClassLoader();
        String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("FoutVoorbeeld2.txt").toURI())));
        List<CourseInstance> result = textFileParser.parse(content);
    }

    @Test(expected = InvalidFileFormatException.class)
    public void TestFoutVoorbeeld3() throws IOException, URISyntaxException, InvalidFileFormatException{
        ClassLoader classLoader = getClass().getClassLoader();
        String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("FoutVoorbeeld3.txt").toURI())));
        List<CourseInstance> result = textFileParser.parse(content);
    }

    @Test(expected = InvalidFileFormatException.class)
    public void TestFoutVoorbeeld4() throws IOException, URISyntaxException, InvalidFileFormatException{
        ClassLoader classLoader = getClass().getClassLoader();
        String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("FoutVoorbeeld4.txt").toURI())));
        List<CourseInstance> result = textFileParser.parse(content);
    }

    @Test(expected = InvalidFileFormatException.class)
    public void TestFoutVoorbeeld5() throws IOException, URISyntaxException, InvalidFileFormatException{
        ClassLoader classLoader = getClass().getClassLoader();
        String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("FoutVoorbeeld5.txt").toURI())));
        List<CourseInstance> result = textFileParser.parse(content);
    }



}
