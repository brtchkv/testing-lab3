package ru.itmo.scs.utils;

import lombok.SneakyThrows;
import java.io.FileInputStream;
import java.io.InputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ReadConfig {

	@SneakyThrows
	private static<T> T getConfigFromInputStream(InputStream is, Class<T> configClass) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();
		return mapper.readValue(is, configClass);
	}

	public static Config getConfigFromInternalFile(String filepath) {
		InputStream inputStream = ReadConfig.class.getClassLoader().getResourceAsStream(filepath);
		return getConfigFromInputStream(inputStream, Config.class);
	}

	@SneakyThrows
	public static Config getConfigFromExternalFile(String filepath) {
		InputStream inputStream = new FileInputStream(filepath);
		return getConfigFromInputStream(inputStream, Config.class);
	}

}
