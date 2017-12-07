package application.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONObject;

import application.Main;
import application.model.FileModel;
import application.model.Share_group;
import application.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;


public class MainViewController {


	static String userID = "userID";
    static String password = "password";
    static String serverIP = "127.0.0.1";

    @FXML
    private AnchorPane testTableAp;

    @FXML
	private TableView<Share_group> GroupTabel;

	@FXML
	private TableColumn<Share_group, String> GroupName;

	@FXML
	private TableColumn<Share_group, Number> Count;
	
	@FXML
	private TableView<FileModel> CloudFileTable;

	@FXML
	private TableColumn<FileModel, String> Name;

	@FXML
	private TableColumn<FileModel, String> Category;

	@FXML
	private TableColumn<FileModel, String> Date;

	@FXML
	private TableColumn<FileModel, Number> Size;

	@FXML
	private TableColumn<FileModel, String> UserID;

	private	Main main;

	@FXML
    private void initialize() {

		UserID.setCellValueFactory(cellData -> cellData.getValue().getuser_IDProperty());
		Name.setCellValueFactory(cellData -> cellData.getValue().getFile_nameProperty());
		Category.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
		Date.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
		Size.setCellValueFactory(cellData -> cellData.getValue().getSizeProperty());
		
		GroupName.setCellValueFactory(cellData -> cellData.getValue().getGroup_nameProperty());
		Count.setCellValueFactory(cellData -> cellData.getValue().getHeadcountProperty());
    }

	public void setMain(Main main){
		this.main = main;
		CloudFileTable.setItems(main.getFileData());
	}

	@FXML
	public void CloudFileUploadAction(){
		FileModel file = new FileModel("", "", "" , "",0);
		int returnValue = main.setFileUploadView(file);
		if(returnValue == 1){
			//main.getFileData().add(file);
		}
	}



	@FXML
	public void WebHardFileUploadAction(){

	}

	@FXML
	public void CloudSearchAction(){

	}

	@FXML
	public void WebHardSearchAction(){

	}

	@FXML
	public void LoginViewAction(){
		User user = new User( "", "");
		int returnValue = main.setLoginView(user);
		if(returnValue == 1){
			//main.getFileData().add(user);
		}
	}

	@FXML
	public void CreateGroupViewAction(){
		User user = new User( "", "");
		int returnValue = main.setCreateGroupView(user);
		if(returnValue == 1){
			//main.getFileData().add(user);
		}
	}

	@FXML
	public void InviteViewAction(){
		User user = new User( "", "");
		int returnValue = main.setInviteGroupView(user);
		if(returnValue == 1){
			//main.getFileData().add(user);
		}
	}

	@FXML
	public void SignUpAction(){
		User user = new User( "", "");
		int returnValue = main.setSignUpView(user);
		if(returnValue == 1){
			//main.getFileData().add(user);
		}
	}
	@FXML
	public void	outGroupViewAction(){
		User user = new User( "", "");
		int returnValue = main.setOutGroupView(user);
		if(returnValue == 1){
			//main.getFileData().add(user);
		}
	}

	@FXML
	public void refreshAction(){
		User user = new User( "", "");
		int returnValue = main.setSignUpView(user);
		if(returnValue == 1){
			//main.getFileData().add(user);
		}
	}

	@FXML
	public void Private2GroupAction(){
		Group2PPAction("Private2Group");
	}

	public void Public2GroupAction(){
		Group2PPAction("Public2Group");
	}

	public void Group2PublicAction(){
		Group2PPAction("Group2Public");
	}

	public void Group2PrivateAction(){
		Group2PPAction("Group2Private");
	}


	@FXML
	public void Public2PrivateAction(){
		PPAction("Private2Public");
	}

	@FXML
	public void Private2PublicAction(){
		PPAction("Private2Public");
	}


	@FXML
	public void FileDownloadAction(){

		FileModel selectedFIle = CloudFileTable.getSelectionModel().getSelectedItem();
		if(selectedFIle != null){
			int returnValue = main.setFileDownloadView(selectedFIle);
			if(returnValue == 1){
				//main.getFileData().add(file);
			}
			//CloudFileTable.getItems().remove(selectedIndex);
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류메세지");
			alert.setHeaderText("다운로드 에러가 발생했습니다.");
			alert.setContentText("다운로드받을 파일을 선택해주십시오");
			alert.showAndWait();
		}
	}

