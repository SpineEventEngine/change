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
import io.spine.time.Interval;
import io.spine.time.Intervals;
import io.spine.time.LocalDate;
import io.spine.time.LocalDates;
import io.spine.time.LocalTime;
import io.spine.time.LocalTimes;
import io.spine.time.OffsetDate;
import io.spine.time.OffsetDateTime;
import io.spine.time.OffsetDateTimes;
import io.spine.time.OffsetDates;
import io.spine.time.OffsetTime;
import io.spine.time.OffsetTimes;
import io.spine.time.ZoneOffset;
import io.spine.time.ZoneOffsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static com.google.protobuf.util.Timestamps.subtract;
import static io.spine.base.Time.getCurrentTime;
import static io.spine.test.DisplayNames.HAVE_PARAMETERLESS_CTOR;
import static io.spine.test.DisplayNames.NOT_ACCEPT_NULLS;
import static io.spine.test.Tests.assertHasPrivateParameterlessCtor;
import static io.spine.time.Durations2.minutes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"ConstantConditions" /* We pass `null` to some of the methods to check handling
                                        of preconditions */,
                   "ResultOfMethodCallIgnored" /* ...when methods throw exceptions */,
                   "ClassWithTooManyMethods",
                   "OverlyCoupledClass" /* we test many data types and utility methods */,
                   "InnerClassMayBeStatic" /* JUnit 5 Nested classes cannot be static */,
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
                .setDefault(OffsetTime.class, OffsetTimes.now(ZoneOffsets.UTC))
                .setDefault(OffsetDate.class, OffsetDates.now(ZoneOffsets.UTC))
                .setDefault(OffsetDateTime.class, OffsetDateTimes.now(ZoneOffsets.UTC))
                .setDefault(LocalDate.class, LocalDates.now())
                .setDefault(LocalTime.class, LocalTimes.now())
                .setDefault(Interval.class,
                            Intervals.between(subtract(getCurrentTime(), minutes(1)),
                                              getCurrentTime()))
                .testAllPublicStaticMethods(Changes.class);
    }

    @Nested
    @DisplayName("create value change for values of type")
    class CreateChangeTest {

        @Test
        @DisplayName("String")
        void createStringChange() {
            final String previousValue = randomUuid();
            final String newValue = randomUuid();

            final StringChange result = Changes.of(previousValue, newValue);

            assertEquals(previousValue, result.getPreviousValue());
            assertEquals(newValue, result.getNewValue());
        }

        @Test
        @DisplayName("ByteString")
        void createByteStringChange() {
            final ByteString previousValue = copyFromUtf8(randomUuid());
            final ByteString newValue = copyFromUtf8(randomUuid());

            final BytesChange result = Changes.of(previousValue, newValue);

            assertEquals(previousValue, result.getPreviousValue());
            assertEquals(newValue, result.getNewValue());
        }

        @Test
        @DisplayName("Timestamp")
        void createTimestampChange() {
            final Timestamp fiveMinutesAgo = TimeTests.Past.minutesAgo(5);
            final Timestamp now = getCurrentTime();

            final TimestampChange result = Changes.of(fiveMinutesAgo, now);

            assertEquals(fiveMinutesAgo, result.getPreviousValue());
            assertEquals(now, result.getNewValue());
        }

        @Test
        @DisplayName("boolean")
        void createBooleanChange() {
            final boolean s1 = true;
            final boolean s2 = false;

            final BooleanChange result = Changes.of(s1, s2);

            assertTrue(Boolean.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Boolean.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("double")
        void createDoubleChange() {
            final double s1 = 1957.1004;
            final double s2 = 1957.1103;

            final DoubleChange result = Changes.of(s1, s2);

            assertTrue(Double.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Double.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("int32")
        void createInt32Change() {
            final int s1 = 1550;
            final int s2 = 1616;

            final Int32Change result = Changes.ofInt32(s1, s2);

            assertTrue(Integer.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Integer.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("int64")
        void createInt64Change() {
            final long s1 = 16420225L;
            final long s2 = 17270320L;

            final Int64Change result = Changes.ofInt64(s1, s2);

            assertTrue(Long.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Long.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("float")
        void createFloatChange() {
            final float s1 = 1473.0219f;
            final float s2 = 1543.0524f;

            final FloatChange result = Changes.of(s1, s2);

            assertTrue(Float.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Float.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("uint32")
        void createUint32Change() {
            final int s1 = 16440925;
            final int s2 = 17100919;

            final UInt32Change result = Changes.ofUInt32(s1, s2);

            assertTrue(Integer.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Integer.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("uint64")
        void createUint64Change() {
            final long s1 = 16290414L;
            final long s2 = 16950708L;

            final UInt64Change result = Changes.ofUInt64(s1, s2);

            assertTrue(Long.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Long.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("sint32")
        void createSint32Change() {
            final int s1 = 16550106;
            final int s2 = 17050816;

            final SInt32Change result = Changes.ofSInt32(s1, s2);

            assertTrue(Integer.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Integer.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("sint64")
        void createSint64Change() {
            final long s1 = 1666L;
            final long s2 = 1736L;

            final SInt64Change result = Changes.ofSInt64(s1, s2);

            assertTrue(Long.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Long.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("fixed32")
        void createFixed32Change() {
            final int s1 = 17070415;
            final int s2 = 17830918;

            final Fixed32Change result = Changes.ofFixed32(s1, s2);

            assertTrue(Integer.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Integer.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("fixed64")
        void createFixed64Change() {
            final long s1 = 17240422L;
            final long s2 = 18040212L;

            final Fixed64Change result = Changes.ofFixed64(s1, s2);

            assertTrue(Long.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Long.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("sfixed32")
        void createSfixed32Change() {
            final int s1 = 1550;
            final int s2 = 1616;

            final Sfixed32Change result = Changes.ofSfixed32(s1, s2);

            assertTrue(Integer.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Integer.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("sfixed64")
        void createSfixed64Change() {
            final long s1 = 16420225L;
            final long s2 = 17270320L;

            final Sfixed64Change result = Changes.ofSfixed64(s1, s2);

            assertTrue(Long.compare(s1, result.getPreviousValue()) == 0);
            assertTrue(Long.compare(s2, result.getNewValue()) == 0);
        }

        @Test
        @DisplayName("Interval")
        void createIntervalChange() {
            final Timestamp fiveMinutesAgo = TimeTests.Past.minutesAgo(5);
            final Timestamp fourMinutesAgo = TimeTests.Past.minutesAgo(4);
            final Timestamp now = getCurrentTime();
            final Interval fourMinutes = Intervals.between(fourMinutesAgo, now);
            final Interval fiveMinutes = Intervals.between(fiveMinutesAgo, now);

            final IntervalChange result = Changes.of(fourMinutes, fiveMinutes);

            assertEquals(fourMinutes, result.getPreviousValue());
            assertEquals(fiveMinutes, result.getNewValue());
        }

        @Test
        @DisplayName("LocalDate")
        void createLocalDateChange() {
            final LocalDate today = LocalDates.now();
            final LocalDate tomorrow = LocalDates.addDays(today, 1);

            final LocalDateChange result = Changes.of(today, tomorrow);

            assertEquals(today, result.getPreviousValue());
            assertEquals(tomorrow, result.getNewValue());
        }

        @Test
        @DisplayName("LocalTime")
        void createLocalTimeChange() {
            final LocalTime now = LocalTimes.now();
            final LocalTime inFiveHours = LocalTimes.addHours(now, 5);

            final LocalTimeChange result = Changes.of(now, inFiveHours);

            assertEquals(now, result.getPreviousValue());
            assertEquals(inFiveHours, result.getNewValue());
        }

        @Test
        @DisplayName("OffsetDate")
        void createOffsetDateChange() {
            final ZoneOffset inKiev = ZoneOffsets.ofHours(3);
            final ZoneOffset inLuxembourg = ZoneOffsets.ofHours(1);
            final OffsetDate previousDate = OffsetDates.now(inKiev);
            final OffsetDate newDate = OffsetDates.now(inLuxembourg);

            final OffsetDateChange result = Changes.of(previousDate, newDate);

            assertEquals(previousDate, result.getPreviousValue());
            assertEquals(newDate, result.getNewValue());
        }

        @Test
        @DisplayName("OffsetTime")
        void createOffsetTimeChange() {
            final ZoneOffset inKiev = ZoneOffsets.ofHours(3);
            final ZoneOffset inLuxembourg = ZoneOffsets.ofHours(1);
            final OffsetTime previousTime = OffsetTimes.now(inKiev);
            final OffsetTime newTime = OffsetTimes.now(inLuxembourg);

            final OffsetTimeChange result = Changes.of(previousTime, newTime);

            assertEquals(previousTime, result.getPreviousValue());
            assertEquals(newTime, result.getNewValue());
        }

        @Test
        @DisplayName("OffsetDateTime")
        void createOffsetDateTimeChange() {
            final ZoneOffset inKiev = ZoneOffsets.ofHours(3);
            final ZoneOffset inLuxembourg = ZoneOffsets.ofHours(1);
            final OffsetDateTime previousDateTime = OffsetDateTimes.now(inKiev);
            final OffsetDateTime newDateTime = OffsetDateTimes.now(inLuxembourg);

            final OffsetDateTimeChange result = Changes.of(previousDateTime, newDateTime);

            assertEquals(previousDateTime, result.getPreviousValue());
            assertEquals(newDateTime, result.getNewValue());
        }

        private String randomUuid() {
            return UUID.randomUUID()
                       .toString();
        }
    }

    @Nested
    @DisplayName("fail to create value change for equal values of type")
    class CreateFromEqualTest {

        @Test
        @DisplayName("String")
        void notAcceptEqualStrings() {
            final String value = ERR_VALUES_CANNOT_BE_EQUAL;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("ByteString")
        void notAcceptEqualByteStrings() {
            final ByteString value = copyFromUtf8(ERR_VALUES_CANNOT_BE_EQUAL);
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("TimeStamp")
        void notAcceptEqualTimestamps() {
            final Timestamp now = getCurrentTime();
            assertThrows(IllegalArgumentException.class, () -> Changes.of(now, now));
        }

        @Test
        @DisplayName("boolean")
        void notAcceptEqualBooleans() {
            final boolean value = true;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("double")
        void notAcceptEqualDoubles() {
            final double value = 1961.0412;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("float")
        void notAcceptEqualFloats() {
            final float value = 1543.0f;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("int32")
        void notAcceptEqualInt32() {
            final int value = 1614;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("int64")
        void notAcceptEqualInt64() {
            final long value = 1666L;
            assertThrows(IllegalArgumentException.class, () -> Changes.of(value, value));
        }

        @Test
        @DisplayName("uint32")
        void notAcceptEqualUint32() {
            final int value = 1776;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofUInt32(value, value));
        }

        @Test
        @DisplayName("uint64")
        void notAcceptEqualUint64() {
            final long value = 1690L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofUInt64(value, value));
        }

        @Test
        @DisplayName("sint32")
        void notAcceptEqualSint32() {
            final int value = 1694;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSInt32(value, value));
        }

        @Test
        @DisplayName("sint64")
        void notAcceptEqualSint64() {
            final long value = 1729L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSInt64(value, value));
        }

        @Test
        @DisplayName("fixed32")
        void notAcceptEqualFixed32() {
            final int value = 1736;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofFixed32(value, value));
        }

        @Test
        @DisplayName("fixed64")
        void notAcceptEqualFixed64() {
            final long value = 1755L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofFixed64(value, value));
        }

        @Test
        @DisplayName("sfixed32")
        void notAcceptEqualSfixed32() {
            final int value = 1614;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSfixed32(value, value));
        }

        @Test
        @DisplayName("sfixed64")
        void notAcceptEqualSfixed64() {
            final long value = 1666L;
            assertThrows(IllegalArgumentException.class, () -> Changes.ofSfixed64(value, value));
        }

        @Test
        @DisplayName("Interval")
        void notAcceptEqualIntervals() {
            final Timestamp fourMinutesAgo = TimeTests.Past.minutesAgo(4);
            final Timestamp now = getCurrentTime();
            final Interval interval = Intervals.between(fourMinutesAgo, now);
            assertThrows(IllegalArgumentException.class, () -> Changes.of(interval, interval));
        }

        @Test
        @DisplayName("LocalDate")
        void notAcceptEqualLocalDates() {
            final LocalDate today = LocalDates.now();
            assertThrows(IllegalArgumentException.class, () -> Changes.of(today, today));
        }

        @Test
        @DisplayName("LocalTime")
        void notAcceptEqualLocalTimes() {
            final LocalTime now = LocalTimes.now();
            assertThrows(IllegalArgumentException.class, () -> Changes.of(now, now));
        }

        @Test
        @DisplayName("OffsetDate")
        void notAcceptEqualOffsetDate() {
            final ZoneOffset inLuxembourg = ZoneOffsets.ofHours(1);
            final OffsetDate date = OffsetDates.now(inLuxembourg);
            assertThrows(IllegalArgumentException.class, () -> Changes.of(date, date));
        }

        @Test
        @DisplayName("OffsetTime")
        void notAcceptEqualOffsetTimes() {
            final ZoneOffset inLuxembourg = ZoneOffsets.ofHours(1);
            final OffsetTime now = OffsetTimes.now(inLuxembourg);
            assertThrows(IllegalArgumentException.class, () -> Changes.of(now, now));
        }

        @Test
        @DisplayName("OffsetDateTime")
        void notAcceptEqualOffsetDateTimes() {
            final ZoneOffset inLuxembourg = ZoneOffsets.ofHours(1);
            final OffsetDateTime now = OffsetDateTimes.now(inLuxembourg);
            assertThrows(IllegalArgumentException.class, () -> Changes.of(now, now));
        }
    }

    @Nested
    @DisplayName("fail to create value change from null")
    class CreateFromNullTest {

        @Test
        @DisplayName("String previousValue")
        void notAcceptNullStringPrevious() {
            assertThrows(NullPointerException.class,
                         () -> Changes.of(null, ERR_PREVIOUS_VALUE_CANNOT_BE_NULL));
        }

        @Test
        @DisplayName("String newValue")
        void notAcceptNullStringNew() {
            assertThrows(NullPointerException.class,
                         () -> Changes.of(ERR_NEW_VALUE_CANNOT_BE_NULL, null));
        }

        @Test
        @DisplayName("ByteString previousValue")
        void notAcceptNullByteStringPrevious() {
            assertThrows(NullPointerException.class,
                         () -> Changes.of(null,
                                          copyFromUtf8(ERR_PREVIOUS_VALUE_CANNOT_BE_NULL)));
        }

        @Test
        @DisplayName("ByteString newValue")
        void notAcceptNullByteStringNew() {
            assertThrows(NullPointerException.class,
                         () -> Changes.of(copyFromUtf8(ERR_NEW_VALUE_CANNOT_BE_NULL), null));
        }

        @Test
        @DisplayName("Timestamp previousValue")
        void notAcceptNullTimestampPrevious() {
            assertThrows(NullPointerException.class, () -> Changes.of(null, getCurrentTime()));
        }

        @Test
        @DisplayName("Timestamp newValue")
        void notAcceptNullTimestampNew() {
            assertThrows(NullPointerException.class, () -> Changes.of(getCurrentTime(), null));
        }

        @Test
        @DisplayName("Interval previousValue")
        void notAcceptNullIntervalPrevious() {
            final Timestamp fourMinutesAgo = TimeTests.Past.minutesAgo(4);
            final Timestamp now = getCurrentTime();
            final Interval fourMinutes = Intervals.between(fourMinutesAgo, now);
            assertThrows(NullPointerException.class, () -> Changes.of(null, fourMinutes));
        }

        @Test
        @DisplayName("Interval newValue")
        void notAcceptNullIntervalNew() {
            final Timestamp fourMinutesAgo = TimeTests.Past.minutesAgo(4);
            final Timestamp now = getCurrentTime();
            final Interval fourMinutes = Intervals.between(fourMinutesAgo, now);
            assertThrows(NullPointerException.class, () -> Changes.of(fourMinutes, null));
        }

        @Test
        @DisplayName("LocalDate previousValue")
        void notAcceptNullLocalDatePrevious() {
            final LocalDate today = LocalDates.now();
            assertThrows(NullPointerException.class, () -> Changes.of(null, today));
        }

        @Test
        @DisplayName("LocalDate newValue")
        void notAcceptNullLocalDateNew() {
            final LocalDate today = LocalDates.now();
            assertThrows(NullPointerException.class, () -> Changes.of(today, null));
        }

        @Test
        @DisplayName("LocalTime previousValue")
        void notAcceptNullLocalTimePrevious() {
            final LocalTime now = LocalTimes.now();
            assertThrows(NullPointerException.class, () -> Changes.of(null, now));
        }

        @Test
        @DisplayName("LocalTime newValue")
        void notAcceptNullLocalTimeNew() {
            final LocalTime now = LocalTimes.now();
            assertThrows(NullPointerException.class, () -> Changes.of(now, null));
        }

        @Test
        @DisplayName("OffsetDate previousValue")
        void notAcceptNullOffsetDatePrevious() {
            final ZoneOffset inLassVegas = ZoneOffsets.ofHours(8);
            final OffsetDate date = OffsetDates.now(inLassVegas);
            assertThrows(NullPointerException.class, () -> Changes.of(null, date));
        }

        @Test
        @DisplayName("OffsetDate newValue")
        void notAcceptNullOffsetDateNew() {
            final ZoneOffset inKiev = ZoneOffsets.ofHours(3);
            final OffsetDate date = OffsetDates.now(inKiev);
            assertThrows(NullPointerException.class, () -> Changes.of(date, null));
        }

        @Test
        @DisplayName("OffsetTime previousValue")
        void notAcceptNullOffsetTimePrevious() {
            final ZoneOffset inLassVegas = ZoneOffsets.ofHours(8);
            final OffsetTime now = OffsetTimes.now(inLassVegas);
            assertThrows(NullPointerException.class, () -> Changes.of(null, now));
        }

        @Test
        @DisplayName("OffsetTime newValue")
        void notAcceptNullOffsetTimeNew() {
            final ZoneOffset inKiev = ZoneOffsets.ofHours(3);
            final OffsetTime now = OffsetTimes.now(inKiev);
            assertThrows(NullPointerException.class, () -> Changes.of(now, null));
        }

        @Test
        @DisplayName("OffsetDateTime previousValue")
        void notAcceptNullOffsetDateTimePrevious() {
            final ZoneOffset inLassVegas = ZoneOffsets.ofHours(8);
            final OffsetDateTime now = OffsetDateTimes.now(inLassVegas);
            assertThrows(NullPointerException.class, () -> Changes.of(null, now));
        }

        @Test
        @DisplayName("OffsetDateTime newValue")
        void notAcceptNullOffsetDateTimeNew() {
            final ZoneOffset inKiev = ZoneOffsets.ofHours(3);
            final OffsetDateTime now = OffsetDateTimes.now(inKiev);
            assertThrows(NullPointerException.class, () -> Changes.of(now, null));
        }
    }
}
