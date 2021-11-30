package com.nutrymaco.reservation.repository;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.nutrymaco.reservation.model.Hotel;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

/**
 * HotelRepositoryImpl
 */
@ApplicationScoped
public class HotelRepositoryImpl implements HotelRepository {

	@Inject
	PgPool client;

	@PostConstruct
	void config() {
		try {
		client.query("""
					 CREATE TABLE hotel
					 (
					  id        SERIAL PRIMARY KEY,
					  hotel_uid uuid         NOT NULL UNIQUE,
					  name      VARCHAR(255) NOT NULL,
					  country   VARCHAR(80)  NOT NULL,
					  city      VARCHAR(80)  NOT NULL,
					  address   VARCHAR(255) NOT NULL,
					  stars     INT,
					  price     INT          NOT NULL
					  );

					 INSERT INTO hotel(id, hotel_uid, name, country, city, address, stars, price)
				VALUES (default, '049161bb-badd-4fa8-9d90-87c9a82b0668', 'Ararat Park Hyatt Moscow',
					 'Россия', 'Москва', 'Неглинная ул., 4', 5, 10000);
					 """)
				.executeAndAwait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public Hotel getHotel(UUID hotelUid) {
	    return getHotels().stream()
			.filter(hotel -> hotelUid.equals(hotel.getHotelUid()))
			.findFirst()
				.orElseThrow();
	}

	@Override
	public List<Hotel> getHotels() {
		return client.query("select * from hotel")
			.execute()
			.toMulti()
			.flatMap(RowSet::toMulti)
			.map(this::convertFromRow)
			.collect().asList()
				.await().atMost(Duration.ofSeconds(30l));
	}


	private Hotel convertFromRow(Row row) {
		return new Hotel(
						 row.getInteger(0),
						 row.getUUID(1),
						 row.getString(2),
						 row.getString(3),
						 row.getString(4),
						 row.getString(5),
						 row.getInteger(6),
						 row.getInteger(7)
		);
	}


	
}
