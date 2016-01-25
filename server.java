import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class server implements Runnable
{  private Socket socket              = null;
   private Thread thread              = null;
   //private DataInputStream  notify   = null;
   private DataOutputStream streamOut = null;
   private serverThread client    = null;
   private BufferedReader br = null;
   static int expec=0;
   static int count=0;
     static int cou=0;
   ArrayList<String> stringList1 = new ArrayList<String>();
  //private String msg;
private String packet;
   public server(String serverName, int serverPort) throws InterruptedException
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
   public void run()
   {  //while (thread != null)
      //{ 
       try
         {  
 streamOut.flush();
         }
                      
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
            stop();
         }
      
   }
   public void handle(String msg) throws IOException
   { // System.out.println("msg length"+msg.length());
   if (msg.equals(".bye"))
      {  System.out.println("Good bye. Press RETURN to exit ...");
         stop();
      }
   if(msg.length()<=13)
   {System.out.println("ACK DROPPED  ");
   String[] values;
    
        values = msg.split(" ");
        int ackid=Integer.parseInt(values[1]);
        int ackseq=Integer.parseInt(values[2]);
         int ackc=Integer.parseInt(values[4]);
         int ackcheck=Integer.parseInt(values[3]);
        if(ackid==2 && ackseq==0 && ackcheck==0)
       {String ack0="0"+" "+"0"+" "+ackc;
        streamOut.writeUTF(ack0);
       System.out.println("resend ack0");}
        else if(ackid==2 && ackseq==0 && ackcheck==1)
            {String ack0="0"+" "+"1"+" "+ackc;
        streamOut.writeUTF(ack0);
       System.out.println("resend ack0");}
          else if(ackid==2 && ackseq==1 && ackcheck==1)
            {String ack0="1"+" "+"1"+" "+ackc;
        streamOut.writeUTF(ack0);
       System.out.println("resend ack0");}
       else
       {//System.out.println("message received from network is   "+msg);
           String ack1="1"+" "+"0"+" "+ackc;
        streamOut.writeUTF(ack1);
       System.out.println("resend ack1");}
   }
   else{
     
       String[] values;
       values = msg.split(" ");
        String v=values[0]+values[1]+values[2]+values[5];
       stringList1.add(v);
      
        int id=Integer.parseInt(values[2]);
        int seq=Integer.parseInt(values[1]);
       
        int x=0;
        String t=values[0]+values[1]+values[2]+values[5];
        for(int k=0;k<stringList1.size()-1;k++)
                    {
                    if (t.equalsIgnoreCase(stringList1.get(k)))
                    {//System.out.println("messages are not equal so incremented count "+t+"**"+stringList1.get(k));
                    x=1;
                    }
                    }
        if(x!=1)
        {count++;}
        if(seq==0 && (seq==expec || seq!=expec) )// wanted
        {
         int total=0;
          String test=values[4];
          //System.out.println("values[4]="+values[4]);
             for( int j = 0; j<test.length(); j++ ) 
             total += (int) test.charAt( j );
                String y=Integer.toString(total);
               // System.out.println("total"+total);
                if(total==Integer.parseInt(values[3]))
                  {//not corrupted
                    for(int i=0;i<stringList1.size()-1;i++)
                    {
                    if (t.equals(stringList1.get(i)))
                    {//System.out.println("messages are not equal so incremented count"+(values[0]+values[1]+values[2]+values[5])+"**"+stringList1.get(i));
                     x=1;
                    }
                    }
                 if(x!=1)
                 expec=1;
                 else expec=0;
                      String co=Integer.toString(count);
                    //System.out.println("message received from network is   "+msg);
                    cou++;
                    String ack0="0"+" "+"0"+" "+co;
                    
                    streamOut.writeUTF(ack0);
                    //System.out.println("message sent"+ack0);
                    System.out.println("waiting0 "+ " "+ cou +" "+msg+" ack0 ");
                           
                    }
                   
                  
                else{//corrupted
                     for(int i=0;i<stringList1.size()-1;i++)
                    {
                    if (t.equals(stringList1.get(i)))
                    {//System.out.println("(corrupt) ACK1");
                     x=1;
                    }
                    }
                 if(x!=1)
                 expec=0;
                 else expec=0;
                 String co=Integer.toString(count);
                    //System.out.println("message received from network is   "+msg);
                    cou++;
                    String ack1="0"+" "+"1"+" "+co;
                streamOut.writeUTF(ack1);
                //System.out.println("message sent"+ack1);
                System.out.println("waiting0 "+ " "+cou +" "+ msg +" ack0 "+" corrupted ");
                expec=0;
                }
        }
        /*else if(seq==0 && seq!=expec)
        {String co=Integer.toString(count);
        System.out.println("message received from network is   "+msg);
        String ack0="0"+" "+"0"+" "+co;
         streamOut.writeUTF(ack0);
         System.out.println("message sent"+ack0);
         System.out.println("waiting1"+co+msg+"ack0");
         expec=0;
        }*/
        else
                {
        //System.out.println("message received from network is   "+msg);
         cou++;  
            
          int total=0;
                                   String test=values[4];
                                       for( int j = 0; j<test.length(); j++ ) 
                                         total += (int) test.charAt( j );
                                       String y=Integer.toString(total);
                                       if(total==Integer.parseInt(values[3]))
                                       { for(int i=0;i<stringList1.size()-1;i++)
                    {
                    if ((values[0]+values[1]+values[2]+values[5]).equals(stringList1.get(i)))
                    {//System.out.println("messages are not equal so incremented count"+(values[0]+values[1]+values[2]+values[5])+"**"+stringList1.get(i));
                     x=1;
                    }
                    }
                 if(x!=1)
                 expec=0;
                 else expec=1;
                                       String co=Integer.toString(count);//not corrupted
                                        String ack1="1"+" "+"0"+" "+co;
                                        streamOut.writeUTF(ack1);
                                        //System.out.println("message sent"+ack1);
                                        System.out.println("waiting1"+" "+cou+ " "+msg+" "+"ack1");}
                     
                                       else{//corrupted
                                           //System.out.println("(corrupt) ACK0");
                                            for(int i=0;i<stringList1.size()-1;i++)
                    {
                    if ((values[0]+values[1]+values[2]+values[5]).equals(stringList1.get(i)))
                    {//System.out.println("messages are equal bt corrupted so changed seq no. expected"+(values[0]+values[1]+values[2]+values[5])+"**"+stringList1.get(i));
                     x=1;
                    }
                    }
                 if(x!=1)
                 expec=1;
                 else expec=1;
                                           String co=Integer.toString(count);
                                           String ack0="1"+" "+"1"+" "+co;
                                       streamOut.writeUTF(ack0);
                                       expec=1;
                                       //System.out.println("message sent"+ack0);
                                      System.out.println("waiting1"+" "+cou+" "+msg+" "+"ack1"+" corrupted");
                                       }}
       /*else{String co=Integer.toString(count);
        System.out.println("message received from network is   "+msg);
        String ack1="1"+" "+"0"+" "+co;
             streamOut.writeUTF(ack1);
             System.out.println("message sent"+ack1);
             System.out.println("waiting0"+" "+co+" "+msg+" "+"ack1");
             expec=1;
            }*/}}
   public void start() throws IOException
   {  //console   = new DataInputStream(System.in);
      streamOut = new DataOutputStream(socket.getOutputStream());
      if (thread == null)
      {  client = new serverThread(this, socket);
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
   {  server client = null;
      String hostName = args[0];
       int portNumber = Integer.parseInt(args[1]);
         client = new server(hostName,portNumber);
   }
}