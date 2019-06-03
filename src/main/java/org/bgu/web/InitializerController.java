package org.bgu.web;

import org.bgu.service.InitializerService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * @author William Gentry
 */
@RestController
public class InitializerController {

	private final InitializerService initializerService;
	private final Logger logger = Loggers.getLogger(getClass());

	public InitializerController(InitializerService initializerService) {
		this.initializerService = initializerService;
	}

	@GetMapping(produces = "application/zip")
	public Mono<? extends ResponseEntity<? extends Resource>> downloadSpringStarter(@RequestParam("projectName") String projectName) {
		return initializerService.downloadAndReturnSpringStarterProject(projectName).flatMap(resource -> {
			return Mono.just(ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + projectName + ".zip\"").body(resource));
		});
	}
}
