package com.iot.building.reservation.service;

import java.util.List;

import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;


public interface IReservationService {
	
	public Boolean saveReservation(ReservationReq reservationReq);

    public void updateReservation(ReservationReq reservationReq);

    public List<ReservationResp> findByReservationList(ReservationReq req);

    public void delReservationById(Long id);

    public ReservationResp findNearResercationByOpenId(ReservationReq req);

    public ReservationResp currentReservationStatus(ReservationReq req);

    public ReservationResp getStartReservation(ReservationReq req);

}
