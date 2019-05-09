package br.com.geovan.Ponto.to;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/***
 * 
 * @author geovan.goes Classe responsável por padronizar a comunicação
 * 
 */
public class ResultBaseFactoryTO implements Serializable
{
	/**
	 * Número usado para identificar a classe no processo de serialização
	 */
	private static final long serialVersionUID = -9025976238031174022L;

	/***
	 * Atributos estáticos usados como chaves para a conversão
	 */
	private static final String RESULT_BASE_FACTORY_ERROR_MESSAGES = "ResultBaseFactoryErrorMessages";
	private static final String RESULT_BASE_FACTORY_SUCCESS = "ResultBaseFactorySuccess";
	private static final String RESULT_BASE_FACTORY_RESULT = "ResultBaseFactoryResult";

	private static final String SERVICE_KEY_MESSAGE = "message";
	private static final String SERVICE_KEY_EXCEPTION = "exception";

	/***
	 * Mapa com os resultados
	 */
	private Map<String, Object> result = new HashMap<String, Object>();

	/**
	 * Lista com mensagens de erro que devem ser exibidas
	 * */
	private Map<String, String> errorMessages = new HashMap<String, String>();

	/***
	 * indicador de sucesso ou falha
	 */
	private boolean success;

	/** Construtor vazio... */
	public ResultBaseFactoryTO()
	{
	}

	/** Construtor privado para inicialização de um novo objeto de dentro dessa classe */
	private ResultBaseFactoryTO(Map<String, Object> result, Map<String, String> errorMessages, boolean success)
	{
		this.result = result;
		this.errorMessages = errorMessages;
		this.success = success;
	}

	/***
	 * @param result
	 */
	public void setSuccess(Map<String, Object> result)
	{
		this.result = result;
		success = true;
	}

	/***
	 * @return
	 */
	public boolean isSuccess()
	{
		return this.success;
	}

	/***
	 * @param errorMessages
	 * @param m
	 */
	public void addErrorMessages(Map<String, String> messages)
	{
		this.success = false;
		this.errorMessages.putAll(messages);
	}

	/***
	 * @param errorMessage
	 */
	public void addErrorMessage(String key, String errorMessage)
	{
		this.success = false;
		this.errorMessages.put(key, errorMessage);
	}

	/***
	 * @return
	 */
	public Map<String, Object> getResult()
	{
		return this.result;
	}

	/***
	 * @return
	 */
	public Map<String, String> getErrorMessages()
	{
		Map<String, String> newErrorMessages = new HashMap<String, String>();

		newErrorMessages.putAll(errorMessages);
		return newErrorMessages;
	}

	/***
	 * Método responsável por converter o ResultBaseFactory em um Map<String, Object> que pode ser ertornado pela camada Service
	 * 
	 * @return
	 */
	public Map<String, Object> toMap()
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RESULT_BASE_FACTORY_RESULT, this.result);
		result.put(RESULT_BASE_FACTORY_SUCCESS, this.success);
		result.put(RESULT_BASE_FACTORY_ERROR_MESSAGES, this.errorMessages);
		return result;
	}

	/***
	 * Método responsável por converter o Map<String, Object> obtido através da camada Service para um ResultBaseFactory
	 * 
	 * @param serviceResponse
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ResultBaseFactoryTO toResultBaseFactory(Map<String, Object> serviceResponse)
	{
		if (serviceResponse.containsKey(SERVICE_KEY_MESSAGE) || serviceResponse.containsKey(SERVICE_KEY_EXCEPTION))
		{
			Map<String, String> errorMessagesFromSource = new HashMap<String, String>();
			errorMessagesFromSource.put(SERVICE_KEY_EXCEPTION, (String) serviceResponse.get(SERVICE_KEY_EXCEPTION));
			errorMessagesFromSource.put(SERVICE_KEY_MESSAGE, (String) serviceResponse.get(SERVICE_KEY_MESSAGE));

			return new ResultBaseFactoryTO(new HashMap<String, Object>(), errorMessagesFromSource, false);
		}
		else
		{
			Map<String, Object> resultFromSource = (Map<String, Object>) serviceResponse.get(RESULT_BASE_FACTORY_RESULT);
			Map<String, String> errorMessagesFromSource = (Map<String, String>) serviceResponse.get(RESULT_BASE_FACTORY_ERROR_MESSAGES);
			boolean successFromSource = (Boolean) serviceResponse.get(RESULT_BASE_FACTORY_SUCCESS);

			ResultBaseFactoryTO resultBaseFactory = new ResultBaseFactoryTO(resultFromSource, errorMessagesFromSource, successFromSource);
			return resultBaseFactory;
		}
	}
}