package com.blade.web.http.wrapper;

import com.blade.web.multipart.MultipartException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ServletRequestQueryAsValueTest {

	private HttpServletRequest mockRequest;
	private ServletRequest servletRequest;

	private String[] input;
	private Integer expectedInt;
	private Long expectedLong;
	private Boolean expectedBool;
	private Float expectedFloat;
	private Double expectedDouble;

	public ServletRequestQueryAsValueTest(List<String> input, Integer expectedInt, Long expectedLong,
										  Boolean expectedBool, Float expectedFloat, Double expectedDouble) {
		if (input == null) {
			this.input = null;
		} else {
			this.input = input.toArray(new String[input.size()]);
		}
		this.expectedInt = expectedInt;
		this.expectedLong = expectedLong;
		this.expectedBool = expectedBool;
		this.expectedFloat = expectedFloat;
		this.expectedDouble = expectedDouble;
	}

	@Parameterized.Parameters(name = "{index} query={0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{Collections.singletonList("1"), 1, 1L, false, 1f, 1d},
				{Collections.singletonList("1.5"), null, null, false, 1.5f, 1.5d},
				{Collections.singletonList(" "), null, null, false, null, null},
				{Collections.singletonList(""), null, null, false, null, null},
				{Collections.singletonList(null), null, null, false, null, null},
				{Collections.singletonList("0"), 0, 0L, false, 0f, 0d},
				{Collections.singletonList("false"), null, null, false, null, null},
				{Collections.singletonList("true"), null, null, true, null, null},
				{Collections.emptyList(), null, null, false, null, null},
				{null, null, null, null, null, null},
		});
	}

	@Before
	public void setUp() throws MultipartException, IOException {
		mockRequest = mock(HttpServletRequest.class);
		when(mockRequest.getMethod()).thenReturn("GET");
		when(mockRequest.getParameterValues("query")).thenReturn(input);

		servletRequest = new ServletRequest(mockRequest);
	}

	@Test
	public void testQueryAs() {
		if (input == null) {
			assertEquals(input, servletRequest.query("query"));
		} else if (input.length > 0) {
			assertEquals("query", "" + input[0], servletRequest.query("query"));
		}
	}

	@Test
	public void testQueryAsInt() {
		assertEquals(expectedInt, servletRequest.queryAsInt("query"));
	}

	@Test
	public void testQueryAsLong() {
		assertEquals(expectedLong, servletRequest.queryAsLong("query"));
	}

	@Test
	public void testQueryAsBool() {
		assertEquals(expectedBool, servletRequest.queryAsBool("query"));
	}

	@Test
	public void testQueryAsFloat() {
		assertEquals(expectedFloat, servletRequest.queryAsFloat("query"));
	}

	@Test
	public void testQueryAsDouble() {
		assertEquals(expectedDouble, servletRequest.queryAsDouble("query"));
	}

}
