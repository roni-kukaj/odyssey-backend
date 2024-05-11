package com.odyssey.plans;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record PlanUpdateRequest(Integer userId, Integer locationId, @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
}
