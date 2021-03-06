/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.jstorm.common.metric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.jstorm.daemon.nimbus.metric.ClusterMetricsContext;
import com.alibaba.jstorm.metric.MetaType;
import com.alibaba.jstorm.metric.MetricType;
import com.alibaba.jstorm.metric.MetricUtils;

/**
 * @author wange
 * @since 15/7/14
 */
public class MetricMetaParser {
    private static final Logger logger = LoggerFactory.getLogger(ClusterMetricsContext.class);

    public static MetricMeta fromMetricName(String name) {
        try {
            String[] parts = name.split(MetricUtils.DELIM);
            char ch = parts[0].charAt(0);
            if (ch == 'W' || ch == 'N' || ch == 'P') {
                return parseWorkerMetricMeta(parts);
            } else {
                return parseTaskMetricMeta(parts);
            }
        } catch (Exception ex) {
            logger.error("Error parsing metric meta, name:{}", name, ex);
        }
        return null;
    }

    private static MetricMeta parseTaskMetricMeta(String[] parts) {
        MetricMeta meta = new MetricMeta();
        meta.setMetaType(MetaType.parse(parts[0].charAt(0)).getT());
        meta.setMetricType(MetricType.parse(parts[0].charAt(1)).getT());
        meta.setTopologyId(parts[1]);
        meta.setComponent(parts[2]);
        meta.setTaskId(Integer.valueOf(parts[3]));
        meta.setStreamId(parts[4]);
        meta.setMetricGroup(parts[5]);
        meta.setMetricName(parts[6]);

        return meta;
    }

    private static MetricMeta parseWorkerMetricMeta(String[] parts) {
        MetricMeta meta = new MetricMeta();
        meta.setMetaType(MetaType.parse(parts[0].charAt(0)).getT());
        meta.setMetricType(MetricType.parse(parts[0].charAt(1)).getT());
        meta.setTopologyId(parts[1]);
        meta.setHost(parts[2]);
        meta.setPort(Integer.valueOf(parts[3]));
        meta.setMetricGroup(parts[4]);
        meta.setMetricName(parts[5]);

        return meta;
    }
}
