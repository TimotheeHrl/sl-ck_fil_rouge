package com.filRouge.filRouge.controller.serialiser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.filRouge.filRouge.model.User;

import java.io.IOException;

public class UserSerializer  extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("username", user.getUsername());
        jsonGenerator.writeStringField("password", user.getPassword());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeStringField("role", user.getRoles().toString());
        jsonGenerator.writeEndObject();

    }
}
