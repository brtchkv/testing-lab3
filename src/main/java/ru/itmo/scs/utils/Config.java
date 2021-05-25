package ru.itmo.scs.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Config {
	private Driver driver;
	private String website;
	private String usernameRegistered;
	private String passwordRegistered;
	private String emailRegistered;

	private String usernameToRegister;
	private String passwordToRegister;
	private String emailToRegister;

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class Driver {
		private String browser;
		private String path;
		private String downloadPath;
		private String uploadFile;
	}
}