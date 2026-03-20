package log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Logger {
    public static void log(String level, String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter("log.txt", true))) {
            out.println(LocalDateTime.now() + " [" + level + "] " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
