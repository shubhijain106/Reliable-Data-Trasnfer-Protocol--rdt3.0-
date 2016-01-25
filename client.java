import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class client implements Runnable
{  private Socket socket              = null;
   private Thread thread              = null;
   private DataOutputStream streamOut = null;
   private ChatClientThread client    = null;
   private BufferedReader br = null;
   static int xi=1;
  ArrayList<String> stringList = new ArrayList<String>();
  private String packet;
   public client(String serverName, int serverPort) throws InterruptedException
   {  System.out.println("Establishing connection. Please wait ...");
      try
      {  socket = new Socket(serverName, serverPort);
         System.out.println("Connected: " + socket);
         start();}
      catch(UnknownHostException uhe)
      {  System.out.println("Host unknown: " + uhe.getMessage()); }
      catch(IOException ioe)
      {  System.out.println("Unexpected exception: " + ioe.getMessage()); }
   }
   public void packet() throws FileNotFoundException, IOException
   { String data;
                        String[] p;
                        br = new BufferedReader(new FileReader("message.txt"));
                       while ((data = br.readLine()) != null)
                       {
                        p = data.split("\\.");
                        for(int k=0;k<p.length;k++)
                        {
                            String[] values; 
                            values = p[k].split(" ");
                                    int flag=0;
                               for(int i=0;i<values.length;i++)
                               {   int total = 0;
                                   String test=values[i];
                                       for( int j = 0; j<test.length(); j++ ) 
                                         total += (int) test.charAt( j );
                                       String y=Integer.toString(total);
                                   if(flag==0)
                                   {int o=i+1;
                                   String  l=Integer.toString(o);
                                     packet ="0"+" "+l+" "+y+" "+values[i];
                                     flag=1;
                                     stringList.add(packet);
                                   }
                               else{ 
                                       int o=i+1;
                                     String  l=Integer.toString(o);
                                    packet = "1"+" "+l+" "+y+" "+values[i];
                                   flag=0;
                                   stringList.add(packet);
                                   } }}}}
public void run()
   {  
       try
         { if(xi==1)
         {packet();
         String t=stringList.get(0)+" "+"1";
         streamOut.writeUTF(t);
         System.out.println("message sent to network is   "+stringList.get(0));
         System.out.println("Waiting ACK1");
         }
         xi=2;
         streamOut.flush();
         }
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
            stop();
         }
   }

public void run2(int a,int y) throws IOException
{
if(y==0){System.out.println("--------------------------------------------------------");
System.out.println("message sent to network is   "+stringList.get(a-1));
String index=Integer.toString(a);
String t=stringList.get(a-1)+" "+index;
streamOut.writeUTF(t);
String[] values; 
values = stringList.get(a-1).split(" ");
System.out.println("SEND PACKET"+values[1]);
System.out.println("Waiting ACK"+values[1]);
}
else if(y==2){System.out.println("--------------------------------------------------------");
System.out.println("message sent to network is   "+stringList.get(0));
String[] values; 
values = stringList.get(0).split(" ");
System.out.println("SEND PACKET"+values[1]);
System.out.println("Waiting ACK"+values[1]);
String t=stringList.get(a-1)+" "+"0";
    streamOut.writeUTF(stringList.get(0));}
else{System.out.println("--------------------------------------------------------");
    System.out.println("message sent to network is   "+stringList.get(a));
String index=Integer.toString(a+1);
String t=stringList.get(a)+" "+index;
    streamOut.writeUTF(t);
    String[] values; 
values = stringList.get(a).split(" ");
System.out.println("SEND PACKET"+values[1]);
System.out.println("Waiting ACK"+values[1]);

}}
   public void handle(String msg) throws IOException
   {
    if (msg.equals("bye"))
      {  System.out.println("Good bye. Press RETURN to exit ...");
         stop();
      }
    else{
        String[] values; 
        values = msg.split(" ");
        
        int d=Integer.parseInt(values[1]);
            int e=Integer.parseInt(values[2]);
        int f=Integer.parseInt(values[3]);
        
        if(d==2 && e==0)
        {
         System.out.println("DROP,RESEND PACKET");
         run2(f,0);
         }
         else if((d==0 && e==0)||(d==1 && e==0))
        {System.out.println("RECEIVED ACK");
            System.out.println("==========================================================");
            run2(f,1);
            
   }
        else if(d==2 && e==1)
        {System.out.println("CORRUPT ACK");
        run2(f,0);}
        else if((d==0 && e==1)|(d==1 && e==1))
        {
            run2(f,0);
        }
        else if((d==0 && e==1)|(d==1 && e==1) && f==0)
        {
            run2(f,0);
            
            
        }
        else
        {run2(f,1);
   }}}
   public void start() throws IOException
   {   System.out.println("in client start");
      streamOut = new DataOutputStream(socket.getOutputStream());
      if (thread == null)
      {  client = new ChatClientThread(this, socket);
         thread = new Thread(this);                   
         thread.start();
      }
   }
   public void stop()
   {  if (thread != null)
      {  thread.stop();  
         thread = null;
      }
      try
      { // if (console   != null)  console.close();
         if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing ..."); }
      client.close();  
      client.stop();
   }
   public static void main(String args[]) throws InterruptedException
   {  client client = null;
      
      String hostName = args[0];
      
       int portNumber = Integer.parseInt(args[1]);
   client = new client(hostName,portNumber);
   }
} 