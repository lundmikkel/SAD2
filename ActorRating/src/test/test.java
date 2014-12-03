package test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.StringTokenizer;

/**
 * Created by Rasmus on 03-12-2014.
 */
public class test {
    public static void main(String[] args) throws Exception{
        String s = "2\n" +
                "10.0\n" +
                "2\n" +
                "3\n";

        StringTokenizer tokenizer = new StringTokenizer(s);
        System.out.println("Tokenizing string: " + s);
        System.out.println("Token: " + tokenizer.nextToken());
        System.out.println("Token: " + tokenizer.nextToken());
        System.out.println("Token: " + tokenizer.nextToken());
        System.out.println("Token: " + tokenizer.nextToken());


    }
}
