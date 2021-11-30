package com.nutrymaco.gateway.model;

import java.util.List;

/**
 * PaginationResponse
 */
public class PaginationResponse<T> {
	
	private final int page;
	private final int pageSize;
	private final int totalElements;
	private final List<T> items;

	public PaginationResponse(int page, int pageSize, int totalElements, List<T> items) {
		this.page = page;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.items = items;
	}

	public int getPage() {
		return page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public List<T> getItems() {
		return items;
	}
	
}
