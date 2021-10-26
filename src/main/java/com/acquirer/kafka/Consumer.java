package com.acquirer.kafka;

import com.model.AcquirerTransaction;
import com.model.AcquirerTransactionDeserializer;
import com.mysql.cj.jdbc.Driver;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) {
        String bootstrapServers="127.0.0.1:9092";
        String grp_id="kafka-consumer";
        String topic[]=new String[]{"transaction"};

        Properties properties=new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AcquirerTransactionDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        KafkaConsumer<String, AcquirerTransaction> consumer = new KafkaConsumer<String, AcquirerTransaction>(properties);

        consumer.subscribe(Arrays.asList(topic));

        while(true){
            ConsumerRecords<String, AcquirerTransaction> records=consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,AcquirerTransaction> record: records){
                System.out.println("Key: " + record.key());
                System.out.println("Topic: " + record.topic());
                System.out.println("Offset: " + record.offset());
                System.out.println(record.value());
                System.out.println("Partition: " + record.partition());
                System.out.println("Offset: " + record.offset());

                String SQL_SELECT = "INSERT  INTO  transaction " +
                        "(tid,customer_name,customer_card_number,merchant_name,merchant_account_id,amount)  VALUES  (?,?,?,?,?,?)";

                try {
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://127.0.0.1:3306/payment", "root", "root");
                    PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);
                    preparedStatement.setString(1, record.value().getId());
                    preparedStatement.setString(2, record.value().getCustomerName());
                    preparedStatement.setString(3, record.value().getCustomerCardNumber());
                    preparedStatement.setString(4, record.value().getMerchantName());
                    preparedStatement.setString(5, record.value().getMerchantAccountId());
                    preparedStatement.setLong(6, record.value().getAmount());
                    int rows = preparedStatement.executeUpdate();

                    conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/payment", "root", "root");
                    preparedStatement = conn.prepareStatement(SQL_SELECT);
                    preparedStatement.setString(1, record.value().getId());
                    preparedStatement.setString(2, record.value().getCustomerName());
                    preparedStatement.setString(3, record.value().getCustomerCardNumber());
                    preparedStatement.setString(4, record.value().getMerchantName());
                    preparedStatement.setString(5, record.value().getMerchantAccountId());
                    preparedStatement.setLong(6, record.value().getAmount());
                    rows = preparedStatement.executeUpdate();
//                    System.out.println(rows);

                } catch (SQLException e) {
                    System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
