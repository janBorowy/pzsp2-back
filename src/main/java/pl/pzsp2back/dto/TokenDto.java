package pl.pzsp2back.dto;

import jakarta.validation.constraints.NotNull;

public record TokenDto(@NotNull String token, String login) {}
