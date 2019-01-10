package com.iot.building.reservation.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.building.reservation.mapper.ReservationMapper;
import com.iot.building.reservation.service.IReservationService;
import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;

@Service("reservationService")
@Transactional
public class ReservationServiceImpl  implements IReservationService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

	@Autowired
	ReservationMapper reservationMapper;

	@Override
	public Boolean saveReservation(ReservationReq reservationReq) {
		return addOrUpdateReservation(reservationReq);
	}
	private synchronized Boolean addOrUpdateReservation(ReservationReq reservationReq) {
		if (reservationReq.getId() == null) {
			int existCount = reservationMapper.countReservByStartAndEndTime(reservationReq);
			if (existCount > 0) {
				return false;
			}
			reservationReq.setFlag(0);
			reservationReq.setCreatTime(new Date());
			reservationMapper.save(reservationReq);
		} else {
			int existCount = reservationMapper.countReservByStartAndEndTimeOutId(reservationReq);
			if (existCount > 0) {
				return false;
			}
			reservationMapper.update(reservationReq);
		}
		return true;
	}

	@Override
	public void updateReservation(ReservationReq reservationReq) {
		addOrUpdateReservation(reservationReq);
	}

	@Override
	public List<ReservationResp> findByReservationList(ReservationReq req) {
		return reservationMapper.findResercationByCondition(req);
	}

	@Override
	public void delReservationById(Long id) {
		reservationMapper.delete(id);
	}

	@Override
	public ReservationResp findNearResercationByOpenId(ReservationReq req) {
		return reservationMapper.findNearResercationByOpenId(req);
	}

	//reservationMapper.getNextReservation(spaceId, current)方法保留，接口往smart-building移动？
	public ReservationResp currentReservationStatus(ReservationReq reservationReq) {
		Long spaceId = reservationReq.getSpaceId();
		Long currentDate = reservationReq.getCurrentDate();
		ReservationResp reservation = reservationMapper.getCurrentReservation(spaceId, currentDate);
		if (reservation == null) {
			List<ReservationResp> reservationList = reservationMapper.getNextReservation(spaceId, currentDate);
			if (reservationList.size() > 0) {
				reservation = reservationList.get(0);// 下个会议
				reservation.setStatus(1);
			}
		} else {
			reservation.setStatus(0);// 当前会议标识
			if (reservation.getFlag() == 1) {
				reservation.setStatus(2);// 会议进行中
			}
		}
		return reservation;
	}

	public ReservationResp getStartReservation(ReservationReq reservationReq) {
		Long currentTime = reservationReq.getCurrentDate();
		Long nextTenTime = currentTime + 600000L;
		Long spaceId = reservationReq.getSpaceId();
		ReservationResp reservation = reservationMapper.getStartReservation(spaceId, currentTime, nextTenTime);
		return reservation;
	}

}
