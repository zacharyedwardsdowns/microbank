package com.microbank.customer.controller;

import com.jayway.jsonpath.InvalidJsonException;
import com.microbank.customer.controller.advice.ControllerExceptionHandler;
import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.MissingRequirementsException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.security.model.Tokens;
import com.microbank.customer.service.CustomerService;
import com.microbank.customer.util.TestUtil;
import com.microbank.customer.util.Util;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.yaml.snakeyaml.nodes.MappingNode;

class CustomerControllerTest {
  private static final Tokens TOKENS = new Tokens("ID", "access", "refresh");
  private static final String BAD_JSON = "{\"Bad\":\"Json\"}";
  private CustomerService mockCustomerService;
  private static String authorizePlaceholder;
  private static String authorizeUri;
  private static String customerUri;
  private static String requestBase;
  private static Customer customer;
  private static String json;
  private MockMvc mockMvc;

  @BeforeAll
  static void setupClass() throws Exception {
    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
    customer = Util.MAPPER.readValue(json, Customer.class);

    final MappingNode properties = TestUtil.getYamlProperties("application.yml");
    authorizePlaceholder = TestUtil.getYamlProperty(properties, "customer.request.authorize");
    requestBase = TestUtil.getYamlProperty(properties, "customer.request.base");
    customerUri = requestBase + "/" + customer.getCustomerId();
    authorizeUri = requestBase + authorizePlaceholder;
  }

  @BeforeEach
  void setup() {
    mockCustomerService = Mockito.mock(CustomerService.class);
    mockMvc =
        MockMvcBuilders.standaloneSetup(new CustomerController(mockCustomerService))
            .addPlaceholderValue("customer.request.authorize", authorizePlaceholder)
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
  }

  @Test
  void testRegister() throws Exception {
    Mockito.when(mockCustomerService.register(customer)).thenReturn(customer);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(requestBase)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Customer result = Util.MAPPER.readValue(response, Customer.class);
    Assertions.assertEquals(customer, result);
  }

  @Test
  void testRegisterInvalidJsonException() throws Exception {
    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(requestBase)
                    .content(BAD_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    TestUtil.verifyError(response, InvalidJsonException.class);
  }

  @Test
  void testRegisterExistingCustomerException() throws Exception {
    Mockito.when(mockCustomerService.register(customer)).thenThrow(ExistingCustomerException.class);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(requestBase)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn()
            .getResponse()
            .getContentAsString();

    TestUtil.verifyError(response, ExistingCustomerException.class);
  }

  @Test
  void testGetCustomerByCustomerId() throws Exception {
    Mockito.when(mockCustomerService.getCustomerByCustomerId(customer.getCustomerId()))
        .thenReturn(customer);

    final String response =
        mockMvc
            .perform(MockMvcRequestBuilders.get(customerUri).accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Customer result = Util.MAPPER.readValue(response, Customer.class);
    Assertions.assertEquals(customer, result);
  }

  @Test
  void testGetCustomerByCustomerIdResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerService.getCustomerByCustomerId(customer.getCustomerId()))
        .thenThrow(ResourceNotFoundException.class);

    final String response =
        mockMvc
            .perform(MockMvcRequestBuilders.get(customerUri).accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    TestUtil.verifyError(response, ResourceNotFoundException.class);
  }

  @Test
  void testDeleteCustomerByCustomerId() throws Exception {
    Mockito.when(mockCustomerService.deleteCustomerByCustomerId(customer.getCustomerId()))
        .thenReturn(customer);

    final String response =
        mockMvc
            .perform(MockMvcRequestBuilders.delete(customerUri).accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Customer result = Util.MAPPER.readValue(response, Customer.class);
    Assertions.assertEquals(customer, result);
  }

  @Test
  void testDeleteCustomerByCustomerIdResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerService.deleteCustomerByCustomerId(customer.getCustomerId()))
        .thenThrow(ResourceNotFoundException.class);

    final String response =
        mockMvc
            .perform(MockMvcRequestBuilders.delete(customerUri).accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    TestUtil.verifyError(response, ResourceNotFoundException.class);
  }

  @Test
  void authorize() throws Exception {
    Mockito.when(
            mockCustomerService.verifyPasswordMatches(
                customer.getUsername(), customer.getPassword()))
        .thenReturn(TOKENS);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(authorizeUri)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Tokens result = Util.MAPPER.readValue(response, Tokens.class);
    Assertions.assertEquals(TOKENS, result);
  }

  @Test
  void authorizeVerifyPasswordMatchesFalse() throws Exception {
    Mockito.when(
            mockCustomerService.verifyPasswordMatches(
                customer.getUsername(), customer.getPassword()))
        .thenReturn(null);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(authorizeUri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void authorizeTokensNull() throws Exception {
    Mockito.when(
            mockCustomerService.verifyPasswordMatches(
                customer.getUsername(), customer.getPassword()))
        .thenReturn(null);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(authorizeUri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void authorizeMissingRequirementsException() throws Exception {
    Mockito.when(
            mockCustomerService.verifyPasswordMatches(
                customer.getUsername(), customer.getPassword()))
        .thenThrow(MissingRequirementsException.class);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(authorizeUri)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    TestUtil.verifyError(response, MissingRequirementsException.class);
  }
}