	@FXML
	public void DeleteFileAction(){
		//좀있다 추가 합시다.
		int selectedIndex = CloudFileTable.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0){
			CloudFileTable.getItems().remove(selectedIndex);
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류메세지");
			alert.setHeaderText("삭제 에러가 발생했습니다.");
			alert.setContentText("삭제할 파일을 선택해주십시오");
			alert.showAndWait();
		}
	}

	@FXML
	public void FileBackupAction(){
		BBAction("BACKUP");
	}

	@FXML
	public void BackupDelectAction(){
		BBAction("DELETE_BACKUP");
	}

	public void PPAction(String command){
		FileModel selectedFIle = CloudFileTable.getSelectionModel().getSelectedItem();
		if(selectedFIle != null){

			OutputStream out = null;
	        FileInputStream fin;

	        try{
	        	Socket soc = new Socket(serverIP,11111);
	            System.out.println("Server Connected!");            //11111�� �꽌踰꾩젒�냽 �룷�듃�엯�땲�떎.
	            out =soc.getOutputStream();                 		  //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            DataOutputStream dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            InputStream in = soc.getInputStream();             //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
	            DataInputStream din = new DataInputStream(in);

	            System.out.print(command);
	            dout.writeUTF(command);

	            JSONObject fileinfo = new JSONObject();	//JSON�쓣 �씠�슜�빐�꽌 �쟾�솚�븯�젮�뒗 �뙆�씪�쓽 �뙆�씪 �븘�씠�뵒, �쑀�� �븘�씠�뵒, 洹몃９ �븘�씠�뵒瑜� ���옣�빀�땲�떎.

	            fileinfo.put("fileID", selectedFIle.getFile_ID());
	    		fileinfo.put("userID", selectedFIle.getuser_ID());
	    		fileinfo.put("groupID",  selectedFIle.getShare_offset());
	    		dout.writeUTF(fileinfo.toString());
	        }
	        catch(Exception e){
	        	System.out.println(e);
	        }
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류메세지");
			alert.setHeaderText("파일 에러가 발생했습니다.");
			alert.setContentText("수정할 파일을 선택해주십시오");
			alert.showAndWait();
		}
	}

	public void BBAction(String command){
		FileModel selectedFIle = CloudFileTable.getSelectionModel().getSelectedItem();
		if(selectedFIle != null){

			OutputStream out = null;
	        FileInputStream fin;

	        try{
	        	Socket soc = new Socket(serverIP,11111);
	            System.out.println("Server Connected!");            //11111�� �꽌踰꾩젒�냽 �룷�듃�엯�땲�떎.
	            out =soc.getOutputStream();                 		  //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            DataOutputStream dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            InputStream in = soc.getInputStream();             //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
	            DataInputStream din = new DataInputStream(in);

	            System.out.print(command);
	            dout.writeUTF(command);

	    		dout.writeUTF(selectedFIle.getFile_ID());
	        }
	        catch(Exception e){
	        	System.out.println(e);
	        }
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류메세지");
			alert.setHeaderText("파일 에러가 발생했습니다.");
			alert.setContentText("수정할 파일을 선택해주십시오");
			alert.showAndWait();
		}
	}

	public void Group2PPAction(String command){
		FileModel selectedFIle = CloudFileTable.getSelectionModel().getSelectedItem();
		if(selectedFIle != null){

			OutputStream out = null;
	        FileInputStream fin;

	        try{
	        	Socket soc = new Socket(serverIP,11111);
	            System.out.println("Server Connected!");            //11111�� �꽌踰꾩젒�냽 �룷�듃�엯�땲�떎.
	            out =soc.getOutputStream();                 		  //�꽌踰꾩뿉 諛붿씠�듃�떒�쐞濡� �뜲�씠�꽣瑜� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            DataOutputStream dout = new DataOutputStream(out); //OutputStream�쓣 �씠�슜�빐 �뜲�씠�꽣 �떒�쐞濡� 蹂대궡�뒗 �뒪�듃由쇱쓣 媛쒗넻�빀�땲�떎.
	            InputStream in = soc.getInputStream();             //�겢�씪�씠�뼵�듃濡� 遺��꽣 諛붿씠�듃 �떒�쐞濡� �엯�젰�쓣 諛쏅뒗 InputStream�쓣 �뼸�뼱�� 媛쒗넻�빀�땲�떎.
	            DataInputStream din = new DataInputStream(in);

	            System.out.print(command);
	            dout.writeUTF(command);

	    		JSONObject fileinfo = new JSONObject();	//JSON�쓣 �씠�슜�빐�꽌 �쟾�솚�븯�젮�뒗 �뙆�씪�쓽 �뙆�씪 �븘�씠�뵒, �쑀�� �븘�씠�뵒, 洹몃９ �븘�씠�뵒瑜� ���옣�빀�땲�떎.
        		fileinfo.put("fileID", selectedFIle.getFile_ID());
        		fileinfo.put("userID", selectedFIle.getuser_ID());
        		fileinfo.put("groupID", "247e807f1f64fb2b783441bf729615e9");
        		dout.writeUTF(fileinfo.toString());
	        }
	        catch(Exception e){
	        	System.out.println(e);
	        }
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류메세지");
			alert.setHeaderText("파일 에러가 발생했습니다.");
			alert.setContentText("수정할 파일을 선택해주십시오");
			alert.showAndWait();
		}
	}

}
