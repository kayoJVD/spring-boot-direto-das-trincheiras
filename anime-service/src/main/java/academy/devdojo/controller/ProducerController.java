package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import academy.devdojo.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {

    private  final ProducerMapper mapper;

    private final ProducerService service;


    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listall(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name '{}'", name);

        var producers = service.findAll(name);
        List<ProducerGetResponse> producerGetResponse = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        Producer producer = service.findByIdOrThrowNotFound(id);

        ProducerGetResponse producerGetResponse = mapper.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-key")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);

        var producer = mapper.toProducer(producerPostRequest);

        Producer producerSaved = service.save(producer);

        var producerGetResponse = mapper.toProducerPostResponse(producerSaved);


        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        log.debug("Request to delete producer by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update (@RequestBody ProducerPutRequest request){
        log.debug("Request to update procuer: {}", request);


        Producer producerToUpdate = mapper.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }

}
