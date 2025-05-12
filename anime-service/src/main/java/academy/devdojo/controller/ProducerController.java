package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;
    private ProducerService service;

    public ProducerController(){
        this.service = new ProducerService();
    }

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listall(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name '{}'", name);

        var producers = service.findAll(name);
        List<ProducerGetResponse> producerGetResponse = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        Producer producer = service.findByIdOrThrowNotFound(id);

        ProducerGetResponse producerGetResponse = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);

        var producer = MAPPER.toProducer(producerPostRequest);

        Producer producerSaved = service.save(producer);

        var producerGetResponse = MAPPER.toProducerGetResponse(producerSaved);


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


        Producer producerToUpdate = MAPPER.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }

}
