package lab4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LSB {
    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("/Users/andrew/Dropbox/CypheringChallenge/Sevidov/Vigenere/src/resources/KHlZjkF.bmp"));
        System.out.printf("len = " + (double) (bytes.length/4096));
        for (int i = 0; i < 10; i++) {
//            System.out.println(bytes[i]);
            bytes[i] = (byte) (bytes[i] | (1 << 5));
            bytes[i] = (byte) (bytes[i] | (1 << 6));
            System.out.println(bytes[i]);
        }
    }
}