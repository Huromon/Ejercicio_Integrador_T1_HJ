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
        String e="";
       for (Oferta o : lista.getListado()){
           if (o!=null){
               e+= o.toString()+"\n";
           }
       }
       if(e.equals("")){e="no hay ninguna candidatura";}
       return e;
    }
    public String Listar(LinkedList<Oferta> filtrado){
        String e="";
        for (Oferta o : filtrado){
            if (o!=null){
                e+= o.toString()+"\n";
            }
        }
        if(e.equals("")){e="no hay ninguna candidatura";}
        return e;
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
                        return "candidatura añadida con exito";
                    }catch (IllegalArgumentException e){
                        return e.getMessage();
                    }

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
        try {
            IllegalArgumentException aa = new IllegalArgumentException("parametros invalidos");
            NullPointerException ll = new NullPointerException("no hay coincidencias");
            if (arg.length<3){
                throw aa;
            }
            if (arg[1].equalsIgnoreCase("--empresa")) {
                if (arg.length == 3) {

                      return  Listar(filtrarEmpresa(arg[2]));


                } else {
                    if (arg[3].equalsIgnoreCase("--estado")) {

                        return  Listar(filtrarEstado(arg[4],filtrarEmpresa(arg[2])));


                    } else {
                        throw aa;
                    }

                }

            } else if (arg[1].equalsIgnoreCase("--estado")) {

                    return  Listar(filtrarEstado(arg[2]));


            }
        }catch (IllegalArgumentException e){
            return e.getMessage();
        }


        return "";
    }

    @Override
    public LinkedList<Oferta> filtrarEmpresa(String empresa) {
        LinkedList<Oferta> filtradas = new LinkedList<>();
        for(Oferta o : lista.getListado()){
            if((o.empresa()).equalsIgnoreCase(empresa)){
                filtradas.add(o);
            }
        }
        return filtradas;
    }
/**
 *
 * AL FILTRAR SE TIENE QUE PONER LA EMPRESA ANTES QUE EL ESTADO SI SE FILTRA CON LOS DOS PARAMETROS
 *
 * */
    @Override
    public LinkedList<Oferta> filtrarEstado(String estado) {
        LinkedList<Oferta> filtradas = new LinkedList<>();
        for(Oferta o : lista.getListado()){
            if(o.estado().equals(comprobar(estado))){
                filtradas.add(o);
            }
        }
        return filtradas;
    }

    @Override
    public LinkedList<Oferta> filtrarEstado(String estado, LinkedList<Oferta> nn) {
        LinkedList<Oferta> filtradas = new LinkedList<>();
        for(Oferta o : nn){
            if(o.estado().equals(comprobar(estado))){
                filtradas.add(o);
            }
        }
        return filtradas;
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
