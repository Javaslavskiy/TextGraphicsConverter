package ru.netology.graphics.image;

public class NewSchema implements TextColorSchema{

    public char[] symbols;
    public NewSchema(char[] symbols) {
        this.symbols = symbols;
    }

    @Override
    public char convert(int color) {
        int index = color / 32;
        return symbols[index];
    } //Отдаем символ с шагом 32
}