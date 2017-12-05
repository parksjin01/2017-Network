package application.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.simple.JSONObject;

import application.Main;
import application.model.FileModel;
import application.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginViewController {

	static String userID = "userID";
    static String password = "password";
    static String serverIP = "192.168.137.199";

	@FXML
	private TextField IDField;

	@FXML
	private TextField PasswordField;

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
		IDField.setText(user.getUser_ID());
		PasswordField.setText(user.getPassword());
	}

	@FXML
	private void initialize(){

	}

	@FXML
	public void LoginAction(){
		if(valid()){
			Login();
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
		if(IDField.getText() == null || IDField.getText().equals("")){
			errorMessage += "아이디를 를 입력해주십시오\n";
		}

		if(PasswordField.getText() == null || PasswordField.getText().equals("")){
			errorMessage += "비밀번호를 입력해주십시오\n";
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

	public void Login(){
		 	OutputStream out = null;
	        FileInputStream fin;

	        try{
	        	Socket soc = new Socket(serverIP,11111);
	            System.out.println("Server Connected!");            //11111�� �꽌踰꾩젒�냽 �룷�듃�엯�땲�떎.
	            out =soc.getOutputStream();                 		  //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            DataOutputStream dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            InputStream in = soc.getInputStream();             //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
	            DataInputStream din = new DataInputStream(in);

	            String command = "Login";	//�궎蹂대뱶濡� 紐낅졊�뼱瑜� �엯�젰諛쏆뒿�땲�떎.
	            System.out.print(command);
	            dout.writeUTF(command);

	            JSONObject user = new JSONObject();	//JSON �쓣 �씠�슜�븯�뿬 �븘�슂�븳 �젙蹂대�� ���빀�땲�떎.
            	user.put("userID", IDField.getText());		//userID�뒗 �쑀���쓽 ID
            	user.put("password", PasswordField.getText());	//password�뒗 �쑀���쓽 password
            	dout.writeUTF(user.toString());		//JSON�젙蹂대�� �꽌踰꾩뿉 �쟾�떖�빀�땲�떎.
            	String result = din.readUTF();		//濡쒓렇�씤 寃곌낵瑜� �꽌踰꾨줈遺��꽣 諛쏆븘�샃�땲�떎.
            	if (result.equalsIgnoreCase("Success"))	//�꽦怨듯븳 寃쎌슦 �쟾�뿭蹂��닔�뿉 �쑀�� �븘�씠�뵒�� 鍮꾨�踰덊샇瑜� ���옣�빀�땲�떎.
            	{
            		userID = IDField.getText();
            		password = PasswordField.getText();
            		System.out.println("Log in success");
            	}else								//濡쒓렇�씤�씠 �떎�뙣�븳 寃쎌슦
            	{
            		System.out.println("Log in failed");
            	}
	        }
	        catch(Exception e){
	        	System.out.println(e);
	        }
	}
}
