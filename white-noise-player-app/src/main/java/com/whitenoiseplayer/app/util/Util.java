package com.whitenoiseplayer.app.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Util {
	public static String readFileContents(String filePath) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(filePath)){
			return new String(fis.readAllBytes());
		} 
	}
}
