/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.metrics;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SharedMetricRegistriesTest {
    @Before
    public void setUp() throws Exception {
        SharedMetricRegistries.clear();
    }

    @Test
    public void memoizesRegistriesByName() throws Exception {
        final MetricRegistry one = SharedMetricRegistries.getOrCreate("one");
        final MetricRegistry two = SharedMetricRegistries.getOrCreate("one");

        assertThat(one)
                .isSameAs(two);
    }

    @Test
    public void hasASetOfNames() throws Exception {
        SharedMetricRegistries.getOrCreate("one");

        assertThat(SharedMetricRegistries.names())
                .containsOnly("one");
    }

    @Test
    public void removesRegistries() throws Exception {
        final MetricRegistry one = SharedMetricRegistries.getOrCreate("one");
        SharedMetricRegistries.remove("one");

        assertThat(SharedMetricRegistries.names())
                .isEmpty();

        final MetricRegistry two = SharedMetricRegistries.getOrCreate("one");
        assertThat(two)
                .isNotSameAs(one);
    }

    @Test
    public void clearsRegistries() throws Exception {
        SharedMetricRegistries.getOrCreate("one");
        SharedMetricRegistries.getOrCreate("two");

        SharedMetricRegistries.clear();

        assertThat(SharedMetricRegistries.names())
                .isEmpty();
    }
}
