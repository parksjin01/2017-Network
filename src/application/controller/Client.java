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

public class Client{

	static String userID = "sad";
    static String password = "adsadsadsasasd";
    static String serverIP = "127.0.0.1";

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

    public static void main(String[] args){
        OutputStream out = null;
        FileInputStream fin;

        try{
        	Socket soc = new Socket(serverIP,11111);
            System.out.println("Server Connected!");            //11111�� �꽌踰꾩젒�냽 �룷�듃�엯�땲�떎.
            out =soc.getOutputStream();                 		  //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
            DataOutputStream dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
            InputStream in = soc.getInputStream();             //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
            DataInputStream din = new DataInputStream(in);     //InputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 DataInputStream�쓣 媛쒗넻�빀�땲�떎.


            Scanner s = new Scanner(System.in);   //�뙆�씪 �씠由꾩쓣 �엯�젰諛쏄린�쐞�빐 �뒪罹먮꼫瑜� �깮�꽦�빀�땲�떎.
            while(true){
                String command = s.nextLine();	//�궎蹂대뱶濡� 紐낅졊�뼱瑜� �엯�젰諛쏆뒿�땲�떎.
                System.out.print(command);
                dout.writeUTF(command);			//�꽌踰꾩뿉 紐낅졊�뼱瑜� 蹂대깄�땲�떎.
                if (command.equalsIgnoreCase("Upload") | command.equalsIgnoreCase("Download"))		//紐낅졊�씠 �뾽濡쒕뱶/�떎�슫濡쒕뱶 �씤寃�
                	{
	                String filename = s.next();    //�뒪罹먮꼫瑜� �넻�빐 �뙆�씪�쓽 �씠由꾩쓣 �엯�젰諛쏄퀬,
	                int port = din.readInt();	  //�꽌踰꾨줈遺��꽣 �깉濡쒖슫 �냼耳볦쓽 �룷�듃踰덊샇瑜� 諛쏄퀬
	                new UpdownData(serverIP, port, filename, command).run(); //�벐�젅�뱶瑜� �깉濡� �깮�꽦�빐�꽌 �꽌踰꾩� �넻�떊�빀�땲�떎.
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

                else if (command.equalsIgnoreCase("Login"))	//紐낅졊�뼱媛� 濡쒓렇�씤�씤 寃쎌슦,
                {
                	JSONObject user = new JSONObject();	//JSON �쓣 �씠�슜�븯�뿬 �븘�슂�븳 �젙蹂대�� ���빀�땲�떎.
                	user.put("userID", "userID");		//userID�뒗 �쑀���쓽 ID
                	user.put("password", "password");	//password�뒗 �쑀���쓽 password
                	dout.writeUTF(user.toString());		//JSON�젙蹂대�� �꽌踰꾩뿉 �쟾�떖�빀�땲�떎.
                	String result = din.readUTF();		//濡쒓렇�씤 寃곌낵瑜� �꽌踰꾨줈遺��꽣 諛쏆븘�샃�땲�떎.
                	if (result.equalsIgnoreCase("Success"))	//�꽦怨듯븳 寃쎌슦 �쟾�뿭蹂��닔�뿉 �쑀�� �븘�씠�뵒�� 鍮꾨�踰덊샇瑜� ���옣�빀�땲�떎.
                	{
                		userID = "userID";
                		password = "password";
                		System.out.println("Log in success");
                	}else								//濡쒓렇�씤�씠 �떎�뙣�븳 寃쎌슦
                	{
                		System.out.println("Log in failed");
                	}
                }

                else if (command.equalsIgnoreCase("Signup"))	//紐낅졊�뼱媛� �쉶�썝媛��엯�씤 寃쎌슦
                {
	                	JSONObject user = new JSONObject();	//JSON�쓣 �씠�슜�븯�뿬 �쑀�� DB�뿉 �뱾�뼱媛��빞�븯�뒗 �젙蹂대�� ���옣�빀�땲�떎.
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
                else if (command.equalsIgnoreCase("Search"))	//紐낅졊�뼱媛� 寃��깋 紐낅졊�뼱�씤 寃쎌슦.
                {
                		JSONObject searchKey = new JSONObject();	//JSON�쓣 �씠�슜�빐�꽌寃��깋�뿉 �븘�슂�븳 �젙蹂대�� ���옣�빀�땲�떎.
                		searchKey.put("keyword", "userID");		//Keyword�뒗 寃��깋�뼱.
                		searchKey.put("std", "user");			//std�뒗 臾댁뾿�쓣 湲곗��쑝濡� 寃��깋�븷 寃껋씤媛�.
                		searchKey.put("range", "all");				//range�뒗 �뙆�씪 寃��깋�쓽 怨듦컻 踰붿쐞(0�� 怨듦컻�뙆�씪, 1�� 媛쒖씤�뙆�씪, 洹몃９�뙆�씪�� 洹몃９ �븘�씠�뵒濡� 寃��깋)
                		dout.writeUTF(searchKey.toString());
                		JSONParser parser = new JSONParser();	//�꽌踰꾩뿉�꽌 蹂대궡�삩 �젙蹂대�� 媛�吏�怨� �뙆�떛.
                		JSONObject result = (JSONObject)parser.parse(din.readUTF());
                		JSONArray files = (JSONArray)result.get("files");
                		for(int i=0; i<files.size(); i++)		//�뙆�씪 1媛쒕떦 �빐�떦 �뙆�씪�쓽 �젙蹂대�� 異쒕젰.
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
                else if (command.equalsIgnoreCase("Private2Public") | command.equalsIgnoreCase("Public2Private"))	//媛쒖씤 �뙆�씪�쓣 怨듦컻�뙆�씪濡� 蹂�寃쏀븯�뒗 寃쎌슦�굹, 怨듦컻�뙆�씪�쓣 媛쒖씤�뙆�씪濡� 蹂�寃쏀븯�뒗 寃�
                {
                		JSONObject info = new JSONObject();	//JSON�쓣 �씠�슜�빐�꽌 �쑀�� �븘�씠�뵒�� �뙆�씪 �븘�씠�뵒瑜� ���옣�빀�땲�떎.
                		info.put("userID", "userID");
                		info.put("fileID", "e4c2710e4c3a1ac59081596970ce0f15");
                		dout.writeUTF(info.toString());
                }
                else if (command.equalsIgnoreCase("CreateGroup"))	//紐낅졊�뼱媛� 洹몃９�쓣 留뚮뱶�뒗 紐낅졊�뼱�씤 寃쎌슦.
                {
                		JSONObject groupinfo = new JSONObject();		//JSON�쓣 �씠�슜�빐�꽌 洹몃９ �씠由�, 洹몃９�쓣 留뚮뱶�젮�뒗 �쑀���쓽 �씠由�, 洹몃９ �븘�씠�뵒瑜� ���옣�빀�땲�떎.
                		groupinfo.put("groupName", "Test");
                		groupinfo.put("userID", "userID");
                		groupinfo.put("groupID", MD5("userID"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())+"Test")));
                		dout.writeUTF(groupinfo.toString());
                }
                else if (command.equalsIgnoreCase("AddMember"))	//紐낅졊�뼱媛� 湲곗〈�쓽 洹몃９�뿉 �깉濡쒖슫 �쑀��瑜� 異붽��븯�뒗 紐낅졊�뼱�씤 寃쎌슦.
                {
                		JSONObject meminfo = new JSONObject();		//JSON�쓣 �씠�슜�빐�꽌 洹몃９ �븘�씠�뵒, 珥덈��븯�젮�뒗 �쑀�� �븘�씠�뵒瑜� �쟾�떖�빀�땲�떎.
                		meminfo.put("groupID", "247e807f1f64fb2b783441bf729615e9");
                		meminfo.put("userID", "userID2");
                		dout.writeUTF(meminfo.toString());
                }
                else if (command.equalsIgnoreCase("OutMember"))	//紐낅졊�뼱媛� 湲곗〈�쓽 洹몃９�뿉�꽌 �굹媛��젮�뒗 紐낅졊�뼱�씤 寃쎌슦.
                {
                		JSONObject meminfo = new JSONObject();		//JSON�쓣 �씠�슜�빐�꽌 �굹媛��젮�뒗 洹몃９�쓽 洹몃９�븘�씠�뵒, �굹媛��젮�뒗 �쑀���쓽 �쑀���븘�씠�뵒瑜� ���옣�빀�땲�떎.
                		meminfo.put("groupID", "247e807f1f64fb2b783441bf729615e9");
                		meminfo.put("userID", "userID2");
                		dout.writeUTF(meminfo.toString());
                }
                else if (command.equalsIgnoreCase("Private2Group") || command.equalsIgnoreCase("Group2Private") || command.equalsIgnoreCase("Public2Group") || command.equalsIgnoreCase("Group2Public"))	//紐낅졊�뼱媛� 怨듦컻/鍮꾧났媛� �뙆�씪�쓣 洹몃９ 怨듦컻 �뙆�씪濡� �쟾�솚�븯�젮�뒗 寃쎌슦�굹, 洹몃９ 怨듦컻 �뙆�씪�쓣 怨듦컻/鍮꾧났媛� �뙆�씪濡� �쟾�솚�븯�젮�뒗 寃쎌슦.
                {
                		JSONObject fileinfo = new JSONObject();	//JSON�쓣 �씠�슜�빐�꽌 �쟾�솚�븯�젮�뒗 �뙆�씪�쓽 �뙆�씪 �븘�씠�뵒, �쑀�� �븘�씠�뵒, 洹몃９ �븘�씠�뵒瑜� ���옣�빀�땲�떎.
                		fileinfo.put("fileID", "e4c2710e4c3a1ac59081596970ce0f15");
                		fileinfo.put("userID", "userID");
                		fileinfo.put("groupID", "247e807f1f64fb2b783441bf729615e9");
                		dout.writeUTF(fileinfo.toString());
                }
                else if (command.equalsIgnoreCase("SearchUser"))
                {
                		JSONObject fileinfo = new JSONObject(); //JSON�쓣 �씠�슜�빐�꽌 寃��깋�븯�젮�뒗 �쑀�� �븘�씠�뵒 ���옣.
                		fileinfo.put("userID", "userID");
                		dout.writeUTF(fileinfo.toString());
                		JSONParser parser = new JSONParser();
                		JSONObject user = (JSONObject)parser.parse(din.readUTF());
                		System.out.println(user.get("userID"));	//�떆�뿕�궪�븘 �쑀���븘�씠�뵒瑜� 寃��깋�븯硫� �쑀���쓽 紐⑤뱺 �젙蹂대�� 異쒕젰�븯�룄濡� 留뚮벉.
                		System.out.println(user.get("name"));
                		System.out.println(user.get("phoneNumber"));
                		System.out.println(user.get("address"));
                		System.out.println(user.get("email"));
                		System.out.println(user.get("gender"));
                		System.out.println(user.get("totalStorage"));
                		System.out.println(user.get("usageStorage"));

                }
                else if (command.equalsIgnoreCase("IDcheck")) //�샊�떆 紐낅졊�뼱媛� �븘�씠�뵒 以묐났 泥댄겕�씪硫�.
                {
                		System.out.println(IDcheck(dout, din, "userID"));		//ID以묐났 泥댄겕瑜� �빐二쇰뒗 �븿�닔瑜� �샇異쒗븳�떎.

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
                else if (command.equalsIgnoreCase("Quit"))	//留뚯빟 醫낅즺�븯�뒗 紐낅졊�뼱�씤 寃�
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

    private static class UpdownData extends Thread
    {
    	   private OutputStream out = null;
    	   private DataOutputStream dout = null;
    	   private InputStream in = null;
    	   private DataInputStream din = null;
    	   private Socket data = null;
    	   private String filename = null;
    	   private String command = null;

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

    	   public void run()
    	   {

    		   try {
    		out =this.data.getOutputStream();                 //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
        dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
        in = this.data.getInputStream();                //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
        DataInputStream din = new DataInputStream(in);  //InputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 DataInputStream�쓣 媛쒗넻�빀�땲�떎.

            if (command.equalsIgnoreCase("Upload"))		//�뾽濡쒕뱶 紐낅졊�뼱�씤 寃쎌슦.
            	{
            		File f = new File(filename);
            		Tika tika = new Tika(); 						 // For detecting file mimetype
                FileInputStream fin = new FileInputStream(f); //FileInputStream - �뙆�씪�뿉�꽌 �엯�젰諛쏅뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
                JSONObject fileinfo = new JSONObject();
		        byte[] buffer = new byte[1024];        //諛붿씠�듃�떒�쐞濡� �엫�떆���옣�븯�뒗 踰꾪띁瑜� �깮�꽦�빀�땲�떎.
		        int len;                               //�쟾�넚�븷 �뜲�씠�꽣�쓽 湲몄씠瑜� 痢≪젙�븯�뒗 蹂��닔�엯�땲�떎.
		        long data = f.length();                            //�쟾�넚�슏�닔, �슜�웾�쓣 痢≪젙�븯�뒗 蹂��닔�엯�땲�떎.
		        if(data%1024 != 0)
		        {
		        		data = data/1024 + 1;
		        }else
		        {
		        		data = data/1024;
		        }

		        long datas = data;                      //�븘�옒 for臾몄쓣 �넻�빐 data媛� 0�씠�릺湲곕븣臾몄뿉 �엫�떆���옣�븳�떎.

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
		        dout.writeLong(data);                   //�뜲�씠�꽣 �쟾�넚�슏�닔瑜� �꽌踰꾩뿉 �쟾�넚�븯怨�,
		        dout.writeUTF(filename);               //�뙆�씪�쓽 �씠由꾩쓣 �꽌踰꾩뿉 �쟾�넚�빀�땲�떎.
		        String e = din.readUTF();

		        if(e.equalsIgnoreCase("EXIST"))
		        {
		        		System.out.println("File already exist");
		        }else
		        {
		         len = 0;

		        for(;data>0;data--){                   //�뜲�씠�꽣瑜� �씫�뼱�삱 �슏�닔留뚰겮 FileInputStream�뿉�꽌 �뙆�씪�쓽 �궡�슜�쓣 �씫�뼱�샃�땲�떎.
		            len = fin.read(buffer);        //FileInputStream�쓣 �넻�빐 �뙆�씪�뿉�꽌 �엯�젰諛쏆� �뜲�씠�꽣瑜� 踰꾪띁�뿉 �엫�떆���옣�븯怨� 洹� 湲몄씠瑜� 痢≪젙�빀�땲�떎.
		            out.write(buffer,0,len);       //�꽌踰꾩뿉寃� �뙆�씪�쓽 �젙蹂�(1kbyte留뚰겮蹂대궡怨�, 洹� 湲몄씠瑜� 蹂대깄�땲�떎.
		        }

		        System.out.println("�빟 "+datas+" kbyte");
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
        	        Long data = din.readLong();           //Int�삎 �뜲�씠�꽣瑜� �쟾�넚諛쏆뒿�땲�떎.
        	        File file = new File("/Users/Knight/download2/" + filename);             //�엯�젰諛쏆� File�쓽 �씠由꾩쑝濡� 蹂듭궗�븯�뿬 �깮�꽦�빀�땲�떎.
        	        out = new FileOutputStream(file);           //�깮�꽦�븳 �뙆�씪�쓣 �겢�씪�씠�뼵�듃濡쒕��꽣 �쟾�넚諛쏆븘 �셿�꽦�떆�궎�뒗 FileOutputStream�쓣 媛쒗넻�빀�땲�떎.

        	        Long datas = data;                            //�쟾�넚�슏�닔, �슜�웾�쓣 痢≪젙�븯�뒗 蹂��닔�엯�땲�떎.
        	        byte[] buffer = new byte[1024];        //諛붿씠�듃�떒�쐞濡� �엫�떆���옣�븯�뒗 踰꾪띁瑜� �깮�꽦�빀�땲�떎.
        	        int len;                               //�쟾�넚�븷 �뜲�씠�꽣�쓽 湲몄씠瑜� 痢≪젙�븯�뒗 蹂��닔�엯�땲�떎.


        	        for(;data>0;data--){                   //�쟾�넚諛쏆� data�쓽 �슏�닔留뚰겮 �쟾�넚諛쏆븘�꽌 FileOutputStream�쓣 �씠�슜�븯�뿬 File�쓣 �셿�꽦�떆�궢�땲�떎.
        	            len = in.read(buffer);
        	            out.write(buffer,0,len);
        	        }
        	        System.out.println("�빟: "+datas+" kbps");
        	        out.flush();
        	        System.out.println("Download Finish");
            }
    		   }
	                catch(Exception e){
		       }
    }
}

}
