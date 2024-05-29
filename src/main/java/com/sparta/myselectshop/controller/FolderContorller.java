package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/folders")
    public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return folderService.getFolders(userDetails.getUser());
    }


}
