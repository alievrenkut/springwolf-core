// SPDX-License-Identifier: Apache-2.0
package io.github.stavshamir.springwolf.asyncapi.v3.jackson.model.channel.message;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.stavshamir.springwolf.asyncapi.v3.model.components.ComponentSchema;

import java.io.IOException;

public class ComponentSchemaSerializer extends JsonSerializer<ComponentSchema> {
    @Override
    public void serialize(ComponentSchema value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value.getReference() != null) {
            gen.writeObject(value.getReference());
        } else if (value.getSchema() != null) {
            gen.writeObject(value.getSchema());
        } else {
            gen.writeObject(value.getMultiFormatSchema());
        }
    }
}
