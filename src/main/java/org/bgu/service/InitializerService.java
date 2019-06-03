package org.bgu.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

/**
 * @author William Gentry
 */
@Service
public class InitializerService {

	private final WebClient webClient;

	public InitializerService(WebClient.Builder applicationWebClientBuilder) {
		this.webClient = applicationWebClientBuilder.build();
	}

	public Mono<Resource> downloadAndReturnSpringStarterProject(String projectName) {
		Flux<DataBuffer> zipDownload = getSpringInitializerZip(projectName);
		ByteArrayOutputStream outputZip = getOutputZipFile();
		return DataBufferUtils.write(zipDownload, outputZip).map(DataBufferUtils::release).then(Mono.just(outputZip)).flatMap(zip -> {
			return Mono.just(new ByteArrayResource(zip.toByteArray()));
		});
	}

	private ByteArrayOutputStream getOutputZipFile() {
		return new ByteArrayOutputStream();
	}

	private Flux<DataBuffer> getSpringInitializerZip(String projectName) {
		return webClient.get().uri("?name={projectName}", projectName).retrieve().bodyToFlux(DataBuffer.class);
	}
}
