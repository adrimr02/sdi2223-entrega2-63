package com.uniovi.sdi2223entrega2test63.util;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.internal.MongoClientImpl;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.mindrot.jbcrypt.BCrypt;

public class MongoDB {
	private MongoClient mongoClient;
	private MongoDatabase mongodb;

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public void setMongodb(MongoDatabase mongodb) {
		this.mongodb = mongodb;
	}

	public MongoDatabase getMongodb() {
		return mongodb;
	}

	public void resetMongo() {
		try {
			setMongoClient(MongoClients.create("mongodb://127.0.0.1:27017"));
			setMongodb(getMongoClient().getDatabase("mywallapop"));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		deleteData();
		insertUsuarios();
		insertarOfertasYConversaciones();
	}

	private void deleteData() {
		getMongodb().getCollection("offers").drop();
		getMongodb().getCollection("users").drop();
		getMongodb().getCollection("conversations").drop();
		getMongodb().getCollection("messages").drop();
		getMongodb().getCollection("logs").drop();
	}

	private void insertUsuarios() {
		MongoCollection<Document> usuarios = getMongodb().getCollection("users");
		Document admin = new Document().append("nombre", "admin").append("apellidos", "admin")
				.append("email", "admin@email.com")
				.append("password", "ebd5359e500475700c6cc3dd4af89cfd0569aa31724a1bf10ed1e3019dcfdb11")
				.append("saldo", 100).append("userType", "admin");
		usuarios.insertOne(admin);

		int wantedUsers = 15;
		for (int i = 1; i <= wantedUsers; i++) {
			String prefix = i < 10 ? "0"+i : String.valueOf(i);
			Document user = new Document().append("name", "user").append("lastname", prefix)
					.append("email", "user"+prefix+"@email.com")
					.append("birthday", LocalDate.of(199810, 2, 19))
					.append("password", BCrypt.hashpw("user"+prefix, BCrypt.gensalt()))
					.append("wallet", 100).append("userType", "standard");
			usuarios.insertOne(user);
		}
	}

	private void insertarOfertasYConversaciones() {

	}

}
