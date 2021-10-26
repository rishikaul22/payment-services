package com.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class AcquirerTransactionDeserializer implements Deserializer<AcquirerTransaction> {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public AcquirerTransaction deserialize(String s, byte[] data) {
        AcquirerTransaction acquirerTransaction = null;
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");

            acquirerTransaction = objectMapper.readValue(data, AcquirerTransaction.class);
            return acquirerTransaction;
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }
}
