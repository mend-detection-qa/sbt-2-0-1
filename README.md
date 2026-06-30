# manifest-format

## Feature exercised

Exercises SBT 2.0.1's fix for parsing `project/build.properties` when
whitespace appears around the `=` sign in the `sbt.version` property.
The file uses `sbt.version = 2.0.1` (with spaces around `=`) to verify
that Mend UA correctly extracts the SBT version and resolves
dependencies when this whitespace-padded format is present.

## Pattern

`manifest_format` — added from SBT 2.0.1 release (2026-06-30).

## Probe files

| File | Purpose |
|---|---|
| `project/build.properties` | `sbt.version = 2.0.1` with spaces around `=` |
| `build.sbt` | Three real Maven Central deps (compile + test) |
| `src/main/scala/Main.scala` | Minimal stub |
| `.whitesource` | Tool version pins (Bucket A) |
| `expected-tree.json` | Expected dependency tree |

## Expected dependency tree

Direct dependencies:

- `org.typelevel:cats-core_2.13:2.12.0` (Compile → `group: "main"`)
  - Transitive: `cats-kernel_2.13` 2.12.0
- `org.slf4j:slf4j-api:2.0.13` (Compile → `group: "main"`, Java artifact)
- `org.scalatest:scalatest_2.13:3.2.19` (Test → `group: "test"`)
  - Transitives: `scalatest-core_2.13`, `scalatest-compatible`,
    `scalactic_2.13`, `scalatest-matchers-core_2.13`,
    `scalatest-shouldmatchers_2.13`

Note: `scala-library` is always excluded from the Mend dependency tree
per the UA resolver implementation.

## Mend config

Bucket A — default-emit. `scala-sbt` has no dynamic version detection
from the manifest. `.whitesource` pins:

- `sbt: "2.0.1"` — exact version under test (targets the whitespace
  parsing fix)
- `scala: ">=2.13 <3"` — LTS range
- `java: ">=17 <22"` — LTS range

No `whitesource.config` needed — this probe uses the default resolver
configuration (`sbt.resolveDependencies=true`, pre-step default).

## Resolver note

The whitespace format in `build.properties` (`sbt.version = 2.0.1`)
is the primary probe axis. If Mend UA fails to parse the version
correctly, the resolver may fall back to an unexpected version or fail
to activate the new SBT 2.x resolver pipeline. The expected tree
encodes what correct behaviour looks like; a downstream comparator
flagging a missing `sbt.version` parse would indicate the bug is
present.
