import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Message {
    Queue<String> queue = new LinkedList<>();
    int maxSize = 5;
}
