package ru.netology.card_transfer_service.util.logger;

import jakarta.inject.Singleton;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Singleton
public class Logger {
    private static final FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter("logs.txt");
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public static synchronized void log(String msg) throws IOException {
        fileWriter.write(LocalDateTime.now() + msg);
        fileWriter.flush();
    }
}