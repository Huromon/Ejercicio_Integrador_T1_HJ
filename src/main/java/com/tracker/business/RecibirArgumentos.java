package com.tracker.business;

import com.tracker.business.OfertaService;
import com.tracker.data.entity.Estado;
import com.tracker.data.entity.Oferta;

import javax.swing.text.html.parser.Parser;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Clase para reducir la complejidad del main*/
 public class RecibirArgumentos {
     public static  void argumento(String[] arg){
        //de prueba
         List<Oferta> candi= new ArrayList<Oferta>();
         Oferta p = new Oferta(1,"tt","eee", Estado.PENDIENTE, LocalDate.now(),"no","lolol",23.0);
         candi.add(p);
         //-

         try{
                 /**
                  * Placeholder, para despues, poner las llamadas a metodos
                  * */
                switch (arg[0].toLowerCase()) {
                    case "--listar": {
                        System.out.println("listar");
                        /** 
                         * Llamar al metodo de listar Para que muestre la "lista" del gestor
                         * */
                        break;
                    }
                    case "--filtrar": {
                        System.out.println("filtrar");
                        /**
                         * Llamar al metodo de filtrar donde segun los demas parametros introducidos,
                         * realizar diferentes "metodos" de filtrado
                         * */
                        break;
                    }
                    case "--añadir": {
                        System.out.println("añadir");
                        /** 
                         * Llamar al metodo de añadir para que se añada una candidatura a la "lista del gestor"
                         * con los parametros indicados en el argumento
                         * */
                        break;
                    }
                    case "--eliminar": {
                        System.out.println("eliminar");
                        /** 
                         * Llamar al metodo de eliminar para eliminar de la lista la candidatura
                         * por el ID indicado en el argumento
                         * */
                        break;
                    }
                    case "--ver":{
                        System.out.println(OfertaService.VerCandidatura(Integer.parseInt(arg[1]),candi));
                        break;
                    }
                    default: {
                        
                        throw new IllegalArgumentException("no se ha detectado ningun metodo habilitado en el argmento");
                    }
                }
         }catch (IllegalArgumentException err){
             System.out.println(err.getMessage());
         }catch (ArrayIndexOutOfBoundsException ar){
             System.out.println("no hay ningun argumento");
         }



    }

}
