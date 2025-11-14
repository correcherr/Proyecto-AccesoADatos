package com.proyectoada.dao;

import java.util.List;
import java.util.Optional;
/*Repasad del curso pasado qués es una clase interfaz y su utilidad */
public interface Dao<T, K> {
    K create(T t);
    Optional<T> findById(K id);
    List<T> findAll();
    boolean update(T t);
    boolean delete(K id);
}