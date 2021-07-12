package com.synectiks.security.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class TemplateReader {
	
	private static final Logger logger = LoggerFactory.getLogger(TemplateReader.class);
	
	private ResourceLoader resourceLoader;
	
	@Autowired
    public TemplateReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
	
	public String readTemplate(String templateFile){
		logger.info("Reading template file");
//		InputStream inputStream = null;
		String fileData = null;
		try {
//			Resource resource = resourceLoader.getResource(templateFile);
//			File file = resource.getFile();
//			File file = ResourceUtils.getFile(templateFile);
//			inputStream = new FileInputStream(file);
			try (InputStream inputStream = getClass().getResourceAsStream(templateFile);
				    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				    fileData = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}
//			try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//				fileData = reader.lines().collect(Collectors.joining("\n"));
//				logger.debug(fileData);
//			}
			return fileData;
		}catch(Exception e) {
			logger.error("Exception while reading template file. ",e);
//			if(inputStream != null) {
//				try {
//					inputStream.close();
//				}catch(Exception ep) {
//					logger.warn("Exception while closing input stream. "+ep.getMessage());
//				}
//				
//			}
		}
		return null;
	}
}
