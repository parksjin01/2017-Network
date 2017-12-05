package application.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONObject;

import application.Main;
import application.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CreateGroupController {

	static String userID = "userID";
    static String password = "password";
    static String serverIP = "192.168.137.199";

    @FXML
	private TextField GroupNameField;

	@FXML
	private TextField UserIDField;

	private Stage dialogStage;
	private User user;
	private int returnValue;

	private	Main main;

	public void SetDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}

	public int getReturnValue(){
		return returnValue;
	}

	public void setLogin(User user){
		this.user = user;
		GroupNameField.setText(user.getUser_ID());
		UserIDField.setText(user.getPassword());
	}

	@FXML
	private void initialize(){

	}

	@FXML
	public void CreateGroupAction(){
		if(valid()){
			CreateGroup();
	        returnValue = 1;
	        dialogStage.close();
		}
	}

	@FXML
	public void SignUpAction(){

		User user = new User( "", "");
		System.out.println("ddd");
		int returnValue = main.setSignUpView(user);
		if(returnValue == 1){
			//main.getFileData().add(user);
		}
	}

	@FXML
	public void CancelAction(){
		dialogStage.close();
	}

	private boolean valid() {
		String errorMessage = "";

		//텍스트 필드 내용 존재 여부
		if(GroupNameField.getText() == null || GroupNameField.getText().equals("")){
			errorMessage += "그룹이름를 를 입력해주십시오\n";
		}

		if(UserIDField.getText() == null || UserIDField.getText().equals("")){
			errorMessage +=  "유저 ID 를 입력해주십시오\n";
		}

		//에러메세지 존재 여부
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

	public void CreateGroup(){
		 	OutputStream out = null;
	        FileInputStream fin;

	        try{
	        	Socket soc = new Socket(serverIP,11111);
	            System.out.println("Server Connected!");            //11111�� �꽌踰꾩젒�냽 �룷�듃�엯�땲�떎.
	            out =soc.getOutputStream();                 		  //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            DataOutputStream dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            InputStream in = soc.getInputStream();             //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
	            DataInputStream din = new DataInputStream(in);

	            String command ="CreateGroup";	//�궎蹂대뱶濡� 紐낅졊�뼱瑜� �엯�젰諛쏆뒿�땲�떎.
	            System.out.print(command);
	            dout.writeUTF(command);
	            
	            JSONObject groupinfo = new JSONObject();		//JSON�쓣 �씠�슜�빐�꽌 洹몃９ �씠由�, 洹몃９�쓣 留뚮뱶�젮�뒗 �쑀���쓽 �씠由�, 洹몃９ �븘�씠�뵒瑜� ���옣�빀�땲�떎.
        		groupinfo.put("groupName", GroupNameField.getText());
        		groupinfo.put("userID",  UserIDField.getText());
        		groupinfo.put("groupID", MD5(UserIDField.getText()+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())+"Test")));
        		dout.writeUTF(groupinfo.toString());
	        }
	        catch(Exception e){
	        	System.out.println(e);
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
}
