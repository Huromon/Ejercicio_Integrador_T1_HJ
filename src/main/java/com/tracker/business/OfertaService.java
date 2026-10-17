package com.tracker.business;

import com.tracker.data.entity.Oferta;

import java.util.List;

public class OfertaService{


    public static String VerCandidatura(Integer idex, List<Oferta> e){
        return (e.get(idex)).toString();
    }

}
