package pl.pzsp2back.dto;

public record SignInDto(String token, String login, Boolean isAdmin, Integer balance, String name, String surname) {}
