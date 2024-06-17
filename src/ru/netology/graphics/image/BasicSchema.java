package ru.netology.graphics.image;

public class BasicSchema implements TextColorSchema{

    char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '.'};

    @Override
    public char convert(int color) {
        int index = color / 32;
        return symbols[index];
    } //Отдаем символ с шагом 32

}