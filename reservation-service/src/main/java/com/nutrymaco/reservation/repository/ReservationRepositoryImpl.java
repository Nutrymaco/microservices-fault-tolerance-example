package com.nutrymaco.reservation.repository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.nutrymaco.reservation.model.Reservation;
import com.nutrymaco.reservation.model.Status;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

/**
 * ReservationRepositoryImpl
 */
@ApplicationScoped
public class ReservationRepositoryImpl implements ReservationRepository {

	@Inject
	PgPool client;

	@PostConstruct
	void config() {
		try {

		client.query("""
					 CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
				CREATE TABLE reservation
				(
				 id              SERIAL PRIMARY KEY,
				 reservation_uid uuid UNIQUE NOT NULL,
				 username        VARCHAR(80) NOT NULL,
				 payment_uid     uuid        NOT NULL,
				 hotel_id        INT REFERENCES hotel (id),
				 status          VARCHAR(20) NOT NULL
				 CHECK (status IN ('PAID', 'CANCELED')),
				 start_date      TIMESTAMP WITH TIME ZONE,
				 end_date        TIMESTAMP WITH TIME ZONE
				 );
				""").executeAndForget();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Reservation createReservation(Reservation reservation) {
		Reservation created = client.preparedQuery("""
					INSERT INTO reservation(
								 id,
								 reservation_uid,
								 username,
								 payment_uid,
							     hotel_id,
								 status,
								 start_date,
				   				 end_date)
												   values (default, uuid_generate_v4(), $1, $2, $3, $4, $5, $6)
				    returning  id,
						   reservation_uid,
				           username,
						   payment_uid,
						   hotel_id,
						   status,
						   start_date,
				   		   end_date
				""")
		.mapping(this::convertFromRow)
			.execute(Tuple.from(convertToRow(reservation)))
		.toMulti()
		.flatMap(RowSet::toMulti)
		.collect()
		.asList()
		.await()
		.atMost(Duration.ofSeconds(30l))
		.get(0);
		return created;

	}

	@Override
	public List<Reservation> getReservationsByUsername(String username) {
		return client.preparedQuery("select * from reservation where username = $1")
			.execute(Tuple.of(username))
			.toMulti()
			.flatMap(RowSet::toMulti)
			.map(this::convertFromRow)
			.collect()
			.asList()
			.await()
			.atMost(Duration.ofSeconds(30l));
	}

	@Override
	public void setReservationStatus(UUID reservationUid, Status status) {
	    client.preparedQuery("update reservation set " +
							 "status = $1" +
							 " where reservation_uid = $2")
			.execute(Tuple.of(status, reservationUid))
				.await().atMost(Duration.ofSeconds(30));
	}

	private Reservation convertFromRow(Row row) {
		var reservation = new Reservation();
		reservation.setId(row.getInteger(0));
		reservation.setReservationUid(row.getUUID(1));
		reservation.setUsername(row.getString(2));
		reservation.setPaymentUid(row.getUUID(3));
		reservation.setHotelId(row.getInteger(4));
		reservation.setStatus(row.getString(5));
		reservation.setStartDate(row.getOffsetDateTime(6).toEpochSecond() * 1000);
		reservation.setEndDate(row.getOffsetDateTime(7).toEpochSecond() * 1000);
		return reservation;
	}

	private List<?> convertToRow(Reservation reservation) {
	
		return List.of(
						reservation.getUsername(),
						reservation.getPaymentUid(),
						reservation.getHotelId(),
						reservation.getStatus(),
						OffsetDateTime.ofInstant(Instant.ofEpochMilli(reservation.getStartDate()), ZoneId.systemDefault()),
						OffsetDateTime.ofInstant(Instant.ofEpochMilli(reservation.getEndDate()), ZoneId.systemDefault())
					   );
	}
}
