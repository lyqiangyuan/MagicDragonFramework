package com.hqy.mdf.common.annotation.security.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hqy.mdf.common.annotation.security.Desensitization;
import com.hqy.mdf.common.enums.DesensitizationTypeEnum;
import com.hqy.mdf.common.util.DesensitizationUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * @author hqy
 */
@NoArgsConstructor
@AllArgsConstructor
public class DesensitizationSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private Integer preLength;

    private Integer sufLength;

    private char MarkChar;



    @Override
    public void serialize(final String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeString(DesensitizationUtils.desensitization(s, preLength, sufLength,MarkChar));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class);
                if (desensitization == null) {
                    desensitization = beanProperty.getContextAnnotation(Desensitization.class);
                }
                if (desensitization != null) {
                    DesensitizationTypeEnum type = desensitization.type();
                    if (DesensitizationTypeEnum.CUSTOMIZE.equals(type)) {
                        return new DesensitizationSerializer(desensitization.preLength(), desensitization.sufLength(), desensitization.markChar());
                    }
                    return new DesensitizationSerializer(type.getPreLength(), type.getSufLength(), type.getMarkChar());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
