import java.net.*;
import java.io.*;

public class serverThread extends Thread
{  private Socket           socket   = null;
   private server       client   = null;
   private DataInputStream  streamIn = null;

   public serverThread(server _client, Socket _socket) throws IOException
   {  System.out.println("in clientth constructor.");
   client   = _client;
      socket   = _socket;
      open(); 
       start();
   
     // catch (Exception ie) {
    //Handle exception
}
    
   
   public void open()
   { 
System.out.println("in clientth open.");try
      {  streamIn  = new DataInputStream(socket.getInputStream());;
      }
      catch(IOException ioe)
      {  System.out.println("Error getting input stream: " + ioe);
         client.stop();
      }
   }
   public void close()
   {  System.out.println("in clientth close."); try
      {  if (streamIn != null) streamIn.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing input stream: " + ioe);
      }
   }
   public void run()
   {   System.out.println("in clientth run.");
  while (true)
      {  try
         {  client.handle(streamIn.readUTF());
         }
         catch(IOException ioe)
         {  System.out.println("Listening error: " + ioe.getMessage());
            client.stop();
         }
      }
   }
}
