package com.iot.building.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.reservation.api.ReservationApi;
import com.iot.building.reservation.service.IReservationService;
import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;

/**
 * @Author: wl
 * @Date: 2018/10/12
 * @Description: *
 */
@RestController
public class ResevationController implements ReservationApi {

	@Autowired
	private IReservationService reservationService;

	@Override
	public Boolean saveReservation(ReservationReq reservationReq) {
		return reservationService.saveReservation(reservationReq);
	}

	@Override
	public void updateReservation(ReservationReq reservationReq) {
		reservationService.updateReservation(reservationReq);
	}

	@Override
	public List<ReservationResp> findByReservationList(ReservationReq req) {
		return reservationService.findByReservationList(req);
	}

	@Override
	public void delReservationById(Long tenantId,Long id) {
		reservationService.delReservationById(id);
	}

	@Override
	public ReservationResp findNearResercationByOpenId(ReservationReq req) {
		return reservationService.findNearResercationByOpenId(req);
	}

	@Override
	public ReservationResp currentReservationStatus(ReservationReq req) {
		return reservationService.currentReservationStatus(req);
	}

	@Override
	public ReservationResp getStartReservation(ReservationReq req) {
		return reservationService.getStartReservation(req);
	}
	
}
