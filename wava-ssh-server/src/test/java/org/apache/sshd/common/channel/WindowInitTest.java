/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sshd.common.channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.sshd.client.future.OpenFuture;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.util.buffer.Buffer;
import org.apache.sshd.common.util.buffer.BufferUtils;
import org.apache.sshd.util.test.BaseTestSupport;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)   // see https://github.com/junit-team/junit/wiki/Parameterized-tests
public class WindowInitTest extends BaseTestSupport {
    private static final AbstractChannel MOCK_CHANNEL = new AbstractChannel(true) {
        @Override
        public OpenFuture open(int recipient, long rwSize, long packetSize, Buffer buffer) {
            return null;
        }

        @Override
        public void handleOpenSuccess(int recipient, long rwSize, long packetSize, Buffer buffer) throws IOException {
            // ignored
        }

        @Override
        public void handleOpenFailure(Buffer buffer) throws IOException {
            // ignored
        }

        @Override
        protected void doWriteData(byte[] data, int off, long len) throws IOException {
            // ignored
        }

        @Override
        protected void doWriteExtendedData(byte[] data, int off, long len) throws IOException {
            // ignored
        }
    };

    private long initialSize;
    private long packetSize;

    public WindowInitTest(long initialSize, long packetSize) {
        this.initialSize = initialSize;
        this.packetSize = packetSize;
    }

    @Parameters(name = "initial-size={0}, packet-size={1}")
    public static List<Object[]> parameters() {
        return Collections.unmodifiableList(new ArrayList<Object[]>() {
            // Not serializing it
            private static final long serialVersionUID = 1L;

            {
                addTestCase(Byte.MIN_VALUE, FactoryManager.DEFAULT_MAX_PACKET_SIZE);
                addTestCase(BufferUtils.MAX_UINT32_VALUE + 1L, FactoryManager.DEFAULT_MAX_PACKET_SIZE);
                addTestCase(FactoryManager.DEFAULT_WINDOW_SIZE, 0L);
                addTestCase(FactoryManager.DEFAULT_WINDOW_SIZE, Byte.MIN_VALUE);
                addTestCase(FactoryManager.DEFAULT_WINDOW_SIZE, BufferUtils.MAX_UINT32_VALUE + 1L);
                addTestCase(FactoryManager.DEFAULT_WINDOW_SIZE, FactoryManager.DEFAULT_LIMIT_PACKET_SIZE + 1L);
            }

            private void addTestCase(long initialSize, long packetSize) {
                add(new Object[]{initialSize, packetSize});
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationFailure() throws IOException {
        try (Window w = new Window(MOCK_CHANNEL, null, true, true)) {
            w.init(initialSize, packetSize, Collections.<String, Object>emptyMap());
            fail("Unexpected success for initialiSize=" + initialSize + ", packetSize=" + packetSize);
        }
    }
}
