package application.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

	private final StringProperty user_ID;
	private final StringProperty password;
	private final StringProperty name ;
	private final StringProperty phone_number;
	private final StringProperty address ;
	private final StringProperty  e_mail ;
	private final StringProperty sex  ;
	private final FloatProperty total_storage;
	private final FloatProperty usage_storage;


	public User(String user_ID, String password, String name, String phone_number, String address
			, String e_mail, String sex , float total_storage, float share_offset){
		this.user_ID = new SimpleStringProperty(user_ID);
		this.password = new SimpleStringProperty(password);
		this.name = new SimpleStringProperty(name);
		this.phone_number = new SimpleStringProperty(phone_number);
		this.address = new SimpleStringProperty(address);
		this.e_mail = new SimpleStringProperty(e_mail);
		this.sex = new SimpleStringProperty(sex);
		this.total_storage = new SimpleFloatProperty(total_storage);
		this.usage_storage = new SimpleFloatProperty(share_offset);
	}

	public User(String user_ID, String password){
		this.user_ID = new SimpleStringProperty(user_ID);
		this.password = new SimpleStringProperty(password);

		this.name = new SimpleStringProperty("name");
		this.phone_number = new SimpleStringProperty("phone_number");
		this.address = new SimpleStringProperty("address");
		this.e_mail = new SimpleStringProperty("e_mail");
		this.sex = new SimpleStringProperty("sex");
		this.total_storage = new SimpleFloatProperty(0);
		this.usage_storage = new SimpleFloatProperty(0);
	}

	public String getUser_ID() {
		return user_ID.get();
	}

	public void SetUser_ID(String user_ID) {
		this.user_ID.set(user_ID);
	}

	public StringProperty getUser_IDProperty(){
		return user_ID;
	}

	public String getPassword() {
		return password.get();
	}

	public void SetPassword(String password) {
		this.password.set(password);
	}

	public StringProperty getPasswordProperty(){
		return password;
	}

	public String getName() {
		return name.get();
	}

	public void SetName(String name) {
		this.name.set(name);
	}

	public StringProperty getNameProperty(){
		return name;
	}

	public String getPhone_number() {
		return phone_number.get();
	}

	public void SetPhone_number(String phone_number) {
		this.phone_number.set(phone_number);
	}

	public StringProperty getPhone_numberProperty(){
		return phone_number;
	}

	public String getAddress() {
		return address.get();
	}

	public void SetAddress(String address) {
		this.address.set(address);
	}

	public StringProperty getAddressProperty(){
		return address;
	}

	public String getE_mail() {
		return e_mail.get();
	}

	public void SetE_mail(String e_mail) {
		this.e_mail.set(e_mail);
	}

	public StringProperty getE_mailProperty(){
		return e_mail;
	}

	public String getSex() {
		return sex.get();
	}

	public void SetSex(String sex) {
		this.sex.set(sex);
	}

	public StringProperty getSexProperty(){
		return sex;
	}

	public float getTotal_storage() {
		return total_storage.get();
	}

	public void SetTotal_storage(float total_storage) {
		this.total_storage.set(total_storage);
	}

	public FloatProperty getTotal_storageProperty(){
		return total_storage;
	}

	public float getUsage_storage() {
		return usage_storage.get();
	}

	public void SetUsage_storage(float usage_storage) {
		this.usage_storage.set(usage_storage);
	}

	public FloatProperty getUsage_storageProperty(){
		return usage_storage;
	}



}
