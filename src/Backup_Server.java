//package Term_Project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Backup_Server {
	
	public static DB_Connection db = new DB_Connection();		// Create instance to connect with MYSQL DB
	private static FileInputStream fin;						// Inputstream read from socket
	
	public static void main(String args[])
	{
		try {
			ServerSocket server = new ServerSocket(11112);
			Socket client = null;
			System.out.println("Backup server start");
			
			while (true)
			{
				client = server.accept();
				new UpdownData(client).start();
				System.out.println("New client connected");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class UpdownData extends Thread
    {
    		private InputStream in = null;
		private FileOutputStream out = null;
		private DataInputStream din = null;
		private OutputStream s_out = null;
		private DataOutputStream dout = null;
		private Socket client = null;
		private String command = null;
		
		public UpdownData(Socket client)
		{
			this.client = client;
			
			try {
				in = client.getInputStream();
				din = new DataInputStream(in);  
			    s_out = client.getOutputStream();    
			    dout = new DataOutputStream(s_out);
			    this.command = din.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
    
    public void run()
    {
    	try {
    		
    		in = client.getInputStream();                
	    din = new DataInputStream(in);  
	    s_out = client.getOutputStream();                 
	    dout = new DataOutputStream(s_out); 
    		
	    // If command is upload
        	if (command.equalsIgnoreCase("BACKUP"))
        	{	
        			String filename = din.readUTF();				// receive name of file to upload
	            long data = din.readLong();					// receive length of file to upload
		        File file = new File("/Users/Knight/backup/"+filename);	// Create file with name which is received from client
		        out = new FileOutputStream(file);           
		 		
		        // If file is already exist in server then don't upload file.
			        	
			        	// Insert file information which is parsed from message user send and update user table.
			        
			        
			        long datas = data;                     
			        byte[] buffer = new byte[1024];        // Buffer to store segment of file
			        int len;                               
			        
			        
			        for(;data>0;data--){                   // Receive file segment from client `data` times
			            len = in.read(buffer);
			            out.write(buffer,0,len);
		        }
		        
		        System.out.println("약: "+datas+" kbps");
		        out.flush();
		        out.close();
    	}
        	
        	// If command is download
        		else if (command.equalsIgnoreCase("SYNC"))
        		{
        			String filename = din.readUTF(); 
        			
        			File f = new File("/Users/Knight/backup/"+filename);
	            
			    byte[] buffer = new byte[1024];        // Buffer to store segment of file
			    int len;                               // Length of file
			    long data=f.length();                 
	        
			    if(data%1024 != 0 )					// Calculate file size. Standard unit is KB
			    {
			    		data = data/1024 + 1;
			    }else
			    {
			    		data = data/1024;
			    }
			    
			    long datas = data;                      
			 
			    fin = new FileInputStream("/Users/Knight/backup/"+filename);   
			    dout.writeLong(data);                   // Send length of file to client
			        
			    len = 0;
			        
			    for(;data>0;data--){                 // Send segment of file to client. size of segment is 1 KB  
			    		len = fin.read(buffer);        
			        s_out.write(buffer,0,len);       
			    }
			    
			    System.out.println("약: "+datas+" kbps");
        		} 
        		else if (command.equalsIgnoreCase("DELETE_BACKUP"))
        		{
        			String filename = din.readUTF();
        			File f = new File("/Users/Knight/backup/"+filename);
        			f.delete();
        			System.out.println("File deleted");
        		}
    	} catch (Exception e)
    	{
    		System.out.println(e);
    		System.out.print(3);
    	}
    }
    }
}
