package com.microbank.customer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.InvalidJsonException;
import com.microbank.customer.controller.advice.ControllerExceptionHandler;
import com.microbank.customer.exception.ExistingCustomerException;
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

  private static CustomerService mockCustomerService;
  private static MockMvc mockMvc;
  private static String json;
  private static Customer customer;

  @BeforeClass
  public static void setupClass() throws Exception {
    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
    json = json.replace(" ", "");
    customer = Util.MAPPER.readValue(json, Customer.class);

    mockCustomerService = Mockito.mock(CustomerService.class);
    final CustomerController customerController = new CustomerController(mockCustomerService);

    mockMvc =
        MockMvcBuilders.standaloneSetup(customerController)
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
  }

  @Test
  public void testRegister() throws Exception {
    Mockito.when(mockCustomerService.register(customer)).thenReturn(customer);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/register")
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
    final String content = "{\"Fake\":\"Json\"}";

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/register")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final JsonNode result = Util.MAPPER.readTree(response);
    Assert.assertEquals(result.get("error").asText(), InvalidJsonException.class.getSimpleName());
  }

  @Test
  public void testRegisterExistingCustomerException() throws Exception {
    Mockito.when(mockCustomerService.register(customer)).thenThrow(ExistingCustomerException.class);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/register")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final JsonNode result = Util.MAPPER.readTree(response);
    Assert.assertEquals(
        result.get("error").asText(), ExistingCustomerException.class.getSimpleName());
  }

  @Test
  public void testGetCustomerByUsername() throws Exception {
    Mockito.when(mockCustomerService.getCustomerByUsername(customer.getUsername()))
        .thenReturn(customer);

    final String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/customer/" + customer.getUsername())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Customer result = Util.MAPPER.readValue(response, Customer.class);
    Assert.assertEquals(customer, result);
  }
}
