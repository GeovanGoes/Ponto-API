/**
 * 
 */
package br.com.geovan.Ponto;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.geovan.Ponto.to.ResultBaseFactoryTO;

/**
 * @author geovan.goes
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ResuultBaseFactoryTest
{
	
	@Test
	public void addResultIsOK()
	{
		ResultBaseFactoryTO resultBaseFactoryTO = new ResultBaseFactoryTO();
		resultBaseFactoryTO.setSuccess(new HashMap<String, Object>());
		
		Assertions.assertNotNull(resultBaseFactoryTO.getResult());
		Assertions.assertTrue(resultBaseFactoryTO.isSuccess());
	}
	
	@Test
	public void successFalse()
	{
		ResultBaseFactoryTO resultBaseFactoryTO = new ResultBaseFactoryTO();
		resultBaseFactoryTO.addErrorMessage("err", "err");
		
		Assertions.assertFalse(resultBaseFactoryTO.isSuccess());
		Assertions.assertTrue(resultBaseFactoryTO.getErrorMessages().containsKey("err"));
		Assertions.assertEquals(0, resultBaseFactoryTO.getResult().size());
	}
	
}
