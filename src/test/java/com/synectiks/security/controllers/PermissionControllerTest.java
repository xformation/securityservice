package com.synectiks.security.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synectiks.security.entities.Permission;
import com.synectiks.security.repositories.PermissionRepository;
import com.synectiks.security.security.AuthoritiesConstants;
import com.synectiks.security.web.rest.AccountResource;
import com.synectiks.security.web.rest.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = PermissionController.class, secure = false)
@ContextConfiguration(locations =  {"classpath*:/application-context.xml"})
class PermissionControllerTest {

	@MockBean
	private PermissionRepository permissionRepository;

	private MockMvc mockMvc;

	Permission permission1;
	Permission permission2;

	private ObjectMapper mapper;

	@BeforeEach
	public void setup() {
		AccountResource accountUserMockResource = new AccountResource();
		this.mockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource)
//     .setControllerAdvice(exceptionTranslator)
				.build();
		permission1 = new Permission();
		permission1.setId("101");
		permission1.setName("Edtior Permission");
		permission1.setPermission("Editor_permission");
		permission2=new Permission();
		permission2.setId("102");
		permission2.setName("Manager Permission");
		permission2.setPermission("Manager_permission");
		mapper = new ObjectMapper();
	}

	@Test
	@WithMockUser(username = "test", roles = "ADMIN")
	public void testCreate() throws IOException, Exception {
		when(permissionRepository.save(Mockito.any(Permission.class))).thenReturn(permission1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/security/permissions/create")
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(permission1))
                .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder)
            		.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.login").value("test"))
                    .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN))
            		.andReturn();
            MockHttpServletResponse response = result.getResponse();
            String outputInJson = response.getContentAsString();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	@WithMockUser(username = "test", roles = "ADMIN")
	public void listAllTest() throws IOException, Exception {
		when(permissionRepository.findAll()).thenReturn(Stream.of(permission1,permission2).collect(Collectors.toList()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/security/permissions/listAll")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder)
            		.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.login").value("test"))
                    .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN))
            		.andReturn();
            MockHttpServletResponse response = result.getResponse();
            String outputInJson = response.getContentAsString();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	@WithMockUser(username = "test", roles = "ADMIN")
	public void findByIdTest() throws IOException, Exception {
		when(permissionRepository.findById(Mockito.any())).thenReturn(Optional.of(permission1));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/security/permissions/101")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder)
            		.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.login").value("test"))
                    .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN))
            		.andReturn();
            MockHttpServletResponse response = result.getResponse();
            String outputInJson = response.getContentAsString();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	@WithMockUser(username = "test", roles = "ADMIN")
	public void deleteByIdTest() throws IOException, Exception {
//		doNothing().when(permissionRepository.deleteById(Mockito.anyString()));
//		when(permissionRepository.findById(Mockito.any())).thenReturn(Mockito.any(VOID));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/security/permissions/delete/101")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder)
            		.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.login").value("test"))
                    .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN))
            		.andReturn();
            MockHttpServletResponse response = result.getResponse();
            String outputInJson = response.getContentAsString();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	@WithMockUser(username = "test", roles = "ADMIN")
	public void updateTest() throws IOException, Exception {
		when(permissionRepository.findById(Mockito.any())).thenReturn(Optional.of(permission1));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/security/permissions/update")
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(permission1))
                .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder)
            		.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.login").value("test"))
                    .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN))
            		.andReturn();
            MockHttpServletResponse response = result.getResponse();
            String outputInJson = response.getContentAsString();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	@WithMockUser(username = "test", roles = "ADMIN")
	public void deleteTest() throws IOException, Exception {
		when(permissionRepository.findById(Mockito.any())).thenReturn(Optional.of(permission1));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/security/permissions/delete")
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(permission1))
                .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder)
            		.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.login").value("test"))
                    .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN))
            		.andReturn();
            MockHttpServletResponse response = result.getResponse();
            String outputInJson = response.getContentAsString();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
}
