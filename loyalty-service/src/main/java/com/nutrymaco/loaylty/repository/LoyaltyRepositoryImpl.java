package com.nutrymaco.loaylty.repository;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.nutrymaco.loaylty.model.Loyalty;
import com.nutrymaco.loaylty.model.LoyaltyStatus;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

/**
 * LoyaltyRepositoryImpl
 */
@ApplicationScoped
public class LoyaltyRepositoryImpl implements LoyaltyRepository {

	@Inject
	PgPool client;

	@PostConstruct
	void config() {
		try {
		client.query("""
					 CREATE TABLE loyalty
					 (
					  id                SERIAL PRIMARY KEY,
					  username          VARCHAR(80) NOT NULL UNIQUE,
					  reservation_count INT         NOT NULL DEFAULT 0,
					  status            VARCHAR(80) NOT NULL DEFAULT 'BRONZE'
					  CHECK (status IN ('BRONZE', 'SILVER', 'GOLD')),
					  discount          INT         NOT NULL
					  );
					 """)
				.executeAndAwait();			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	@Override
	public void decreaseReservationCount(String username) {
	    if (isLoyaltyExist(username)) {
			int newReservationCount = getReservationCount(username) - 1;
			LoyaltyStatus newLoyaltyStatus = LoyaltyStatus.resolveByReservationCount(newReservationCount);
			client.preparedQuery("""
								 UPDATE loyalty SET
								 reservation_count = $1,
								 status = $2,
								 discount = $3
								 WHERE username = $4
								 """)
				.execute(Tuple.of(
								  newReservationCount,
								  newLoyaltyStatus.name().toUpperCase(),
								  newLoyaltyStatus.discount,
								  username
								  ))
				.await()
					.atMost(Duration.ofSeconds(30l));
		} else {
			createLoyalty(username);
		}
		
	}

	@Override
	public LoyaltyStatus getLoyaltyStatus(String username) {
		if (isLoyaltyExist(username)) {
			return client.preparedQuery("""
									SELECT status FROM loyalty
									WHERE username = $1
									""")
			.execute(Tuple.of(username))
			.toMulti()
			.flatMap(RowSet::toMulti)
			.map(row -> LoyaltyStatus.valueOf(row.getString(0)))
			.collect()
			.asList()
			.await()
			.atMost(Duration.ofSeconds(30l))
				.get(0);
		} else {
			createLoyalty(username);
			return LoyaltyStatus.BRONZE;
		}
		
	}


	@Override
	public Loyalty getLoyalty(String username) {
		if (isLoyaltyExist(username)) {
			return client.preparedQuery("""
										SELECT * FROM loyalty
										WHERE username = $1
										""")
				.execute(Tuple.of(username))
				.toMulti()
				.flatMap(RowSet::toMulti)
				.map(row -> {
						var l = new Loyalty();
						l.setId(row.getInteger(0));
						l.setUsername(row.getString(1));
						l.setReservationCount(row.getInteger(2));
						l.setStatus(LoyaltyStatus.valueOf(row.getString(3)));
						l.setDiscount(row.getInteger(4));
						return l;
					})
				.collect().asList()
				.await().atMost(Duration.ofSeconds(30l))
					.get(0);
		} else{
			createLoyalty(username);
			return getLoyalty(username);
		}
	}
	   
		
	
	private int getReservationCount(String username) {
		return client.preparedQuery("""
									SELECT reservation_count FROM loyalty
									WHERE username = $1
									""")
			.execute(Tuple.of(username))
			.toMulti()
			.flatMap(RowSet::toMulti)
			.map(row -> row.getInteger(0))
			.collect()
			.asList()
			.await()
			.atMost(Duration.ofSeconds(30l))
				.get(0);
	}

	@Override
	public void increaseReservationCount(String username) {
		int newReservationCount = getReservationCount(username) + 1;
		LoyaltyStatus newStatus = LoyaltyStatus.resolveByReservationCount(newReservationCount);
	    client.preparedQuery("""
							 UPDATE loyalty SET
							 reservation_count = $1,
							 status = $2,
							 discount = $3
							 WHERE username = $4
							 """)
			.execute(Tuple.of(
							  newReservationCount,
							  newStatus.name().toUpperCase(),
							  newStatus.discount,
							  username))
				.await().atMost(Duration.ofSeconds(30l));
		
	}

	private void createLoyalty(String username) {
		client.preparedQuery("""
					 INSERT INTO loyalty(id, username, reservation_count, status, discount)
					 VALUES (default, $1, 0, $2, $3);
					 """)
			.execute(Tuple.of(username, LoyaltyStatus.BRONZE, LoyaltyStatus.BRONZE.discount))
				.await().atMost(Duration.ofSeconds(30l));
	}
	
	private boolean isLoyaltyExist(String username) {
		return client.preparedQuery("select * from loyalty where username = $1")
			.execute(Tuple.of(username))
			.toMulti()
			.flatMap(RowSet::toMulti)
			.collect()
			.asList()
			.await()
				.atMost(Duration.ofSeconds(30l)).size() > 0;
	}
	
}
