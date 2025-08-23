# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.5] - 2025-08-23

### Fixed

- Updated refresh token endpoint in API base (frontend).
- Updated refresh token endpoint in `CookieService` and related tests (backend).

### Changed

- Bumped `actions/setup-java` from 4 to 5.

## [1.0.4] - 2025-08-22

### Added

- Persist quiz preferences on the frontend.

### Fixed

- Added unique constraint on `quiz_answer_choices(quiz_id, text)` to prevent duplicate answers.

## [1.0.3] - 2025-08-20

### Changed

- Updated default difficulty level in quiz store to **ADVANCED**.

### Added

- Added foreign key constraint to `solved_quizzes.quiz_id` referencing `quizzes(id)` with
  `ON DELETE CASCADE`.

## [1.0.2] - 2025-08-11

### Added

- Nginx configuration updated to support additional `server_name` addresses:
  `www.code-output-quiz.ru` and `grafana.code-output-quiz.ru`.

## [1.0.1] - 2025-08-10

### Changed

- Improved scheduler to handle all errors gracefully and ensure continuous execution.
- Updated `generateNewQuizzes` to use `delayElement` for consistent delay after each iteration.
- Refactored scheduler `start` method to simplify retry logic and add safer error handling.

## [1.0.0] - 2025-08-09

### Added

- Added basic functionality.
- Automatic publication of a release from `CHANGELOG.md `.
