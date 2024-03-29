package com.glinboy.ip.address.web.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.glinboy.ip.address.model.dto.IpInfoResponseDTO;
import com.glinboy.ip.address.model.enums.ResponseType;
import com.glinboy.ip.address.util.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

	@Value("#{'${application.default-response-type}'.toUpperCase()}")
	private String responseType;

	@Value("${application.header-candidates}")
	private String[] headerCandidates;

	public static final String USER_IP_NAME = "USER_IP";

	@GetMapping
	public RedirectView redirectToHome() {
		ResponseType rt = ResponseType.lookup(responseType);
		if (ResponseType.HTML.equals(rt)) {
			return new RedirectView("/index.html");
		} else if (ResponseType.JSON.equals(rt)) {
			return new RedirectView("/index.json");
		} else {
			return new RedirectView("/index.html");
		}
	}

	@GetMapping("/index.html")
	public String getHomeHtml(HttpServletRequest request, @RequestHeader MultiValueMap<String, String> requestHeaders,
			@RequestParam(required = false, defaultValue = "") Collection<String> includes, Model model) {
		model.addAttribute("includes", includes);
		Optional<String> userIpOptional = RequestUtils.extractIP(request, headerCandidates);

		userIpOptional.ifPresentOrElse(i -> model.addAttribute(USER_IP_NAME, i),
				() -> model.addAttribute(USER_IP_NAME, request.getRemoteAddr()));

		if (includes.contains("headers")) {
			model.addAttribute("headers", requestHeaders);
		}

		return "index";
	}

	@GetMapping("/index.json")
	public ResponseEntity<IpInfoResponseDTO> getHomeJson(HttpServletRequest request,
			@RequestHeader MultiValueMap<String, String> requestHeaders,
			@RequestParam(required = false, defaultValue = "") Collection<String> includes) {
		Optional<String> userIpOptional = RequestUtils.extractIP(request, headerCandidates);
		IpInfoResponseDTO ipInfo = IpInfoResponseDTO.builder().build();

		userIpOptional.ifPresentOrElse(ipInfo::setIp, () -> ipInfo.setIp(request.getRemoteAddr()));

		if (includes.contains("headers")) {
			ipInfo.setHeadears(requestHeaders);
		}

		return ResponseEntity.ok(ipInfo);
	}

}
