package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import application.controller.*;
import application.model.*;


public class Main extends Application {

	public static String userID = "";
	public static String password = "";
	public static String serverIP = "127.0.0.1";

	private Stage primaryStage;
	private Stage LoginStage;
	private Stage SignUpStage;

    private BorderPane rootLayout;

    private ObservableList<FileModel> Data = FXCollections.observableArrayList();

    public Main() {
    }

    public ObservableList<FileModel> getFileData() {
        return Data;
    }

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("GAFT HARD");

        initRootLayout();

        showPersonOverview();
	}

	public void initRootLayout() {
        try {
            // fxml 파일에서 상위 레이아웃을 가져온다.

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainBorderPane.fxml"));
            rootLayout = (BorderPane) loader.load();
            // 상위 레이아웃을 포함하는 scene을 보여준다.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void showPersonOverview() {
        try {
            // 연락처 요약을 가져온다.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainView.fxml"));
            AnchorPane MainView = (AnchorPane) loader.load();

            // 연락처 요약을 상위 레이아웃 가운데로 설정한다.
            rootLayout.setCenter(MainView);

            MainViewController controller = loader.getController();
            controller.setMain(this);

            this.RefreshFile();

        } catch (IOException e) {
            e.printStackTrace();
            }
    }

	/*
	 * 파일을 추가하는 창입니다.
	 */
	public int setFileUploadView(FileModel file){

		try{
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/FileUploadView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("파일 업로드");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FileUploadController controller = loader.getController();
            controller.SetDialogStage(dialogStage);
            controller.setFile(file);


            dialogStage.showAndWait();

	        this.RefreshFile();
            return controller.getReturnValue();
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	public int setFileDownloadView(FileModel file){

		try{
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/FileDownloadView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("파일 다운로드");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FileDownloadController controller = loader.getController();
            controller.SetDialogStage(dialogStage);
            controller.setFile(file);

            dialogStage.showAndWait();
            return controller.getReturnValue();

		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}



	/*
	 * 파일을 추가하는 창입니다.
	 */
	public int setSignUpView(User user){

		try{
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/SignUpView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("파일 회원가입");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SignUpViewController controller = loader.getController();
            controller.SetDialogStage(dialogStage);
            controller.setSignUp(user);

            dialogStage.showAndWait();
            return controller.getReturnValue();
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}


	}

	public int setLoginView(User user){

		try{
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/LoginView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("로그인");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            LoginViewController controller = loader.getController();
            controller.SetDialogStage(dialogStage);
            controller.setLogin(user);


            dialogStage.showAndWait();
            return controller.getReturnValue();
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	public int setCreateGroupView(User user){

		try{
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CreateGroupView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("그룹만들기");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CreateGroupController controller = loader.getController();
            controller.SetDialogStage(dialogStage);
            controller.setLogin(user);


            dialogStage.showAndWait();
            return controller.getReturnValue();
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	public int setInviteGroupView(User user){

		try{
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/InviteGroupView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("그룹초대");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            InviteGroupController controller = loader.getController();
            controller.SetDialogStage(dialogStage);
            controller.setLogin(user);


            dialogStage.showAndWait();
            return controller.getReturnValue();
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	public int setOutGroupView(User user){

		try{
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/outGroupView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("그룹나가기");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            OutGroupController controller = loader.getController();
            controller.SetDialogStage(dialogStage);
            controller.setLogin(user);


            dialogStage.showAndWait();
            return controller.getReturnValue();
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void RefreshFile(){
        FileDB fileDB = new FileDB();
        ObservableList<FileModel> tempList = fileDB.getFileList();
        for(int i = 0; i < tempList.size(); i++){
        	Data.add(tempList.get(i));
        }
    }
    
    public void RefreshFile(String cal, String input){
        FileDB fileDB = new FileDB();
        ObservableList<FileModel> tempList = fileDB.getFileList();
        for(int i = 0; i < tempList.size(); i++){
        	Data.add(tempList.get(i));
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
	


}
