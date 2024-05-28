package pl.pzsp2back.dto;

public record UserDto(String login, String password, Boolean ifAdmin, Integer balance, String email, String name, String surname, Long groupId) {
}
