package edu.gatech.seclass.textprocessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyMainTest {
    private final String usageStr =
            "Usage: textprocessor [ -o filename | -i | -k substring | -r old new | -n padding | -w | -s suffix ] FILE"
                    + System.lineSeparator();

    @TempDir
    Path tempDirectory;

    @RegisterExtension
    OutputCapture capture = new OutputCapture();

    /*
     * Test Utilities
     */

    private Path createFile(String contents) throws IOException {
        return createFile(contents, "input.txt");
    }

    private Path createFile(String contents, String fileName) throws IOException {
        Path file = tempDirectory.resolve(fileName);
        Files.write(file, contents.getBytes(StandardCharsets.UTF_8));

        return file;
    }

    private String getFileContent(Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Test Cases
     */

    @Test
    // Frame #:1
    public void exampleTest1() throws IOException {
        String input = " " + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "Java","-n", "2","-s", "##", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:2
    public void exampleTest2() throws IOException {
        String input = "hello, world";
        Path inputFile = createFile(input);
        String[] args = {"-k", "Java", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:3
    public void exampleTest3() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = "01 hello, world " + System.lineSeparator()+ "02 haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "2", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:4
    public void exampleTest4() throws IOException {
        String input =
                "hello1" + System.lineSeparator()+
                "hello2" + System.lineSeparator()+
                "hello3" + System.lineSeparator()+
                "hello4" + System.lineSeparator()+
                "hello5" + System.lineSeparator()+
                "hello6" + System.lineSeparator()+
                "hello7" + System.lineSeparator()+
                "hello8" + System.lineSeparator()+
                "hello9" + System.lineSeparator()+
                "hello10" + System.lineSeparator()
                ;
        String expected =
                "1 hello1" + System.lineSeparator()+
                "2 hello2" + System.lineSeparator()+
                "3 hello3" + System.lineSeparator()+
                "4 hello4" + System.lineSeparator()+
                "5 hello5" + System.lineSeparator()+
                "6 hello6" + System.lineSeparator()+
                "7 hello7" + System.lineSeparator()+
                "8 hello8" + System.lineSeparator()+
                "9 hello9" + System.lineSeparator()+
                "10 hello10" + System.lineSeparator()
                ;
        Path inputFile = createFile(input);
        String[] args = {"-n", "1", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:5
    public void exampleTest5() throws IOException {
        String[] args = {"-n", "2"};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
    }

    @Test
    // Frame #:6
    public void exampleTest6() throws IOException {
        String input = "This is green." + System.lineSeparator();
        String expected = "This is green." + System.lineSeparator();

        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:7
    public void exampleTest7() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = "01 hello, world " + System.lineSeparator()+ "02 haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "8","-n", "2", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:8
    public void exampleTest8() throws IOException {
        String input = "java is good" + System.lineSeparator()+
                "life is nice"+System.lineSeparator();
        Path outputFile = tempDirectory.resolve("output.txt");
        String expected= "01 Python is good!" + System.lineSeparator()+
                "02 life is nice!"+System.lineSeparator();;
        Path inputFile = createFile(input);
        String[] args = {"-n", "2","-o", outputFile.toString(),"-s", "!","-r","java","Python", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:9
    public void exampleTest9() throws IOException {
        String input = "java is good" + System.lineSeparator()+
                "life is nice"+System.lineSeparator();
        Path outputFile = tempDirectory.resolve("output.txt");
        String expected= "javaisgood!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-w","-o", outputFile.toString(),"-s", "!","-k","java", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    @Test
    // Frame #:10
    public void exampleTest10() throws IOException {
        String input = "java is good" + System.lineSeparator()+
                "life is nice"+System.lineSeparator();
        Path outputFile = tempDirectory.resolve("output.txt");
        String expected= "01 java is good!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "2","-o", outputFile.toString(),"-s", "!","-k","java", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected,getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:11
    public void exampleTest11() throws IOException {
        String input = "java is good" + System.lineSeparator()+
                "life is nice"+System.lineSeparator();
        Path outputFile = tempDirectory.resolve("output.txt");
        String expected= "Pythonisgood!" + System.lineSeparator()+
                "lifeisnice!"+System.lineSeparator();;
        Path inputFile = createFile(input);
        String[] args = {"-w","-o", outputFile.toString(),"-s", "!","-r","java","Python" ,inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected,getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }



    @Test
    // Frame #:12
    public void exampleTest12() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-o", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:13
    public void exampleTest13() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:14
    public void exampleTest14() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:15
    public void exampleTest15() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }



    @Test
    // Frame #:16
    public void exampleTest16() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    @Test
    // Frame #:17
    public void exampleTest17() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k","", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(input,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:18
    public void exampleTest18() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r","","python", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:19
    public void exampleTest19() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s","", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:20
    public void exampleTest20() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n","10", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:21
    public void exampleTest21() throws IOException {
        String input = "This is the first line of the input file." + System.lineSeparator();
        String expected = "This is the first line of the input file." + System.lineSeparator();

        Path inputFile = createFile(input);
        createFile(" ", "output.txt");
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:22
    public void exampleTest22() throws IOException {
        String input = "This is." + System.lineSeparator();
        String expected = "This is." + System.lineSeparator();

        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:23
    public void exampleTest23() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "01 Java, world?" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-i","-s","?","-n" ,"2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:24
    public void exampleTest24() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java,world?" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-i","-s","?","-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:25
    public void exampleTest25() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java, world?" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-i","-s","?",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:26
    public void exampleTest26() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "01 Java, world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-i","-n","2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:27
    public void exampleTest27() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java,world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-i","-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:28
    public void exampleTest28() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java, world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-i",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:29
    public void exampleTest29() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-s","?","-n","2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:30
    public void exampleTest30() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-s","?","-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:31
    public void exampleTest31() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-s","?",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:32
    public void exampleTest32() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-n","2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:33
    public void exampleTest33() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k", "java","-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:34
    public void exampleTest34() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k", "java",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:35
    public void exampleTest35() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "01 python, world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python","-i", "-s", "!","-n", "2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:36
    public void exampleTest36() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "python,world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python","-i", "-s", "!","-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:37
    public void exampleTest37() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "python, world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python","-i", "-s", "!",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:38
    public void exampleTest38() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "01 python, world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python","-i", "-n", "2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:39
    public void exampleTest39() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "python,world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python","-i", "-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:40
    public void exampleTest40() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "python, world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python","-i",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:41
    public void exampleTest41() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "01 Java, world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python", "-s", "!","-n", "2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:42
    public void exampleTest42() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java,world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python", "-s", "!","-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:43
    public void exampleTest43() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java, world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python", "-s", "!",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:44
    public void exampleTest44() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "01 Java, world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python", "-n", "2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:45
    public void exampleTest45() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java,world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python", "-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:46
    public void exampleTest46() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java, world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "java","python",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:47
    public void exampleTest47() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "01 Java, world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = { "-s", "!","-n","2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:48
    public void exampleTest48() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java,world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = { "-s", "!","-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:49
    public void exampleTest49() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java, world!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = { "-s", "!",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:50
    public void exampleTest50() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "01 Java, world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = { "-n","2",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:51
    public void exampleTest51() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "Java,world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = { "-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:52
    public void exampleTest52() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = { inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(input,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:53
    public void exampleTest53() throws IOException {
        String input = "This Sentence Ends In A Question Mark?"+ System.lineSeparator();
        String expected = "ThisSentenceEndsInAExclamationMark?!" + System.lineSeparator();
        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = { "-r","Question","Exclamation","-o",outputFile.toString(),"-s","!","-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:54
    public void exampleTest54() throws IOException {
        String input = "I wish this line had a line number.."+ System.lineSeparator()
        +"I also wish that.." + System.lineSeparator();
        String expected ="01 I wish this line had a line number..!"+ System.lineSeparator()
                +"02 I also wish that..!" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = { "-n","8","-n","2","-s","##","-s","!",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:55
    public void exampleTest55() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:56
    public void exampleTest56() throws IOException {
        String input = "This course's title is CS6300. #keep"+ System.lineSeparator()
                +"CS stands for Counter Strike."+ System.lineSeparator()
                +"It is part of the OMSCS program. #KEEP"+ System.lineSeparator();
        String expected = "1 This course's title is CS6300. #keep#" + System.lineSeparator()
                +"3 It is part of the OMSCS program. #KEEP#"+ System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = { "-n","1","-i","-k","#keep","-s","#",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:57
    public void exampleTest57() throws IOException {
        String input = "This list contains words that start with -k:"+ System.lineSeparator()
                +"-kale"+ System.lineSeparator()
                +"-kilo"+ System.lineSeparator()
                +"-kite"+ System.lineSeparator()
                +"- knot"+ System.lineSeparator()
                ;
        String expected = "This list contains words that start with -s:" + System.lineSeparator()
                +"-sale"+ System.lineSeparator()
                +"-silo"+ System.lineSeparator()
                +"-site"+ System.lineSeparator()
                +"- knot"+ System.lineSeparator()
                ;
        Path inputFile = createFile(input);
        String[] args = { "-r","-k","-s",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:58
    public void exampleTest58() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n","haha", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:59
    public void exampleTest59() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-i", "-n", "2",inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:60
    public void exampleTest60() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = ", world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r","hello","", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:61
    public void exampleTest61() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k","Java", "-r", "Java","python",inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    @Test
    // Frame #:62
    public void exampleTest62() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n","2", "-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:63
    public void exampleTest63() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "hello",inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:64
    public void exampleTest64() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        Path outputFile2 = tempDirectory.resolve("output2.txt");
        String[] args = {"-o", outputFile.toString(),"-o", outputFile2.toString(), inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile2));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:65
    public void exampleTest65() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-o", "8", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:66
    public void exampleTest66() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r","apple","water", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:67
    public void exampleTest67() throws IOException {
        String input = "Java, world"+ System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k","?",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:68
    public void exampleTest68() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k", "hello","-k", "apple", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:69
    public void exampleTest69() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "hello","green","-r", "apple","pear",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:70
    public void exampleTest70() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        String expected = "hello, world apple" + System.lineSeparator()+ "haha again..apple" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", "?","-s", "apple",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:64
    public void exampleTest71() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f","haha", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:72
    public void exampleTest72() throws IOException {
        String input = "" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "Java", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:73
    public void exampleTest73() throws IOException {
        String input = "" + System.lineSeparator();
        String expected= "" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "Java","python", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:73
    public void exampleTest74() throws IOException {
        String input = "" + System.lineSeparator();
        String expected= "" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "Java","python","-i" ,inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:75
    public void exampleTest75() throws IOException {
        String input = "789" + System.lineSeparator();
        String expected = "189" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "7","1","-i" ,inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:76
    public void exampleTest76() throws IOException {
        String input = "" + System.lineSeparator();
        String expected= "01 " + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "2", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:77
    public void exampleTest77() throws IOException {
        String input = "" + System.lineSeparator();
        String expected= "2" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", "2", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:78
    public void exampleTest78() throws IOException {
        String input = "" + System.lineSeparator();
        String expected= "" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-w", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    @Test
    // Frame #:79
    public void exampleTest79() throws IOException {
        String input = "" + System.lineSeparator();
        String expected = "" + System.lineSeparator();
        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:80
    public void exampleTest80() throws IOException {
        String input = "";
        String expected = "";
        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:81
    public void exampleTest81() throws IOException {
        Path inputFile  = tempDirectory.resolve("input.txt");
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
    }

    @Test
    // Frame #:82
    public void exampleTest82() throws IOException {
        String input = "hello,\t\tworld " + System.lineSeparator();
        String expected = "hello,world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-w",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:83
    public void exampleTest83() throws IOException {
        String input = "hello, world " + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"funny", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:84
    public void exampleTest84() throws IOException {
        String input = "hello, world " + System.lineSeparator();
        String expected = "";
        Path inputFile = createFile(input);
        String[] args = {"-k","话",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:85
    public void exampleTest85() throws IOException {
        String input = "green" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", "", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:86
    public void exampleTest86() throws IOException {
        String input = "花花" + System.lineSeparator();
        String expected= "01 花花" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "2", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected,capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
    @Test
    // Frame #:87
    public void exampleTest87() throws IOException {
        String input = "green -s" + System.lineSeparator();
        String expected= "green -s" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "-s", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:88
    public void exampleTest88() throws IOException {
        String input = "green" + System.lineSeparator();
        String expected= "green-k" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", "-k", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:89
    public void exampleTest89() throws IOException {
        String input = "green -i" + System.lineSeparator();
        String expected= "green -i" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "-i", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:89
    public void exampleTest90() throws IOException {
        String input = "green -w" + System.lineSeparator();
        String expected= "green -w" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "-w", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
    @Test
    // Frame #:91
    public void exampleTest91() throws IOException {
        String input = "hello" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-i","-s","!",inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:92
    public void exampleTest92() throws IOException {
        String input = "green -w" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "-w", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:93
    public void exampleTest93() throws IOException {
        String input = "hello, world " + System.lineSeparator()+ "haha again.." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-o", inputFile.toString(), inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile)); }

    @Test
    // Frame #:94
    public void exampleTest94() throws IOException {
        String input = "green -w" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-R", "-w", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:95
    public void exampleTest95() throws IOException {
        String input = "green -w" + System.lineSeparator();
        String expected= "green-w" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-w", "-w", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:96
    public void exampleTest96() throws IOException {
        String input = "green -k" + System.lineSeparator();
        String expected=  "green -k" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "-k", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:97
    public void exampleTest97() throws IOException {
        String input = "green -k" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"k",  inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame #:98
    public void exampleTest98() throws IOException {
        String input = "green -k" + System.lineSeparator();
        String expected=  "green -k" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
}
