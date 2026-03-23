public class Cosumer implements Runnable{
    private Message message;
    public Cosumer(Message message){
        this.message = message;
    }
    @Override
    public void run() {
//        while(true){
//            synchronized (message){
//                while(message.queue.isEmpty()){
//                    try {
//                        System.out.println("Queue is empty , Cosumer is waiting");
//                        message.wait();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                String data = message.queue.remove();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println("Consumer add:" + data);
//                message.notify();
//            }
//        }
        while (true){
            String data  = null;
            try {
                data = message.consumer();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Cosumer : " + data);
        }
    }
}
