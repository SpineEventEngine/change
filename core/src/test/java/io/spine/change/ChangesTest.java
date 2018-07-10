/*
 * Copyright 2018, TeamDev. All rights reserved.
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
import io.spine.test.TimeTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static io.spine.base.Time.getCurrentTime;
import static io.spine.test.DisplayNames.HAVE_PARAMETERLESS_CTOR;
import static io.spine.test.DisplayNames.NOT_ACCEPT_NULLS;
import static io.spine.test.Tests.assertHasPrivateParameterlessCtor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"ConstantConditions" /* We pass `null` to some of the methods to check handling
                                        of preconditions */,
                   "ResultOfMethodCallIgnored" /* ...when methods throw exceptions */,
                   "ClassWithTooManyMethods",
                   "OverlyCoupledClass" /* we test many data types and utility methods */,
                   "InnerClassMayBeStatic" /* JUnit nested classes cannot be static */,
                   "DuplicateStringLiteralInspection" /* A lot of similar test display names */})
@DisplayName("Changes utility should")
class ChangesTest {

    private static final String ERR_PREVIOUS_VALUE_CANNOT_BE_NULL =
            "do_not_accept_null_previousValue";
    private static final String ERR_NEW_VALUE_CANNOT_BE_NULL =
            "do_not_accept_null_newValue";
    private static final String ERR_VALUES_CANNOT_BE_EQUAL =
            "do_not_accept_equal_values";

    @Test
    @DisplayName(HAVE_PARAMETERLESS_CTOR)
    void haveUtilityConstructor() {
        assertHasPrivateParameterlessCtor(Changes.class);
    }

    @Test
    @DisplayName(NOT_ACCEPT_NULLS)
    void passNullToleranceCheck() {
        new NullPointerTester()
                .setDefault(ByteString.class, ByteString.EMPTY)
                .setDefault(Timestamp.class, Time.getCurrentTime())
                .testAllPublicStaticMethods(Changes.class);
    }

    @Nested
    @DisplayName("create value change for values of type")
    class Create {

        @Test
        @DisplayName("String")
        void forStrings() {
            final String previousValue = randomUuid();
            final String newValue = randomUuid();

            final StringChange result = Changes.of(previousValue, newValue);

            assertEquals(previousValue, result.getPreviousValue());
            assertEquals(newValue, result.getNewValue());
        }

        @Test
        @DisplayName("ByteString")
        void forByteStrings() {
            final ByteString previousValue = copyFromUtf8(randomUuid());
            final ByteString newValue = copyFromUtf8(randomUuid());

            final BytesChange result = Changes.of(previousValue, newValue);

            assertEquals(previousValue, result.getPreviousValue());
            assertEquals(newValue, result.getNewValue());
        }

        @Test
        @DisplayName("Timestamp")
        void forTimestamps() {
            final Timestamp fiveMinutesAgo = TimeTests.Past.minutesAgo(5);
            final Timestamp now = getCurrentTime();

            final TimestampChange result = Changes.of(fiveMinutesAgo, now);

            assertEquals(fiveMinutesAgo, result.getPreviousValue());
            assertEquals(now, result.getNewValue());
        }

