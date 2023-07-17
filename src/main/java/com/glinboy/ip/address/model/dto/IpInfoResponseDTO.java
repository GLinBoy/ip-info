package com.glinboy.ip.address.model.dto;

import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpInfoResponseDTO {
	private String ip;
	private MultiValueMap<String, String> headears;
}
