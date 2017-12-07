/*
 * Server
 *
 * Share_offset :0(public), 1(private)
 *
 */

package application.controller;

import java.io.DataInputStream;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.*;

public class Server {

	public static DB_Connection db = new DB_Connection();		// Create instance to connect with MYSQL DB
	public static int portList[] = new int[70000];			// To manage used port and unused port, 1 is used port and 0 is unused port
    static Socket client = new Socket();						// Socket instance which communicate with client
	private static FileInputStream fin;						// Inputstream read from socket

    public static void main(String[] args) throws Exception{
        ServerSocket soc = new ServerSocket(11111);  // Open server socket with port number 11111
        portList[11111] = 1;							// Now port 11111 is used so change to 1
        System.out.println("Server Start");
        while(true)
        {
	        client = soc.accept(); 					// Wait and accept client connection request
	        portList[client.getPort()] = 1;			// Now new socket is created to communicate with socket so change port number of it's socket to 1
	        System.out.println("client accept!");
	        new Updown(client).start();				// Each client request is handled in each thread
        }

    }

    private static class Updown extends Thread
    {
    		private InputStream in = null;
    		private FileOutputStream out = null;
    		private DataInputStream din = null;
    		private OutputStream s_out = null;
    		private DataOutputStream dout = null;
    		private Socket client = null;

    		private ServerSocket data = null;					// If client request upload or download file, then this thread create new child thread
    		private Socket sock = null;
    		private int port = new Random().nextInt(60000)+1000;	// Server socket's port number is randomly decided

    		public Updown(Socket client)
    		{
    			this.client = client;							// Receive socket instance from parent to communicate with client
    		}

//    		public static String stringToHex(String s) {
//    		    String result = "";
//
//    		    for (int i = 0; i < s.length(); i++) {
//    		      result += String.format("%02X ", (int) s.charAt(i));
//    		    }
//
//    		    return result;
//    		  }

