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

import java.lang.management.ManagementFactory;
import java.time.Instant;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/** 汇总健康状态并检查诊所服务依赖。 */
@Service
class ClinicHealthService {

	private final JdbcTemplate jdbcTemplate;

	ClinicHealthService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	ClinicHealthResponse health() {
		boolean databaseAvailable = isDatabaseAvailable();
		return new ClinicHealthResponse(databaseAvailable ? "正常" : "异常", databaseAvailable ? "宠物诊所服务运行中" : "数据库暂时不可用",
				databaseAvailable ? "正常" : "异常", Instant.now().toString(),
				ManagementFactory.getRuntimeMXBean().getUptime() / 1000);
	}

	ClinicProbeResponse liveness() {
		// 存活检查只确认应用进程能响应，不依赖数据库状态。
		return new ClinicProbeResponse("存活", "宠物诊所应用正在运行", "未检查");
	}

	ClinicProbeResponse readiness() {
		// 就绪检查执行只读查询，确认应用可以访问数据库。
		boolean databaseAvailable = isDatabaseAvailable();
		return new ClinicProbeResponse(databaseAvailable ? "就绪" : "未就绪", databaseAvailable ? "应用可以接收请求" : "数据库暂时不可用",
				databaseAvailable ? "正常" : "异常");
	}

	private boolean isDatabaseAvailable() {
		try {
			return Integer.valueOf(1).equals(jdbcTemplate.queryForObject("SELECT 1", Integer.class));
		}
		catch (DataAccessException exception) {
			return false;
		}
	}

}
