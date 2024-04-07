package br.utfpr.edu;

import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        final int MAXSIZE = 600;

        byte[][] image = new byte[MAXSIZE][MAXSIZE];

        for (int x = 0; x < MAXSIZE; x++) {
            for (int y = 0; y < MAXSIZE; y++) {
                image[x][y] = (byte) 255;
            }
        }

        int width = image[0].length;
        int height = image.length;
        BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = image[y][x] & 0xFF;
                bf.getRaster().setSample(x, y, 0, pixelValue);
            }
        }

        File outputFile = new File("output.png");
        try {
            ImageIO.write(bf, "png", outputFile);
            System.out.println("Image.");
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }

        Image img = bf;
        ImagePlus imagePlus = new ImagePlus("image", img);
        imagePlus.show();
    }
}