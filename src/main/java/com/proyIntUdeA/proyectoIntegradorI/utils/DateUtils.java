package com.proyIntUdeA.proyectoIntegradorI.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    // Método para validar que una hora sea una hora antes a otra. Previamente
    // comparadas las fechas. Funciona para
    // horas entre 6am y 8pm
    public boolean isBusyOneHourBefore(String hourStudent, String periodStudent, String hourTuto, String periodTuto) {
        boolean isBusy = false;

        int numHourStudent = Integer.parseInt(hourStudent.substring(0, 2));
        int numHourTuto = Integer.parseInt(hourTuto.substring(0, 2));
        int finalnumHourTutor = numHourTuto == 1 ? 12 : numHourTuto - 1;
        if (hourStudent.equals("11:00") && periodStudent.equals("AM") && hourTuto.equals("12:00")
                && periodTuto.equals("PM")) {
            isBusy = true;
        } else if ((finalnumHourTutor) == numHourStudent) {
            isBusy = true;
        }

        return isBusy;
    }

    // Método para validar que una hora sea una hora después a otra. Previamente
    // comparadas las fechas. Funciona para
    // horas entre 6am y 8pm
    public boolean isBusyOneHourLater(String hourStudent, String periodStudent, String hourTuto, String periodTuto) {
        boolean isBusy = false;

        int numHourStudent = Integer.parseInt(hourStudent.substring(0, 2));
        int numHourTuto = Integer.parseInt(hourTuto.substring(0, 2));
        int finalnumHourTutor = numHourTuto == 12 ? 1 : numHourTuto + 1;
        if (hourStudent.equals("11:00") && periodStudent.equals("AM") && hourTuto.equals("12:00")
                && periodTuto.equals("PM")) {
            isBusy = true;
        } else if ((finalnumHourTutor) == numHourStudent) {
            isBusy = true;
        }

        return isBusy;
    }

    public String formatearfecha(Date fecha) {
        ZonedDateTime date = fecha.toInstant().atZone(ZoneId.of("UTC"));
        System.out.println("Fecha traida del Back en formato instant " + date);
        ZonedDateTime colombiaDateTime = date.withZoneSameInstant(ZoneId.of("America/Bogota"));
        System.out.println("Fecha traida del Back en formato colombiano " + colombiaDateTime);
        DateTimeFormatter formatter12h = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return colombiaDateTime.format(formatter12h);
    }

}
