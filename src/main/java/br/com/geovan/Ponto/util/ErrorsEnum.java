package br.com.geovan.Ponto.util;

public enum ErrorsEnum {
	DATA_INVALIDA(-1, "Data inválida"),
	USUARIO_NAO_INDENTIFICADO(-2, "Usuário não indenficado"),
	LANCAMENTO_DUPLICADO(-3, "Lancamento duplicado");
	
	private int code;
	private String message;

	private ErrorsEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	

}
