package hr.foi.servicesync.business

import org.springframework.stereotype.Service

@Service
class ReservationService(
    private val reservationDataProvider: IReservationDataProvider,
    private val messagingProvider: IMessagingProvider
) {


}