        public void run()
        {
        	try {

        		in = client.getInputStream();
		    din = new DataInputStream(in);
		    s_out = client.getOutputStream();
		    dout = new DataOutputStream(s_out);

	        while(true){
	        		String command = din.readUTF().toString();				// Receive command from client
	        		System.out.print(command);
	        		if (command.equalsIgnoreCase("UPLOAD") || command.equalsIgnoreCase("DOWNLOAD"))
	        		{
	        			// If command is upload or download,
	        			// Choose port number which is not used yet
	        			while (portList[port] == 1)
	        			{
	    		    		port = new Random().nextInt(60000)+1000;
	        			}

	        			// Create server socket with port number randomly selected
	        			this.data = new ServerSocket(this.port);
	        		    dout.writeInt(this.port);				// Notice this port number to client.
	        			sock = data.accept();
	        			portList[port] = 1;
	        			portList[sock.getPort()] = 1;
	        			System.out.flush();
	        			try {
	        				new UpdownData(sock, command).run();
	        			} finally
	        			{
	        				portList[sock.getPort()] = 0;
	        				portList[port] = 0;
	        				sock.close();
	        				data.close();
	        			}
		        }

	        		else if (command.equalsIgnoreCase("DELETE"))
	        		{
	        			JSONParser parser = new JSONParser();
	        			JSONObject fileInfo = (JSONObject)parser.parse(din.readUTF());
	        			String file[] = db.searchFile(fileInfo.get("fileID").toString(), "file_id", fileInfo.get("range").toString()).split("12345");
	        			String user[] = db.searchUser(fileInfo.get("userID").toString()).split("12345");
	        			if (file[0].equalsIgnoreCase(user[0]) && user[1].equalsIgnoreCase(fileInfo.get("password").toString()))
	        			{
		        			File f = new File("/Users/Knight/upload/"+file[2]);
		        			f.delete();
		        			db.deleteFile(file[1]);
		        			db.updateUser(user[0], user[1], user[2], user[3], user[4], user[5], user[6], Double.parseDouble(user[7]), Double.parseDouble(user[8])-Double.parseDouble(file[6]));
		        			System.out.println("File deleted");
	        			}
	        			else
	        			{
	        				System.out.println("You can't delete it");
	        			}

	        		}

	        		else if (command.equalsIgnoreCase("BACKUP") || command.equalsIgnoreCase("DELETE_BACKUP"))
	        		{
	        			new UpdownData(client, command).run();
	        		}

	        		else if (command.equalsIgnoreCase("SIGNUP"))
	        		{
	        			// If command is signup
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject user = (JSONObject)parser.parse(din.readUTF());

	        			// Insert information which is parsed from message to DB
	        			db.insertUser(user.get("userID").toString(), user.get("password").toString(), user.get("name").toString(),
	        					user.get("phoneNumber").toString(), user.get("address").toString(), user.get("email").toString(),
	        					user.get("gender").toString(), 20.0*1024*1024, 0);
	        		}
	        		else if (command.equalsIgnoreCase("LOGIN"))
	        		{
	        			// If command is login
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject user = (JSONObject)parser.parse(din.readUTF());

	        			// Search user table with information which is parsed from message
	        			// Send the result of login to client
	        			String cur_user[] = db.searchUser(user.get("userID").toString()).split("12345");
	        			if(cur_user[0].equals("userID") && cur_user[1].equals("password"))
	        			{
	        				dout.writeUTF("Success");
	        			}else
	        			{
	        				dout.writeUTF("Fail");
	        			}
	        		}
	        		else if (command.equalsIgnoreCase("SEARCH"))
	        		{
	        			// If command is search
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject searchKey = (JSONObject)parser.parse(din.readUTF());

	        			// Create JSON objects to store result information
	        			JSONArray result = new JSONArray();
	        			JSONObject output = new JSONObject();

	        			// Search file table with information which is parsed from message which client send
	        			// Put the result of searching to JSON object and send it to client
	        			String files[] = db.searchFile(searchKey.get("keyword").toString(), searchKey.get("std").toString(), searchKey.get("range").toString()).split("54321");
	        			for(int i=0; i<files.length; i++)
	        			{
	        				JSONObject tmpJSON = new JSONObject();
	        				String tmp[] = files[i].split("12345");
//	        				System.out.println(db.searchFile(searchKey.get("keyword").toString(), searchKey.get("std").toString(), searchKey.get("range").toString()));
	        				tmpJSON.put("userID"	, tmp[0]);
	        				tmpJSON.put("fileID", tmp[1]);
	        				tmpJSON.put("fileName", tmp[2]);
	        				tmpJSON.put("type", tmp[3]);
	        				tmpJSON.put("category", tmp[4]);
	        				tmpJSON.put("directory", tmp[5]);
	        				tmpJSON.put("size", tmp[6]);
	        				tmpJSON.put("date", tmp[7]);
	        				tmpJSON.put("shareOffset", tmp[8]);
	        				tmpJSON.put("download", tmp[9]);
	        				tmpJSON.put("backup", tmp[10]);
	        				result.add(tmpJSON);
	        			}
	        			output.put("files", result);
	        			System.out.println(result);
	        			dout.writeUTF(output.toString());
	        		}
	        		else if (command.equalsIgnoreCase("QUIT"))
	        		{
	        			// If command is quit, close all stream and socket
	        			in.close();
	        			din.close();
	        			out.close();
	        			dout.close();
	        			s_out.close();
	        			this.client.close();
	        			break;
	        		}
	        		else if (command.equalsIgnoreCase("PRIVATE2PUBLIC"))
	        		{
	        			// If command is private2public which means sharing file in personal cloud storage
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());

	        			// Search file table and user table with information which is parsed from message user send
	        			String file[] = db.searchFile(info.get("fileID").toString(), "file_id", "1").split("12345");
	        			String user[] = db.searchUser(info.get("userID").toString()).split("12345");

	        			// Change file table and user table.
	        			// Change file flag to 0 which means this file is set to be share
	        			// Change user storage field. Minus storage field with file size
	        			db.updateFile(file[0], file[1], file[2], file[3], file[4], file[5], Double.parseDouble(file[6]), file[7], "0", Integer.parseInt(file[9]), Integer.parseInt(file[10]));
	        			db.updateUser(user[0], user[1], user[2], user[3], user[4], user[5], user[6], Double.parseDouble(user[7]), Double.parseDouble(user[8])-Double.parseDouble(file[6]));

	        		}
	        		else if (command.equalsIgnoreCase("PUBLIC2PRIVATE"))
	        		{
	        			// If command is public2private which means don't sharing file in personal cloud storage
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());

	        			// Search file table and user table with information which is parsed from message user send
	        			String file[] = db.searchFile(info.get("fileID").toString(), "file_id", "0").split("12345");
	        			String user[] = db.searchUser(info.get("userID").toString()).split("12345");

	        			// Change file table and user table.
	        			// Change file flag to 1 which means this file isn't share
	        			// Change user storage field. Add storage field with file size
	        			db.updateFile(file[0], file[1], file[2], file[3], file[4], file[5], Double.parseDouble(file[6]), file[7], "1", Integer.parseInt(file[9]), Integer.parseInt(file[10]));
	        			db.updateUser(user[0], user[1], user[2], user[3], user[4], user[5], user[6], Double.parseDouble(user[7]), Double.parseDouble(user[8])+Double.parseDouble(file[6]));
	        		}
	        		else if (command.equalsIgnoreCase("CREATEGROUP"))
	        		{
	        			// If command is creategroup
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());

	        			// Insert new group information which is parsed from message user send
	        			db.insertGroup(info.get("groupID").toString(), info.get("groupName").toString(), 1);
	        			// Insert new group member information
	        			db.insertGroupMember(info.get("groupID").toString(), info.get("userID").toString());
	        		}
	        		else if (command.equalsIgnoreCase("ADDMEMBER"))
	        		{
	        			// If command is addmember
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());

