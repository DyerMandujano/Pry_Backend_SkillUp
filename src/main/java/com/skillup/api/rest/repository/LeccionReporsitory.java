package com.skillup.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillup.api.rest.model.Leccion;

public interface LeccionReporsitory extends JpaRepository<Leccion, Integer>{

}
