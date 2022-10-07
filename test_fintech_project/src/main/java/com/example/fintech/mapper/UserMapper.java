package com.example.fintech.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.fintech.dtos.FintechUserDto;

@Repository
@Mapper
public interface UserMapper {
	public int addUser(FintechUserDto dto);
	public FintechUserDto login (FintechUserDto dto);
}
