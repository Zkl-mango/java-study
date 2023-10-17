package thread;

public class ThreadLockTest {

    private static final Object resource1 = new Object(); //资源1
    private static final Object resource2 = new Object(); //资源2

    public static void main(String[] args) {
        new Thread1().start();
        new Thread2().start();
    }

    //占有资源1，请求资源2
    static class Thread1 extends Thread {
        @Override
        public void run() {
            synchronized (resource1) {
                System.out.println(Thread.currentThread() + "get resource1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "wating get resource2");
                synchronized (resource2) {
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        }
    }
    //占有资源2，请求资源1
    static class Thread2 extends Thread {
        @Override
        public void run() {
            synchronized (resource2) {
                System.out.println(Thread.currentThread() + "get resource2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "wating for resource1");
                synchronized (resource1) {
                    System.out.println(Thread.currentThread() + "get resource1");
                }
            }
        }
    }
}
