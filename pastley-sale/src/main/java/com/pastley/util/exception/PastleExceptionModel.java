package com.pastley.util.exception;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project Pastley-Sale.
 * @author Sergio Stives Barrios Buitrago.
 * @Github https://github.com/SerBuitrago.
 * @contributors soleimygomez, leynerjoseoa, jhonatanbeltran.
 * @version 1.0.0.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PastleExceptionModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;
	private String exception;
	private String path;
	private int statu;
}
