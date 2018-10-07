package com.planonsoftware.tms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planonsoftware.tms.dto.LoginDto;
import com.planonsoftware.tms.dto.LoginMasterDto;

@Controller
public class OnDoorController {

	@Autowired
	JdbcTemplate jdbc;
	OnDoorService service = new OnDoorService();

	@RequestMapping("home")
	public String getHomePage() {
		return "index.html";
	}

	@RequestMapping(value = "home/getLoginDetails", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody LoginMasterDto getLoginDetails(@RequestBody LoginDto user) {
		if(service.isBlank(user.getUserName())||service.isBlank(user.getPassword())) {
			LoginMasterDto loginDetails=new LoginMasterDto();
			loginDetails.setErrorMsg("Invalid username and password");
			return loginDetails;
		}

		LoginMasterDto loginDetails = service.getLoginDetails(jdbc, user);
		System.out.println(loginDetails.getErrorMsg());
		return loginDetails;
	}

	@RequestMapping(value = "home/createUser", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody LoginMasterDto createUser(@RequestBody LoginMasterDto user) {
		String validateMessage = service.validateUserCreation(user);
		if (validateMessage.isEmpty()) {
			LoginMasterDto loginDetails = service.createUser(jdbc, user);
			System.out.println(loginDetails.getErrorMsg());
			user.setPassword("");
			return loginDetails;
		}
		LoginMasterDto dto=new LoginMasterDto();
		dto.setErrorMsg(validateMessage);
		return dto;
	}
}
