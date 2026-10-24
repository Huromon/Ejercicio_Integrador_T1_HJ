package com.tracker.business;

import com.tracker.data.entity.Estado;
import com.tracker.data.entity.Oferta;
import com.tracker.data.entity.candidaturas;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class OfertaService implements OfertaServicesint{
   candidaturas lista = new candidaturas();
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   static Integer ids = 1;

@Override
    public  String VerCandidatura(Integer idex ) {
        for (Oferta l : lista.getListado()){
            if(l.id().equals(idex)){
                return l.toString();
            }
        }
        return "no se ha encontrado una candidatura con ese index";
    }
@Override
    public String Listar(){
        return lista.getListado().stream().toString();
    }

    @Override
    public String añadir(String[] arg) {
    if(arg.length >= 9){
        if (arg[1].equalsIgnoreCase("--titulo") &&
                arg[3].equalsIgnoreCase("--empresa") &&
                arg[5].equalsIgnoreCase("--estado") &&
                arg[7].equalsIgnoreCase("--fecha")
        ) {
            switch (arg.length) {
                case 9: {
                    try{
                        lista.getListado().add(new
                                Oferta(
                                        ids,
                                        arg[2],
                                        arg[4],
                                        comprobar(arg[6].toString()),
                                        LocalDate.parse(arg[8].trim(),FORMATO),
                                        "",
                                        "",
                                        0.0
                                )
                        );
                        ids++;
                        return "añadido con exito";
                    }catch (IllegalArgumentException e){
                        return e.getMessage();
                    }

                }
                case 11: {
                    if(arg[9].equalsIgnoreCase("--enlace")){

                    }
                    break;
                }
                case 13: {


                    break;
                }
                case 15: {


                    break;
                }
                default: return "parametros invalidos";


            }
        }


        return "";
    }
    return "";

}

    @Override
    public String filtrar(String[] arg) {
        return "";
    }

    @Override
    public LinkedList<Oferta> filtrarEmpresa(String empresa) {
        return null;
    }

    @Override
    public LinkedList<Oferta> filtrarEstado(String estado) {
        return null;
    }

    @Override
    public LinkedList<Oferta> filtrarEstado(String estado, LinkedList<Oferta> nn) {
        return null;
    }

    public Estado comprobar(String s ){
        switch (s.toUpperCase()){
            case "PENDIENTE" -> {
                return Estado.PENDIENTE;
            }
            case "ACEPTADO" -> {
                return Estado.PENDIENTE;
            }
            case "RECHAZADO" -> {
                return Estado.PENDIENTE;
            }
            case "ENTREVISTA" -> {
                return Estado.PENDIENTE;
            }
            default -> throw new IllegalArgumentException("estado invalido");


        }

    }

}
