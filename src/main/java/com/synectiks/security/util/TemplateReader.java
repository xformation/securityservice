package com.synectiks.security.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class TemplateReader {
	
	private static final Logger logger = LoggerFactory.getLogger(TemplateReader.class);
	
	public String readTemplate(String templateFile){
		logger.info("Reading template file");
		InputStream resource = null;
		String fileData = null;
		try {
			File file = ResourceUtils.getFile(templateFile);
			resource = new FileInputStream(file);
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
				fileData = reader.lines().collect(Collectors.joining("\n"));
				logger.debug(fileData);
			}
			return fileData;
		}catch(Exception e) {
			logger.error("Exception while reading template file. ",e);
			if(resource != null) {
				try {
					resource.close();
				}catch(Exception ep) {
					logger.warn("Exception while closing input stream. "+ep.getMessage());
				}
				
			}
		}
		return null;
	}
}
