package com.shishu.utility.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * BusinessException聚合类
 * @see com.core.exception.BusinessException
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午8:57:03
 */
@SuppressWarnings("serial")
public class MultiBusinessException extends RuntimeException {
	private List<BusinessException> exceptionList = new ArrayList<BusinessException>();

	public MultiBusinessException() {
	}

	public MultiBusinessException(String messageList) {
		super(messageList);
	}

	public boolean put(BusinessException e) {
		return exceptionList.add(e);
	}

	public void remove(BusinessException e) {
		exceptionList.remove(e);
	}

	@Override
	public String getMessage() {
		StringBuffer msgBuf = new StringBuffer();
		for (int i = 0; i < exceptionList.size(); i++) {
			msgBuf.append(exceptionList.get(i).getMessage());
		}
		return msgBuf.toString();
	}

	public boolean hasExcption() {
		return exceptionList.size() > 0;
	}

	public Iterator<BusinessException> iterator() {
		return exceptionList.iterator();
	}
}
