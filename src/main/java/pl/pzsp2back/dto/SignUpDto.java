package pl.pzsp2back.dto;


public record SignUpDto(String login, String password, String email, String name, String surname, long groupId) {}
