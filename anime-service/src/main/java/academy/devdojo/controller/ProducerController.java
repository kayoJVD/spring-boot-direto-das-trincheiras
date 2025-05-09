package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
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
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listall(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name '{}'", name);

        var producers = Producer.producerList();
        List<ProducerGetResponse> producerGetResponseList = MAPPER.toProducerGetResponseList(producers);
        if (name == null) return ResponseEntity.ok(producerGetResponseList);

        List<ProducerGetResponse> response = producerGetResponseList.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        var producerGetResponse = Producer.producerList()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .map(MAPPER::toProducerGetResponse)
                .orElse(null);

        return ResponseEntity.ok(producerGetResponse);
    }


//    @GetMapping
//    public List<Producer> listall(@RequestParam(required = false) String name) {
//
//        var producers = Producer.producerList();
//        if (name == null) return producers;
//
//        return producers.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
//    }
//
//    @GetMapping("{id}")
//    public Producer findById(@PathVariable Long id) {
//        return Producer.producerList().stream().filter(producer -> producer.getId().equals(id))
//                .findFirst().orElse(null);
//    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);

        var producer = MAPPER.toProducer(producerPostRequest);

        var response = MAPPER.toProducerGetResponse(producer);

//        Producer producer = Producer.builder()
//                .id(ThreadLocalRandom.current().nextLong(100_000))
//                .name(producerPostRequest.getName())
//                .createdAt(LocalDateTime.now()).build();

        Producer.producerList().add(producer);

//        var response = ProducerGetResponse.builder()
//                .id(producer.getId())
//                .name(producer.getName())
//                .creatdAt(producer.getCreatedAt()).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
