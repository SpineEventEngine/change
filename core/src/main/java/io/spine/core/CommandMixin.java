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

import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import io.spine.annotation.Internal;
import io.spine.base.CommandMessage;
import io.spine.protobuf.Messages;
import io.spine.validate.FieldAwareMessage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;
import static io.spine.protobuf.Messages.isNotDefault;

/**
 * Mixin interface for command objects.
 */
@Immutable
interface CommandMixin
        extends Signal<CommandId, CommandMessage, CommandContext>,
                CommandOrBuilder,
                FieldAwareMessage {

    /**
     * Obtains the ID of the tenant of the command.
     */
    @Override
    default TenantId tenant() {
        return actorContext().getTenantId();
    }

    @Override
    default Timestamp time() {
        return actorContext().getTimestamp();
    }

    @Override
    default ActorContext actorContext() {
        return context().getActorContext();
    }

    @Override
    default MessageId rootMessage() {
        MessageId rootId = context().getOrigin()
                                    .root();
        return isNotDefault(rootId)
               ? rootId
               : messageId();
    }

    @Override
    default Optional<Origin> origin() {
        Origin parent = context().getOrigin();
        return Optional.of(parent)
                       .filter(Messages::isNotDefault);
    }

    /**
     * Checks if the command is scheduled to be delivered later.
     *
     * @return {@code true} if the command context has a scheduling option set,
     *         {@code false} otherwise
     */
    default boolean isScheduled() {
        CommandContext.Schedule schedule = context().getSchedule();
        Duration delay = schedule.getDelay();
        if (isNotDefault(delay)) {
            checkState(delay.getSeconds() > 0,
                       "Command delay seconds must be a positive value.");
            return true;
        }
        return false;
    }

    @Internal
    @Override
    default Object readValue(Descriptors.FieldDescriptor field) {
        switch (field.getIndex()) {
            case 0:
                return getId();
            case 1:
                return getMessage();
            case 2:
                return getContext();
            case 3:
                return getSystemProperties();
            default:
                return getField(field);
        }
    }
}
