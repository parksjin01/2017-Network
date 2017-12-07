package application.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Share_group {

	private final StringProperty group_ID ;
	private final StringProperty group_name;
	private final IntegerProperty headcount;
	private final StringProperty User_ID;

	public Share_group(String group_ID, String group_name, int headcount, String userid){
		this.group_ID = new SimpleStringProperty(group_ID);
		this.group_name = new SimpleStringProperty(group_name);

		this.headcount = new SimpleIntegerProperty(headcount);
		this.User_ID = new SimpleStringProperty(userid);
	}

	public String getGroup_ID() {
		return group_ID.get();
	}

	public void SetGroup_ID(String group_ID) {
		this.group_ID.set(group_ID);
	}

	public StringProperty getGroup_IDProperty(){
		return group_ID;
	}

	public String getGroup_name() {
		return group_name.get();
	}

	public void SetGroup_name(String group_name) {
		this.group_name.set(group_name);
	}

	public StringProperty getGroup_nameProperty(){
		return group_name;
	}

	public int getHeadcount() {
		return headcount.get();
	}

	public void SetHeadcount(int headcount) {
		this.headcount.set(headcount);
	}

	public IntegerProperty getHeadcountProperty(){
		return headcount;
	}

	public String getUserid() {
		return User_ID.get();
	}

	public void SetUserid(String userid) {
		this.User_ID.set(userid);
	}

	public StringProperty getUseridProperty(){
		return User_ID;
	}
}
