package com.glinboy.ip.address.web.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

	@Value("${application.header-candidates}")
	private String[] headerCandidates;
	
	public static final String USER_IP_NAME = "USER_IP";

	@GetMapping
	public String getHome(Model model) {
		Optional<String> userIpOptional = Optional.empty();
		
		if (RequestContextHolder.getRequestAttributes() == null) {
			userIpOptional = Optional.of("0.0.0.0");
		}

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		for (String header : headerCandidates) {
			String ipList = request.getHeader(header);
			if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
				userIpOptional = Optional.of(ipList.split(",")[0]);
			}
		}
		
		userIpOptional.ifPresentOrElse(i -> model.addAttribute(USER_IP_NAME, i),
				() -> model.addAttribute(USER_IP_NAME, request.getRemoteAddr()));
		return "index";
	}
}
