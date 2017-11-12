package fi.metatavu.soteapi.wordpress.events.model.deserialization;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FalseAsNullDeserializer <T> extends JsonDeserializer<T> {

  @Override
  public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContetx) throws IOException {
    JsonToken currentToken = jsonParser.getCurrentToken();
    
    if (currentToken.equals(JsonToken.VALUE_FALSE)) {
      return getNullValue(deserializationContetx);
    }
    
    if (currentToken.equals(JsonToken.VALUE_STRING)) {
      return (new ObjectMapper()).readValue(jsonParser.getText().trim(), getGenericTypeClass());
    } else if (currentToken.equals(JsonToken.VALUE_NULL)) {
      return getNullValue(deserializationContetx);
    }

    throw deserializationContetx.mappingException("Failed to map type");
  }

  @SuppressWarnings("unchecked")
  private Class<? extends T> getGenericTypeClass() {
    Type genericSuperclass = getClass().getGenericSuperclass();

    if (genericSuperclass instanceof ParameterizedType) {
      return (Class<? extends T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    }
    
    return null;
  }
}
