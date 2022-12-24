package com.glinboy.ip.address.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class HomeResource {

	@Value("${application.header-candidates}")
	private String[] headerCandidates;

	@GetMapping
	public ResponseEntity<String> getHome() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return ResponseEntity.ok("0.0.0.0");
		}

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		log.info("User-Agent: {}", request.getHeader("User-Agent"));
		for (String header : headerCandidates) {
			String ipList = request.getHeader(header);
			if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
				String ip = ipList.split(",")[0];
				return ResponseEntity.ok(ip);
			}
		}
		return ResponseEntity.ok(request.getRemoteAddr());
	}
}
