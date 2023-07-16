package com.glinboy.ip.address.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IpInfoResponseDTO {
	private String ip;
}
