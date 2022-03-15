package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DataHelper {


    private DataHelper() {
    }


    public static String generateDate(int shift) {
        return LocalDateTime.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity(String locale) {
        Faker faker = new Faker(new Locale(locale));
        String addr;
        Sities sities = new Sities();
        List<String> list = Arrays.asList(sities.getAlphabet());

        do {
            addr = faker.address().cityName();
        } while (!list.contains(addr));
        return addr;
    }

    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().fullName();
    }

    public static String generatePhone(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }


    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            String city = generateCity(locale);
            String name = generateName(locale);
            String phone = generatePhone(locale);
            return new UserInfo(city, name, phone);
        }
    }
}