package application.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.tika.*;

/**
* @file Client.java
* @breif Client program for Web hard service which is made for 2017-Network term project
* @breif Full source code is in github https://github.com/parksjin01/2017-Network
* @author Seong Jin Park
* @return None
* @remark None
* @version 1.0
*/
public class Client{

	static String userID = "userID";
    static String password = "password";
    static String serverIP = "127.0.0.1";

    /**
    * @brief Check UserID
    * @details Check userID whether userID is already registered or not
    * @author Seong Jin Park
    * @version 1.0
    * @date 2017.11.07
    * @pre Called by main function
    * @param dout DataOutputStream to server socket
    * @param din DataInputStream from server socket
    * @param userID UserId which user wants to check whether it is already registered
    * @return True if registered already
    * @return False if it's not registered
    */
    public static boolean IDcheck(DataOutputStream dout, DataInputStream din, String userID)
    {
    		JSONObject user = new JSONObject();
    		user.put("userID", userID);
    		try {
				dout.writeUTF(user.toString());
				String result = din.readUTF();
				if (result != "")
	    				return true;
	    			else
	    				return false;
    			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}

    }

    /**
    * @brief Generate MD5 hash
    * @details Generate MD5 hash with parameter string
    * @author Seong Jin Park
    * @version 1.0
    * @date 2017.11.07
    * @pre Called by main function
    * @param String which is used to generate MD5 hash
    * @return MD5 hash value which is calculated from parameter string
    */
	public static String MD5(String str){

		String MD5 = "";

		try{

			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(str.getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();

			for(int i = 0 ; i < byteData.length ; i++){

				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));

			}

			MD5 = sb.toString();



		}catch(NoSuchAlgorithmException e){

			e.printStackTrace();

			MD5 = null;

		}

