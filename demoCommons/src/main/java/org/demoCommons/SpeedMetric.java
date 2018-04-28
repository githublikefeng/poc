package org.demoCommons;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
public class SpeedMetric {
    /**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
    private static final MetricRegistry metrics = new MetricRegistry();
    /**
     * 在日志上打印输出
     */
    private static Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics).build();
    private static ConcurrentHashMap<String, Meter> meterMap = new ConcurrentHashMap<>();
    
    public static void handleRequest(String key) {
    	Meter meter = meterMap.get(key);
    	if (meter == null) {
			meter = metrics.meter(key);
			meterMap.put(key, meter);
		}
    	meter.mark();
    }
    
    public static void stopHandleRequest(String key){
    	Meter meter = meterMap.get(key);
    	if (meter != null) {
    		metrics.remove(key);
		}
    }
    
    public static void stopHandleRequestOnCount(String key,long count){
    	Meter meter = meterMap.get(key);
    	if (meter != null) {
    		while(meter.getCount()<count){
    			try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    		try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		metrics.remove(key);
    	}
    }
    
    static {
        reporter.start(5, TimeUnit.SECONDS);
    }
}
