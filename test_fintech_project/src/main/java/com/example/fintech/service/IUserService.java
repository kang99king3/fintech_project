package com.example.fintech.service;

import com.example.fintech.dtos.FintechUserDto;

public interface IUserService {

	public boolean addUser(FintechUserDto dto);
	public FintechUserDto login(FintechUserDto dto);
}
