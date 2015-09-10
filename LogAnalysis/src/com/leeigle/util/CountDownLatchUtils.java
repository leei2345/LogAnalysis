package com.leeigle.util;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchUtils {
	private int amount;
	private CountDownLatch cdl;

	public CountDownLatchUtils(int count) {
		this.amount = count;
		this.cdl = new CountDownLatch(count);
	}

	public void countDown() {
		this.cdl.countDown();
	}

	public void await() throws InterruptedException {
		this.cdl.await();
	}
	
	public void await(long time) throws InterruptedException {
		this.cdl.await(time, TimeUnit.MILLISECONDS);
	}

	public long getCount() {
		return this.cdl.getCount();
	}

	public int getAmount() {
		return this.amount;
	}
	
	public int getPrecent () {
		BigDecimal count = new BigDecimal(this.getCount());
		BigDecimal amount = new BigDecimal(this.amount);
		BigDecimal yu = amount.subtract(count).multiply(new BigDecimal(100));
		int res = 0;
		if (amount.intValue() != 0) {
			res = yu.divide(amount, 0, BigDecimal.ROUND_HALF_UP).intValue();
		}
		return res;
	}
	
}