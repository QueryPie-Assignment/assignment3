package assignment.presentation.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import assignment.presentation.base.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "서버 Health Check")
public class ServerController {
	@GetMapping("/actuator/health")
	public ResponseEntity<BaseResponse<String>> healthCheck() {
		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}
