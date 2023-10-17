package zcat;

public class Custom {
    public static void run(String[] args) throws Exception {
        CustomServer server = new CustomServer("zcat.webapp");
        server.start();
    }
}
