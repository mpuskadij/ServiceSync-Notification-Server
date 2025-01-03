package hr.foi.servicesync.business

interface IFcmTokenProvider {
    fun getFcmToken(username: String) : String
}