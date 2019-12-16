/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2018 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.pscanrulesBeta;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

/**
 * Unit test for {@link PiiScanner}.
 *
 * @see PiiScannerCreditCardUnitTest
 */
public class PiiScannerUnitTest extends PassiveScannerTest<PiiScanner> {

    @Override
    protected PiiScanner createScanner() {
        return new PiiScanner();
    }

    @Test
    public void shouldNotFailWithStackOverflowErrorWhenScanningResponseWithManyNumbers()
            throws Exception {
        // Given
        HttpMessage msg = new HttpMessage();
        msg.setRequestHeader("GET https://www.example.com/test/ HTTP/1.1");
        msg.setResponseBody(numbers(15000));
        // When
        rule.scanHttpResponseReceive(msg, -1, this.createSource(msg));
        // Then = No StackOverflowError
    }

    @Test
    public void shouldNotRaiseAlertWhenNumberDoesntHaveWordBoundaries() throws Exception {
        // Given
        String cardNumber = "8.46786664623715e-47";
        HttpMessage msg = createMsg(cardNumber);
        // When
        rule.scanHttpResponseReceive(msg, -1, this.createSource(msg));
        // Then
        assertThat(alertsRaised.size(), is(0));
    }

    private HttpMessage createMsg(String cardNumber) throws HttpMalformedHeaderException {
        HttpMessage msg = new HttpMessage();
        msg.setRequestHeader("GET https://www.example.com/test/ HTTP/1.1");
        msg.setResponseHeader("HTTP/1.1 200 OK\r\n" + "Server: Apache-Coyote/1.1\r\n");
        msg.setResponseBody("{\"cc\": \"" + cardNumber + "\"}");
        return msg;
    }

    private static String numbers(int count) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 1; i <= count; i++) {
            strBuilder.append(i).append('\n');
        }
        return strBuilder.toString();
    }
}
