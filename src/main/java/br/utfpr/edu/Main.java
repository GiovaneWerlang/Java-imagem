package br.utfpr.edu;

import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //tamanho
        final int TAMANHO = 600;
        final byte VALORINICIAL = (byte) 250;
        final byte VALORDIMINUIR = (byte) 10;


        //criar array 2d de bytes
        byte[][] imagem = new byte[TAMANHO][TAMANHO];

        //setar cor padrão branca
        for (int x = 0; x < TAMANHO; x++) {
            for (int y = 0; y < TAMANHO; y++) {
                imagem[x][y] = VALORINICIAL;
            }
        }

        Random rand = new Random();
        //seed baseada no tempo atual
        rand.setSeed(System.currentTimeMillis());

        int startposX = rand.nextInt(0,TAMANHO-1);
        int startposY = rand.nextInt(0,TAMANHO-1);

        long start = System.currentTimeMillis();
        int c1 = 0;
        int c2 = 0;
        int c3 = 0;
        int c4 = 0;

        int dir = 0;

        System.out.println("inicio: " + start);
        System.out.println("agora: " + System.currentTimeMillis());
        while((System.currentTimeMillis() - start) < 1000L){

            boolean next = false;
            while(!next) {

                dir = rand.nextInt(1, 5);

                switch (dir) {
                    case 1: {
                        startposX = anterior(startposX, TAMANHO);
                        c1++;
                        break;
                    }
                    case 2: {
                        startposY = proximo(startposY, TAMANHO);
                        c2++;
                        break;
                    }
                    case 3: {
                        startposX = proximo(startposX, TAMANHO);
                        c3++;
                        break;
                    }
                    case 4: {
                        startposY = anterior(startposY, TAMANHO);
                        c4++;
                        break;
                    }
                }

                imagem[startposX][startposY] -= VALORDIMINUIR;
                next = true;

            }
        }

        //contar pixels gerados
        int count = 0;
        for (int x = 0; x < TAMANHO; x++) {
            for (int y = 0; y < TAMANHO; y++) {
                if(imagem[x][y] < 255){
                    count++;
                }
            }
        }
        System.out.println("total gerado: " + count);
        System.out.println("tempo: " + (System.currentTimeMillis() - start));
        System.out.println("1: " + c1 + ", 2: " + c2 + ", 3: " + c3 + ", 4: " + c4);

        //gerar buffer
        int width = imagem[0].length;
        int height = imagem.length;
        BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = imagem[y][x] & 0xFF;
                bf.getRaster().setSample(x, y, 0, pixelValue);
            }
        }

        //gerar arquivo
        File outputFile = new File("output.png");
        try {
            ImageIO.write(bf, "png", outputFile);
            System.out.println("Imagem gerada.");
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
        Image img = bf;
        ImagePlus imagePlus = new ImagePlus("imagem", img);
        imagePlus.show();
    }

    public static int proximo(int p, int TAMANHO){
        return (p + 1) < TAMANHO ? (p + 1) : 0;
    }

    public static int anterior(int a, int TAMANHO){
        return (a - 1) >= 0 ? (a - 1) : TAMANHO-1;
    }

}