		return MD5;

	}

	/**
    * @brief Main function of client program
    * @details Create socket which is connected to server socket
    * @details Create stream to read and write data to socket
    * @details Receive user command and call appropriate method or commands to handle it
    * @author Seong Jin Park
    * @version 1.0
    * @date 2017.11.07
    * @pre First initialize the system
    * @param Parameters which is inputed when program is started
    * @return None
    */
    public static void main(String[] args){
        OutputStream out = null;
        FileInputStream fin;

        try{
        	Socket soc = new Socket(serverIP,11111);
        	System.out.println("Server Connected!");            // 11111 is Server port number
            out =soc.getOutputStream();                         // Create outputstream to socket 
            DataOutputStream dout = new DataOutputStream(out);
            InputStream in = soc.getInputStream();             // Create inputstream to socket
            DataInputStream din = new DataInputStream(in);     
            
            
            Scanner s = new Scanner(System.in);   // Create Scanner instance to receive file name from user
            while(true){
                String command = s.nextLine();   // Receive command from keyboard
                System.out.print(command);
                dout.writeUTF(command);         // Send command to server
                if (command.equalsIgnoreCase("Upload") | command.equalsIgnoreCase("Download"))      //명령이 업로드/다운로드 인경
                   {
                   String filename = s.next();    // Receive file name from keyboard and store it
                   int port = din.readInt();     // Receive new port number from user 
                   new UpdownData(serverIP, port, filename, command).run(); // Create new thread and communicate with server using thread
                   }


                else if(command.equalsIgnoreCase("DELETE"))
                {
                		JSONObject fileInfo = new JSONObject();
                		String fileID = s.next();
                		fileInfo.put("fileID", fileID);
                		fileInfo.put("range", "1");
                		fileInfo.put("userID", "userID");
                		fileInfo.put("password", "password");
                		dout.writeUTF(fileInfo.toString());
                }

                else if (command.equalsIgnoreCase("Login"))   // If command is login
                {
                   JSONObject user = new JSONObject();   // Use JSON to store needed information
                   user.put("userID", "userID");      // `userID` is user ID
                   user.put("password", "password");   // `password` is user's password
                   dout.writeUTF(user.toString());      // Transmit JSON information to server
                   String result = din.readUTF();      // Receive result from user
                   if (result.equalsIgnoreCase("Success"))   // If success, store user ID and password in global variable
                   {
                      userID = "userID";
                      password = "password";
                      System.out.println("Log in success");
                   }else                        // If failed
                   {
                      System.out.println("Log in failed");
                   }
                }
                
                else if (command.equalsIgnoreCase("Signup"))   // If command is sign up
                {
                      JSONObject user = new JSONObject();   // Use JSON to store information which has to be stored at server
                      user.put("userID", "userID");
                      user.put("password", "password");
                      user.put("name", "name");
                      user.put("phoneNumber", "phoneNumber");
                      user.put("address", "address");
                      user.put("email", "email");
                      user.put("gender", "male");
                      user.put("totalStorage", "totalStorage");
                      user.put("usageStorage", "usageStorage");
                      dout.writeUTF(user.toString());
                }
                else if (command.equalsIgnoreCase("Search"))   // If command is search
                {
                      JSONObject searchKey = new JSONObject();   // Use JSON to store needed information to search
                      searchKey.put("keyword", "userID");      // keyword is search word
                      searchKey.put("std", "user");         // std is standard of searching
                      searchKey.put("range", "0");            // range is scope of file sharing (0 is shared, 1 is private, Group file is searhed with group id)
                      dout.writeUTF(searchKey.toString());
                      JSONParser parser = new JSONParser();   // Parsing from data which server sent
                      JSONObject result = (JSONObject)parser.parse(din.readUTF());
                      JSONArray files = (JSONArray)result.get("files");
                      for(int i=0; i<files.size(); i++)      // print file information per file
                      {
                         JSONObject file = (JSONObject)files.get(i);
                         System.out.println(file.get("userID"));
                         System.out.println(file.get("fileID"));
                         System.out.println(file.get("fileName"));
                         System.out.println(file.get("type"));
                         System.out.println(file.get("category"));
                         System.out.println(file.get("directory"));
                         System.out.println(file.get("size"));
                         System.out.println(file.get("date"));
                         System.out.println(file.get("shareOffset"));
                         System.out.println(file.get("download"));
                      }
                }
                else if (command.equalsIgnoreCase("Private2Public") | command.equalsIgnoreCase("Public2Private"))   // Change private to public or public to private
                {
                      JSONObject info = new JSONObject();   // Use JSON to store user_id and file_id
                      info.put("userID", "userID");
                      info.put("fileID", "e4c2710e4c3a1ac59081596970ce0f15");
                      dout.writeUTF(info.toString());
                }
                else if (command.equalsIgnoreCase("CreateGroup"))   // If command is create group
                {
                      JSONObject groupinfo = new JSONObject();      // Use JSON to store group name, user's ID and group ID
                      groupinfo.put("groupName", "Test");
                      groupinfo.put("userID", "userID");
                      groupinfo.put("groupID", MD5("userID"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())+"Test")));
                      dout.writeUTF(groupinfo.toString());
                }
                else if (command.equalsIgnoreCase("AddMember"))   // If command is add member to exist group
                {
                      JSONObject meminfo = new JSONObject();      // Use JSON to store group ID(which user want to join) and user ID
                      meminfo.put("groupID", "247e807f1f64fb2b783441bf729615e9");
                      meminfo.put("userID", "userID2");
                      dout.writeUTF(meminfo.toString());
                }
                else if (command.equalsIgnoreCase("OutMember"))   // If command is exiting from exist group
                {
                      JSONObject meminfo = new JSONObject();      // Use JSON to store group id(which user want to exit), user's ID
                      meminfo.put("groupID", "247e807f1f64fb2b783441bf729615e9");
                      meminfo.put("userID", "userID2");
                      dout.writeUTF(meminfo.toString());
                }
                else if (command.equalsIgnoreCase("Private2Group") || command.equalsIgnoreCase("Group2Private") || command.equalsIgnoreCase("Public2Group") || command.equalsIgnoreCase("Group2Public"))   // If command is changing public or private file to group sharing file or changing group sharing file to public or private file .
                {
                      JSONObject fileinfo = new JSONObject();   // Use JSON to store file ID, user ID, group ID
                      fileinfo.put("fileID", "e4c2710e4c3a1ac59081596970ce0f15");
                      fileinfo.put("userID", "userID");
                      fileinfo.put("groupID", "247e807f1f64fb2b783441bf729615e9");
                      dout.writeUTF(fileinfo.toString());
                }
                else if (command.equalsIgnoreCase("SearchUser"))
                {
                      JSONObject fileinfo = new JSONObject(); // Use JSON to store information which is needed to search user
                      fileinfo.put("userID", "userID");
                      dout.writeUTF(fileinfo.toString());
                      JSONParser parser = new JSONParser();
                      JSONObject user = (JSONObject)parser.parse(din.readUTF());
                      System.out.println(user.get("userID"));   
                      System.out.println(user.get("name"));
                      System.out.println(user.get("phoneNumber"));
                      System.out.println(user.get("address"));
                      System.out.println(user.get("email"));
                      System.out.println(user.get("gender"));
                      System.out.println(user.get("totalStorage"));
                      System.out.println(user.get("usageStorage"));
                      
                }
                else if (command.equalsIgnoreCase("IDcheck")) // If command is check ID duplication
                {
                      System.out.println(IDcheck(dout, din, "userID"));
                      
                }

                else if (command.equalsIgnoreCase("BACKUP"))
                {
                		Scanner ss = new Scanner(System.in);
                		String filename = ss.nextLine();
                		dout.writeUTF(filename);
                }

                else if (command.equalsIgnoreCase("DELETE_BACKUP"))
                {
                		Scanner ss = new Scanner(System.in);
                		String filename = ss.nextLine();
                		dout.writeUTF(filename);
                }
                else if (command.equalsIgnoreCase("Quit"))   // If command is quit command to quit program
                {
	                	out.close();
	                	soc.close();
	                	dout.close();
	                	din.close();
	                	in.close();
	                	System.exit(0);
                }
            }
        }
		                catch(Exception e){
			       }

    }

    /**
    @brief Class to handle file upload and download
    @details Class to handle file upload and download with using thread
    @details Using this class, we can upload file and handle other commands simultaneously because it extends thread
    @author Seong Jin Park
    @version 1.0
    @date 2017.11.07
    @pre Called by main function
    */
    private static class UpdownData extends Thread
    {
    	   private OutputStream out = null;
    	   private DataOutputStream dout = null;
    	   private InputStream in = null;
    	   private DataInputStream din = null;
    	   private Socket data = null;
    	   private String filename = null;
    	   private String command = null;

    	   /**
    	    @brief Constructor
    	    @details Constructor of file upload/download class
    	    @author Seong Jin Park
    	    @version 1.0
    	    @date 2017.11.07
    	    @pre Called by main function
    	    @param serverIP IP address of server which should be connected to client
    	    @param port port number of socket in server which should be connected to client
    	    @param filename filename which should be uploaded or download
    	    @param command designate user wants upload or download
    	    @return None
    	    */
    	   public UpdownData(String serverIP, int port, String filename, String command)
    	   {
    		   try {
				Thread.sleep(100);
			   System.out.println(port);
			   this.data = new Socket(serverIP, port);
	    		   this.filename = filename;
	    		   this.command = command;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	   }
    	   
    	   /**
    	    @brief Handle upload or download file
    	    @details Handle file upload to server or file download from server
    	    @author Seong Jin Park
    	    @version 1.0
    	    @date 2017.11.07
    	    @pre Called by main function
    	    @return None
    	    */
    	   public void run()
    	   {

    		   try {
    		out =this.data.getOutputStream();                 
        dout = new DataOutputStream(out);
        in = this.data.getInputStream();                
        DataInputStream din = new DataInputStream(in);  

            if (command.equalsIgnoreCase("Upload"))		
            	{
            		File f = new File(filename);
            		Tika tika = new Tika(); 						 
                FileInputStream fin = new FileInputStream(f); 
                JSONObject fileinfo = new JSONObject();
		        byte[] buffer = new byte[1024];        
		        int len;                               
		        long data = f.length();                            
		        if(data%1024 != 0)
		        {
		        		data = data/1024 + 1;
		        }else
		        {
		        		data = data/1024;
		        }

		        long datas = data;                      

		        fileinfo.put("userID", "userID");
		        fileinfo.put("fileID", MD5("userID"+filename+"directory"));
		        fileinfo.put("type", tika.detect(f));
		        fileinfo.put("category", "Movie");
		        fileinfo.put("directory", "directory");
		        fileinfo.put("size", datas);
		        fileinfo.put("date", new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
		        fileinfo.put("shareOffset", "1");

		        System.out.println(Files.probeContentType(Paths.get(filename)));
		        dout.writeUTF(fileinfo.toString());
		        dout.writeLong(data);                   
		        dout.writeUTF(filename);               
		        String e = din.readUTF();

		        if(e.equalsIgnoreCase("EXIST"))
		        {
		        		System.out.println("File already exist");
		        }else
		        {
		         len = 0;

		        for(;data>0;data--){                   
		            len = fin.read(buffer);        
		            out.write(buffer,0,len);       
		        }

		        System.out.println("Send "+datas+" kbyte");
		        }
            	}
            else if (command.equalsIgnoreCase("Download"))
            {
            		JSONObject fileinfo = new JSONObject();
            		fileinfo.put("name", filename);
            		fileinfo.put("user", "userID");
            		fileinfo.put("range", "1");
        	        dout.writeUTF(fileinfo.toString());
        	        String result = din.readUTF();
        	        System.out.println(result);
        	        if (result.contains("backup"))
        	        {
        	        		String option = new Scanner(System.in).nextLine();
        	        		dout.writeUTF(option);
        	        }
        	        Long data = din.readLong();           
        	        File file = new File("/Users/Knight/download2/" + filename);             
        	        out = new FileOutputStream(file);           

        	        Long datas = data;                            
        	        byte[] buffer = new byte[1024];        
        	        int len;                               


        	        for(;data>0;data--){                   
        	            len = in.read(buffer);
        	            out.write(buffer,0,len);
        	        }
        	        System.out.println("Recv: "+datas+" kbps");
        	        out.flush();
        	        System.out.println("Download Finish");
            }
    		   }
	                catch(Exception e){
		       }
    }
}

}