        @Test
        @DisplayName("boolean")
        void forBooleans() {
            final boolean s1 = true;
            final boolean s2 = false;

            final BooleanChange result = Changes.of(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("double")
        void forDoubles() {
            final double s1 = 1957.1004;
            final double s2 = 1957.1103;

            final DoubleChange result = Changes.of(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("int32")
        void forInt32s() {
            final int s1 = 1550;
            final int s2 = 1616;

            final Int32Change result = Changes.ofInt32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("int64")
        void forInt64s() {
            final long s1 = 16420225L;
            final long s2 = 17270320L;

            final Int64Change result = Changes.ofInt64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("float")
        void forFloats() {
            final float s1 = 1473.0219f;
            final float s2 = 1543.0524f;

            final FloatChange result = Changes.of(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("uint32")
        void forUint32s() {
            final int s1 = 16440925;
            final int s2 = 17100919;

            final UInt32Change result = Changes.ofUInt32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("uint64")
        void forUint64s() {
            final long s1 = 16290414L;
            final long s2 = 16950708L;

            final UInt64Change result = Changes.ofUInt64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("sint32")
        void forSint32s() {
            final int s1 = 16550106;
            final int s2 = 17050816;

            final SInt32Change result = Changes.ofSInt32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("sint64")
        void forSint64s() {
            final long s1 = 1666L;
            final long s2 = 1736L;

            final SInt64Change result = Changes.ofSInt64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("fixed32")
        void forFixed32s() {
            final int s1 = 17070415;
            final int s2 = 17830918;

            final Fixed32Change result = Changes.ofFixed32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("fixed64")
        void forFixed64s() {
            final long s1 = 17240422L;
            final long s2 = 18040212L;

            final Fixed64Change result = Changes.ofFixed64(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("sfixed32")
        void forSfixed32s() {
            final int s1 = 1550;
            final int s2 = 1616;

            final Sfixed32Change result = Changes.ofSfixed32(s1, s2);

            assertEquals(s1, result.getPreviousValue());
            assertEquals(s2, result.getNewValue());
        }

        @Test
        @DisplayName("sfixed64")
        void forSfixed64s() {
            final long s1 = 16420225L;
            final long s2 = 17270320L;

            final Sfixed64Change result = Changes.ofSfixed64(s1, s2);

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
        @DisplayName("String")
        void strings() {
            final String value = ERR_VALUES_CANNOT_BE_EQUAL;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("ByteString")
        void byteStrings() {
            final ByteString value = copyFromUtf8(ERR_VALUES_CANNOT_BE_EQUAL);
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("TimeStamp")
        void timestamps() {
            final Timestamp now = getCurrentTime();
            assertThrows(IllegalArgumentException.class, () -> Changes.of(now, now));
        }

        @Test
        @DisplayName("boolean")
        void booleans() {
            final boolean value = true;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("double")
        void doubles() {
            final double value = 1961.0412;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("float")
        void floats() {
            final float value = 1543.0f;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("int32")
        void int32s() {
            final int value = 1614;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("int64")
        void int64s() {
            final long value = 1666L;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("uint32")
        void uint32s() {
            final int value = 1776;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofUInt32(value, value));
        }

        @Test
        @DisplayName("uint64")
        void uint64s() {
            final long value = 1690L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofUInt64(value, value));
        }

        @Test
        @DisplayName("sint32")
        void sint32s() {
            final int value = 1694;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSInt32(value, value));
        }

        @Test
        @DisplayName("sint64")
        void sint64s() {
            final long value = 1729L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSInt64(value, value));
        }

        @Test
        @DisplayName("fixed32")
        void fixed32s() {
            final int value = 1736;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofFixed32(value, value));
        }

        @Test
        @DisplayName("fixed64")
        void fixed64s() {
            final long value = 1755L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofFixed64(value, value));
        }

        @Test
        @DisplayName("sfixed32")
        void sfixed32s() {
            final int value = 1614;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSfixed32(value, value));
        }

        @Test
        @DisplayName("sfixed64")
        void sfixed64s() {
            final long value = 1666L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSfixed64(value, value));
        }
    }

    @Nested
    @DisplayName("fail to create value change from null")
    class NotAcceptNull {

        @Test
        @DisplayName("String previousValue")
        void stringPrevious() {
            assertThrows(NullPointerException.class,
                         () -> Changes.of(null, ERR_PREVIOUS_VALUE_CANNOT_BE_NULL));
        }

        @Test
        @DisplayName("String newValue")
        void stringNew() {
            assertThrows(NullPointerException.class,
                         () -> Changes.of(ERR_NEW_VALUE_CANNOT_BE_NULL, null));
        }

        @Test
        @DisplayName("ByteString previousValue")
        void byteStringPrevious() {
            assertThrows(NullPointerException.class,
                         () -> Changes.of(null,
                                          copyFromUtf8(ERR_PREVIOUS_VALUE_CANNOT_BE_NULL)));
        }

        @Test
        @DisplayName("ByteString newValue")
        void byteStringNew() {
            assertThrows(NullPointerException.class,
                         () -> Changes.of(copyFromUtf8(ERR_NEW_VALUE_CANNOT_BE_NULL), null));
        }

        @Test
        @DisplayName("Timestamp previousValue")
        void timestampPrevious() {
            assertThrows(NullPointerException.class, () -> Changes.of(null, getCurrentTime()));
        }

        @Test
        @DisplayName("Timestamp newValue")
        void timestampNew() {
            assertThrows(NullPointerException.class, () -> Changes.of(getCurrentTime(), null));
        }
    }
}
