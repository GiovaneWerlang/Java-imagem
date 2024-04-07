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
        final int MAXSIZE = 600;
        final int GERAR = 500;

        byte[][] imagem = new byte[MAXSIZE][MAXSIZE];

        //setar cores
        for (int x = 0; x < MAXSIZE; x++) {
            for (int y = 0; y < MAXSIZE; y++) {
                imagem[x][y] = (byte) 255;
            }
        }

        //ponto inicial
        int metade = (MAXSIZE/2) -1;
        imagem[metade][metade] = (byte) 0;

        int glued = 0;
        Random rand = new Random();

        while(glued < GERAR){
            if(glued == GERAR * 0.25 || glued == GERAR * 0.5 || glued == GERAR * 0.75 || glued == GERAR-1) {
                System.out.println("Gerou: " + glued);
            }
            rand.setSeed(System.currentTimeMillis());
            int startposX = rand.nextInt(0,MAXSIZE-1);

            rand.setSeed(System.currentTimeMillis());
            int startposY = rand.nextInt(0,MAXSIZE-1);
            if(imagem[startposX][startposY] != 0){

                boolean next = false;
                while(!next) {


                    rand.setSeed(System.currentTimeMillis());
                    int dir1 = rand.nextInt(1, 4);

                    rand.setSeed(System.currentTimeMillis());
                    int dir2 = rand.nextInt(1, 4);

                    rand.setSeed(System.currentTimeMillis());
                    int dir3 = rand.nextInt(1, 4);

                    rand.setSeed(System.currentTimeMillis());
                    int dir4 = rand.nextInt(1, 4);
                    int dir = ((dir1 + dir2) + (dir3 + dir4)) / 4;

                    switch (dir) {
                        case 1: {//Norte
                            startposX = anterior(startposX, MAXSIZE);
                            break;
                        }
                        case 2: {//Leste
                            startposY = proximo(startposY, MAXSIZE);
                            break;
                        }
                        case 3: {//Sul
                            startposX = proximo(startposX, MAXSIZE);
                            break;
                        }
                        case 4: {//Oeste
                            startposY = anterior(startposY, MAXSIZE);
                            break;
                        }
                    }

                    if (validaProximidade(startposX, startposY, imagem, MAXSIZE)) {
                        if(imagem[startposX][startposY] == (byte) 0){
                            imagem[startposX][startposY] = (byte) ((byte) imagem[startposX][startposY] + (byte) 1);
                        }else{
                            imagem[startposX][startposY] = (byte) 0;
                        }
                        int count = 0;
                        for (int x = 0; x < MAXSIZE; x++) {
                            for (int y = 0; y < MAXSIZE; y++) {
                                if(imagem[x][y] == 0){
                                    count++;
                                }
                            }
                        }
                        glued = count;
                        next = true;
                    }
                }
            }
        }

        //contar
        int count = 0;
        for (int x = 0; x < MAXSIZE; x++) {
            for (int y = 0; y < MAXSIZE; y++) {
                if(imagem[x][y] == 0){
                    count++;
                }
            }
        }
        System.out.println(count);

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

    public static boolean validaProximidade(int x, int y, byte[][] imagemProx, int MAXSIZE){
//        boolean a = imagemProx[proximo(x,MAXSIZE)][y] == 0;
//        boolean b = imagemProx[anterior(x,MAXSIZE)][y] == 0;
//        boolean c = imagemProx[x][proximo(y,MAXSIZE)] == 0;
//        boolean d = imagemProx[x][anterior(y,MAXSIZE)] == 0;
        boolean a = imagemProx[proximo(x,MAXSIZE)][proximo(y,MAXSIZE)] == (byte) 0;
        boolean b = imagemProx[anterior(x,MAXSIZE)][anterior(y,MAXSIZE)] == (byte) 0;
        boolean c = imagemProx[anterior(x,MAXSIZE)][proximo(y,MAXSIZE)] == (byte) 0;
        boolean d = imagemProx[proximo(x,MAXSIZE)][anterior(y,MAXSIZE)] == (byte) 0;
//        if(a || b || c || d){
//            System.out.println("valor: " + imagemProx[x][y] + ", a: " + a + ", b: " + b +", c: " + c +", d: " + d);
//        }
        return a || b || c || d;
    }

    public static int proximo(int p, int MAXSIZE){
        return (p + 1) < MAXSIZE ? (p + 1) : 0;
    }

    public static int anterior(int a, int MAXSIZE){
        return (a - 1) >= 0 ? (a - 1) : (MAXSIZE-1);
    }

    public static void setarParteInicialLinhaMeio(byte[][] imagem, int tamanho){
        int metade = (tamanho/2) -1;
        for (int i = 0; i < tamanho; i++) {
            imagem[i][metade] = (byte) 0;
        }
    }

    public static void setarParteInicialLinhaAbaixo(byte[][] imagem, int tamanho){
        int metade = (tamanho/2) -1;
        for (int i = 0; i < tamanho; i++) {
            imagem[tamanho-1][i] = (byte) 0;
        }
    }
}