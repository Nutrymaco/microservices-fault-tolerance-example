package com.nutrymaco.gateway.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nutrymaco.gateway.dto.CreateReservationResponse;
import com.nutrymaco.gateway.dto.PaymentDto;
import com.nutrymaco.gateway.dto.ReservationDto;
import com.nutrymaco.gateway.exception.ServiceNotAvailableException;
import com.nutrymaco.gateway.model.*;
import com.nutrymaco.gateway.service.LoyaltyServiceClient;
import com.nutrymaco.gateway.service.PaymentServiceClient;
import com.nutrymaco.gateway.service.ReservationServiceClient;

import com.nutrymaco.gateway.wrapper.RequestScheduler;
import com.nutrymaco.gateway.wrapper.impl.LoyaltyServiceWrapper;
import com.nutrymaco.gateway.wrapper.impl.PaymentServiceWrapper;
import com.nutrymaco.gateway.wrapper.impl.ReservationServiceWrapper;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import static com.nutrymaco.gateway.wrapper.Status.BAD;


/**
 * GatewayResource
 */

@Path("/api/v1")
public class GatewayResource {

	@Inject
	ReservationServiceWrapper reservationServiceWrapper;

	@Inject
	PaymentServiceWrapper paymentServiceWrapper;

	@Inject
	LoyaltyServiceWrapper loyaltyServiceWrapper;

	@Inject
	RequestScheduler requestScheduler;

	@GET
	@Path("/reservations")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> getReservations(@HeaderParam("X-User-Name") String username) {
		return reservationServiceWrapper.getReservations(username)
				.orElseThrow(() -> new ServiceNotAvailableException("reservation-service")).stream()
				.map(this::getReservationResponseBody)
				.collect(Collectors.toList());
	}

	@GET
	@Path("/reservations/{reservationUid}")
	public Map<String, Object> getUserReservation(@PathParam("reservationUid") UUID reservationUid,
												@HeaderParam("X-User-Name") String username) {
		Reservation reservation = reservationServiceWrapper.getUserReservation(reservationUid, username)
				.orElseThrow(() -> new ServiceNotAvailableException("reservation-service"));
		return getReservationResponseBody(reservation);
	}

	@GET
	@Path("/hotels")
	@Produces(MediaType.APPLICATION_JSON)
	public PaginationResponse<Hotel> getHotels(@QueryParam("page") int page, @QueryParam("size") int size) {
		var hotels = reservationServiceWrapper.getHotels()
				.orElseThrow(() -> new ServiceNotAvailableException("reservation-service"));
		return new PaginationResponse<>(page, size, hotels.size(),
				hotels.stream().skip(size * (page - 1)).limit(size).collect(Collectors.toList()));
	}

	@GET
	@Path("/loyalty")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> getLoyalty(@HeaderParam("X-User-Name") String username) {
		var l = loyaltyServiceWrapper.getLoyalty(username)
				.orElseThrow(() -> new ServiceNotAvailableException("loyalty-service"));
		return Map.of(
					  "status", l.getStatus().name().toUpperCase(),
					  "discount", l.getDiscount(),
					  "reservationCount", l.getReservationCount()
		);
	}

