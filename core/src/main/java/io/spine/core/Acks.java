/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.core;

import com.google.protobuf.Message;
import io.spine.protobuf.AnyPacker;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.protobuf.TextFormat.shortDebugString;
import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * Utilities for working with {@linkplain Ack acknowledgements}.
 */
public final class Acks {

    /** Prevents instantiation of this utility class. */
    private Acks() {
    }

    /**
     * Extracts the ID of the acknowledged command.
     *
     * @throws IllegalArgumentException
     *         if the acknowledgement does not contain a command ID
     */
    public static CommandId toCommandId(Ack ack) {
        checkNotNull(ack);
        Message unpacked = AnyPacker.unpack(ack.getMessageId());
        if (!(unpacked instanceof CommandId)) {
            throw newIllegalArgumentException(
                    "Unable to get a command ID from the acknowledgement: `%s`.",
                    shortDebugString(ack)
            );
        }
        CommandId commandId = (CommandId) unpacked;
        return commandId;
    }
}
