package com.iot.building.reservation.api.callback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.iot.building.reservation.api.ReservationApi;
import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;

import feign.hystrix.FallbackFactory;

/**
 * @Author: Xieby
 * @Date: 2018/9/4
 * @Description: *
 */
@Component
public class ReservationApiFallbackFactory implements FallbackFactory<ReservationApi> {


    @Override
    public ReservationApi create(Throwable throwable) {
        return new ReservationApi() {

			@Override
			public Boolean saveReservation(ReservationReq reservationReq) {
				return null;
			}

			@Override
			public void updateReservation(ReservationReq reservationReq) {
				
			}

			@Override
			public List<ReservationResp> findByReservationList(ReservationReq req) {
				return null;
			}

			@Override
			public ReservationResp findNearResercationByOpenId(ReservationReq req) {
				return null;
			}

			@Override
			public ReservationResp currentReservationStatus(ReservationReq req) {
				return null;
			}

			@Override
			public ReservationResp getStartReservation(ReservationReq req) {
				return null;
			}

			@Override
			public void delReservationById(Long tenantId, Long id) {
			}

        };
    }
}
