package com.glinboy.ip.address.util;

import java.util.Optional;

import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpServletRequest;

public final class RequestUtils {

	private RequestUtils() {}

	public static final Optional<String> extractIP(HttpServletRequest request, String[] headerCandidates) {
		Optional<String> userIpOptional = Optional.empty();

		if (RequestContextHolder.getRequestAttributes() == null) {
			userIpOptional = Optional.of("0.0.0.0");
		}

		for (String header : headerCandidates) {
			String ipList = request.getHeader(header);
			if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
				userIpOptional = Optional.of(ipList.split(",")[0]);
			}
		}
		return userIpOptional;
	}
}
