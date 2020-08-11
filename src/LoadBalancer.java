import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class LoadBalancer {

    // public variables
    public static volatile HashMap<String, Socket> nodeToSocket;
    public static volatile HashMap<String, Socket> nodeToSenderSocket;
    public static volatile ArrayList<String> nodes;
    public static Integer position;

    // public methods
    public static void main(String[] args) {

        /// initializing static objects
        nodeToSocket = new HashMap<>();
        nodeToSenderSocket = new HashMap<>();
        position = 0;
        nodes = new ArrayList<>();

        try{
            Integer prt = 3881;
            if(args.length == 1)
                prt = Integer.parseInt(args[0]);

            System.out.println("================================================================");
            System.out.println("LoadBalancer started.");
            System.out.println("Address: 127.0.0.1 (local host)");
            System.out.println("Port: " + prt);
            System.out.println("================================================================\n");

            ServerSocket srvSocket = new ServerSocket(prt);
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
