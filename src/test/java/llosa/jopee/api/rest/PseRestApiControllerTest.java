package llosa.jopee.api.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PseRestApiControllerTest {
	
	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new PseRestApiController()).build();
	}

	@Test
	public void testGetStockNameBySymbol() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api?symbol=AC").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json("{symbol: 'AC', name: 'Ayala Corporation'}"));
	}
	
	@Test
	public void testGetAllStockNamesAndSymbols() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/all-stock-names-and-symbols").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[16]symbol").value("URC"))
			.andExpect(jsonPath("$[16]name").value("Universal Robina Corporation"))
			.andExpect(jsonPath("$[3]symbol").value("BPI"))
			.andExpect(jsonPath("$[3]name").value("Bank of the Philippine Islands"))
			.andExpect(jsonPath("$[0]symbol").value("AC"))
			.andExpect(jsonPath("$[0]name").value("Ayala Corporation"))
			.andDo(print());
		
//			.andExpect(jsonPath("$..*").isArray())
//			.andExpect(jsonPath("$..*", "{symbol: 'URC', name: 'Universal Robina Corporation'}").exists())
//			.andExpect(jsonPath("$..*", "{symbol: 'SC', name: 'San Miguel Corporation'}").exists());
//			.andDo(print());
	}
	
	@Test
	public void testGetAllStockSymbols() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/all-stock-symbols").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(17)))
			.andExpect(jsonPath("$[1]symbol").value("AEV"))
			.andExpect(jsonPath("$[5]symbol").value("GLO"))
			.andExpect(jsonPath("$[15]symbol").value("TEL"))
			.andExpect(jsonPath("$[14]symbol").value("SMPH"))
			.andDo(print());
		
//		To test size of array: jsonPath("$", hasSize(4))
//		To count members of object: jsonPath("$.*", hasSize(4))
	}
	
	@Test
	public void testGetStartDate() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/startDate/2005-01-02").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("date").value("2005-01-02"));
//			.andDo(print());
	}
	
	@Test
	public void testGetStartDateThatDoesNotExist() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/startDate/2004-01-02").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("date").value("1970-01-01"))
			.andExpect(jsonPath("symbol").value(""))
			.andExpect(jsonPath("close").value(0.00))
			.andExpect(jsonPath("volume").value(0.00));
//			.andDo(print());
	}
	
	@Test
	public void testGetClosingPrice() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/closingPrice/2016-06-29/MBT").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("date").value("2016-06-29"))
			.andExpect(jsonPath("close").value(90.35));
//			.andDo(print());
	}
	
	@Test
	public void testGetChartData() throws Exception { //TODO: check  if json array is received
		mvc.perform(MockMvcRequestBuilders.post("/api/chart-data").contentType(MediaType.APPLICATION_JSON)
			.content("{\"startDate\": \"2005-01-12\", \"yearsHeld\": 1, \"stockSymbols\": [\"TEL\",\"SMC\"]}=")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(494)))
			.andExpect(jsonPath("$[0]symbol").value("TEL"))
			.andExpect(jsonPath("$[493]symbol").value("SMC"))
//			.andDo(print())
			;
	}
}
