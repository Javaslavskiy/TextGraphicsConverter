package ru.netology.graphics;

import ru.netology.graphics.image.ImageToUnicodeConverter;
import ru.netology.graphics.image.NewSchema;
import ru.netology.graphics.image.TextColorSchema;
import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.server.GServer;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new ImageToUnicodeConverter(); // Объект класса конвертера
        TextColorSchema newSchema = new NewSchema (new char[] {'!','@','#','$','%','^','&','*'}); //Объект новой текстовой схемы
        converter.setMaxRatio(2);
        converter.setMaxWidth(200);
        converter.setMaxHeight(100);
        converter.setTextColorSchema(newSchema);

        GServer server = new GServer(converter); // Объект сервера
        server.start(); // Запускаем сервер

//        Или то же, но с выводом на экран:
//        String url = "https://stihi.ru/pics/2022/08/05/801.jpg";
//        String imgTxt = converter.convert(url);
//        System.out.println(imgTxt);
    }
}