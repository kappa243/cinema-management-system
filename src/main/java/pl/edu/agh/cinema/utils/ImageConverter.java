package pl.edu.agh.cinema.utils;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageConverter {

    public static byte[] fileToByte(File file) throws IOException {
        //noinspection resource
        FileInputStream fis = new FileInputStream(file);
        return fis.readAllBytes();
    }

    public static Image byteToImage(byte[] bytes) {
        return new Image(new java.io.ByteArrayInputStream(bytes));
    }
}
