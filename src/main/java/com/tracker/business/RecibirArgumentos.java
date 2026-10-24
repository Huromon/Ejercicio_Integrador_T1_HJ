package com.tracker.business;


import jdk.swing.interop.SwingInterOpUtils;

/** Clase para reducir la complejidad del main*/
 public class RecibirArgumentos {

     public static  void argumento(String[] arg){

        OfertaService OS = new OfertaService();
         OS.persistencia();





         try{

                 /**
                  * Placeholder, para despues, poner las llamadas a metodos
                  * */
                 if(arg.length == 1 && arg[0].equalsIgnoreCase("listar")){
                     System.out.println(OS.Listar());
                 }else{

                switch (arg[0].toLowerCase()) {

                    case "listar": {
                        System.out.println("filtrar");
                        /**
                         * Llamar al metodo de filtrar donde segun los demas parametros introducidos,
                         * realizar diferentes "metodos" de filtrado
                         * */
                        // de prueba
                        String[] h = {"añadir","--titulo","X","--empresa","Y","--estado","PENDIENTE","--fecha","13/10/2027"};
                        OS.añadir(h);
                        //----
                        System.out.println(OS.filtrar(arg));

                        break;
                    }
                    case "añadir": {

                            System.out.println("añadir");
                            System.out.println(OS.añadir(arg));



                        break;
                    }
                    case "eliminar": {
                        System.out.println("eliminar");
                        /**
                         * Llamar al metodo de eliminar para eliminar de la lista la candidatura
                         * por el ID indicado en el argumento
                         * */

                        System.out.println(OS.EliminarCandidatura(Integer.parseInt(arg[1])));
                        System.out.println(OS.Listar());
                        break;
                    }
                    case "ver": {
                        System.out.println(OS.VerCandidatura(Integer.parseInt(arg[1])));
                        break;
                    }
                 case "importar" :{
                     System.out.println("importar");
                     OS.Importar();



                        break;
                }
                    default: {

                        throw new IllegalArgumentException("no se ha detectado ningun metodo habilitado en el argmento");
                    }
                }
                }
         }catch (IllegalArgumentException err){
             System.out.println(err.getMessage());
         }catch (ArrayIndexOutOfBoundsException ar){
             System.out.println("no hay ningun argumento");
         }



    }

}
