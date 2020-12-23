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

  private static final String VERIFY_CUSTOMER_EXISTS_ENDPOINT = "/verify/customer/exists";
  private static final String REGISTER_ENDPOINT = "/customer";
  private static final String BAD_JSON = "{\"Bad\":\"Json\"}";
  private static String CUSTOMER_INFO_ENDPOINT = "/customer/";
  private static CustomerService mockCustomerService;
  private static Customer customer;
  private static MockMvc mockMvc;
  private static String json;

  @BeforeClass
  public static void setupClass() throws Exception {
    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
    customer = Util.MAPPER.readValue(json, Customer.class);

    mockCustomerService = Mockito.mock(CustomerService.class);
    final CustomerController customerController = new CustomerController(mockCustomerService);

    mockMvc =
        MockMvcBuilders.standaloneSetup(customerController)
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();

    CUSTOMER_INFO_ENDPOINT += customer.getUsername();
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
  public void testVerifyCustomerExists() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(VERIFY_CUSTOMER_EXISTS_ENDPOINT)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void testVerifyCustomerExistsResourceNotFoundException() throws Exception {
    Mockito.doThrow(ResourceNotFoundException.class)
        .when(mockCustomerService)
        .verifyCustomerExists(customer);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(VERIFY_CUSTOMER_EXISTS_ENDPOINT)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, ResourceNotFoundException.class);
  }

  @Test
  public void testVerifyCustomerExistsMissingRequirementsException() throws Exception {
    Mockito.doThrow(MissingRequirementsException.class)
        .when(mockCustomerService)
        .verifyCustomerExists(customer);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(VERIFY_CUSTOMER_EXISTS_ENDPOINT)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, MissingRequirementsException.class);
  }

  @Test
  public void testVerifyCustomerExistsInvalidJsonException() throws Exception {
    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(VERIFY_CUSTOMER_EXISTS_ENDPOINT)
                    .content(BAD_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    verifyError(response, InvalidJsonException.class);
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
    final JsonNode result = Util.MAPPER.readTree(response);
    Assert.assertEquals(result.get("error").asText(), clazz.getSimpleName());
  }
}
