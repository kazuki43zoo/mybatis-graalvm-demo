package com.example.mapper;

import com.example.domain.City;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface CityMapper {

  City findById(Long id);

  @Insert("INSERT INTO city (name, state, country) VALUES(#{name}, #{state}, #{country}) ")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(City city);

}
