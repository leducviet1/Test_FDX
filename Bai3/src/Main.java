//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Message message = new Message();
        Thread t1 = new Thread(new Producer(message));
        Thread t2 = new Thread(new Cosumer(message));
        t1.start();
        t2.start();
    }
}