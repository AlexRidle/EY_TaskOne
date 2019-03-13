package sample.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class LineGenerator {

    private int length = 10; //length of the random symbols
    private Random rnd = new Random();
    private String charsCapsEng = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String charsEng = "abcdefghijklmnopqrstuvwxyz";
    private String charsCapsRus = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private String charsRus = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private String rusSymbols = charsCapsRus + charsRus;
    private String engSymbols = charsCapsEng + charsEng;

    String generateLine(){
        char[] randomRus = new char[length];
        char[] randomEng = new char[length];
        for (int i = 0; i < length; i++) {
            randomEng[i] = engSymbols.charAt(rnd.nextInt(engSymbols.length()));
            randomRus[i] = rusSymbols.charAt(rnd.nextInt(rusSymbols.length()));
        }

        int randomInt = rnd.nextInt(100_000_000/2) *2;
        String randomDouble = new DecimalFormat("#0.00000000").format(Math.random() * 20);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.now();
        LocalDate startDate = localDate.minusYears(5); //date 5 years ago
        long start = startDate.toEpochDay();
        long end = localDate.toEpochDay();
        long rndDay = ThreadLocalRandom.current().longs(start, end).findAny().getAsLong();
        String randomDay = (LocalDate.ofEpochDay(rndDay).format(dateTimeFormatter));

        return randomDay + "||"
                + String.valueOf(randomEng) + "||"
                + String.valueOf(randomRus) + "||"
                + randomInt + "||"
                + randomDouble;
    }
}
