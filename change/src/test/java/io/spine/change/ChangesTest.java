/*
 * Copyright 2022, TeamDev. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.change;

import com.google.common.testing.NullPointerTester;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import io.spine.base.Time;
import io.spine.testing.UtilityClassTest;
import io.spine.time.testing.Past;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static io.spine.base.Time.currentTime;
import static io.spine.testing.TestValues.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"ConstantConditions" /* We pass `null` to some methods to check handling
                                        of preconditions */,
                   "ResultOfMethodCallIgnored" /* ...when methods throw exceptions */,
                   "OverlyCoupledClass" /* we test many data types and utility methods */})
@DisplayName("`Changes` utility should")
class ChangesTest extends UtilityClassTest<Changes> {

    ChangesTest() {
        super(Changes.class);
    }

    @Override
    protected void configure(NullPointerTester tester) {
        super.configure(tester);
        tester.setDefault(ByteString.class, ByteString.EMPTY)
               .setDefault(Timestamp.class, Time.currentTime());
    }

    @Nested
    @DisplayName("create value change for values of type")
    class Create {

        @Test
        @DisplayName("`String`")
        void forStrings() {
            var previousValue = randomUuid();
            var newValue = randomUuid();

            var result = Changes.of(previousValue, newValue);

            assertEquals(previousValue, result.getPreviousValue());
            assertEquals(newValue, result.getNewValue());
        }

        @Test
        @DisplayName("`ByteString`")
        void forByteStrings() {
            var previousValue = copyFromUtf8(randomUuid());
            var newValue = copyFromUtf8(randomUuid());

            var result = Changes.of(previousValue, newValue);

            assertEquals(previousValue, result.getPreviousValue());
            assertEquals(newValue, result.getNewValue());
        }

        @Test
        @DisplayName("`Timestamp`")
        void forTimestamps() {
            var fiveMinutesAgo = Past.minutesAgo(5);
            var now = currentTime();

            var result = Changes.of(fiveMinutesAgo, now);

            assertEquals(fiveMinutesAgo, result.getPreviousValue());
            assertEquals(now, result.getNewValue());
        }

        @Test
        @DisplayName("`boolean`")
        void forBooleans() {
            var s1 = true;
            var s2 = false;

            var result = Changes.of(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`double`")
        void forDoubles() {
            var s1 = 1957.1004;
            var s2 = 1957.1103;

            var result = Changes.of(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`int32`")
        void forInt32s() {
            var s1 = 1550;
            var s2 = 1616;

            var result = Changes.ofInt32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`int64`")
        void forInt64s() {
            var s1 = 16420225L;
            var s2 = 17270320L;

            var result = Changes.ofInt64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`float`")
        void forFloats() {
            var s1 = 1473.0219f;
            var s2 = 1543.0524f;

            var result = Changes.of(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`uint32`")
        void forUint32s() {
            var s1 = 16440925;
            var s2 = 17100919;

            var result = Changes.ofUInt32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`uint64`")
        void forUint64s() {
            var s1 = 16290414L;
            var s2 = 16950708L;

            var result = Changes.ofUInt64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`sint32`")
        void forSint32s() {
            var s1 = 16550106;
            var s2 = 17050816;

            var result = Changes.ofSInt32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`sint64`")
        void forSint64s() {
            var s1 = 1666L;
            var s2 = 1736L;

            var result = Changes.ofSInt64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`fixed32`")
        void forFixed32s() {
            var s1 = 17070415;
            var s2 = 17830918;

            var result = Changes.ofFixed32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`fixed64`")
        void forFixed64s() {
            var s1 = 17240422L;
            var s2 = 18040212L;

            var result = Changes.ofFixed64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`sfixed32`")
        void forSfixed32s() {
            var s1 = 1550;
            var s2 = 1616;

            var result = Changes.ofSfixed32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("`sfixed64`")
        void forSfixed64s() {
            var s1 = 16420225L;
            var s2 = 17270320L;

            var result = Changes.ofSfixed64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        private String randomUuid() {
            return UUID.randomUUID()
                       .toString();
        }
    }

    @Nested
    @DisplayName("fail to create value change for equal values of type")
    class NotAcceptEqual {

        @Test
        @DisplayName("`String`")
        void strings() {
            var value = randomString();
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("`ByteString`")
        void byteStrings() {
            var value = copyFromUtf8(randomString());
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("`Timestamp`")
        void timestamps() {
            var now = currentTime();
            assertThrows(IllegalArgumentException.class, () -> Changes.of(now, now));
        }

        @Test
        @DisplayName("`boolean`")
        void booleans() {
            var value = true;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("`double`")
        void doubles() {
            var value = 1961.0412;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("`float`")
        void floats() {
            var value = 1543.0f;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("`int32`")
        void int32s() {
            var value = 1614;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofInt32(value, value));
        }

        @Test
        @DisplayName("`uint32`")
        void uint32s() {
            var value = 1776;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofUInt32(value, value));
        }

        @Test
        @DisplayName("`sint32`")
        void sint32s() {
            var value = 1694;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSInt32(value, value));
        }

        @Test
        @DisplayName("`fixed32`")
        void fixed32s() {
            var value = 1736;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofFixed32(value, value));
        }

        @Test
        @DisplayName("`int64`")
        void int64s() {
            var value = 1666L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofInt64(value, value));
        }

        @Test
        @DisplayName("`uint64`")
        void uint64s() {
            var value = 1690L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofUInt64(value, value));
        }

        @Test
        @DisplayName("`sint64`")
        void sint64s() {
            var value = 1729L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSInt64(value, value));
        }

        @Test
        @DisplayName("`fixed64`")
        void fixed64s() {
            var value = 1755L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofFixed64(value, value));
        }

        @Test
        @DisplayName("`sfixed32`")
        void sfixed32s() {
            var value = 1614;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSfixed32(value, value));
        }

        @Test
        @DisplayName("`sfixed64`")
        void sfixed64s() {
            var value = 1666L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSfixed64(value, value));
        }
    }

    @Nested
    @DisplayName("fail to create value change from `null`")
    class NotAcceptNull {

        @Test
        @DisplayName("`String` `previousValue`")
        void stringPrevious() {
            assertThrowsNpe(() -> Changes.of(null, randomString()));
        }

        @Test
        @DisplayName("`String` `newValue`")
        void stringNew() {
            assertThrowsNpe(() -> Changes.of(randomString(), null));
        }

        @Test
        @DisplayName("`ByteString` `previousValue`")
        void byteStringPrevious() {
            assertThrowsNpe(() -> Changes.of(null, copyFromUtf8(randomString())));
        }

        @Test
        @DisplayName("`ByteString` `newValue`")
        void byteStringNew() {
            assertThrowsNpe(() -> Changes.of(copyFromUtf8(randomString()), null));
        }

        @Test
        @DisplayName("`Timestamp` `previousValue`")
        void timestampPrevious() {
            assertThrowsNpe(() -> Changes.of(null, currentTime()));
        }

        @Test
        @DisplayName("`Timestamp` `newValue`")
        void timestampNew() {
            assertThrowsNpe(() -> Changes.of(currentTime(), null));
        }

        void assertThrowsNpe(Executable executable) {
            assertThrows(NullPointerException.class, executable);
        }
    }
}
