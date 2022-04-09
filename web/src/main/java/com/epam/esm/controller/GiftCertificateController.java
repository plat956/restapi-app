package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public List<GiftCertificate> getAll(@RequestParam(value = "tag", required = false) String tag,
                                        @RequestParam(value = "search", required = false) String search,
                                        @RequestParam(value = "sort", required = false)  String sort) {
        return giftCertificateService.findAll(tag, search, sort);
    }

    @GetMapping("/{id}")
    public GiftCertificate getOne(@PathVariable("id") Long id) {
        return giftCertificateService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate save(@RequestBody @Valid GiftCertificate giftCertificate) {
        return giftCertificateService.save(giftCertificate);
    }

    @PatchMapping("/{id}")
    public GiftCertificate update(@PathVariable("id") Long id, @RequestBody @Valid GiftCertificate source) {
        return giftCertificateService.update(id, source);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        giftCertificateService.delete(id);
    }
}
