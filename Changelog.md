# Changelog
All notable changes to SigmonLED will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
SigmonLED uses [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

[comment]: # (This is a comment, it will not be included)

## [0.2.0] - 2021-02-22
### Added
- This Changelog :)
- JavaDoc-style documentation for source code.
- Dark mode setting. Off by default.
- Auto-reconnect, will automatically connect to a previous device on app boot. On by default.
- Solid Palette mode functionality.
### Fixed
- Android icons missing (#2). No longer icons, but text. Temporary solution until this Qt bug can be fixed. Unfortunately looks to be out of my control currently.
- Potential bug that would cause Arduino to lock if a device lost connection while communicating.
