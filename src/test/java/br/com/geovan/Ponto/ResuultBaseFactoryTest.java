/**
 * 
 */
package br.com.geovan.Ponto;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.geovan.Ponto.to.ResultBaseFactoryTO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author geovan.goes
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResuultBaseFactoryTest
{
	
	@Test
	public void addResultIsOK()
	{
		ResultBaseFactoryTO resultBaseFactoryTO = new ResultBaseFactoryTO();
		resultBaseFactoryTO.setSuccess(new HashMap<String, Object>());
		
		assertNotNull(resultBaseFactoryTO.getResult());
		assertTrue(resultBaseFactoryTO.isSuccess());
	}
	
	@Test
	public void successFalse()
	{
		ResultBaseFactoryTO resultBaseFactoryTO = new ResultBaseFactoryTO();
		resultBaseFactoryTO.addErrorMessage("err", "err");
		
		assertFalse(resultBaseFactoryTO.isSuccess());
		assertTrue(resultBaseFactoryTO.getErrorMessages().containsKey("err"));
		assertEquals(0, resultBaseFactoryTO.getResult().size());
	}
	
}
