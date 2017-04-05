import org.jetbrains.annotations.TestOnly;

import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;



public class Testing {

    private void testLauncher(byte[] inArray) throws IOException, DecodeException {
        OutputStream encoded = new ByteArrayOutputStream();
        OutputStream decoded = new ByteArrayOutputStream();
        Encoder.encode(new ByteArrayInputStream(inArray), encoded);
        byte[] encodedByteArray = encoded.toString().getBytes();
        Decoder.decode(new ByteArrayInputStream(encodedByteArray), decoded);
        assertArrayEquals(inArray, decoded.toString().getBytes());
    }

    private void checkFilesEqual(String fin1Name, String fin2Name) throws IOException{
        byte [] buff1 = new byte[Encoder.BUFFER_SIZE];
        byte [] buff2 = new byte[Encoder.BUFFER_SIZE];
        FileInputStream fin1 = new FileInputStream(fin1Name);
        FileInputStream fin2 = new FileInputStream(fin2Name);
        while (fin1.read(buff1) >= 0){
            fin2.read(buff2);
            assertArrayEquals(buff1, buff2);
        }
    }

    private void testLauncher(String nameFile) throws IOException, DecodeException {
        String encodedTestFile = "encodedTest.data";
        String decodedTestFile = "decodedTest.data";
        OutputStream encoded = new FileOutputStream(encodedTestFile);
        OutputStream decoded = new FileOutputStream(decodedTestFile);
        InputStream inEncoded, inDecoded, inputFile;
        inputFile = new FileInputStream(nameFile);
        Encoder.encode(inputFile, encoded);
        encoded.close();
        inEncoded = new FileInputStream(encodedTestFile);
        Decoder.decode(inEncoded, decoded);
        decoded.close();
        inEncoded.close();
        checkFilesEqual(nameFile, decodedTestFile);
    }

    @Test

    public void simpleTest() throws IOException, DecodeException{
        String simpleArray = "aaaaaaaaaaaaaaaaaaaaaaafgggggggggggggggggggggggggghjjjjjjjjjjjjjjjjjjjj";
        testLauncher(simpleArray.toString().getBytes());
    }

    @Test

    public void bigDataTest() throws IOException, DecodeException{
        for(int i = 0; i < 3; i++) {
            String testFile = "test.data";
            OutputStream output = new FileOutputStream(testFile);
            Generator.generate(output, 1000000, 0.90);
            output.close();
            testLauncher(testFile);
        }
    }

    @Test

    public void pictureTest() throws IOException, DecodeException{
            String testFile = "test.jpg";
            testLauncher(testFile);
    }

//    @Test
//    public void wrongParamsTest() {
//
//        String inText1[] = {"--wrongCommand", ""};
//        Main.main(inText1);
//
//    }

}
