package com.whitenoiseplayer.app.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

public class Util {
	public static String resourceToString(Resource resource)  {
		try(Reader reader = new InputStreamReader(resource.getInputStream(), Charset.forName("UTF-8"))) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
            throw new UncheckedIOException(e);
        }
	}
	
	public static ZonedDateTime toSysDefault(Instant instant) {
		return instant.atZone(ZoneId.systemDefault());
	}
}
