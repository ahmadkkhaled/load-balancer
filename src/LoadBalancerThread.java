import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoadBalancerThread extends Thread{
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private synchronized void handleRequest(String jobRequest) throws IOException {

        if(LoadBalancer.nodes.isEmpty()){
            writer.println("No free nodes available, job request has been dropped");
            return;
        }

        String nodeID = LoadBalancer.nodes.get(LoadBalancer.position);
        if(LoadBalancer.nodeToSocket.containsKey(nodeID)){
            writer.println("Assigning job to node with ID: " + nodeID);

            Socket nodeSocket = LoadBalancer.nodeToSocket.get(nodeID);
            PrintWriter nodeWriter = new PrintWriter(nodeSocket.getOutputStream(), true);
            nodeWriter.println(jobRequest);

            // mark assigned node as busy and increment pointer
            LoadBalancer.nodeToSocket.remove(nodeID, nodeSocket);
            LoadBalancer.position = (LoadBalancer.position + 1) % LoadBalancer.nodes.size();
        }
        else
        {
            writer.println("Node with current turn is busy, job request has been dropped");
        }
    }
    public LoadBalancerThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run()
    {
        try{
            String messageFromClient = reader.readLine();
            System.out.println(messageFromClient);

            if(messageFromClient.contains("Sender")){
                while(true){
                    writer.println("Awaiting job request...");
                    String jobRequest = reader.readLine();

                    handleRequest(jobRequest);
                }
            }
            else if(messageFromClient.contains("finished")){ // a node has finished its job and requests to be added back to the pool of free nodes
                String nodeID = messageFromClient.split(" ")[3];
                LoadBalancer.nodeToSocket.put(nodeID, socket);
            }
            else if(messageFromClient.contains("node")){ // a node requests addition to pool of free nodes
                String nodeID = messageFromClient.split(" ")[5];
                LoadBalancer.nodeToSocket.put(nodeID, socket);
                LoadBalancer.nodes.add(nodeID);

                while(true){
                    String response = reader.readLine();
                    System.out.println(response);
                    LoadBalancer.nodeToSocket.put(nodeID, socket);
                }
            }
            else{
                writer.println("Invalid message format");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
