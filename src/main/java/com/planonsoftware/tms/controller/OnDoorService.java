package com.planonsoftware.tms.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.jdbc.core.JdbcTemplate;

import com.planonsoftware.tms.constants.Constants;
import com.planonsoftware.tms.dto.LoginDto;
import com.planonsoftware.tms.dto.LoginMasterDto;
import com.planonsoftware.tms.utils.MiscUtils;

public class OnDoorService extends MiscUtils{

	public LoginMasterDto getLoginDetails(JdbcTemplate jdbc, LoginDto user) {
		String sql=Constants.CHECK_LOGIN;
		int count=jdbc.queryForObject(sql, Integer.class ,new Object[] {user.getUserName(),user.getPassword()});
		LoginMasterDto masterDto=new LoginMasterDto();
		if(count>0) {
			sql=Constants.GET_LOGIN_DETAILS;
			List<Map<String, Object>> details = jdbc.queryForList(sql,new Object[] {user.getUserName()});
			setLoginDetails(masterDto,details.get(0));
			masterDto.setErrorMsg("Successfully Logged in");
		}else {
			masterDto.setErrorMsg("Invalid UserName and password ");
		}
		return masterDto;
	}

	private void setLoginDetails(LoginMasterDto masterDto, Map<String, Object> map) {
		masterDto.setFullName(getMapValue(map,"fullName"));
		masterDto.setAddress(getMapValue(map,"address"));
		masterDto.setNoOfOrderTillNow(getMapValueInt(map,"no_of_order_till_now"));
		masterDto.setEmail(getMapValue(map, "email"));
		masterDto.setJoiningDate(getMapValue(map, "joining_date"));
		
		
	}

	private int getMapValueInt(Map<String, Object> map, String key) {
		if(map.get(key)==null) {
			return 0;
		}
		return (Integer) map.get(key);
	}

	private String getMapValue(Map<String, Object> map, String key) {
		if(map.get(key)==null) {
			return null;
		}
		return (String) map.get(key);
	}

	public LoginMasterDto createUser(JdbcTemplate jdbc, LoginMasterDto user) {
		if(ifElementExistInDatabase(jdbc,"login","username",user.getUserName())) {
			user.setErrorMsg("Username exists");
			return user;
		}else if(ifElementExistInDatabase(jdbc,"login","ph_no",user.getPhno())) {
			user.setErrorMsg("Phno alreadyvexists");
			return user;
		}else if(ifElementExistInDatabase(jdbc,"user_info","email",user.getEmail())) {
			user.setErrorMsg("Email already exists");
			return user;
		}
		
		insertToLoginTable(jdbc,user);
		insertToUserInfoTable(jdbc,user);
		user.setErrorMsg("Successfully signed up");
		return user;
	}

	private void insertToLoginTable(JdbcTemplate jdbc, LoginMasterDto user) {
		String sql="INSERT INTO public.login(username, password, role, ph_no)	VALUES ( ?, ?, ?, ?)";
		jdbc.update(sql,new Object[] {user.getUserName(),user.getPassword(),"user",user.getPhno()});
				
	}

	private void insertToUserInfoTable(JdbcTemplate jdbc, LoginMasterDto user) {
		String sql="INSERT INTO public.user_info(fullname, address, joining_date, no_of_order_till_now, email, username)	VALUES (?, ?, ?, ?, ?, ?)";
		jdbc.update(sql,new Object[] {user.getFullName(),user.getAddress(),new Date().toString(),0,user.getEmail(),user.getUserName()});
		
	}

	private boolean ifElementExistInDatabase(JdbcTemplate jdbc, String tableName, String column, String value) {
		String sql="select count(*) from "+tableName+" where "+column+" = '"+value+"'";
		int a=jdbc.queryForObject(sql, Integer.class);
		if(a>0) {
			return true;
		}
		return false;
	}

	public String validateUserCreation(LoginMasterDto user) {
		StringBuffer message=new StringBuffer();
		
		if(isBlank(user.getUserName())) {
			message.append("\nUserName Cant be null");
		}else if(user.getUserName().length()<6) {
			message.append("\nUserName must containg 6 character");
		}
		if(isBlank(user.getFullName())) {
			message.append("\nFullName Cant be null");
		}
		if(isBlank(user.getEmail())) {
			message.append("\nEmail Cant be null");
		}else 
			if(!EmailValidator.getInstance().isValid(user.getEmail())) {
				message.append("\nInvalid email");
			}
		
		if(isBlank(user.getPhno())) {
			message.append("\nPhno Cant be null");
		}else 
			if (user.getPhno().contains("[a-zA-Z]+") == true || user.getPhno().length() !=10){
				message.append("\nInvalid Phone number");
			
		}
		if(isBlank(user.getPassword())) {
			message.append("\nPassword Cant be null");
		}else if(user.getPassword().length()<6) {
			message.append("\nPassword should contain atleast 6 digit");
		}
		return message.toString();
	}

}
