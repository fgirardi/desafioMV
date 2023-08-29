package com.mouts.mvteste.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.mouts.mvteste.exception.BussinessException;

public class Util {

	public static LocalDate convertToLocalDate(String dateString) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dateString, formatter);

		return localDate;
	}
	
	public static void checkPeriodoInformado(LocalDate dataInicio, LocalDate dataFim) {
		
		if (!dataInicio.isEqual(dataFim) && dataInicio.isAfter(dataFim)) {
		    throw new BussinessException("A data de inicio deve ser maior ou igual que a data fim");
		}
	}

}
