package application.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import application.model.FileModel;
import application.model.Share_group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GroupDB {

	//server
		static String userID = "userID";
	    static String password = "";
	    static String serverIP = "127.0.0.1";

		public GroupDB(){

		}

		public ObservableList<Share_group> getFileList(){

			ObservableList<Share_group> GroupList = FXCollections.observableArrayList();

			OutputStream out = null;
	        FileInputStream fin;

			try{
				Socket soc = new Socket(serverIP,11111);
	            System.out.println("Server Connected!");            //11111�� �꽌踰꾩젒�냽 �룷�듃�엯�땲�떎.
	            out =soc.getOutputStream();                 		  //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            DataOutputStream dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            InputStream in = soc.getInputStream();             //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
	            DataInputStream din = new DataInputStream(in);

	            String command = "Groupsearch";	//�궎蹂대뱶濡� 紐낅졊�뼱瑜� �엯�젰諛쏆뒿�땲�떎.
	            System.out.print(command);
	            dout.writeUTF(command);

				JSONObject searchKey = new JSONObject();
				searchKey.put("userID", userID);
				dout.writeUTF(searchKey.toString());
				JSONParser parser = new JSONParser();
				JSONObject result = (JSONObject)parser.parse(din.readUTF());
				JSONArray files = (JSONArray)result.get("groups");

				for(int i=0; i<files.size(); i++)
				{
					JSONObject file = (JSONObject)files.get(i);

					String groupID = file.get("groupID").toString();
					String UserID = file.get("userID").toString();
					String groupname =file.get("groupname").toString();
					int headcount = Integer.parseInt(file.get("headcount").toString());



					Share_group shareGroup = new Share_group(groupID, groupname, headcount
							,UserID);


					GroupList.add(shareGroup);
				}
			} catch(Exception e){
				e.printStackTrace();
			}

			return GroupList;

		}
}
