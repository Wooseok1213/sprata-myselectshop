package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FolderContorller {

    private final FolderService folderService;

    @PostMapping("/Folders")
    public void addFolders(@RequestBody ProductMypriceRequestDto.FolderRequestDto foldeRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> folderNames = foldeRequestDto.getFolderNames();
        folderService.addFolders(folderNames, userDetails.getUser());
    }
}
