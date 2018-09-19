package org.no.sw.core.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class IdentyServiceDefault implements IdentyService {

	@Override
	public String getId() {
		return new UUID(0L, System.currentTimeMillis()).toString();
	}

}
