package com.example.CovidApi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.CovidApi.model.State;

public interface StateRepo extends JpaRepository<State, Integer>
{

	@Query("from State where code=?1 order by district")
	List<State> findByCode(String code);
	
}
