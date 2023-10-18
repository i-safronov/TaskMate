package safronov.apps.domain.exception

class DomainException(msg: String? = null, caz: Throwable? = null): RuntimeException(msg, caz)