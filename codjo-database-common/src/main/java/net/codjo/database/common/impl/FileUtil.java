package net.codjo.database.common.impl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileUtil {

    private FileUtil() {
    }


    public static String loadContent(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        try {
            return loadContent(fileReader);
        }
        finally {
            fileReader.close();
        }
    }


    public static String loadContent(Reader reader) throws IOException {
        Reader bufferedReader = new BufferedReader(reader);
        StringBuffer fileContent = new StringBuffer();
        char[] buffer = new char[10000];
        int charRead = bufferedReader.read(buffer);
        while (charRead != -1) {
            fileContent.append(buffer, 0, charRead);
            charRead = bufferedReader.read(buffer);
        }
        return fileContent.toString();
    }
}
