package com.acquirer.rmi;

import com.model.AcquirerTransaction;
import com.model.AcquirerTransactionSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class AcquirerProcessImpl implements AcquirerProcessInterface, AcquirerTimeInterface, AcquirerLoadInterface {

    private Integer requestCount = 0;

    private ReentrantLock mutex = new ReentrantLock();

    private int add(int a, int b) {
        return a + b;
    }

    // User ( id, cardNo, customerName, balance)

    @Override
    public Map<String, String> processMerchantTransaction(AcquirerTransaction acquirerTransaction) throws Exception {

        System.out.println("Request Received with Body " + acquirerTransaction);
        Map<String, String> map = new HashMap<>();
        requestCount++;
        String sql_get = "SELECT * from account WHERE card_number='" + acquirerTransaction.getCustomerCardNumber() + "'";

        ResultSet resultSet;
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/payment", "root", "root");
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(sql_get);
            while(resultSet.next()) {
                mutex.lock();
                long balance =  resultSet.getLong("balance");
                if(acquirerTransaction.getAmount() <= balance) {
                    balance -= acquirerTransaction.getAmount();
                    Statement upstatement = conn.createStatement();
                    String sql_update = "UPDATE account SET balance = " + balance + " WHERE card_number='" + acquirerTransaction.getCustomerCardNumber() + "'";
                    upstatement.executeUpdate(sql_update);
                    map.put("status", "approved");
                }
                else {
                    map.put("status", "failed");
                }
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("id", acquirerTransaction.getId());

        map.put("customerName", acquirerTransaction.getCustomerName());
        map.put("merchantName", acquirerTransaction.getMerchantName());
        mutex.unlock();
        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AcquirerTransactionSerializer.class.getName());
        KafkaProducer kafkaProducer = new KafkaProducer<String, AcquirerTransaction>(properties);
        ProducerRecord<String, AcquirerTransaction> producerRecord = new ProducerRecord<>("transaction",acquirerTransaction);
        kafkaProducer.send(producerRecord);
        Thread.sleep(2000);
        requestCount--;
        return map; //passed by value
    }
    @Override
    public long getSystemTime(){
        long time = Instant.now().toEpochMilli();
        System.out.println("This is the current time on the server, "+ time);
        return time;
    }
    public int getRequestCount(){
        return requestCount;
    }
}
