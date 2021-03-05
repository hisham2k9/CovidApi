package com.example.CovidApi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.CovidApi.model.Country;
public interface CountryRepo extends JpaRepository<Country, Integer>
{
	
	@Query("from Country where code=?1 order by date desc")
	List<Country> findByCode(String code);
	
	@Query("from Country where date=?1 order by date desc")
	List<Country> findByDate(String date);

}
