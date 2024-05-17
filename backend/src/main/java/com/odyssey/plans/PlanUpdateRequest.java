package com.odyssey.plans;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record PlanUpdateRequest(Integer locationId, LocalDate date) {
}
