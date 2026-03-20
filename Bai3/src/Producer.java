public class Producer implements Runnable{
    private Message message;
    public Producer(Message message){
        this.message = message;
    }
    @Override
    public void run() {
        while(true){
            synchronized (message){
                while(message.queue.size() > message.maxSize){
                    try {
                        System.out.println("Queue is full , Producer is waiting");
                        message.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                String data ="data"+ System.currentTimeMillis();
                message.queue.offer(data);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Producer add:" + data);
                message.notify();
            }

        }
    }
}
