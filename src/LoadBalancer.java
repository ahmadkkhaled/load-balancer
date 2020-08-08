import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class LoadBalancer {

    // public variables
    public static volatile HashMap<String, Socket> nodeToSocket;
    public static volatile ArrayList<String> nodes;
    public static Integer position;

    // public methods
    public static void main(String[] args) {

        /// initializing static objects
        position = 0;
        nodeToSocket = new HashMap<>();
        nodes = new ArrayList<>();

        try{
            int prt = 3881; /// TODO prt = args[0]
            ServerSocket srvSocket = new ServerSocket(prt);

            System.out.println("================================================================");
            System.out.println("LoadBalancer successfully started.");
            System.out.println("Address: 127.0.0.1 (local host)");
            System.out.println("Port: " + prt);
            System.out.println("================================================================\n");

            while(true){
                Socket clientSocket = srvSocket.accept();
                LoadBalancerThread loadBalancerThread = new LoadBalancerThread(clientSocket);
                loadBalancerThread.start();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
