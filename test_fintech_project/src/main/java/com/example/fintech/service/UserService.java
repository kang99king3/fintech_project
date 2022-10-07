package com.example.fintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fintech.dtos.FintechUserDto;
import com.example.fintech.mapper.UserMapper;

@Service
@Transactional
public class UserService implements IUserService{

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public boolean addUser(FintechUserDto dto) {
		int count=userMapper.addUser(dto);
		return count>0?true:false;
	}

	@Override
	public FintechUserDto login(FintechUserDto dto) {
		return userMapper.login(dto);
	}
	
}
