package zig.gestion.guide_touristique.convocation.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zig.gestion.guide_touristique.convocation.dto.ConvocationCreation;
import zig.gestion.guide_touristique.convocation.dto.ConvocationDto;
import zig.gestion.guide_touristique.convocation.service.ConvocationService;

@RestController
@RequestMapping("/api/v1/candidatures/{candidatureId}/convocation")
public class ConvocationController {
    private final ConvocationService convocationService;

    public ConvocationController(ConvocationService convocationService) {
        this.convocationService = convocationService;
    }

    @PostMapping
    public ConvocationDto convoquer(@PathVariable @Min(1) Long candidatureId,
                                    @Valid @RequestBody ConvocationCreation creation) {
        return convocationService.convoquer(candidatureId, creation);
    }
}
