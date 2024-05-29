package com.sparta.myselectshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductMypriceRequestDto {
    private int myprice;

    @Getter
    public static class FolderRequestDto {
        List<String> folderNames;
    }
}