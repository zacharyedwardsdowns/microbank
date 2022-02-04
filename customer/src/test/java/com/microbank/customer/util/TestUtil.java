package com.microbank.customer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

/** A testing utility class. */
public final class TestUtil {
  private static final Yaml yaml = new Yaml();

  /** Prevents instantiation. */
  private TestUtil() {}

  /**
   * Verifies the given response contains an error with the name of the given class.
   *
   * @param response The response containing the given class name.
   * @param clazz The class to check for in the response.
   * @throws JsonProcessingException Failure to map the response to JsonNode.
   */
  public static void verifyError(final String response, final Class<?> clazz)
      throws JsonProcessingException {
    Assertions.assertNotNull(response);
    final JsonNode result = Util.MAPPER.readTree(response);
    Assertions.assertEquals(result.get("error").asText(), clazz.getSimpleName());
  }

  /**
   * Loads yaml file as a MappingNode. (Does not support ---).
   *
   * @param filePath Path to the yaml file.
   * @return The yaml file as a MappingNode.
   * @throws IOException Upon failure to load the yaml file.
   */
  public static MappingNode getYamlProperties(@NonNull final String filePath) throws IOException {
    return (MappingNode)
        yaml.compose(new InputStreamReader(new ClassPathResource(filePath).getInputStream()));
  }

  /**
   * Get a yaml property from a MappingNode using a given key.
   *
   * @param node A MappingNode containing a yaml file.
   * @param key The key to search for.
   * @return The value of the given key or null.
   */
  public static String getYamlProperty(@NonNull final MappingNode node, @NonNull final String key) {
    return getYamlPropertyRecursive(node.getValue(), List.of(key.split("\\.")), 0);
  }

  /**
   * Get a yaml property from a list of NodeTuples and a list of Strings used to represent a nested
   * yaml key. Uses recursion to traverse the yaml structure.
   *
   * @param tuples The yaml structure to traverse.
   * @param keys Nested yaml key to search for.
   * @param index The current index of the nested key.
   * @return The value of the given nested key or null.
   */
  public static String getYamlPropertyRecursive(
      final List<NodeTuple> tuples, final List<String> keys, final int index) {
    if (index >= keys.size()) {
      return null;
    }

    final boolean lastKey = index == keys.size() - 1;

    for (final NodeTuple tuple : tuples) {
      final ScalarNode key = (ScalarNode) tuple.getKeyNode();
      if (keys.get(index).equals(key.getValue())) {
        if (lastKey) {
          final ScalarNode value = (ScalarNode) tuple.getValueNode();
          return value.getValue();
        } else {
          final MappingNode value = (MappingNode) tuple.getValueNode();
          return getYamlPropertyRecursive(value.getValue(), keys, index + 1);
        }
      }
    }

    return null;
  }
}
