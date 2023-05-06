package com.uniovi.sdi2223entrega2test63.util;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
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
		insertarOfertas();
		insertarConversaciones();
	}

	private void deleteData() {
		getMongodb().getCollection("offers").drop();
		getMongodb().getCollection("users").drop();
		getMongodb().getCollection("conversations").drop();
		getMongodb().getCollection("messages").drop();
		getMongodb().getCollection("logs").drop();
	}

	private void insertUsuarios() {
		MongoCollection<Document> users = getMongodb().getCollection("users");
		Document admin = new Document().append("nombre", "admin").append("apellidos", "admin")
				.append("email", "admin@email.com")
				.append("password", BCrypt.hashpw("admin", BCrypt.gensalt()))
				.append("saldo", 100).append("userType", "admin");
		users.insertOne(admin);

		int wantedUsers = 15;
		for (int i = 1; i <= wantedUsers; i++) {
			String prefix = i < 10 ? "0"+i : String.valueOf(i);
			Document user = new Document().append("name", "user").append("lastname", prefix)
					.append("email", "user"+prefix+"@email.com")
					.append("birthday", LocalDate.of(199810, 2, 19))
					.append("password", BCrypt.hashpw("user"+prefix, BCrypt.gensalt()))
					.append("wallet", 100).append("userType", "standard");
			users.insertOne(user);
		}
	}

	private void insertarOfertas() {
		MongoCollection<Document> offers = getMongodb().getCollection("offers");
		Document offer1 = new Document()
				.append("title", "Silla")
				.append("description", "Silla fabricada con madera de castaño")
				.append("price", 150)
				.append("date", LocalDate.now())
				.append("seller", "user02@email.com")
				.append("available", true)
				.append("featured", true);
		offers.insertOne(offer1);

		Document offer2 = new Document()
				.append("title", "Mesa")
				.append("description", "Mesa redonda fabricada con madera de roble")
				.append("price", 350)
				.append("date", LocalDate.now())
				.append("seller", "user01@email.com")
				.append("available", false)
				.append("featured", false);
		offers.insertOne(offer2);

		Document offer3 = new Document()
				.append("title","Sofá")
				.append("description", "Sofá de cuero negro")
				.append("price", 500)
				.append("date", LocalDate.now())
				.append("seller", "user03@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer3);

		Document offer4 = new Document()
				.append("title","Lámpara")
				.append("description", "Lámpara de pie moderna")
				.append("price", 100)
				.append("date", LocalDate.now())
				.append("seller", "user04@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer4);

		Document offer5 = new Document()
				.append("title","Alfombra")
				.append("description", "Alfombra de lana tejida a mano")
				.append("price", 200)
				.append("date", LocalDate.now())
				.append("seller", "user04@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer5);

		Document offer6 = new Document()
				.append("title","Mesa grande")
				.append("description", "Mesa de comedor extensible de roble")
				.append("price", 800)
				.append("date", LocalDate.now())
				.append("seller", "user05@email.com")
				.append("available", false)
				.append("featured", false);
		offers.insertOne(offer6);

		Document offer7 = new Document()
				.append("title","Cuadro")
				.append("description", "Cuadro abstracto pintado a mano")
				.append("price", 300)
				.append("date", LocalDate.now())
				.append("seller", "user04@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer7);

		Document offer8 = new Document()
				.append("title","Sillón")
				.append("description", "Sillón orejero tapizado en tela de lino")
				.append("price", 400)
				.append("date", LocalDate.now())
				.append("seller", "user01@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer8);

		Document offer9 = new Document()
				.append("title","Juego de café")
				.append("description", "Juego de café de porcelana china")
				.append("price", 50)
				.append("date", LocalDate.now())
				.append("seller", "user03@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer9);

		Document offer10 = new Document()
				.append("title","Cama grande")
				.append("description", "Cama de matrimonio con cabecero de hierro forjado")
				.append("price", 600)
				.append("date", LocalDate.now())
				.append("seller", "user02@email.com")
				.append("available", false)
				.append("featured", false);
		offers.insertOne(offer10);

		Document offer11 = new Document()
				.append("title","Cortina")
				.append("description", "Cortina de seda estampada")
				.append("price", 150)
				.append("date", LocalDate.now())
				.append("seller", "user02@email.com")
				.append("available", false)
				.append("featured", false);
		offers.insertOne(offer11);

		Document offer12 = new Document()
				.append("title","Jarrón")
				.append("description", "Jarrón de cristal de Murano")
				.append("price", 250)
				.append("date", LocalDate.now())
				.append("seller", "user01@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer12);

		Document offer13 = new Document()
				.append("title","Estantería")
				.append("description", "Estantería de madera maciza")
				.append("price", 200)
				.append("date", LocalDate.now())
				.append("seller", "user01@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer13);

		Document offer14 = new Document()
				.append("title","Cuadro pequeño")
				.append("description", "Cuadro al óleo de paisaje")
				.append("price", 150)
				.append("date", LocalDate.now())
				.append("seller", "user02@email.com")
				.append("available", true)
				.append("featured", true);
		offers.insertOne(offer14);

		Document offer15 = new Document()
				.append("title","Sofá cama")
				.append("description", "Sofá cama de piel blanca")
				.append("price", 600)
				.append("date", LocalDate.now())
				.append("seller", "user01@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer15);

		Document offer16 = new Document()
				.append("title","Lámpara de techo")
				.append("description", "Lámpara de techo de diseño")
				.append("price", 80)
				.append("date", LocalDate.now())
				.append("seller", "user05@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer16);

		Document offer17 = new Document()
				.append("title","Mesa pequeña")
				.append("description", "Mesa de centro de cristal y acero")
				.append("price", 250)
				.append("date", LocalDate.now())
				.append("seller", "user03@email.com")
				.append("available", false)
				.append("featured", false);
		offers.insertOne(offer17);

		Document offer18 = new Document()
				.append("title","Escultura")
				.append("description", "Escultura de bronce abstracta")
				.append("price", 450)
				.append("date", LocalDate.now())
				.append("seller", "user07@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer18);

		Document offer19 = new Document()
				.append("title","Alfombra persa")
				.append("description", "Alfombra persa de seda")
				.append("price", 750)
				.append("date", LocalDate.now())
				.append("seller", "user04@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer19);

		Document offer20 = new Document()
				.append("title","Espejo")
				.append("description", "Espejo con marco dorado")
				.append("price", 120)
				.append("date", LocalDate.now())
				.append("seller", "user05@email.com")
				.append("available", true)
				.append("featured", false);
		offers.insertOne(offer20);
	}

	private void insertarConversaciones() {

	}

}
