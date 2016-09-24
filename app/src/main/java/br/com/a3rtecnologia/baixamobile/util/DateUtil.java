package br.com.a3rtecnologia.baixamobile.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by maclemon on 03/09/16.
 */
public class DateUtil {


    public static String getDataAtual(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String dataAtual = sdf.format(Calendar.getInstance().getTime());

        return dataAtual;
    }


}
