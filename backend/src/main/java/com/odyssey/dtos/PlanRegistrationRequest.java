package com.odyssey.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record PlanRegistrationRequest(Integer userId, Integer locationId, LocalDate date) {
}
