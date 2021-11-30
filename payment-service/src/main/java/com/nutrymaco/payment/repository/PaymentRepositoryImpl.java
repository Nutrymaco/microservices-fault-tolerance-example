package com.nutrymaco.payment.repository;

import java.time.Duration;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.nutrymaco.payment.model.Payment;
import com.nutrymaco.payment.model.Status;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

/**
 * PaymentRepositoryImpl
 */
@ApplicationScoped
public class PaymentRepositoryImpl implements PaymentRepository {


	@Inject
	PgPool client;

	@PostConstruct
	void config() {
		try {
			client.query(
					 """
					 CREATE TABLE payment
					 (
					  id          SERIAL PRIMARY KEY,
					  payment_uid uuid        NOT NULL,
					  status      VARCHAR(20) NOT NULL
					  CHECK (status IN ('PAID', 'CANCELED')),
					  price       INT         NOT NULL
					  );
					 """)
				.executeAndAwait();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Payment createPayment(Payment payment) {
		return client.preparedQuery("""
									INSERT INTO payment(
														id,
														payment_uid,
														status,
														price
														)
									VALUES (default, uuid_generate_v4(), $1, $2)
									returning id, payment_uid, status, price;
									""")
			.mapping(this::convertFromRow)
			.execute(convertToRow(payment))
			.toMulti()
			.flatMap(RowSet::toMulti)
			.collect()
			.asList()
			.await().atMost(Duration.ofSeconds(30l))
			.get(0);

		  }

	
	@Override
	public Payment getPayment(UUID paymentUid) {
		return client.preparedQuery("""
									SELECT * FROM payment
									WHERE payment_uid = $1
									""")
			.execute(Tuple.of(paymentUid))
			.toMulti()
			.flatMap(RowSet::toMulti)
			.map(this::convertFromRow)
			.collect().asList()
			.await().atMost(Duration.ofSeconds(30l))
				.get(0);
	}

	@Override
	public void cancelPayment(UUID paymentUid) {
	    client.preparedQuery("""
							 UPDATE payment SET
							 status = $1
							 WHERE payment_uid = $2;
							 """)
			.execute(Tuple.of(Status.CANCELED, paymentUid))
			.await().atMost(Duration.ofSeconds(30l));
		
	}

	   

	private Payment convertFromRow(Row row) {
		return new Payment(row.getInteger(0),
						   row.getUUID(1),
						   row.getString(2),
						   row.getInteger(3));
	}

	private Tuple convertToRow(Payment payment) {
		return Tuple.of(
				payment.getStatus(),
				payment.getPrice());
	}
	
}