	@POST
	@Path("/reservations")
	@Produces(MediaType.APPLICATION_JSON)
	public CreateReservationResponse createReservation(ReservationDto reservationDto,
										 @HeaderParam("X-User-Name") String username) throws ParseException {
		UUID hotelUid = reservationDto.getHotelUid();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long startDate = dateFormat.parse(reservationDto.getStartDate()).getTime();
		long endDate = dateFormat.parse(reservationDto.getEndDate()).getTime();

		System.out.println("end - start = " + (endDate - startDate));

		Hotel hotel = reservationServiceWrapper.getHotels()
				.orElseThrow(() -> new ServiceNotAvailableException("reservation-service")).stream()
				.filter(h -> h.getHotelUid().equals(hotelUid))
				.findFirst().orElseThrow(() -> new ServiceNotAvailableException("reservation-service"));

		System.out.println("hotel - " + hotel);

		int discount = loyaltyServiceWrapper.getLoyalty(username)
				.orElseThrow(() -> new ServiceNotAvailableException("loyalty-service")).getDiscount();

		System.out.println("discount = " + discount);

		long days = (endDate - startDate) / (24 * 3600 * 1000);

		System.out.println("hotel price = " + hotel.getPrice());

		int price = (int)(hotel.getPrice() * days * ((double)(100 - discount) / 100));

		System.out.println("price = " + price);

		var payment = paymentServiceWrapper.createPayment(new Payment(-1, null, Status.PAID, price))
				.orElseThrow(() -> new ServiceNotAvailableException("payment-service"));
		System.out.println("payment - " + payment);

		var reservation = reservationServiceWrapper.createReservation(new Reservation(0,
																			  null,
																			 username,
																			 payment.getPaymentUid(),
																			 hotel.getId(),
																			 Status.PAID,
																			 startDate, endDate));
		if (reservation.isEmpty()) {
			paymentServiceWrapper.cancelPayment(payment.getPaymentUid());
			throw new ServiceNotAvailableException("reservation-service");
		}
		System.out.println("invoke reservation-service createReservation");

		var status = loyaltyServiceWrapper.increaseReservationCount(username);
		if (status == BAD) {
			paymentServiceWrapper.cancelPayment(payment.getPaymentUid());
			reservationServiceWrapper.cancelReservation(reservation.get().getReservationUid(), username);
			throw new ServiceNotAvailableException("loyalty-service");
		}

		return new CreateReservationResponse(reservation.get().getReservationUid(),
											 hotelUid,
											 reservationDto.getStartDate(),
											 reservationDto.getEndDate(),
											 discount,
											 Status.PAID,
				new PaymentDto(Status.PAID, price));
	}


	@DELETE
	@Path("/reservations/{reservationUid}")
	public Response cancelReservation(@PathParam("reservationUid") UUID reservationUid,
								  @HeaderParam("X-User-Name") String username) {
		System.out.println("cancelReservation()");
		var reservation = reservationServiceWrapper.getUserReservation(reservationUid, username)
				.orElseThrow(() -> new ServiceNotAvailableException("reservation-service"));

		var cancelPaymentStatus = paymentServiceWrapper.cancelPayment(reservation.getPaymentUid());
		if (cancelPaymentStatus == BAD) {
			throw new ServiceNotAvailableException("payment-service");
		}
		var cancelReservationStatus = reservationServiceWrapper.cancelReservation(reservation.getReservationUid(), username);
		if (cancelReservationStatus == BAD) {
			requestScheduler.tryExecuteUntilSuccess(
					() -> reservationServiceWrapper.cancelReservation(reservation.getReservationUid(), username),
					() -> System.out.println("request to cancel reservation succeed"));
		}

		var decreaseReservationsCountStatus = loyaltyServiceWrapper.decreaseReservationCount(username);
		if (decreaseReservationsCountStatus == BAD) {
			requestScheduler.tryExecuteUntilSuccess(
					() -> loyaltyServiceWrapper.decreaseReservationCount(username),
					() -> System.out.println("request to decrease reservations count succeed")
			);
		}

		System.out.println("cancelled");
		return Response.status(204).build();
	}

	@GET
	@Path("/me")
	public Map<String, Object> getUserInfo(@HeaderParam("X-User-Name") String username) {
		return Map.of(
					  "reservations", getReservations(username),
					  "loyalty", loyaltyServiceWrapper.getLoyalty(username).orElse(new Loyalty())
		);
	}

	private Date dateFromEpochMilli(long epoch) {
		return Date.from(Instant.ofEpochMilli(epoch));
	}

	private String dateToString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	private Map<String, Object> getReservationResponseBody(Reservation reservation) {
		Hotel hotel = reservationServiceWrapper.getHotels().orElse(List.of()).stream()
				.filter(h -> h.getId() == reservation.getHotelId())
				.findFirst().orElseThrow(() -> new ServiceNotAvailableException("reservation-service"));
		Optional<Payment> payment = paymentServiceWrapper.getPayment(reservation.getPaymentUid());
		return Map.of(
				"reservationUid", reservation.getReservationUid(),
				"hotel", Map.of(
						"hotelUid", hotel.getHotelUid(),
						"name", hotel.getName(),
						"fullAddress", hotel.getCountry() + ", " + hotel.getCity() + ", " + hotel.getAddress(),
						"stars", hotel.getStars()
				),
				"startDate", dateToString(dateFromEpochMilli(reservation.getStartDate())),
				"endDate", dateToString(dateFromEpochMilli(reservation.getEndDate())),
				"status", reservation.getStatus(),
				"payment", payment.isEmpty()
						? Map.of()
						: Map.of(
						"status", payment.get().getStatus(),
						"price", payment.get().getPrice())
		);
	}
}
