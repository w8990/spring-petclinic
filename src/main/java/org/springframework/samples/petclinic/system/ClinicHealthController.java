/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 提供诊所服务的健康、存活和就绪状态接口。 */
@RestController
class ClinicHealthController {

	private final ClinicHealthService clinicHealthService;

	ClinicHealthController(ClinicHealthService clinicHealthService) {
		this.clinicHealthService = clinicHealthService;
	}

	@GetMapping("/api/clinic/health")
	ClinicHealthResponse health() {
		return clinicHealthService.health();
	}

	@GetMapping("/api/clinic/health/live")
	ClinicProbeResponse liveness() {
		return clinicHealthService.liveness();
	}

	@GetMapping("/api/clinic/health/ready")
	ResponseEntity<ClinicProbeResponse> readiness() {
		ClinicProbeResponse readiness = clinicHealthService.readiness();
		HttpStatus status = "就绪".equals(readiness.status()) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
		return ResponseEntity.status(status).body(readiness);
	}

}
