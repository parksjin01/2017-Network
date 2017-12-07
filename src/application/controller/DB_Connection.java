package application.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_Connection {

	public Connection getConnection() throws ClassNotFoundException, SQLException{
	      
	      Connection con = null;

	      Class.forName("com.mysql.jdbc.Driver");
	      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaft", "root", "Sj199402");
	      
	      return con;
	   }
	   
	   public void selectAll() throws ClassNotFoundException, SQLException{
	      Connection con = getConnection();
	      String sql = "SELECT * FROM USER";
	      ResultSet rs = null;
	      Statement st = null;
	      
	      st = con.createStatement();
	      rs = st.executeQuery(sql);
	      
	      while(rs.next()){
	         String user_id = rs.getString(1);
	         String password = rs.getString(2);
	         String name = rs.getString(3);
	         String phone_number = rs.getString(4);
	         String address = rs.getString(5);
	         String e_mail = rs.getString(6);
	         String sex = rs.getString(7);
	           Double total_storage = rs.getDouble(8);
	           Double usage_storage = rs.getDouble(9);
	      }
	      if(rs!= null) rs.close();
	      if(st != null) st.close();
	      if(con != null) con.close();
	      
	   }
	   
	   public boolean insertUser(String user_id, String password, String name, String phone_number, String address,
		         String e_mail, String sex, double total_storage, double usage_storage) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "insert into user(user_id,password,name,phone_number,address,e_mail,sex,total_storage,usage_storage) values(?,?,?,?,?,?,?,?,?)";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, user_id);
		      pst.setString(2, password);
		      pst.setString(3, name);
		      pst.setString(4, phone_number);
		      pst.setString(5, address);
		      pst.setString(6, e_mail);
		      pst.setString(7, sex);
		      pst.setDouble(8, total_storage);
		      pst.setDouble(9, usage_storage);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         return true;
		      }
		      else
		         return false;
		   }
		   
		   public boolean updateUser(String user_id, String password, String name, String phone_number, String address,
		         String e_mail, String sex, double total_storage, double usage_storage) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "update user set user_id = ?, password = ?, name = ?, phone_number = ?, address = ?, e_mail = ?, sex = ?, total_storage = ?, usage_storage =? where user_id = ? and phone_number = ?";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, user_id);
		      pst.setString(2, password);
		      pst.setString(3, name);
		      pst.setString(4, phone_number);
		      pst.setString(5, address);
		      pst.setString(6, e_mail);
		      pst.setString(7, sex);
		      pst.setDouble(8, total_storage);
		      pst.setDouble(9, usage_storage);
		      pst.setString(10, user_id);
		      pst.setString(11, phone_number);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         return true;
		      }
		      else
		         return false;
		      
		   }
		   
		   public void deleteUser(String user_id, String phone_number) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "delete from user where user_id = ? and phone_number = ?";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, user_id);
		      pst.setString(2, phone_number);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         System.out.println("OK");
		      }
		      if(pst != null) pst.close();
		      if(con != null) con.close();
		      
		   }
		   
		   public String searchUser(String user_ID) throws ClassNotFoundException, SQLException{
			      Connection con = getConnection();
			      String sql = "SELECT * FROM user WHERE user_ID = '" + user_ID + "'";
			      String result = "";
			      ResultSet rs = null;
			      Statement st = null;
			      
			      st = con.createStatement();
			      rs = st.executeQuery(sql);
			      
			      while(rs.next())
			      {
			    	  	result += "54321";
			    	  	result += rs.getString(1)+"12345";
			    	  	result += rs.getString(2)+"12345";
			    	  	result += rs.getString(3)+"12345";
			    	  	result += rs.getString(4)+"12345";
			    	  	result += rs.getString(5)+"12345";
			    	  	result += rs.getString(6)+"12345";
			    	  	result += rs.getString(7)+"12345";
			    	  	result += rs.getDouble(8)+"12345";
			    	  	result += rs.getDouble(9);
			      }
			      if (result.length() == 0)
			      {
			    	  return "";
			      }
			      result = result.substring(5, result.length());
			      
			      if(st != null) st.close();
			      if(con != null) con.close();
			      
			      return result;
			   }
		   
		   public boolean insertFile(String user_id, String file_id, String file_name, String type, String category, String directory,
		         double size, String date, String share_offset, int download, int backup) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "insert into file(user_id,file_id,file_name,type,category,directory,size,date,share_offset, download, backup) values(?,?,?,?,?,?,?,?,?,?,?)";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, user_id);
		      pst.setString(2, file_id);
		      pst.setString(3, file_name);
		      pst.setString(4, type);
		      pst.setString(5, category);
		      pst.setString(6, directory);
		      pst.setDouble(7, size);
		      pst.setString(8, date);
		      pst.setString(9, share_offset);
		      pst.setInt(10, download);
		      pst.setInt(11, backup);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         return true;
		      }
		      else
		         return false;
		   }
		   
		   public  boolean updateFile(String user_id, String file_id, String file_name, String type, String category, String directory,
		         double size, String date, String share_offset, int download, int backup) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "update file set user_id = ?, file_id = ?, file_name = ?, type = ?, category = ?, directory = ?, size = ?, date = ?, share_offset = ?, download = ?, backup = ? where file_id = ?";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, user_id);
		      pst.setString(2, file_id);
		      pst.setString(3, file_name);
		      pst.setString(4, type);
		      pst.setString(5, category);
		      pst.setString(6, directory);
		      pst.setDouble(7, size);
		      pst.setString(8, date);
		      pst.setString(9, share_offset);
		      pst.setInt(10, download);
		      pst.setInt(11, backup);
		      pst.setString(12, file_id);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         return true;
		      }
		      else
		         return false;
		      
		   }
		   
		   public  void deleteFile(String file_id) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "delete from file where file_id = ?";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, file_id);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         System.out.println("OK");
		      }
		      if(pst != null) pst.close();
		      if(con != null) con.close();
		      
		   }
		   
		   public  String searchFile(String keyword, String std, String range) throws ClassNotFoundException, SQLException{
			      Connection con = getConnection();
			      String sql = "";
			      if (std.equals("name"))
			      {
			    	  	sql = "SELECT * FROM file WHERE file_name like '%" + keyword + "%' and share_offset = '" + range + "'";
			      }else if(std.equals("type"))
			      {
			    	  	sql = "SELECT * FROM file WHERE type = '" + keyword + "' and share_offset = '" + range + "'";
			      }else if(std.equals("category"))
			      {
			    	  	sql = "SELECT * FROM file WHERE category = '" + keyword + "' and share_offset = '" + range + "'";
			      }else if(std.equalsIgnoreCase("user") && range.equalsIgnoreCase("all"))
			      {
			    	  sql = "SELECT * FROM file WHERE user_ID = '" + keyword + "'";
			      }else if(std.equals("user"))
			      {
			    	  	sql = "SELECT * FROM file WHERE user_ID = '" + keyword + "' and share_offset = '" + range + "'";
			      }else if(std.equals("file_id"))
			      {
			    	  	sql = "SELECT * FROM file WHERE file_ID = '" + keyword + "' and share_offset = '" + range + "'";
			      }
			      
			      ResultSet rs = null;
			      Statement st = null;
			      String result = "";
			      
			      st = con.createStatement();
			      rs = st.executeQuery(sql);
			      
			      while(rs.next())
			      {
			    	  	result += "54321";
			    	  	result += rs.getString(1)+"12345";
			    	  	result += rs.getString(2)+"12345";
			    	  	result += rs.getString(3)+"12345";
			    	  	result += rs.getString(4)+"12345";
			    	  	result += rs.getString(5)+"12345";
			    	  	result += rs.getString(6)+"12345";
			    	  	result += rs.getDouble(7)+"12345";
			    	  	result += rs.getString(8)+"12345";
			    	  	result += rs.getString(9)+"12345";
			    	  	result += rs.getInt(10)+"12345";
			    	  	result += rs.getInt(11);
			      }
			      			      
			      if (result.length() == 0)
			      {
			    	  return "";
			      }
			      
			      result = result.substring(5, result.length());
			      
			      if(st != null) st.close();
			      if(con != null) con.close();
			      
			      return result;
			   }
		   
		   public  boolean insertGroup(String group_id, String group_name, int headcount) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "insert into share_group(group_id,group_name,headcount) values(?,?,?)";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, group_id);
		      pst.setString(2, group_name);
		      pst.setInt(3, headcount);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         return true;
		      }
		      else
		         return false;
		   }
		   
		   public  boolean updateGroup(String group_id, String group_name, int headcount) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "update share_group set group_id = ?, group_name = ?, headcount = ? where group_id = ?";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, group_id);
		      pst.setString(2, group_name);
		      pst.setInt(3, headcount);
		      pst.setString(4, group_id);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         return true;
		      }
		      else
		         return false;
		      
		   }
		   
		   public  void deleteGroup(String group_id) throws ClassNotFoundException, SQLException{
		      
		      Connection con = getConnection();
		      String sql = "delete from share_group where group_id = ?";
		      PreparedStatement pst = con.prepareStatement(sql);
		      
		      pst.setString(1, group_id);
		      
		      int res = pst.executeUpdate();
		      if(res>0){
		         System.out.println("OK");
		      }
		      if(pst != null) pst.close();
		      if(con != null) con.close();
		      
		   }
		   
		   public  String searchGroup(String group_id) throws ClassNotFoundException, SQLException{
			      Connection con = getConnection();
			      String sql = "SELECT * FROM share_group WHERE group_id = '" + group_id + "'";
			      ResultSet rs = null;
			      Statement st = null;
			      String result = "";
			      
			      st = con.createStatement();
			      rs = st.executeQuery(sql);
			      
			      while(rs.next())
			      {
			    	  	result += "54321";
			    	  	result += rs.getString(1)+"12345";
			    	  	result += rs.getString(2)+"12345";
			    	  	result += rs.getString(3);
			      }
			      
			      if (result.length() == 0)
			      {
			    	  return "";
			      }
			      
			      result = result.substring(5, result.length());
			      
			      if(st != null) st.close();
			      if(con != null) con.close();
			      
			      return result;
			   }
		   
		   public  boolean insertGroupMember(String group_id, String user_id) throws ClassNotFoundException, SQLException{
			      
			      Connection con = getConnection();
			      String sql = "insert into group_member(group_id, user_id) values(?,?)";
			      PreparedStatement pst = con.prepareStatement(sql);
			      
			      pst.setString(1, group_id);
			      pst.setString(2, user_id);
			      
			      int res = pst.executeUpdate();
			      if(res>0){
			         return true;
			      }
			      else
			         return false;
			   }
			   
			   public  boolean updateGroupMember(String group_id, String user_id) throws ClassNotFoundException, SQLException{
			      
			      Connection con = getConnection();
			      String sql = "update group_member set group_id = ?, user_id = ? where group_id = ?";
			      PreparedStatement pst = con.prepareStatement(sql);
			      
			      pst.setString(1, group_id);
			      pst.setString(2, user_id);
			      pst.setString(3, group_id);
			      
			      int res = pst.executeUpdate();
			      if(res>0){
			         return true;
			      }
			      else
			         return false;
			      
			   }
			   
			   public  void deleteGroupMember(String group_id, String user_id) throws ClassNotFoundException, SQLException{
			      
			      Connection con = getConnection();
			      String sql = "delete from group_member where group_id = ? and user_id = ?";
			      PreparedStatement pst = con.prepareStatement(sql);
			      
			      pst.setString(1, group_id);
			      pst.setString(2, user_id);
			      
			      int res = pst.executeUpdate();
			      if(res>0){
			         System.out.println("OK");
			      }
			      if(pst != null) pst.close();
			      if(con != null) con.close();
			      
			   }
			   
			   public  String searchGroupMember(String group_id) throws ClassNotFoundException, SQLException{
				      Connection con = getConnection();
				      String sql = "SELECT * FROM group_member WHERE group_id = '" + group_id + "'";
				      ResultSet rs = null;
				      Statement st = null;
				      String result = "";
				      
				      st = con.createStatement();
				      rs = st.executeQuery(sql);
				      
				      while(rs.next())
				      {
				    	  	result += "54321";
				    	  	result += rs.getString(1)+"12345";
				    	  	result += rs.getString(2);
				      }
				      
				      if (result.length() == 0)
				      {
				    	  return "";
				      }
				      
				      result = result.substring(5, result.length());
				      
				      if(st != null) st.close();
				      if(con != null) con.close();
				      
				      return result;
				   }
	
	
}
