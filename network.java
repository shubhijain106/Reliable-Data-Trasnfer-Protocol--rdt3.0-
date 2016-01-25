import java.net.*;
import java.io.*;


public class network implements Runnable
{  private networkThread clients[] = new networkThread[2];
   private ServerSocket server = null;
   private Thread       thread = null;
   private int clientCount = 0;

private DataOutputStream streamOut = null;
   public network(int port)
   {  try
      {  System.out.println("Binding to port " + port + ", please wait  ...");
         server = new ServerSocket(port);  
         System.out.println("Server started: " + server);
         start(); 
      }
      catch(IOException ioe)
      {  System.out.println("Can not bind to port " + port + ": " + ioe.getMessage()); }
   }
   public void run()
   {  while (thread != null)
      {  try
         {  System.out.println("Waiting for a client ..."); 
            addThread(server.accept()); 
         
         }
         catch(IOException ioe)
         {  System.out.println("Server accept error: " + ioe); stop(); }
      }
   }
   public void start()  { if (thread == null)
      {  thread = new Thread(this); 
         thread.start();
      } }
   public void stop()   { if (thread != null)
      {  thread.stop(); 
         thread = null;
      } }
   private int findClient(int ID)
   {  for (int i = 0; i < clientCount; i++)
         if (clients[i].getID() == ID)
            return i;
      return -1;
   }
   public synchronized void handle(int ID, String input) throws IOException
   { /* streamOut = new DataOutputStream(new BufferedOutputStream(server.accept().getOutputStream()));
         System.out.println("message heellooo sent");
         streamOut.writeUTF("hello");*/
       //System.out.println("msg length"+input.length());
       if (input.equals(".bye"))
      {  clients[findClient(ID)].send(".bye");
         remove(ID); }
   else{
       RandomJava r=new RandomJava();
          double t;
          t = r.getRandomValue();
         
          //System.out.println("============t=============== "+t);
        
       if(t<0.5 && input.length()>5)
       { String[] values; 
        values = input.split(" ");
        
        System.out.println("Received;Packet"+values[1]+" :"+values[3]+ ",PASS");//PASS
         
         for (int i = 0; i < clientCount; i++)
                 {
                     if(!(clients[i].getID()==ID))
                     clients[i].send(ID + ": " + input);
                     
                 } //System.out.println("============Message sent to server is=====passed packet========== "+input);
            
       }
        //{for (int i = 0; i < clientCount; i++)
            //clients[i].send(ID + ": " + input);}
       else if(t>=0.5 && t<=0.75 && input.length()>5)//CORRUPT
       { //System.out.println("============Message received from client is============ "+input);
        String[] values; 
        values = input.split(" ");
        //System.out.println("values[2]"+values[2]);
        int i=Integer.parseInt(values[2]);
        //System.out.println("value of i "+i);
        int z=i+1;
        String packet=Integer.toString(z);
        //System.out.println("value of pac"+packet);
        //System.out.println("values[4]"+values[4]);
        String packet2 =values[0]+" "+values[1]+" "+packet+" "+values[3]+" "+values[4];
        //System.out.println("value of pac2"+packet2);
        System.out.println("Received Packet"+values[1]+" :"+values[3]+ ",CORRUPT");
        for (int j = 0; j < clientCount; j++){
            if(!(clients[j].getID()==ID))
        clients[j].send(ID + ": " + packet2);
        } //System.out.println("============Message sent to server is=====corrupt packet========== "+packet2);
       }
       else if(t>0.75 && input.length()>5)//DROP
       { String[] values; 
        values = input.split(" ");
        String ack2="2"+" "+"0"+" "+values[4];
       for (int j = 0; j < clientCount; j++){
            if((clients[j].getID()==ID))
        clients[j].send(ID + " " + ack2);
       } System.out.println("Received Packet"+values[1]+" :"+values[3]+ ",DROP");}
          
       else if(t<0.5 && input.length()<=5) //pass ack 
       {String[] values; 
        
        values = input.split(" ");
        System.out.println("Received Acknowledgement   ACK"+values[2]+ ",PASS");//PASS
           
         for (int i = 0; i < clientCount; i++)
                 {if(!(clients[i].getID()==ID))
                     clients[i].send(ID + ": " + input);
                     
                 }//System.out.println("============ack sent to client is=====passed ack========== "+input);
       }
       else if(t>=0.5 && t<=0.75 && input.length()<=5)//CORRUPT ack
       { //System.out.println("============Message received from server is============== "+input);
        String[] values; 
        
        values = input.split(" ");
        String ack2="2"+" "+"1"+" "+values[2];
        for (int j = 0; j < clientCount; j++){
            if(!(clients[j].getID()==ID))
        clients[j].send(ID +" "+ ack2);
          System.out.println("Received Acknowledgement   ACK"+values[2]+ ",CORRUPT");  
        }
             
   }
       else//drop ack
       {//System.out.println("============Message received from server is=============== "+input);
        
        String[] values; 
        String ack2;
        values = input.split(" ");
        int se=Integer.parseInt(values[0]);
        if (se==1)
        ack2= "2"+" "+"1"+" "+values[1]+" "+values[2];
        else
        ack2= "2"+" "+"0"+" "+values[1]+" "+values[2];
        for (int j = 0; j < clientCount; j++){ if((clients[j].getID()==ID))
        clients[j].send(ID +" " +ack2);
        
        }System.out.println("Received Acknowledgement   ACK"+values[2]+ ",DROP"); 
   }}}
   public synchronized void remove(int ID)
   {  int pos = findClient(ID);
      if (pos >= 0)
      {  networkThread toTerminate = clients[pos];
         System.out.println("Removing client thread " + ID + " at " + pos);
         if (pos < clientCount-1)
            for (int i = pos+1; i < clientCount; i++)
               clients[i-1] = clients[i];
         clientCount--;
         try
         {  toTerminate.close(); }
         catch(IOException ioe)
         {  System.out.println("Error closing thread: " + ioe); }
         toTerminate.stop(); }
   }
   private void addThread(Socket socket) throws IOException
   {  if (clientCount < clients.length)
      {  System.out.println("Client accepted: " + socket);
       
         clients[clientCount] = new networkThread(this, socket);
         System.out.println("clientServer"+clients[clientCount].getID());
         System.out.println("ccount"+clientCount);
         clients[clientCount].open();
         clients[clientCount].start();
         clientCount++;
}
      else
         System.out.println("Client refused: maximum " + clients.length + " reached.");
   }
   public static void main(String args[]) { network server1 = null;
     
       int portNumber = Integer.parseInt(args[0]);
     
         server1 = new network(portNumber);
   } }
