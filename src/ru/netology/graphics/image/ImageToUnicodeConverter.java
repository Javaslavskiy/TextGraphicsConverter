package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;


public class ImageToUnicodeConverter implements TextGraphicsConverter {

    TextColorSchema newSchema;
    TextColorSchema schema = new BasicSchema();

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int currentWidth = img.getWidth();
        int currentHeight = img.getHeight();
        double ratio;
        double compressionRatio;

        if (currentWidth > currentHeight) {
            ratio =  (double) currentWidth / (double) currentHeight;
        } else {
            ratio = (double) currentHeight / (double) currentWidth;
        } //Проверка на соотношение сторон стандартного изображения

        if (maxRatio != 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        } //Проверка на макс. допустимое соотношение сторон

        if (currentWidth <= maxWidth && currentHeight <= maxHeight ||
                maxWidth == 0 && maxHeight == 0) {
            compressionRatio = 1;
        } else if (currentWidth > currentHeight && maxWidth != 0) {
            compressionRatio = (double) currentWidth / (double) maxWidth;
        } else if (currentHeight > currentWidth && maxHeight != 0) {
            compressionRatio = (double) currentHeight / (double) maxHeight;
        } else if (maxWidth > maxHeight){
            compressionRatio = (double) currentWidth / (double) maxWidth;
        } else {
            compressionRatio = (double) currentHeight / (double) maxHeight;
        } //Вычисляем степень сжатия изображения
        int newWidth = (int) (currentWidth / compressionRatio); //Новая ширина
        int newHeight = (int) (currentHeight / compressionRatio); //Новая высота
        char[][] cc = new char[newHeight][newWidth]; // Двумерный массив с новыми длиной и шириной

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH); //Сужаем картинку
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY); //Делаем ее ч/б
        Graphics2D graphics = bwImg.createGraphics(); //Инструмент для рисования на картинке
        graphics.drawImage(scaledImage, 0,0, null); //Копируем содержимое из суженой картинки
//        ImageIO.write(bwImg, "png", new File("out.png")); //Сохраняем промежуточную картинку out.png
        WritableRaster bwRaster = bwImg.getRaster(); //Проходим по пикселям картинки

        if (newSchema != null) {
            schema = newSchema;
        } //Выбираем схему

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                cc[h][w] = c;
            }
        } //Проходим по двумерному массиву, закрашивая каждую ячейку

        int w = cc[0].length; //Узнаем ширину изображения
        int h = cc.length; //Узнаем высоту изображения
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                for (int r = 0; r < 2; r++) {
                    sb.append(cc[i][j]);
                }
            }
            sb.append("\n");
        } //Строим картинку
        String textImg = sb.toString(); //Сохраняем в textImg
        return textImg; //Возвращаем готовую картинку
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema newSchema) {
        this.newSchema = newSchema;
    }

}