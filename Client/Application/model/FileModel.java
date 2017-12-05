package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FileModel {

	private final StringProperty user_ID;
	private final StringProperty file_ID;
	private final StringProperty file_name;
	private final StringProperty type;
	private final StringProperty category ;
	private final StringProperty directory ;
	private final DoubleProperty size ;
	private final StringProperty date ;
	private final StringProperty share_offset;
	private final IntegerProperty download;
	private final IntegerProperty backup;

	public FileModel(String UserID, String file_name, String category, String date,double size){

		this.user_ID = new SimpleStringProperty(UserID);
		this.file_name = new SimpleStringProperty(file_name);
		this.category = new SimpleStringProperty(category);
		this.date = new SimpleStringProperty(date);
		this.size = new SimpleDoubleProperty(size);


		// 테스트를 위해 초기화하는 더미 데이터


		this.file_ID = new SimpleStringProperty("file_ID");
		this.type = new SimpleStringProperty("type");
		this.directory = new SimpleStringProperty("directory");
		//this.date = new SimpleStringProperty("date");
		this.share_offset = new SimpleStringProperty("share_offset");
		this.download = new SimpleIntegerProperty(0);
		this.backup = new SimpleIntegerProperty(0);

		/*
		this.user_ID = new SimpleStringProperty(user_ID);
		this.file_ID = new SimpleStringProperty(file_ID);
		this.file_name = new SimpleStringProperty(file_name);
		this.type = new SimpleStringProperty(type);
		this.category = new SimpleStringProperty(category);
		this.directory = new SimpleStringProperty(directory);
		this.size = new SimpleFloatProperty(size);
		this.date = new SimpleStringProperty(date);
		this.share_offset = new SimpleStringProperty(share_offset);
		this.download = new SimpleIntegerProperty(download);
		this.backup = new SimpleIntegerProperty(backup);
		*/

	}

	public FileModel(String user_ID, String file_ID,String file_name, String type,
			String category, String directory , double size,
			  String date, String share_offset ,int download, int backup){

		this.user_ID = new SimpleStringProperty(user_ID);
		this.file_ID = new SimpleStringProperty(file_ID);
		this.file_name = new SimpleStringProperty(file_name);
		this.type = new SimpleStringProperty(type);
		this.category = new SimpleStringProperty(category);
		this.directory = new SimpleStringProperty(directory);
		this.size = new SimpleDoubleProperty(size);
		this.date = new SimpleStringProperty(date);
		this.share_offset = new SimpleStringProperty(share_offset);
		this.download = new SimpleIntegerProperty(download);
		this.backup = new SimpleIntegerProperty(backup);


	}

	public String getuser_ID() {
		return user_ID.get();
	}

	public void Setuser_ID(String user_ID) {
		this.user_ID.set(user_ID);
	}

	public StringProperty getuser_IDProperty(){
		return user_ID;
	}

	public String getFile_ID() {
		return file_ID.get();
	}

	public void SetFile_ID(String file_ID) {
		this.file_ID.set(file_ID);
	}

	public StringProperty getFile_IDProperty(){
		return file_ID;
	}

	public String getFile_name() {
		return file_name.get();
	}

	public void SetFile_name(String file_name) {
		this.file_name.set(file_name);
	}

	public StringProperty getFile_nameProperty(){
		return file_name;
	}

	public String getType() {
		return type.get();
	}

	public void SetType(String type) {
		this.type.set(type);
	}

	public StringProperty getTypeProperty(){
		return type;
	}

	public String getCategory() {
		return category.get();
	}

	public void SetCategory(String category) {
		this.category.set(category);
	}

	public StringProperty getCategoryProperty(){
		return category;
	}

	public String getDirectory() {
		return directory.get();
	}

	public void SetDirectory(String directory) {
		this.directory.set(directory);
	}

	public StringProperty getDirectoryProperty(){
		return directory;
	}

	public double getSize() {
		return size.get();
	}

	public void SetSize(float size) {
		this.size.set(size);
	}

	public DoubleProperty getSizeProperty(){
		return size;
	}

	public String getDate() {
		return file_ID.get();
	}

	public void SetDate(String date) {
		this.date.set(date);
	}

	public StringProperty getDateProperty(){
		return date;
	}

	public String getShare_offset() {
		return share_offset.get();
	}

	public void SetShare_offset(String share_offset) {
		this.share_offset.set(share_offset);
	}

	public StringProperty getShare_offsetProperty(){
		return share_offset;
	}

	public int getDownload() {
		return download.get();
	}

	public void SetDownload(int download) {
		this.download.set(download);
	}

	public IntegerProperty getDownloadProperty(){
		return download;
	}

	public int getBackup() {
		return download.get();
	}

	public void SetBackup(int backup) {
		this.backup.set(backup);
	}

	public IntegerProperty getBackupProperty(){
		return backup;
	}
}
