package application.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import org.apache.tika.Tika;
import org.json.simple.JSONObject;

import application.model.FileModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FileDownloadController {

	static String userID = "";
    static String password = "";
    static String serverIP = "192.168.137.199";

	@FXML
	private TextField FilePathField;

	@FXML
	private TextField FileNameField;

	private String selectFileName;

	private Stage dialogStage;
	private FileModel file;
	private int returnValue;

	@FXML
	private void initialize(){

	}

	public void SetselectFileName(String filename){
		this.selectFileName = filename;
	}

	public void SetDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}

	public void setFile(FileModel file){
		//여기 손보기
		this.file = file;
		FilePathField.setText("");
		FileNameField.setText(file.getFile_name());
	}

	public int getReturnValue(){
		return returnValue;
	}
	@FXML
	public void FilePathAction(){

	}

	@FXML
	public void FileDownloadAction(){
		if(valid()){
			OutputStream out = null;
	        FileInputStream fin;

	        try{


	        	Socket soc = new Socket(serverIP,11111);
	            System.out.println("Server Connected!");
	            out =soc.getOutputStream();
	            DataOutputStream dout = new DataOutputStream(out);

	            InputStream in = soc.getInputStream();
	            DataInputStream din = new DataInputStream(in);

	            String filename = FileNameField.getText();

	            dout.writeUTF("Download");
                int port = din.readInt();

                //텍스트 붙여넣기
                new UpdownData(serverIP, port, filename, "Download").run();

                returnValue = 1;


	        }catch(Exception e){
	        	System.out.println(e);
		    }

	        dialogStage.close();
		}
	}

	@FXML
	public void CancelAction(){

	}

	private boolean valid() {
		String errorMessage = "";

		if(FilePathField.getText() == null || FilePathField.getText().equals("")){
			errorMessage += "파일 경로를 입력해주십시오\n";
		}
		if(FileNameField.getText() == null || FileNameField.getText().equals("")){
			errorMessage += "파일 이름를 입력해주십시오\n";
		}
		if(errorMessage.equals("")){
			return true;
		}
		else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("오류메세지");
			alert.setHeaderText("값을 제대로 입력하십시오");
			alert.setContentText(errorMessage);
			alert.showAndWait();
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

		        System.out.println("dd "+datas+" kbyte");
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
	    	        File file = new File(filename);             //�엯�젰諛쏆� File�쓽 �씠由꾩쑝濡� 蹂듭궗�븯�뿬 �깮�꽦�빀�땲�떎.
	    	        out = new FileOutputStream(file);           //�깮�꽦�븳 �뙆�씪�쓣 �겢�씪�씠�뼵�듃濡쒕��꽣 �쟾�넚諛쏆븘 �셿�꽦�떆�궎�뒗 FileOutputStream�쓣 媛쒗넻�빀�땲�떎.

	    	        Long datas = data;                            //�쟾�넚�슏�닔, �슜�웾�쓣 痢≪젙�븯�뒗 蹂��닔�엯�땲�떎.
	    	        byte[] buffer = new byte[1024];        //諛붿씠�듃�떒�쐞濡� �엫�떆���옣�븯�뒗 踰꾪띁瑜� �깮�꽦�빀�땲�떎.
	    	        int len;                               //�쟾 �넚�븷 �뜲�씠�꽣�쓽 湲몄씠瑜� 痢≪젙�븯�뒗 蹂��닔�엯�땲�떎.


	    	        for(;data>0;data--){                   //�쟾�넚諛쏆� data�쓽 �슏�닔留뚰겮 �쟾�넚諛쏆븘�꽌 FileOutputStream�쓣 �씠�슜�븯�뿬 File�쓣 �셿�꽦�떆�궢�땲�떎.
	    	            len = in.read(buffer);
	    	            out.write(buffer,0,len);
	    	        }
	    	        System.out.println("dd: "+datas+" kbps");
	    	        out.flush();
	    	        System.out.println("Download Finish");
	        }
			   }
	                catch(Exception e){
		       }
		   }
	}
}
