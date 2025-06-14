package com.optiroute.com.domain.usecase.route

import com.optiroute.com.domain.models.Route
import com.optiroute.com.domain.repository.RouteRepository
import com.optiroute.com.utils.Resource
import javax.inject.Inject

class GetRouteByIdUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(routeId: String): Resource<Route> {
        if (routeId.isBlank()) {
            return Resource.Error("Route ID cannot be empty.")
        }
        return routeRepository.getRouteById(routeId)
    }
}