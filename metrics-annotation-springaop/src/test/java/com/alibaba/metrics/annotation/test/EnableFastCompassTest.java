package com.alibaba.metrics.annotation.test;

import com.alibaba.metrics.FastCompass;
import com.alibaba.metrics.MetricManager;
import com.alibaba.metrics.MetricName;
import com.alibaba.metrics.annotation.MetricsAnnotationInterceptor;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MetricsAnnotationInterceptor.class, TestConfig.class })
public class EnableFastCompassTest {

    @Autowired
    private MetricsAnnotationTestService metricsTestService;

    @Test
    public void test() {
        this.metricsTestService.testFastCompass1();

        FastCompass fastCompass = MetricManager.getFastCompass("test",
            MetricName.build("ascp.upcp-scitem.metrics-annotation.fastCompass.test1")
                .tagged("purpose", "test"));

        Map<String, Map<Long, Long>> countAndRtPerCategory = fastCompass.getCountAndRtPerCategory();
        Map<Long, Long> success = countAndRtPerCategory.get("success");
        TestCase.assertNotNull(success);

        Map<Long, Long> exception = countAndRtPerCategory.get("exception");
        TestCase.assertNull(exception);

        try {
            this.metricsTestService.testFastCompass1();
            this.metricsTestService.testFastCompass2();
            TestCase.fail();
        } catch (Exception e) {
            //ignore
        }

        countAndRtPerCategory = fastCompass.getCountAndRtPerCategory();
        success = countAndRtPerCategory.get("success");
        TestCase.assertNotNull(success);

        exception = countAndRtPerCategory.get("exception");
        TestCase.assertNotNull(exception);
    }
}