	        			// Search user table with information which is parsed from message user send
	        			String user = db.searchUser(info.get("userID").toString());

	        			// If user exist
	        			if(user != "")
	        			{
	        				// Insert new group member information
	        				db.insertGroupMember(info.get("groupID").toString(), info.get("userID").toString());
	        				// Update number of group member
	        				String groupinfo[] = db.searchGroup(info.get("groupID").toString()).split("12345");
	        				db.updateGroup(groupinfo[0], groupinfo[1], Integer.parseInt(groupinfo[2])+1);
	        			}
	        		}
	        		else if (command.equalsIgnoreCase("OUTMEMBER"))
	        		{
	        			// If command is outmember
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());
	        			// Search user table with information which is parsed from message user send
	        			String user = db.searchUser(info.get("userID").toString());
	        			// If user exist
	        			if(user != "")
	        			{
	        				// Search all member of specific group with information which is parsed from message user send
	        				String member[] = db.searchGroupMember(info.get("groupID").toString()).split("54321");
	        				for (int i=0; i<member.length; i++)
	        				{
	        					String mem[] = member[i].split("12345");
	        					System.out.println(mem[1]+" "+info.get("userID").toString());
	        					// If user_id is same with client user_id then delete member in group_member table
	        					if (mem[1].equals(info.get("userID").toString()))
	        					{
	        						System.out.println("1");
	        						db.deleteGroupMember(info.get("groupID").toString(), info.get("userID").toString());
	        						String groupinfo[] = db.searchGroup(info.get("groupID").toString()).split("12345");

	        						// If group_member was 1 before remove group, now no member in group so delete group
	        						if (groupinfo[2] == "1")
	        						{
	        							db.deleteGroup(info.get("groupID").toString());
	        						}
	        						else
	        						{
	        							db.updateGroup(groupinfo[0], groupinfo[1], Integer.parseInt(groupinfo[2])-1);
	        						}
	        					}
	        				}
	        			}
	        		}
	        		else if (command.equalsIgnoreCase("PRIVATE2GROUP"))
	        		{
	        			// If command is private2group which means group sharing file in user cloud storage
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());
	        			// Search file table with information which is parsed from message user send and update file
	        			String file[] = db.searchFile(info.get("fileID").toString(), "file_id", "1").split("12345");
	        			db.updateFile(file[0], file[1], file[2], file[3], file[4], file[5], Double.parseDouble(file[6]), file[7], info.get("groupID").toString(), Integer.parseInt(file[9]), Integer.parseInt(file[10]));
	        		}
	        		else if (command.equalsIgnoreCase("GROUP2PRIVATE"))
	        		{
	        			// If command is group2private which means don't sharing file in user cloud storage
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());
	        			// Search file table with information which is parsed from message user send and update file
	        			String file[] = db.searchFile(info.get("fileID").toString(), "file_id", info.get("groupID").toString()).split("12345");
	        			db.updateFile(file[0], file[1], file[2], file[3], file[4], file[5], Double.parseDouble(file[6]), file[7], "1", Integer.parseInt(file[9]), Integer.parseInt(file[10]));
	        		}
	        		else if (command.equalsIgnoreCase("PUBLIC2GROUP"))
	        		{
	        			// If command is public2group which means group sharing file in user cloud storage
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());
	        			// Search file table and user table with information which is parsed from message user send
	        			// and update file table and user table
	        			String file[] = db.searchFile(info.get("fileID").toString(), "file_id", "0").split("12345");
	        			String user[] = db.searchUser(info.get("userID").toString()).split("12345");
	        			db.updateFile(file[0], file[1], file[2], file[3], file[4], file[5], Double.parseDouble(file[6]), file[7], info.get("groupID").toString(), Integer.parseInt(file[9]), Integer.parseInt(file[10]));
	        			db.updateUser(user[0], user[1], user[2], user[3], user[4], user[5], user[6], Double.parseDouble(user[7]), Double.parseDouble(user[8])+Double.parseDouble(file[6]));
	        		}
	        		else if (command.equalsIgnoreCase("GROUP2PUBLIC"))
	        		{
	        			// If command is group2public which means sharing file in user cloud storage
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject info = (JSONObject)parser.parse(din.readUTF());
	        			// Search file table and user table with information which is parsed from message user send
	        			// and update file table and user table
	        			String file[] = db.searchFile(info.get("fileID").toString(), "file_id", info.get("groupID").toString()).split("12345");
	        			String user[] = db.searchUser(info.get("userID").toString()).split("12345");
	        			db.updateFile(file[0], file[1], file[2], file[3], file[4], file[5], Double.parseDouble(file[6]), file[7], "0", Integer.parseInt(file[9]), Integer.parseInt(file[10]));
	        			db.updateUser(user[0], user[1], user[2], user[3], user[4], user[5], user[6], Double.parseDouble(user[7]), Double.parseDouble(user[8])-Double.parseDouble(file[6]));
	        		}
	        		else if (command.equalsIgnoreCase("SEARCHUSER"))
	        		{
	        			// If command is searchuser
	        			// Create JSON objects to parse information from message which client send
	        			// Create JSON objects to store result of searching
	        			JSONParser parser = new JSONParser();
	        			JSONObject key = (JSONObject)parser.parse(din.readUTF());

	        			// Search user table with information which is parsed from message user send
	        			// and store result to JSON object and send it to client
	        			String user[] = db.searchUser(key.get("userID").toString()).split("12345");
	        			JSONObject result = new JSONObject();
	        			result.put("userID", user[0]);
	        			result.put("name", user[2]);
	        			result.put("phoneNumber", user[3]);
	        			result.put("address", user[4]);
	        			result.put("email", user[5]);
	        			result.put("gender", user[6]);
	        			result.put("totalStorage", user[7]);
	        			result.put("usageStorage", user[8]);
	        			dout.writeUTF(result.toString());
	        		}
	        		else if(command.equalsIgnoreCase("IDCHECK"))
	        		{
	        			// If command is idcheck
	        			// Create JSON objects to parse information from message which client send
	        			JSONParser parser = new JSONParser();
	        			JSONObject key = (JSONObject)parser.parse(din.readUTF());

	        			// Searching user table with information which is parsed from message user send
	        			// and send it to client
	        			String user = db.searchUser(key.get("userID").toString());
	        			dout.writeUTF(user);
	        		}
	        }
        	} catch (Exception e)
        	{
        		System.out.println(e);
        		System.out.print(2);
        	}
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
		private static String backUp_IP = "127.0.0.1";
		private static int backUp_PORT = 11112;
		private String sync_file = null;

		public UpdownData(Socket client, String command, String sync_file)
		{
			this.client = client;
			this.command = command;
			this.sync_file = sync_file;
		}

		public UpdownData(Socket client, String command)
		{
			this.client = client;
			this.command = command;
		}

    public void run()
    {
    	try {

    		in = client.getInputStream();
	    din = new DataInputStream(in);
	    s_out = client.getOutputStream();
	    dout = new DataOutputStream(s_out);

	    // If command is upload
        	if (command.equalsIgnoreCase("UPLOAD"))
        	{

        		// Create JSON object to parse information from message which user send
        		JSONParser parser = new JSONParser();
        		JSONObject fileinfo = (JSONObject)parser.parse(din.readUTF());

	            long data = din.readLong();					// receive length of file to upload
		        String filename[] = din.readUTF().split("/");				// receive name of file to upload
		        File file = new File("/Users/Knight/upload/"+filename[filename.length-1]);	// Create file with name which is received from client
		        out = new FileOutputStream(file);

		        // If file is already exist in server then don't upload file.
			        	dout.writeUTF("NOTEXIST");

			        	// Insert file information which is parsed from message user send and update user table.
			        db.insertFile(fileinfo.get("userID").toString(), fileinfo.get("fileID").toString(), filename[filename.length-1], fileinfo.get("type").toString(),
	        				fileinfo.get("category").toString(), fileinfo.get("directory").toString(), Double.parseDouble(fileinfo.get("size").toString()),
	        				fileinfo.get("date").toString(), fileinfo.get("shareOffset").toString(), 0, 0);

			        System.out.println(filename[filename.length-1]);

			        // Set file offset as 1 which means don't sharing this file.
			        if(fileinfo.get("shareOffset").toString().equalsIgnoreCase("1"))
			        {
			        		String user[] = db.searchUser(fileinfo.get("userID").toString()).split("12345");
			        		db.updateUser(user[0], user[1], user[2], user[3], user[4], user[5], user[6], Double.parseDouble(user[7]), Double.parseDouble(user[8])+Double.parseDouble(fileinfo.get("size").toString()));
			        }


			        long datas = data;
			        byte[] buffer = new byte[1024];        // Buffer to store segment of file
			        int len;


			        for(;data>0;data--){                   // Receive file segment from client `data` times
			            len = in.read(buffer);
			            out.write(buffer,0,len);
		        }

		        System.out.println("?•½: "+datas+" kbps");
		        String searchFile[] = db.searchFile(fileinfo.get("fileID").toString(), "file_id", "1").split("12345");
		        if(searchFile.length != 0 && searchFile[10].equalsIgnoreCase("1"))
		        {
		        		new UpdownData(this.client, "BACKUP").run();
		        }

		        out.flush();
		        out.close();
	        }

        	// If command is download
        		else if (command.equalsIgnoreCase("DOWNLOAD"))
        		{

        			// Create JSON object to parse information from message user send.
        			JSONParser parser = new JSONParser();
        			JSONObject fileinfo = (JSONObject)parser.parse(din.readUTF());
        			String filename = fileinfo.get("name").toString();

        			// Search file table with information which is parsed from message user send
        			String fi[] = db.searchFile(fileinfo.get("name").toString(), "name", fileinfo.get("range").toString()).split("12345");
        			System.out.print(filename);
        			File f = null;

        			f = new File("/Users/Knight/upload/"+filename);
        			if ((f.exists()))
        			{
        				dout.writeUTF("File open success");
        			}else
        			{
        				if(fileinfo.get("user").toString().equalsIgnoreCase(fi[0]) && fi[10].equalsIgnoreCase("1"))
        				{
        					dout.writeUTF("File couldn't be opened, Do you want backup(Y/N) ");
        					if(din.readUTF().equalsIgnoreCase("Y"))
        					{
        						UpdownData ud = new UpdownData(this.client, "SYNC", fi[1]);
        						ud.run();
        						ud.join();
        					}
        				}
        				else
        				{
        					dout.writeUTF("File couldn't be opened");
        					return;
        				}
        			}

        			if(fileinfo.get("range").toString() == "0" && fi[0] != fileinfo.get("user").toString())
        			{
        				db.updateFile(fi[0], fi[1], fi[2], fi[3], fi[4], fi[5], Double.parseDouble(fi[6]), fi[7], fi[8], Integer.parseInt(fi[9])+1, Integer.parseInt(fi[10]));
        				if((Integer.parseInt(fi[9]) + 1)%10 == 0)
        				{
        					String user[] = db.searchUser(fi[0]).split("12345");
        					db.updateUser(user[0], user[1], user[2], user[3], user[4], user[5], user[6], Double.parseDouble(user[7])+1024, Double.parseDouble(user[8]));
        				}

        			}

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

			    fin = new FileInputStream(filename);
			    dout.writeLong(data);                   // Send length of file to client

			    len = 0;

			    for(;data>0;data--){                 // Send segment of file to client. size of segment is 1 KB
			    		len = fin.read(buffer);
			        s_out.write(buffer,0,len);
			    }
        		}

        		else if (command.equalsIgnoreCase("BACKUP"))
        		{
        			InputStream b_in = null;
        			FileOutputStream b_out = null;
        			DataInputStream b_din = null;
        			OutputStream b_s_out = null;
        			DataOutputStream b_dout = null;
        			FileInputStream b_fin = null;
        			Socket sock = new Socket(backUp_IP, backUp_PORT);
        			String file[] = null;

        			b_in = sock.getInputStream();
        		    b_din = new DataInputStream(b_in);
        		    b_s_out = sock.getOutputStream();
        		    b_dout = new DataOutputStream(b_s_out);

        		    b_dout.writeUTF(command);
        			String fileID = din.readUTF();
        			b_dout.writeUTF(fileID);
        			file = db.searchFile(fileID, "file_id", "1").split("12345");
        			File f = new File(file[2]);

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

    			    b_dout.writeLong(data);

    			    long datas = data;

    			    b_fin = new FileInputStream(file[2]);
    			    b_dout.writeLong(data);                   // Send length of file to client

    			    len = 0;

    			    for(;data>0;data--){                 // Send segment of file to client. size of segment is 1 KB
    			    		len = b_fin.read(buffer);
    			        b_s_out.write(buffer,0,len);
    			    }

    			    db.updateFile(file[0], file[1], file[2], file[3], file[4], file[5], Double.parseDouble(file[6]), file[7], file[8], 0, 1);
        		}

        		else if (command.equalsIgnoreCase("SYNC"))
        		{
        			InputStream b_in = null;
        			FileOutputStream b_out = null;
        			DataInputStream b_din = null;
        			OutputStream b_s_out = null;
        			DataOutputStream b_dout = null;
        			FileInputStream b_fin = null;
        			Socket sock = new Socket(backUp_IP, backUp_PORT);

        			b_in = sock.getInputStream();
        		    b_din = new DataInputStream(b_in);
        		    b_s_out = sock.getOutputStream();
        		    b_dout = new DataOutputStream(b_s_out);

        		    b_dout.writeUTF(command);
        			String fileinfo[] = db.searchFile(this.sync_file, "file_id", "1").split("12345");				// receive name of file to upload
        			b_dout.writeUTF(fileinfo[1]);
    	            long data = b_din.readLong();					// receive length of file to upload
    		        File file = new File("/Users/Knight/upload/"+fileinfo[2]);	// Create file with name which is received from client
    		        out = new FileOutputStream(file);

    		        // If file is already exist in server then don't upload file.

    			        	// Insert file information which is parsed from message user send and update user table.


    			        long datas = data;
    			        byte[] buffer = new byte[1024];        // Buffer to store segment of file
    			        int len;


    			        for(;data>0;data--){                   // Receive file segment from client `data` times
    			            len = b_in.read(buffer);
    			            out.write(buffer,0,len);
    		        }

    		        System.out.println("?•½: "+datas+" kbps");
    		        out.flush();
    		        out.close();
        		}

        		else if (command.equalsIgnoreCase("DELETE_BACKUP"))
        		{
        			InputStream b_in = null;
        			FileOutputStream b_out = null;
        			DataInputStream b_din = null;
        			OutputStream b_s_out = null;
        			DataOutputStream b_dout = null;
        			FileInputStream b_fin = null;
        			Socket sock = new Socket(backUp_IP, backUp_PORT);


        			b_in = sock.getInputStream();
        		    b_din = new DataInputStream(b_in);
        		    b_s_out = sock.getOutputStream();
        		    b_dout = new DataOutputStream(b_s_out);

        		    b_dout.writeUTF(command);
        		    String fileID = din.readUTF();
        		    b_dout.writeUTF(fileID);
        		    String file[] = db.searchFile(fileID, "file_id", "1").split("12345");
        		    db.updateFile(file[0], file[1], file[2], file[3], file[4], file[5], Double.parseDouble(file[6]), file[7], file[8], 0, 0);
        		}
    	} catch (Exception e)
    	{
    		System.out.println(e);
    		System.out.print(3);
    	}
    }
    }

}
