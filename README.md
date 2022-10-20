# Spine Change: Protobuf-based types for working with changes in data

[![Ubuntu build][ubuntu-build-badge]][gh-actions]
[![codecov][codecov-badge]][codecov] &nbsp;
[![license][license-badge]][license]

[gh-actions]: https://github.com/SpineEventEngine/change/actions
[ubuntu-build-badge]: https://github.com/SpineEventEngine/change/actions/workflows/build-on-ubuntu.yml/badge.svg

## Supported Languages

Currently, the library supports only Java, with JavaScript and Dart being on the priority list.

All modules are built with Java 11. Therefore, consumer projects should aim for Java 11+
to use them.

## Using Spine Change in a Java Project

To add a dependency to a Gradle project, please use the following:

```groovy
dependencies {
    compile "io.spine:spine-change:$spineVersion"
}
```

[codecov]: https://codecov.io/gh/SpineEventEngine/change
[codecov-badge]: https://codecov.io/gh/SpineEventEngine/change/branch/master/graph/badge.svg
[license-badge]: https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat
[license]: http://www.apache.org/licenses/LICENSE-2.0
