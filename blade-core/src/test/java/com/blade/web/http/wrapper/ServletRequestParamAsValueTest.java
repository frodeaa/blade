package com.blade.web.http.wrapper;

import com.blade.web.multipart.MultipartException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ServletRequestParamAsValueTest {

	private HttpServletRequest mockRequest;
	private ServletRequest servletRequest;

	private String input;
	private Integer expectedInt;
	private Long expectedLong;
	private Boolean expectedBool;

	public ServletRequestParamAsValueTest(String input, Integer expectedInt, Long expectedLong, Boolean expectedBool) {
		this.input = input;
		this.expectedInt = expectedInt;
		this.expectedLong = expectedLong;
		this.expectedBool = expectedBool;
	}

	@Parameterized.Parameters(name = "{index}: param=''{0}''")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"1", 1, 1L, false},
				{"1.5", null, null, false},
				{" ", null, null, false},
				{"0", 0, 0L, false},
				{"false", null, null, false},
				{"true", null, null, true},
		});
	}

	@Before
	public void setUp() throws MultipartException, IOException {
		mockRequest = mock(HttpServletRequest.class);
		when(mockRequest.getMethod()).thenReturn("GET");
		when(mockRequest.getRequestURI()).thenReturn("/path/" + input + "/");

		servletRequest = new ServletRequest(mockRequest);
		servletRequest.initPathParams("/path/:param");
	}

	@Test
	public void testParam() {
		assertEquals("param", input, servletRequest.param("param"));
	}

	@Test
	public void testParamAsInt() {
		assertEquals("paramAsInt", expectedInt, servletRequest.paramAsInt("param"));
	}

	@Test
	public void testParamAsLong() {
		assertEquals("paramAsLong", expectedLong, servletRequest.paramAsLong("param"));
	}

	@Test
	public void testParamAsBool() {
		assertEquals("paramAsBool", expectedBool, servletRequest.paramAsBool("param"));
	}

}
