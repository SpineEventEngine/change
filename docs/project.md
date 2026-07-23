# Project: Spine Change

## Overview

Spine Change is a small, foundational library of the Spine Event Engine SDK. It
provides Protobuf-based types for describing changes to field values in data: a
`Change` message states how a value moves from a previous value to a new one, and
a `ValueMismatch` message reports that an entity's actual value differed from the
one a command expected. These types are used both as a request to change a value
(for example, as a field of a command) and as a record of a change that already
happened (for example, as part of an event). The library sits near the bottom of
the SDK dependency graph and is reused by higher-level modules across the SDK.

## Architecture

**Role:** a published Java library — `io.spine:spine-change`. The repository is a
single Gradle module (`change`); its production code is Protobuf and hand-written
Java, with Kotlin used only for the build logic.

**Public API** lives in the `io.spine.change` package:

- The `*Change` messages from `change.proto` (`StringChange`, `TimestampChange`,
  `Int32Change`, `BooleanChange`, `BytesChange`, and their siblings for the
  remaining scalar types), each carrying a `previous_value` and a `new_value`.
- The `ValueMismatch` message from `value_mismatch.proto`, which holds the
  `expected`, `actual`, and `new_value` of a rejected change together with the
  entity `version`.
- `Changes` — a utility class whose `of(...)` factory methods build a `*Change`
  for a given pair of values.
- The per-type `*Mismatch` utility classes (`StringMismatch`, `IntMismatch`,
  `MessageMismatch`, and the rest) that build a `ValueMismatch` for the various
  discovery scenarios, plus `ChangePreconditions` with the shared argument checks.

**Key invariant:** for every `Change` and `ValueMismatch`, the `new_value` must not
equal the `previous_value`. `ChangePreconditions` enforces this and rejects equal
values with an `IllegalArgumentException`.

Read [`.agents/guidelines/jvm-project.md`](.agents/guidelines/jvm-project.md) for
build stack, coding style, tests, and versioning.
