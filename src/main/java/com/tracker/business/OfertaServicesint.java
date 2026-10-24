package com.tracker.business;

import com.tracker.data.entity.Oferta;

import java.util.LinkedList;

public interface OfertaServicesint {
    String VerCandidatura(Integer i);
    String Listar();
    String Listar(LinkedList<Oferta> e);
    String añadir(String[] arg);
    String filtrar(String [] arg);
    LinkedList<Oferta> filtrarEmpresa(String empresa);
    LinkedList<Oferta> filtrarEstado(String estado);
    LinkedList<Oferta> filtrarEstado(String estado,LinkedList<Oferta> nn);







}
