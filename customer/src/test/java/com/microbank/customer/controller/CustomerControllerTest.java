package com.microbank.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.InvalidJsonException;
import com.microbank.customer.controller.advice.ControllerExceptionHandler;
import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.MissingRequirementsException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.service.CustomerService;
import com.microbank.customer.util.Util;
import java.io.File;
import java.nio.file.Files;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CustomerControllerTest {

  private static String VERIFY_PASSWORD_MATCHES_ENDPOINT = "/password/match";
  private static final String REGISTER_ENDPOINT = "/customer";
  private static final String BAD_JSON = "{\"Bad\":\"Json\"}";
  private static String CUSTOMER_INFO_ENDPOINT = "/customer/";
  private CustomerService mockCustomerService;
  private static Customer customer;
  private static String json;
  private MockMvc mockMvc;

  @BeforeClass
  public static void setupClass() throws Exception {
    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
    customer = Util.MAPPER.readValue(json, Customer.class);

    CUSTOMER_INFO_ENDPOINT += customer.getUsername();
    VERIFY_PASSWORD_MATCHES_ENDPOINT = CUSTOMER_INFO_ENDPOINT + VERIFY_PASSWORD_MATCHES_ENDPOINT;
  }

  @Before
  public void setup() {
    mockCustomerService = Mockito.mock(CustomerService.class);
    mockMvc =
        MockMvcBuilders.standaloneSetup(new CustomerController(mockCustomerService))
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
  }

  @Test
  public void testRegister() throws Exception {
    Mockito.when(mockCustomerService.register(customer)).thenReturn(customer);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(REGISTER_ENDPOINT)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Customer result = Util.MAPPER.readValue(response, Customer.class);
    Assert.assertEquals(customer, result);
  }

  @Test
  public void testRegisterInvalidJsonException() throws Exception {
    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(REGISTER_ENDPOINT)
                    .content(BAD_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, InvalidJsonException.class);
  }

  @Test
  public void testRegisterExistingCustomerException() throws Exception {
    Mockito.when(mockCustomerService.register(customer)).thenThrow(ExistingCustomerException.class);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(REGISTER_ENDPOINT)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, ExistingCustomerException.class);
  }

  @Test
  public void testGetCustomerByUsername() throws Exception {
    Mockito.when(mockCustomerService.getCustomerByUsername(customer.getUsername()))
        .thenReturn(customer);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(CUSTOMER_INFO_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Customer result = Util.MAPPER.readValue(response, Customer.class);
    Assert.assertEquals(customer, result);
  }

  @Test
  public void testGetCustomerByUsernameResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerService.getCustomerByUsername(customer.getUsername()))
        .thenThrow(ResourceNotFoundException.class);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(CUSTOMER_INFO_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, ResourceNotFoundException.class);
  }

  @Test
  public void testDeleteCustomerByUsername() throws Exception {
    Mockito.when(mockCustomerService.deleteCustomerByUsername(customer.getUsername()))
        .thenReturn(customer);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.delete(CUSTOMER_INFO_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Customer result = Util.MAPPER.readValue(response, Customer.class);
    Assert.assertEquals(customer, result);
  }

  @Test
  public void testDeleteCustomerByUsernameResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerService.deleteCustomerByUsername(customer.getUsername()))
        .thenThrow(ResourceNotFoundException.class);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.delete(CUSTOMER_INFO_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, ResourceNotFoundException.class);
  }

  @Test
  public void verifyPasswordMatches() throws Exception {
    Mockito.when(
            mockCustomerService.verifyPasswordMatches(
                customer.getUsername(), customer.getPassword()))
        .thenReturn(true);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(VERIFY_PASSWORD_MATCHES_ENDPOINT)
                .header("password", customer.getPassword()))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void verifyPasswordMatchesFalse() throws Exception {
    Mockito.when(
            mockCustomerService.verifyPasswordMatches(
                customer.getUsername(), customer.getPassword()))
        .thenReturn(false);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(VERIFY_PASSWORD_MATCHES_ENDPOINT)
                .header("password", customer.getPassword()))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  public void verifyPasswordMatchesResourceNotFoundException() throws Exception {
    Mockito.when(
            mockCustomerService.verifyPasswordMatches(
                customer.getUsername(), customer.getPassword()))
        .thenThrow(ResourceNotFoundException.class);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(VERIFY_PASSWORD_MATCHES_ENDPOINT)
                    .header("password", customer.getPassword()))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, ResourceNotFoundException.class);
  }

  @Test
  public void verifyPasswordMatchesMissingRequirementsException() throws Exception {
    Mockito.when(
            mockCustomerService.verifyPasswordMatches(
                customer.getUsername(), customer.getPassword()))
        .thenThrow(MissingRequirementsException.class);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(VERIFY_PASSWORD_MATCHES_ENDPOINT)
                    .header("password", customer.getPassword()))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, MissingRequirementsException.class);
  }

  /**
   * Verifies the given response contains an error with the name of the given class.
   *
   * @param response The response containing the given class name.
   * @param clazz The class to check for in the response.
   * @throws JsonProcessingException Failure to map the response to JsonNode.
   */
  private void verifyError(final String response, final Class<?> clazz)
      throws JsonProcessingException {
    Assert.assertNotNull(response);
    final JsonNode result = Util.MAPPER.readTree(response);
    Assert.assertEquals(result.get("error").asText(), clazz.getSimpleName());
  }
}
