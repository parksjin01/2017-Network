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
import application.model.Word;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SignUpViewController {

	static String userID = "userID";
    static String password = "password";
    static String serverIP = "192.168.137.199";

	@FXML
	private TextField IDField;

	@FXML
	private TextField PasswordField;

	@FXML
	private TextField rePasswordField;

	@FXML
	private TextField NameField;

	@FXML
	private TextField PhoneField;

	@FXML
	private TextField AddressField;

	@FXML
	private TextField EmailField;

	@FXML
	private TextField SexField;

	private Stage dialogStage;
	private User user;
	private int returnValue;

	public int getReturnValue(){
		return returnValue;
	}

	public void setSignUp(User user){
		this.user = user;
		IDField.setText(user.getUser_ID());
		PasswordField.setText(user.getPassword());
		rePasswordField.setText(user.getPassword());
		NameField.setText(user.getName());
		PhoneField.setText(user.getPhone_number());
		AddressField.setText(user.getAddress());
		EmailField.setText(user.getE_mail());
		SexField.setText(user.getSex());
	}

	public void SetDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}


	@FXML
	private void initialize(){

	}
	@FXML
	public void SignUpAction(){

		if(valid()){
			SignUp();
	        returnValue = 1;
	        dialogStage.close();
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

		if(rePasswordField.getText() == null || rePasswordField.getText().equals("")){
			errorMessage += "비밀번호를 입력해주십시오\n";
		}

		if(NameField.getText() == null || NameField.getText().equals("")){
			errorMessage += "이름을 입력해주십시오\n";
		}

		if(PhoneField.getText() == null || PhoneField.getText().equals("")){
			errorMessage += "번호를 입력해주십시오\n";
		}

		if(AddressField.getText() == null || AddressField.getText().equals("")){
			errorMessage += "주소를 입력해주십시오\n";
		}

		if(EmailField.getText() == null || EmailField.getText().equals("")){
			errorMessage += "이메일을 입력해주십시오\n";
		}

		if(SexField.getText() == null || SexField.getText().equals("")){
			errorMessage += "성별를 입력해주십시오\n";
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

	public void SignUp(){
		
        OutputStream out = null;
        FileInputStream fin;

        try{
        	Socket soc = new Socket(serverIP,11111);
            System.out.println("Server Connected!");            //11111�� �꽌踰꾩젒�냽 �룷�듃�엯�땲�떎.
            out =soc.getOutputStream();                 		  //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
            DataOutputStream dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
            InputStream in = soc.getInputStream();             //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
            DataInputStream din = new DataInputStream(in); 
            
            String command = "SignUp";	//�궎蹂대뱶濡� 紐낅졊�뼱瑜� �엯�젰諛쏆뒿�땲�떎.
            System.out.print(command);
            dout.writeUTF(command);
            
            JSONObject user = new JSONObject();	//JSON�쓣 �씠�슜�븯�뿬 �쑀�� DB�뿉 �뱾�뼱媛��빞�븯�뒗 �젙蹂대�� ���옣�빀�땲�떎.
            user.put("userID", IDField.getText());
            user.put("password", PasswordField.getText());
            user.put("name", rePasswordField.getText());
            user.put("phoneNumber", PhoneField.getText());
            user.put("address", AddressField.getText());
            user.put("email", AddressField.getText());
            user.put("gender", SexField.getText());
            user.put("totalStorage", 20);
            user.put("usageStorage", 0);
            dout.writeUTF(user.toString());
        }
        catch(Exception e){
        	System.out.println(e);
        }
	}
}
