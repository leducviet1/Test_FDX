import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Message {
    Queue<String> queue = new LinkedList<>();
    int maxSize = 5;
    public synchronized void producer(String message) throws InterruptedException {
        while(queue.size() >= maxSize){
            System.out.println("Queue is full - Producer must wait");
            wait();
        }
        queue.add(message);
        System.out.println("Producer : " + message);
        notifyAll();
    }
    public synchronized String consumer() throws InterruptedException {
        while(queue.isEmpty()){
            System.out.println("Queue is empty - Consumer must wait");
            wait();
        }
        String message = queue.remove();
        notifyAll();
        return message;
    }
}
