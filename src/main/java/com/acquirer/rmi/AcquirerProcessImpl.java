package com.acquirer.rmi;

import com.model.AcquirerTransaction;
import com.model.AcquirerTransactionSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class AcquirerProcessImpl implements AcquirerProcessInterface, AcquirerTimeInterface, AcquirerLoadInterface, AcquirerLeaderInterface{
    // processes with priority
    // leader is process with highest priority

    boolean isLeader;
    private Integer requestCount = 0;

    private Integer serverId;

    private ReentrantLock mutex = new ReentrantLock();

    public AcquirerProcessImpl(Integer serverId, boolean leader) {
        this.serverId = serverId;
        this.isLeader = leader;
    }

    private int add(int a, int b) {
        return a + b;
    }


    @Override
    public Map<String, String> processMerchantTransaction(AcquirerTransaction acquirerTransaction) throws Exception {

        System.out.println("Request Received with Body " + acquirerTransaction);
        Map<String, String> map = new HashMap<>();
        requestCount++;
        String sql_get = "SELECT * from account WHERE card_number='" + acquirerTransaction.getCustomerCardNumber() + "'";

        ResultSet resultSet;
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/PaymentService", "root", "Khushi");
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
        System.out.println("***");
        Registry registry = LocateRegistry.getRegistry(2000);

        for(int i=1; i<=3; i++){
            if(i != serverId){
                try {
                    AcquirerProcessInterface server = (AcquirerProcessInterface) registry.lookup("acquirer" + i);
                    server.updateOtherServers(acquirerTransaction.getId(), serverId.toString());
                } catch (Exception e){
                    System.out.println("Error1");
                }
            }
        }
        System.out.println("****");
        Thread.sleep(2000);
        requestCount--;
        return map; //passed by value
    }
    public Map<String, String> getMerchantTransaction(String transactionId){
        return null;
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

    @Override
    public void setAsLeader() throws RemoteException {
        isLeader = true;
        System.out.println("Leader");
        Registry registry = LocateRegistry.getRegistry(2000);
        for (int i = 1; i <= 3; i++) {
            if(i != serverId) {
                try {
                    AcquirerLeaderInterface servers = (AcquirerLeaderInterface) registry.lookup("acquirer" + i);
                    servers.updateServersAboutNewLeader(serverId.toString());
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void unsetLeader() throws RemoteException {
        isLeader = false;
    }



    public void updateOtherServers(String transactionId, String server){
        System.out.println(transactionId+" received on "+ serverId+ " from "+server);
    }

    public void updateServersAboutNewLeader(String server){
        System.out.println("Server "+ server+ " is the new leader.");
    }

}
