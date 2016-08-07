package llosa.jopee.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExampleControllerIT {

	@LocalServerPort
    private int port;
	
	@Autowired
	private TestRestTemplate template;

	@Before
	public void setUp() throws Exception {
		// nothing to do
	}

	@Test
	public void getExample() throws Exception {
		ResponseEntity<String> response = template.getForEntity("/example", String.class);
		assertThat(response.getBody(), equalTo("this is an example"));
	}

}
